package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.exception.ItemNotBelongsToUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto item) throws UserNotFoundException;

    ItemDto updateItem(ItemDto item) throws UserNotFoundException, ItemNotFoundException, ItemValidateException;

    ItemDto getItemById(Long itemId, Long userId) throws ItemNotFoundException;

    boolean exists(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(CommentDto commentDto) throws ItemNotBelongsToUserException, ItemNotFoundException, UserNotFoundException;
}
