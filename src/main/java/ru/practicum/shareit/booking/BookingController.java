package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.exception.BookingAccessDeniedException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingStartAndEndDatesAreNotEqualsException;
import ru.practicum.shareit.booking.exception.NoItemsAvailableForBookingException;
import ru.practicum.shareit.item.exception.ItemNotBelongsToUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

import static ru.practicum.shareit.ShareitAppConfig.HEADER_USER_ID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(
            @RequestBody @Validated(BookingDto.Create.class) BookingDto bookingDto,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId) throws UserNotFoundException, ItemNotFoundException, NoItemsAvailableForBookingException, BookingStartAndEndDatesAreNotEqualsException {
        bookingDto.setBookerId(userId);
        return bookingService.createBooking(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @PathVariable("bookingId") @Positive Long bookingId,
            @RequestParam("approved") @NotNull Boolean approved,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId) throws UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException, ItemNotFoundException {
        BookingDto bookingDto = BookingDto.builder().id(bookingId).bookerId(userId).approved(approved).build();
        return bookingService.approveBooking(bookingDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable("bookingId") @Positive Long bookingId,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId) throws BookingNotFoundException, BookingAccessDeniedException {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(
            @RequestParam(defaultValue = "ALL") BookingState state,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId) throws NoItemsAvailableForBookingException, UserNotFoundException {
        return bookingService.getOwnerBookings(userId, state);
    }

}
