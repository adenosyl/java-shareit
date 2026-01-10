package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(
                item.getRequest() != null ? item.getRequest().getId() : null
        );
        dto.setComments(List.of());
        return dto;
    }

    public static ItemOwnerDto toOwnerDto(Item item) {
        ItemOwnerDto dto = new ItemOwnerDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());

        dto.setLastBooking(null);
        dto.setNextBooking(null);

        return dto;
    }
}