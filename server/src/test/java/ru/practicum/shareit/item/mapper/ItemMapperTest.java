package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

class ItemMapperTest {

    @Test
    void toDto_shouldMapItem() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(owner);
    }
}