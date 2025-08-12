package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> getById(Long itemId);

    boolean existsById(Long itemId);

    boolean deleteById(Long itemId);

    Item save(Item item);

    List<Item> getAllByOwner(Long ownerId);

    List<Item> searchByText(String searchText);

}
