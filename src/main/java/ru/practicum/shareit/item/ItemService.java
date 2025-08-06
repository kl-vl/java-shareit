package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto item);

    ItemDto updateItem(ItemDto item);

    ItemDto getItemById(Long itemId);

    boolean exists(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text);
}
