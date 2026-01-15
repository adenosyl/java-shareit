package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    // POST /requests
    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestCreateDto dto
    ) {
        return requestClient.create(userId, dto);
    }

    // GET /requests
    @GetMapping
    public ResponseEntity<Object> getOwn(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return requestClient.getOwn(userId);
    }

    // GET /requests/all?from&size
    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return requestClient.getAll(userId, from, size);
    }

    // GET /requests/{requestId}
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId
    ) {
        return requestClient.getById(userId, requestId);
    }

    // DTO для создания запроса (gateway)
    public static class ItemRequestCreateDto {

        @jakarta.validation.constraints.NotBlank
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}