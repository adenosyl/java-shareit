package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingCreateDto bookingDto
    ) {
        return bookingClient.create(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBooker(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingClient.getByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingClient.getByOwner(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved
    ) {
        return bookingClient.approve(userId, bookingId, approved);
    }
}