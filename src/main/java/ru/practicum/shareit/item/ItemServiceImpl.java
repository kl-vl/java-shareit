package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto) throws UserNotFoundException {
        Long userId = itemDto.getOwnerId();
        log.info("CreateItem input: name={}, ownerId={}", itemDto.getName(), userId);

        User owner = userRepository.getById(userId)
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
    public ItemDto updateItem(ItemDto itemDto) throws UserNotFoundException, ItemNotFoundException, ItemValidateException {
        Long userId = itemDto.getOwnerId();

        log.info("UpdateItem input: id={}, ownerId={}", itemDto.getId(), userId);

        User owner = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Item existingItem = itemRepository.getById(itemDto.getId())
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemDto.getId() + " not found"));

        if (!Objects.equals(owner.getId(), existingItem.getOwner().getId())) {
            throw new ItemValidateException("Item id " + itemDto.getId() + " belongs to user id " + existingItem.getOwner().getId() + ", not " + owner.getId());
        }

        itemMapper.updateItemFromDto(itemDto, existingItem);
        Item updatedItem = itemRepository.save(itemMapper.toItem(itemDto));

        log.info("UpdateItem success: id={}", updatedItem.getId());

        return itemMapper.toDto(updatedItem);
    }

    public ItemDto getItemById(Long itemId) throws ItemNotFoundException {
        log.info("GetItem input: id={}", itemId);

        ItemDto itemDto = itemRepository.getById(itemId).stream().map(itemMapper::toDto).findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId));

        log.info("GetItem success: id={}", itemDto.getId());

        return itemDto;
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("GetItemsByUser input: ownerId={}", userId);


        List<ItemDto> items = itemRepository.getAllByOwner(userId).stream().map(itemMapper::toDto).toList();

        log.info("GetItemsByUser success: size={}", items.size());

        return items;
    }

    public List<ItemDto> searchItems(String text) {
       log.info("SearchItemsByText input: text={}", text);

       List<ItemDto> items = itemRepository.searchByText(text).stream().map(itemMapper::toDto).toList();

       log.info("SearchItemsByText success: size={}", items.size());

       return items;
    }

}
