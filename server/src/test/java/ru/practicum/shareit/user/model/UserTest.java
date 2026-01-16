package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testEqualsAndHashCode() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("a@mail.com");

        User u2 = new User();
        u2.setId(1L);
        u2.setEmail("a@mail.com");

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(3L);

        assertThat(user.toString()).contains("User");
    }
}