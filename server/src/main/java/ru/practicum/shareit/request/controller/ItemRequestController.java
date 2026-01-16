package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto create(
            @RequestHeader(USER_HEADER) Long userId,
            @RequestBody ItemRequestDto dto
    ) {
        return itemRequestService.create(userId, dto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getOwnRequests(
            @RequestHeader(USER_HEADER) Long userId
    ) {
        return itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getOtherRequests(
            @RequestHeader(USER_HEADER) Long userId
    ) {
        return itemRequestService.getOtherRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(
            @RequestHeader(USER_HEADER) Long userId,
            @PathVariable Long requestId
    ) {
        return itemRequestService.getById(userId, requestId);
    }
}