package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemRequest {
    private final Long id;
    private final String description;
    private final User requestor;
    private final LocalDateTime created;

}