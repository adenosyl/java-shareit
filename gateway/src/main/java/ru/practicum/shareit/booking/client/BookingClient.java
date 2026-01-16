package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {

    public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    // POST /bookings
    public ResponseEntity<Object> create(Long userId, BookingCreateDto dto) {
        return post("/bookings", userId, dto);
    }

    // GET /bookings/{bookingId}
    public ResponseEntity<Object> getById(Long userId, Long bookingId) {
        return get("/bookings/" + bookingId, userId);
    }

    // GET /bookings?state=
    public ResponseEntity<Object> getByBooker(Long userId, String state) {
        return get("/bookings", userId, Map.of("state", state));
    }

    // GET /bookings/owner?state=
    public ResponseEntity<Object> getByOwner(Long userId, String state) {
        return get("/bookings/owner", userId, Map.of("state", state));
    }

    // PATCH /bookings/{bookingId}?approved=
    public ResponseEntity<Object> approve(Long userId, Long bookingId, boolean approved) {
        return patch(
                "/bookings/" + bookingId + "?approved=" + approved,
                userId,
                null
        );
    }
}