package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds ORDER BY b.item.id, b.start DESC")
    List<Booking> findByItemIdIn(@Param("itemIds") List<Long> itemIds);

    boolean existsBookingByItemIdAndBookerIdAndStatusIsAndEndBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime now);

    List<Booking> findByItemId(Long itemId);

    @Query("SELECT b FROM Booking AS b " + "WHERE b.item = :item AND " + "b.start <= :date " + "ORDER BY b.start DESC " + "LIMIT 1")
    Optional<Booking> findLastBooking(Item item, LocalDateTime date);


    @Query("SELECT b FROM Booking AS b " + "WHERE b.item = :item AND " + "b.start > :date " + "ORDER BY b.start ASC " + "LIMIT 1")
    Optional<Booking> findNextBooking(Item item, LocalDateTime date);
}
