package ru.practicum.shareit.client;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

public class BaseClient {

    protected final RestTemplate rest;

    public BaseClient(String serverUrl) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        this.rest = new RestTemplate(factory);
        this.rest.setUriTemplateHandler(new DefaultUriBuilderFactory(serverUrl));
    }

    /* ==================== CORE ==================== */

    protected ResponseEntity<Object> exchange(
            String path,
            HttpMethod method,
            HttpEntity<?> entity,
            Map<String, Object> params
    ) {
        try {
            return rest.exchange(path, method, entity, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        }
    }

    /* ==================== HTTP ==================== */

    protected ResponseEntity<Object> get(String path, Long userId) {
        return exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(headers(userId)),
                Map.of()
        );
    }

    protected ResponseEntity<Object> get(String path, Long userId, Map<String, Object> params) {
        return exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(headers(userId)),
                params
        );
    }

    protected ResponseEntity<Object> post(String path, Long userId, Object body) {
        return exchange(
                path,
                HttpMethod.POST,
                new HttpEntity<>(body, headers(userId)),
                Map.of()
        );
    }

    protected ResponseEntity<Object> patch(String path, Long userId, Object body) {
        return exchange(
                path,
                HttpMethod.PATCH,
                new HttpEntity<>(body, headers(userId)),
                Map.of()
        );
    }

    protected ResponseEntity<Object> delete(String path, Long userId) {
        return exchange(
                path,
                HttpMethod.DELETE,
                new HttpEntity<>(headers(userId)),
                Map.of()
        );
    }

    /* ==================== HEADERS ==================== */

    private HttpHeaders headers(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (userId != null) {
            headers.set("X-Sharer-User-Id", userId.toString());
        }

        return headers;
    }
}