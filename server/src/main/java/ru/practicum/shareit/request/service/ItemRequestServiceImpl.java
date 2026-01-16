package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemShortDto;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestResponseDto create(Long userId, ItemRequestDto dto) {
        User user = getUser(userId);

        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        return toResponseDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestResponseDto> getOwnRequests(Long userId) {
        getUser(userId);

        return requestRepository
                .findAllByRequester_IdOrderByCreatedDesc(userId)
                .stream()
                .map(this::toResponseDtoWithItems)
                .toList();
    }

    @Override
    public List<ItemRequestResponseDto> getOtherRequests(Long userId) {
        getUser(userId);

        return requestRepository
                .findAllByRequester_IdNotOrderByCreatedDesc(userId)
                .stream()
                .map(this::toResponseDtoWithItems)
                .toList();
    }

    @Override
    public ItemRequestResponseDto getById(Long userId, Long requestId) {
        getUser(userId);

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        return toResponseDtoWithItems(request);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private ItemRequestResponseDto toResponseDto(ItemRequest request) {
        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(List.of());
        return dto;
    }

    private ItemRequestResponseDto toResponseDtoWithItems(ItemRequest request) {
        ItemRequestResponseDto dto = toResponseDto(request);

        List<ItemShortDto> items = itemRepository
                .findByRequest_Id(request.getId())
                .stream()
                .map(item -> new ItemShortDto(
                        item.getId(),
                        item.getName()
                ))
                .toList();

        dto.setItems(items);
        return dto;
    }
}