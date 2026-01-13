package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody BookingCreateDto dto) {
        return bookingService.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable Long bookingId,
                              @RequestParam boolean approved) {
        return bookingService.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getByBooker(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.getByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.getByOwner(ownerId, state);
    }
}