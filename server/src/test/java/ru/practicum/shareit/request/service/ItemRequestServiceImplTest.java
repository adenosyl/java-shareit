package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @Test
    void createRequest_shouldReturnRequest() {
        UserDto user = userService.create(
                new UserDto(null, "User", "user@mail.com")
        );

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need drill");

        ItemRequestResponseDto response =
                itemRequestService.create(user.getId(), dto);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getDescription()).isEqualTo("Need drill");
    }

    @Test
    void getOwnAndOtherRequests_shouldWork() {
        UserDto requester = userService.create(
                new UserDto(null, "Requester", "req@mail.com")
        );

        UserDto other = userService.create(
                new UserDto(null, "Other", "other@mail.com")
        );

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need hammer");

        ItemRequestResponseDto created =
                itemRequestService.create(requester.getId(), dto);

        var ownRequests =
                itemRequestService.getOwnRequests(requester.getId());

        var otherRequests =
                itemRequestService.getOtherRequests(other.getId());

        var byId =
                itemRequestService.getById(requester.getId(), created.getId());

        assertThat(ownRequests).hasSize(1);
        assertThat(otherRequests).hasSize(1);
        assertThat(byId.getDescription()).isEqualTo("Need hammer");
    }
}
