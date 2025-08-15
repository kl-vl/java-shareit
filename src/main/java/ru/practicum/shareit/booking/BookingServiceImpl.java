package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.BookingAccessDeniedException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingStartAndEndDatesAreNotEqualsException;
import ru.practicum.shareit.booking.exception.NoItemsAvailableForBookingException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exception.ItemNotBelongsToUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final BookingMapperDependencies bookingMapperDependencies;

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) throws ItemNotFoundException, UserNotFoundException, NoItemsAvailableForBookingException, BookingStartAndEndDatesAreNotEqualsException {
        Long userId = bookingDto.getBookerId();
        Long itemId = bookingDto.getItemId();

        log.info("CreateBooking input: itemId={}, ownerId={}", itemId, userId);

        User owner = userRepository.findById(bookingDto.getBookerId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Item id=%d not found".formatted(bookingDto.getItemId())));

        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingStartAndEndDatesAreNotEqualsException("Start and end dates must not be equal");
        }

        if (!item.getAvailable()) {
            throw new NoItemsAvailableForBookingException("Item id=%d not available for booking".formatted(bookingDto.getItemId()));
        }

        Booking booking = bookingMapper.toBooking(bookingDto, bookingMapperDependencies);
        Booking createdBooking = bookingRepository.save(booking);

        log.info("CreateBooking success: id={}", createdBooking.getId());

        return bookingMapper.toDto(createdBooking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(BookingDto bookingDto) throws ItemNotFoundException, UserNotFoundException, BookingNotFoundException, ItemNotBelongsToUserException {
        Long userId = bookingDto.getBookerId();
        Long bookingId = bookingDto.getId();

        log.info("ApproveBooking input: bookingId={}, ownerId={}, available={}", bookingId, userId, bookingDto.isApproved());

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking id=%d not found".formatted(bookingId)));

        Long ownerId = booking.getItem().getOwner().getId();

        if (!ownerId.equals(userId)) {
            throw new ItemNotBelongsToUserException("Item id=%d not belongs to user id=%d"
                    .formatted(bookingId, ownerId));
        }
        booking.setStatus(bookingDto.isApproved() ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);

        log.info("ApproveBooking success: id={}", updatedBooking.getId());

        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) throws BookingNotFoundException, BookingAccessDeniedException {

        log.info("GetBookingById input: bookingId={}, userId={}", bookingId, userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking id=%d not found".formatted(bookingId)));

        if (!booking.getItem().getOwner().getId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new BookingAccessDeniedException("User id=%d has no access to booking id=%d".formatted(userId, bookingId));
        }

        log.info("GetBookingById success: id={}", booking.getId());

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        log.info("GetUserBookings input: userId={}, state={}", userId, state);

        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);

        return bookings.stream()
                .filter(booking -> filterByState(booking, state))
                .map(bookingMapper::toDto)
                .toList();
    }

    private boolean filterByState(Booking booking, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case ALL -> true;
            case CURRENT -> booking.getStart().isBefore(now) && (booking.getEnd().isAfter(now) ||
                    booking.getEnd() == null);
            case PAST -> booking.getEnd().isBefore(now);
            case FUTURE -> booking.getStart().isAfter(now);
            case WAITING -> booking.getStatus() == BookingStatus.WAITING;
            case REJECTED -> booking.getStatus() == BookingStatus.REJECTED;
        };
    }

    public List<BookingDto> getOwnerBookings(Long userId, BookingState state) throws NoItemsAvailableForBookingException, UserNotFoundException {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (!itemRepository.existsByOwnerId(userId)) {
            throw new NoItemsAvailableForBookingException("User id=%d has no items for booking".formatted(userId));
        }

        List<Long> itemIds = itemRepository.findAllByOwnerIdOrderById(userId)
                .stream()
                .map(Item::getId)
                .toList();

        return bookingRepository.findByItemIdIn(itemIds)
                .stream()
                .filter(booking -> filterByState(booking, state))
                .map(bookingMapper::toDto)
                .toList();
    }
}
