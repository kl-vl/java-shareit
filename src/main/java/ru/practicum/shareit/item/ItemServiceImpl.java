package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingMapperDependencies;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.exception.ItemNotBelongsToUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidateException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapperDependencies bookingMapperDependencies;


    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto) throws UserNotFoundException {
        Long userId = itemDto.getOwnerId();
        log.info("CreateItem input: name={}, ownerId={}", itemDto.getName(), userId);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Item item = itemMapper.toItem(itemDto);
        Item createdItem = itemRepository.save(item);

        log.info("CreateItem success: id={}", createdItem.getId());

        return itemMapper.toDto(createdItem);
    }

    @Override
    public boolean exists(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto) throws UserNotFoundException, ItemNotFoundException, ItemValidateException {
        Long userId = itemDto.getOwnerId();

        log.info("UpdateItem input: id={}, ownerId={}", itemDto.getId(), userId);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Item existingItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemDto.getId() + " not found"));

        if (!Objects.equals(owner.getId(), existingItem.getOwner().getId())) {
            throw new ItemValidateException("Item id " + itemDto.getId() + " belongs to user id " + existingItem.getOwner().getId() + ", not " + owner.getId());
        }

        itemMapper.updateFromDto(itemDto, existingItem);
        Item updatedItem = itemRepository.save(existingItem);

        log.info("UpdateItem success: id={}", updatedItem.getId());

        return itemMapper.toDto(updatedItem);
    }

    public ItemDto getItemById(Long itemId, Long userId) throws ItemNotFoundException {
        log.info("GetItem input: id={}", itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item id=%d not found".formatted(itemId)));

        ItemDto itemDto = itemMapper.toDtoWithComments(item);
        enrichItemDtoWithBookings(itemDto, item, userId);
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        itemDto.setComments(itemMapper.mapComments(comments));

        log.info("GetItem success: id={}", itemDto.getId());

        return itemDto;
    }

    private void enrichItemDtoWithBookings(ItemDto itemDto, Item item, Long userId) {
        if (Objects.equals(userId, item.getOwner().getId())) {
            LocalDateTime now = LocalDateTime.now();
            bookingRepository.findLastBooking(item, now)
                    .ifPresent(b -> itemDto.setLastBooking(bookingMapper.toDto(b)));
            bookingRepository.findNextBooking(item, now)
                    .ifPresent(b -> itemDto.setNextBooking(bookingMapper.toDto(b)));
        }
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("GetItemsByUser input: ownerId={}", userId);

        List<ItemDto> items = itemRepository.findAllByOwnerIdOrderById(userId).stream().map(itemMapper::toDto).toList();

        log.info("GetItemsByUser success: size={}", items.size());

        return items;
    }

    public List<ItemDto> searchItems(String text) {
        log.info("SearchItemsByText input: text={}", text);

        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<ItemDto> items = itemRepository.searchAvailableByText(text).stream().map(itemMapper::toDto).toList();

        log.info("SearchItemsByText success: size={}", items.size());

        return items;
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto) throws ItemNotBelongsToUserException, ItemNotFoundException, UserNotFoundException {
        Long userId = commentDto.getUserId();
        Long itemId = commentDto.getItemId();

        log.info("CreateComment input: userId={}, itemId={}", userId, itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item id=%d not found".formatted(itemId)));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User id=%d not found".formatted(userId)));

        //TODO
        //LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);;
        boolean hasBookings = bookingRepository.existsBookingByItemIdAndBookerIdAndStatusIsAndEndBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (!hasBookings) {
            throw new ItemNotBelongsToUserException("User id=%d can't comment on item id=%d".formatted(userId, itemId));
        }

        Comment comment = commentMapper.toEntity(commentDto);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment, bookingMapperDependencies);
    }

}
