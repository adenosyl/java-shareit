package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    @Test
    void testEqualsAndHashCode() {
        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setStart(LocalDateTime.now());
        b1.setEnd(LocalDateTime.now().plusDays(1));
        b1.setStatus(BookingStatus.WAITING);

        Booking b2 = new Booking();
        b2.setId(1L);
        b2.setStart(b1.getStart());
        b2.setEnd(b1.getEnd());
        b2.setStatus(BookingStatus.WAITING);

        assertThat(b1).isEqualTo(b2);
        assertThat(b1.hashCode()).isEqualTo(b2.hashCode());
    }

    @Test
    void testToString() {
        Booking booking = new Booking();
        booking.setId(10L);

        assertThat(booking.toString()).contains("Booking");
    }
}