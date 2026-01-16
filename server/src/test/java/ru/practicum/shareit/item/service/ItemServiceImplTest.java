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

    @Test
    void createItem_userNotFound_shouldThrowException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Desc");
        itemDto.setAvailable(true);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> itemService.create(999L, itemDto)
        );
    }

    @Test
    void updateItem_notOwner_shouldThrowException() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));
        UserDto stranger = userService.create(new UserDto(null, "Stranger", "s@mail.com"));

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Desc");
        itemDto.setAvailable(true);

        ItemDto saved = itemService.create(owner.getId(), itemDto);

        ItemDto update = new ItemDto();
        update.setName("Updated");

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> itemService.update(stranger.getId(), saved.getId(), update)
        );
    }

    @Test
    void updateItem_owner_shouldUpdateFields() {
        UserDto owner = userService.create(new UserDto(null, "Owner", "o@mail.com"));

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Desc");
        itemDto.setAvailable(true);

        ItemDto saved = itemService.create(owner.getId(), itemDto);

        ItemDto update = new ItemDto();
        update.setName("Updated");
        update.setDescription("Updated desc");

        ItemDto updated = itemService.update(owner.getId(), saved.getId(), update);

        assertThat(updated.getName()).isEqualTo("Updated");
        assertThat(updated.getDescription()).isEqualTo("Updated desc");
    }

    @Test
    void getById_userNotFound_shouldThrowException() {
        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> itemService.getById(999L, 1L)
        );
    }
}