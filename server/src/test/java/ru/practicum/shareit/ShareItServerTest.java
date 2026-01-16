package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShareItServerTest {

    @Test
    void contextClassExists() {
        ShareItServer app = new ShareItServer();
        assertThat(app).isNotNull();
    }
}