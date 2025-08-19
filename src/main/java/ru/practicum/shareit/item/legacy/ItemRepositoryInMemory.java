package ru.practicum.shareit.item.legacy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation.
 * @deprecated replaced with JPA-implementation {@link ItemRepository}
 */
@Deprecated
@Repository
@Profile("legacy")
public class ItemRepositoryInMemory implements ItemRepositoryDeprecated {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public boolean existsById(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public boolean deleteById(Long itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        return items.remove(itemId) != null;
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            long newId = idCounter.getAndIncrement();
            item.setId(newId);
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> searchByText(String searchText) {
        if (StringUtils.isBlank(searchText)) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item -> StringUtils.containsIgnoreCase(item.getName(), searchText) ||
                        StringUtils.containsIgnoreCase(item.getDescription(), searchText))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllByOwner(Long ownerId) {
        synchronized (items) {
            return items.values().stream()
                    .filter(item -> item.getOwner() != null)
                    .filter(item -> ownerId.equals(item.getOwner().getId()))
                    .collect(Collectors.toList());
        }
    }
}
