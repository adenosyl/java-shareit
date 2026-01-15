package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser_shouldReturnUser() {
        UserDto user = userService.create(
                new UserDto(null, "Ivan", "ivan@mail.com")
        );

        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("ivan@mail.com");
    }
}
