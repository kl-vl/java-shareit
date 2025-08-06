package ru.practicum.shareit.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class UserDto {
    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = {Create.class, Update.class})
    private final Long id;

    @NotBlank(groups = Create.class)
    @Nullable
    private final String name;

    @NotBlank(groups = Create.class) // Только при создании
    @Email(groups = {Create.class, Update.class}) // Формат всегда
    private final String email;

}