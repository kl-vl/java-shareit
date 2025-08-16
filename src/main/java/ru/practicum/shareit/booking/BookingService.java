package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.exception.BookingAccessDeniedException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingStartAndEndDatesAreNotEqualsException;
import ru.practicum.shareit.item.exception.ItemNotBelongsToUserException;
import ru.practicum.shareit.booking.exception.NoItemsAvailableForBookingException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto) throws ItemNotFoundException, UserNotFoundException, NoItemsAvailableForBookingException, BookingStartAndEndDatesAreNotEqualsException;

    BookingDto approveBooking(BookingDto bookingDto) throws ItemNotFoundException, UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException;

    BookingDto getBookingById(Long bookingId, Long userId) throws BookingNotFoundException, BookingAccessDeniedException;

    List<BookingDto> getUserBookings(Long userId, BookingState state);

    List<BookingDto> getOwnerBookings(Long ownerId, BookingState state) throws NoItemsAvailableForBookingException, UserNotFoundException;
}
