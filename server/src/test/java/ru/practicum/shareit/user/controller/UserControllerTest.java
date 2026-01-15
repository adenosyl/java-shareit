package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_shouldReturn200() throws Exception {
        UserDto request = new UserDto();
        request.setName("Ivan");
        request.setEmail("ivan@mail.ru");

        UserDto response = new UserDto();
        response.setId(1L);
        response.setName("Ivan");
        response.setEmail("ivan@mail.ru");

        when(userService.create(any(UserDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Ivan")));

        verify(userService).create(any(UserDto.class));
    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        UserDto request = new UserDto();
        request.setName("Ivan Updated");

        UserDto response = new UserDto();
        response.setId(1L);
        response.setName("Ivan Updated");
        response.setEmail("ivan@mail.ru");

        when(userService.update(eq(1L), any(UserDto.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ivan Updated")));

        verify(userService).update(eq(1L), any(UserDto.class));
    }

    @Test
    void getUserById_shouldReturn200() throws Exception {
        UserDto response = new UserDto();
        response.setId(1L);
        response.setName("Ivan");
        response.setEmail("ivan@mail.ru");

        when(userService.getById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("ivan@mail.ru")));

        verify(userService).getById(1L);
    }

    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        when(userService.getAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getAll();
    }

    @Test
    void deleteUser_shouldReturn200() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }
}