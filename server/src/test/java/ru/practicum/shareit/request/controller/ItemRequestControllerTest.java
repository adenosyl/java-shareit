package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Test
    void create_shouldReturn200() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужна дрель");

        ItemRequestResponseDto responseDto = new ItemRequestResponseDto(
                1L,
                "Нужна дрель",
                LocalDateTime.now(),
                List.of()
        );

        when(itemRequestService.create(eq(1L), any(ItemRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна дрель"));

        verify(itemRequestService).create(eq(1L), any(ItemRequestDto.class));
    }

    @Test
    void getOwnRequests_shouldReturn200() throws Exception {
        ItemRequestResponseDto dto = new ItemRequestResponseDto(
                1L,
                "Нужна дрель",
                LocalDateTime.now(),
                List.of()
        );

        when(itemRequestService.getOwnRequests(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests")
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(itemRequestService).getOwnRequests(1L);
    }

    @Test
    void getOtherRequests_shouldReturn200() throws Exception {
        when(itemRequestService.getOtherRequests(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk());

        verify(itemRequestService).getOtherRequests(1L);
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        ItemRequestResponseDto dto = new ItemRequestResponseDto(
                1L,
                "Нужна дрель",
                LocalDateTime.now(),
                List.of()
        );

        when(itemRequestService.getById(1L, 1L))
                .thenReturn(dto);

        mockMvc.perform(get("/requests/1")
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(itemRequestService).getById(1L, 1L);
    }
}