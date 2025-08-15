package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
public class BookingDto {
    public interface Create {
    }

    public interface Update {
    }

    public interface Approve {
    }

    @Null(groups = {BookingDto.Create.class, BookingDto.Approve.class})
    private Long id;

    @NotNull(groups = BookingDto.Create.class)
    @FutureOrPresent(groups = BookingDto.Create.class)
    private LocalDateTime start;

    @NotNull(groups = BookingDto.Create.class)
    @FutureOrPresent(groups = BookingDto.Create.class)
    private LocalDateTime end;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ItemDto item;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto booker;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BookingStatus status;

    @NotNull(groups = BookingDto.Approve.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long bookerId;

    @NotNull(groups = BookingDto.Approve.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    boolean approved;

    @NotNull(groups = {BookingDto.Create.class, BookingDto.Approve.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long itemId;


}
