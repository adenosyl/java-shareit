package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto create(Long userId, BookingCreateDto dto) {
        User booker = getUserOrThrow(userId);
        Item item = getItemOrThrow(dto.getItemId());

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Нельзя бронировать свою вещь");
        }

        if (!Boolean.TRUE.equals(item.getAvailable())) {
            throw new BadRequestException("Вещь недоступна");
        }

        if (dto.getStart() == null || dto.getEnd() == null ||
                !dto.getStart().isBefore(dto.getEnd())) {
            throw new BadRequestException("Некорректные даты бронирования");
        }

        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("Недостаточно прав для подтверждения бронирования");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BadRequestException("Бронирование уже обработано");
        }

        booking.setStatus(
                approved ? BookingStatus.APPROVED : BookingStatus.REJECTED
        );

        return toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = getBookingOrThrow(bookingId);

        if (!booking.getBooker().getId().equals(userId)
                && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Бронирование не найдено");
        }

        return toDto(booking);
    }

    @Override
    public List<BookingDto> getByBooker(Long userId, BookingState state) {
        getUserOrThrow(userId);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBooker_Id(userId, sort);
            case CURRENT -> bookingRepository
                    .findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, now, now, sort);
            case PAST -> bookingRepository
                    .findByBooker_IdAndEndIsBefore(userId, now, sort);
            case FUTURE -> bookingRepository
                    .findByBooker_IdAndStartIsAfter(userId, now, sort);
            case WAITING -> bookingRepository
                    .findByBooker_IdAndStatus(userId, BookingStatus.WAITING, sort);
            case REJECTED -> bookingRepository
                    .findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, sort);
        };

        return bookings.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<BookingDto> getByOwner(Long ownerId, BookingState state) {
        getUserOrThrow(ownerId);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItem_Owner_Id(ownerId, sort);
            case CURRENT -> bookingRepository
                    .findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, sort);
            case PAST -> bookingRepository
                    .findByItem_Owner_IdAndEndIsBefore(ownerId, now, sort);
            case FUTURE -> bookingRepository
                    .findByItem_Owner_IdAndStartIsAfter(ownerId, now, sort);
            case WAITING -> bookingRepository
                    .findByItem_Owner_IdAndStatus(ownerId, BookingStatus.WAITING, sort);
            case REJECTED -> bookingRepository
                    .findByItem_Owner_IdAndStatus(ownerId, BookingStatus.REJECTED, sort);
        };

        return bookings.stream()
                .map(this::toDto)
                .toList();
    }

    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    private BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new ItemShortDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                ),
                new UserShortDto(
                        booking.getBooker().getId()
                )
        );
    }
}