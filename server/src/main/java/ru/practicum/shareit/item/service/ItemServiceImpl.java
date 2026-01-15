package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = getUserOrThrow(userId);

        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new BadRequestException("Название не может быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new BadRequestException("Описание не может быть пустым");
        }
        if (itemDto.getAvailable() == null) {
            throw new BadRequestException("Поле available обязательно");
        }

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository
                    .findById(itemDto.getRequestId())
                    .orElseThrow(() ->
                            new NotFoundException("Запрос с id=" + itemDto.getRequestId() + " не найден")
                    );
            item.setRequest(request);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = getItemOrThrow(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь не найдена");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item saved = itemRepository.save(item);
        return ItemMapper.toItemDto(saved);
    }

    @Override
    public ItemOwnerDto getById(Long userId, Long itemId) {
        Item item = getItemOrThrow(itemId);

        ItemOwnerDto dto = ItemMapper.toOwnerDto(item);
        dto.setComments(getComments(itemId));

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();

            bookingRepository.findLastBooking(itemId, now)
                    .stream()
                    .findFirst()
                    .ifPresent(b ->
                            dto.setLastBooking(new BookingShortDto(
                                    b.getId(),
                                    b.getBooker().getId()
                            ))
                    );

            bookingRepository.findNextBooking(itemId, now)
                    .stream()
                    .findFirst()
                    .ifPresent(b ->
                            dto.setNextBooking(new BookingShortDto(
                                    b.getId(),
                                    b.getBooker().getId()
                            ))
                    );
        }

        return dto;
    }

    @Override
    public List<ItemOwnerDto> getByOwner(Long userId) {
        getUserOrThrow(userId);
        LocalDateTime now = LocalDateTime.now();

        return itemRepository.findByOwner_Id(userId).stream()
                .map(item -> {
                    ItemOwnerDto dto = ItemMapper.toOwnerDto(item);

                    bookingRepository.findLastBooking(item.getId(), now)
                            .stream()
                            .findFirst()
                            .ifPresent(b ->
                                    dto.setLastBooking(
                                            new BookingShortDto(
                                                    b.getId(),
                                                    b.getBooker().getId()
                                            )
                                    ));

                    bookingRepository.findNextBooking(item.getId(), now)
                            .stream()
                            .findFirst()
                            .ifPresent(b ->
                                    dto.setNextBooking(
                                            new BookingShortDto(
                                                    b.getId(),
                                                    b.getBooker().getId()
                                            )
                                    ));

                    dto.setComments(getComments(item.getId()));
                    return dto;
                })
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        boolean hasBooking = bookingRepository
                .existsByItem_IdAndBooker_IdAndEndBefore(
                        itemId, userId, LocalDateTime.now()
                );

        if (!hasBooking) {
            throw new BadRequestException("Пользователь не бронировал эту вещь");
        }

        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Вещь с id=" + itemId + " не найдена"));
    }

    private List<CommentDto> getComments(Long itemId) {
        return commentRepository.findByItem_Id(itemId).stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getName(),
                        c.getCreated()
                ))
                .toList();
    }
}