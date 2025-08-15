package ru.practicum.shareit.booking;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
public class BookingMapperDependencies {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Item findItemById(Long id) {
        if (id == null) return null;
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
    }

    public User findUserById(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public String getUserName(User author) {
        if (author == null) return null;
        if (author.getName() != null) return author.getName();
        return userRepository.findById(author.getId())
                .map(User::getName)
                .orElse(null);
    }
}