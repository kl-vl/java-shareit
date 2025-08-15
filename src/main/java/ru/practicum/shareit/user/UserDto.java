package ru.practicum.shareit.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255)
    private final String name;

    @NotBlank(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    @Size(max = 512)
    private final String email;

}