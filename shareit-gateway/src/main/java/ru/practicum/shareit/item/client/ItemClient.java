package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemClient extends BaseClient {

    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    // POST /items
    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post("/items", userId, itemDto);
    }

    // PATCH /items/{itemId}
    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        return patch("/items/" + itemId, userId, itemDto);
    }

    // GET /items/{itemId}
    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        return get("/items/" + itemId, userId);
    }

    // GET /items
    public ResponseEntity<Object> getAll(Long userId) {
        return get("/items", userId);
    }

    // DELETE /items/{itemId} — на будущее (не мешает тестам)
    public ResponseEntity<Object> delete(Long userId, Long itemId) {
        return delete("/items/" + itemId, userId);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentCreateDto dto) {
        return post("/items/" + itemId + "/comment", userId, dto);
    }
}