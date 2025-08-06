package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidateException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Long userId = itemDto.getOwnerId();
        User owner = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        Item item = itemMapper.toItem(itemDto);
        Item createdItem = itemRepository.save(item);
        return itemMapper.toDto(createdItem);
    }

    @Override
    public boolean exists(Long itemId) {
        return  itemRepository.existsById(itemId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Long userId = itemDto.getOwnerId();
        User owner = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Item existingItem = itemRepository.getById(itemDto.getId())
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemDto.getId() + " not found"));;
        if (!Objects.equals(owner.getId(),existingItem.getOwner().getId())) {
            throw new ItemValidateException("Item id " +  itemDto.getId() + " belongs to user id " + existingItem.getOwner().getId() + ", not " + owner.getId());
        }

        itemMapper.updateItemFromDto(itemDto, existingItem);
        Item updatedItem = itemRepository.save(itemMapper.toItem(itemDto));
        return itemMapper.toDto(updatedItem);
    }

    public ItemDto getItemById(Long itemId) {
        return itemRepository.getById(itemId).stream().map(itemMapper::toDto).findFirst().orElse(null);
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        return itemRepository.getAllByOwner(userId).stream().map(itemMapper::toDto).toList();
    }

    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchByText(text).stream().map(itemMapper::toDto).toList();
    }

}
