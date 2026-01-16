package ru.practicum.shareit.user.client;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserClient {

    private final RestTemplate rest;

    public UserClient(@Value("${shareit-server.url}") String serverUrl) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        this.rest = new RestTemplate(factory);
        this.rest.setUriTemplateHandler(new DefaultUriBuilderFactory(serverUrl));

        this.rest.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        return rest.exchange(
                "/users",
                HttpMethod.POST,
                new HttpEntity<>(userDto),
                Object.class
        );
    }

    public ResponseEntity<Object> update(Long userId, UserDto userDto) {
        return rest.exchange(
                "/users/" + userId,
                HttpMethod.PATCH,
                new HttpEntity<>(userDto),
                Object.class
        );
    }

    public ResponseEntity<Object> getById(Long userId) {
        return rest.exchange(
                "/users/" + userId,
                HttpMethod.GET,
                null,
                Object.class
        );
    }

    public ResponseEntity<Object> getAll() {
        return rest.exchange(
                "/users",
                HttpMethod.GET,
                null,
                Object.class
        );
    }

    public ResponseEntity<Object> delete(Long userId) {
        return rest.exchange(
                "/users/" + userId,
                HttpMethod.DELETE,
                null,
                Object.class
        );
    }
}