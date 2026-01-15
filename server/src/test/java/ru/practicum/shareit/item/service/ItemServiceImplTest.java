package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Test
    void getByOwner_shouldReturnOwnerItems() {
        // ---------- arrange ----------
        UserDto owner = userService.create(
                new UserDto(null, "Owner", "owner@mail.com")
        );

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Drill");
        itemDto.setDescription("Power drill");
        itemDto.setAvailable(true);

        itemService.create(owner.getId(), itemDto);

        // ---------- act ----------
        List<ItemOwnerDto> items = itemService.getByOwner(owner.getId());

        // ---------- assert ----------
        assertThat(items).hasSize(1);

        ItemOwnerDto item = items.get(0);
        assertThat(item.getName()).isEqualTo("Drill");
        assertThat(item.getDescription()).isEqualTo("Power drill");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getComments()).isEmpty();
    }
}