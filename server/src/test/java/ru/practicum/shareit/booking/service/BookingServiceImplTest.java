package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        UserDto owner = userService.create(
                new UserDto(null, "Owner", "owner@mail.com")
        );

        UserDto booker = userService.create(
                new UserDto(null, "Booker", "booker@mail.com")
        );

        ItemDto item = new ItemDto();
        item.setName("Drill");
        item.setDescription("Power drill");
        item.setAvailable(true);

        ItemDto savedItem = itemService.create(owner.getId(), item);

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(savedItem.getId());
        bookingCreateDto.setStart(LocalDateTime.now().plusDays(1));
        bookingCreateDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking =
                bookingService.create(booker.getId(), bookingCreateDto);

        assertThat(booking.getId()).isNotNull();
        assertThat(booking.getStatus()).isNotNull();
    }
}