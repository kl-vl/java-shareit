package ru.practicum.shareit.item.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
public class CommentDto {
    public interface Create {
    }

    @Null(groups = Create.class)
    private long id;
    @NotBlank(groups = ItemDto.Create.class)
    private String text;

    private String authorName;

    @Null
    private Long userId;

    private LocalDateTime created;

    @Null
    private Long itemId;
}