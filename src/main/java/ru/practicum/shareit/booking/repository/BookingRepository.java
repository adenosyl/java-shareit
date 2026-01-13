package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(
            Long bookerId, LocalDateTime time, Sort sort);

    List<Booking> findByBooker_IdAndEndIsBefore(
            Long bookerId, LocalDateTime time, Sort sort);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStatus(
            Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItem_Owner_Id(Long ownerId, Sort sort);

    List<Booking> findByItem_Owner_IdAndStartIsAfter(
            Long ownerId, LocalDateTime time, Sort sort);

    List<Booking> findByItem_Owner_IdAndEndIsBefore(
            Long ownerId, LocalDateTime time, Sort sort);

    List<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
            Long ownerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByItem_Owner_IdAndStatus(
            Long ownerId, BookingStatus status, Sort sort);

    @Query("""
        select b from Booking b
        where b.item.id = ?1
          and b.status = 'APPROVED'
          and b.start < ?2
        order by b.start desc
    """)
    List<Booking> findLastBooking(Long itemId, LocalDateTime now);

    @Query("""
        select b from Booking b
        where b.item.id = ?1
          and b.status = 'APPROVED'
          and b.start > ?2
        order by b.start asc
    """)
    List<Booking> findNextBooking(Long itemId, LocalDateTime now);

    boolean existsByBooker_IdAndItem_IdAndEndIsBefore(
            Long userId,
            Long itemId,
            LocalDateTime time
    );
}