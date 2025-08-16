package ru.practicum.shareit.item.legacy;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import java.util.List;
import java.util.Optional;

/**
 * In-memory interface.
 * @deprecated replaced with JPA-implementation {@link ItemRepository}
 */
@Deprecated
public interface ItemRepositoryDeprecated {

    Optional<Item> getById(Long itemId);

    boolean existsById(Long itemId);

    boolean deleteById(Long itemId);

    Item save(Item item);

    List<Item> getAllByOwner(Long ownerId);

    List<Item> searchByText(String searchText);

}
