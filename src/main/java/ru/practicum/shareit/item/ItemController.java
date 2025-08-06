package ru.practicum.shareit.item;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                              @RequestBody @Validated(ItemDto.Create.class) ItemDto itemDto) {
        log.info("Create item {} by user {}", itemDto.getName(), userId);

        itemDto.setOwnerId(userId);
        ItemDto createdItem = itemService.createItem(itemDto);

        log.debug("Created item: {}", createdItem);

        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                              @PathVariable @Positive Long itemId,
                              @RequestBody @Validated(ItemDto.Update.class) ItemDto itemDto) {

        log.info("Update item {} by user id {}", itemId, userId);

        itemDto.setId(itemId);
        itemDto.setOwnerId(userId);
        ItemDto updatedItem = itemService.updateItem(itemDto);

        log.debug("Updated item: {}", updatedItem);

        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Get item by ID {}", itemId);
        ItemDto returnedItem = itemService.getItemById(itemId);
        log.debug("Returned item: {}", returnedItem);
        return returnedItem;
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Get items by owner {}", userId);
        List<ItemDto> items = itemService.getItemsByUserId(userId);
        log.debug("Items by owner {} count: {}", userId, items.size());
        return items;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Search items by text: '{}'", text);
        List<ItemDto> items = itemService.searchItems(text);
        log.debug("Items by text {} count: {}", text, items.size());
        return items;
    }

}