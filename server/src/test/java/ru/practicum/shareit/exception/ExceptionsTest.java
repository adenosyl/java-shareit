package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsTest {

    @Test
    void testNotFoundException() {
        NotFoundException ex = new NotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
    }

    @Test
    void testBadRequestException() {
        BadRequestException ex = new BadRequestException("bad request");
        assertThat(ex.getMessage()).isEqualTo("bad request");
    }

    @Test
    void testForbiddenException() {
        ForbiddenException ex = new ForbiddenException("forbidden");
        assertThat(ex.getMessage()).isEqualTo("forbidden");
    }
}