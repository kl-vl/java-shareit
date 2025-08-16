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

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds ORDER BY b.item.id, b.start DESC")
    List<Booking> findByItemIdIn(@Param("itemIds") List<Long> itemIds);

    boolean existsBookingByItemIdAndBookerIdAndStatusIsAndEndBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime now);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId " +
            "AND b.start <= :now AND (b.end >= :now OR b.end IS NULL) " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId " +
            "AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId " +
            "AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByItemId(Long itemId);

    @Query("SELECT b FROM Booking AS b " + "WHERE b.item = :item AND " + "b.start <= :date " + "ORDER BY b.start DESC " + "LIMIT 1")
    Optional<Booking> findLastBooking(Item item, LocalDateTime date);


    @Query("SELECT b FROM Booking AS b " + "WHERE b.item = :item AND " + "b.start > :date " + "ORDER BY b.start ASC " + "LIMIT 1")
    Optional<Booking> findNextBooking(Item item, LocalDateTime date);

    // ALL
    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    // CURRENT
    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.start <= :now AND (b.end >= :now OR b.end IS NULL) " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByItemOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    // PAST
    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByItemOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    // FUTURE
    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByItemOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    // WAITING и REJECTED
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);
}