package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

class BookingMapperTest {

    @Test
    void toDto_shouldMapAllFields() {
        User booker = new User();
        booker.setId(1L);
        booker.setName("Booker");
        booker.setEmail("b@mail.com");

        Item item = new Item();
        item.setId(2L);
        item.setName("Drill");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(booker);

        Booking booking = new Booking();
        booking.setId(3L);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.APPROVED);
    }
}