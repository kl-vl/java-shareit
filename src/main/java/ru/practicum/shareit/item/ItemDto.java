package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;


@Getter
@Setter
@Builder(toBuilder = true)
public class ItemDto {
    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = Create.class)
    private Long id;

    @NotBlank(groups = Create.class)
    @Nullable
    @Size(max = 255, groups = {Create.class, Update.class})
    private String name;

    @NotBlank(groups = Create.class)
    @Nullable
    @Size(max = 255, groups = {Create.class, Update.class})
    private String description;

    @NotNull(groups = Create.class)
    private Boolean available;

    private Long ownerId;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}