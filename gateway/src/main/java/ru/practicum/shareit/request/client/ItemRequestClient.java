package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Component
public class ItemRequestClient extends BaseClient {

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    // POST /requests
    public ResponseEntity<Object> create(Long userId, Object requestDto) {
        return post("/requests", userId, requestDto);
    }

    // GET /requests
    public ResponseEntity<Object> getOwn(Long userId) {
        return get("/requests", userId);
    }

    // GET /requests/{requestId}
    public ResponseEntity<Object> getById(Long userId, Long requestId) {
        return get("/requests/" + requestId, userId);
    }

    // GET /requests/all?from={from}&size={size}
    public ResponseEntity<Object> getAll(Long userId, int from, int size) {
        return get(
                "/requests/all",
                userId,
                Map.of(
                        "from", from,
                        "size", size
                )
        );
    }
}