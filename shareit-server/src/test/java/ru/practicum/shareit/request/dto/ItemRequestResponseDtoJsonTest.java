package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestResponseDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestResponseDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serialize_shouldCorrectlyWriteJson() throws Exception {
        ItemRequestResponseDto dto = new ItemRequestResponseDto(
                1L,
                "Нужна дрель",
                LocalDateTime.of(2026, 1, 14, 12, 0),
                List.of()
        );

        String result = objectMapper.writeValueAsString(dto);

        assertThat(result).contains("\"id\":1");
        assertThat(result).contains("\"description\":\"Нужна дрель\"");
        assertThat(result).contains("\"created\":\"2026-01-14T12:00:00\"");
        assertThat(result).contains("\"items\":[]");
    }

    @Test
    void deserialize_shouldCorrectlyReadJson() throws Exception {
        String jsonContent = """
                {
                  "id": 1,
                  "description": "Нужна дрель",
                  "created": "2026-01-14T12:00:00",
                  "items": []
                }
                """;

        ItemRequestResponseDto dto =
                objectMapper.readValue(jsonContent, ItemRequestResponseDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Нужна дрель");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2026, 1, 14, 12, 0));
        assertThat(dto.getItems()).isEmpty();
    }
}