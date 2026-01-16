package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Test
    void createBooking_shouldReturnBooking() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "owner@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "booker@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Drill");
        item.setDescription("Power drill");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking = bookingService.create(booker.getId(), dto);

        assertThat(booking.getId()).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void approveBooking_shouldChangeStatus() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "owner@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "booker@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking = bookingService.create(booker.getId(), dto);

        BookingDto approved = bookingService.approve(owner.getId(), booking.getId(), true);

        assertThat(approved.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void rejectBooking_shouldChangeStatus() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "owner@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "booker@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking = bookingService.create(booker.getId(), dto);

        BookingDto rejected = bookingService.approve(owner.getId(), booking.getId(), false);

        assertThat(rejected.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    void getByOwner_stateAll_shouldReturnList() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "b@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        bookingService.create(booker.getId(), dto);

        List<BookingDto> bookings =
                bookingService.getByOwner(owner.getId(), BookingState.ALL);

        assertThat(bookings).isNotEmpty();
    }

    @Test
    void getByBooker_future_shouldReturnList() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "b@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(5));
        dto.setEnd(LocalDateTime.now().plusDays(6));

        bookingService.create(booker.getId(), dto);

        List<BookingDto> bookings =
                bookingService.getByBooker(booker.getId(), BookingState.FUTURE);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void approveBooking_twice_shouldThrowException() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "b@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking = bookingService.create(booker.getId(), dto);

        bookingService.approve(owner.getId(), booking.getId(), true);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingService.approve(owner.getId(), booking.getId(), true)
        );
    }

    @Test
    void approveBooking_notOwner_shouldThrowException() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));
        UserDto stranger = userService.create(new UserDto(null, "Stranger", "s@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "b@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking = bookingService.create(booker.getId(), dto);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingService.approve(stranger.getId(), booking.getId(), true)
        );
    }

    @Test
    void getByBooker_past_shouldReturnList() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));
        UserDto booker = userService.create(new UserDto(null, "Booker", "b@mail.com"));

        ItemDto item = new ItemDto();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(savedItem.getId());
        dto.setStart(LocalDateTime.now().minusDays(3));
        dto.setEnd(LocalDateTime.now().minusDays(1));

        bookingService.create(booker.getId(), dto);

        List<BookingDto> bookings =
                bookingService.getByBooker(booker.getId(), BookingState.PAST);

        assertThat(bookings).hasSize(1);
    }
}