package ru.practicum.shareit.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("All users");
        List<UserDto> users = userService.getAllUsers();
        log.debug("Users list size: {}", users.size());
        return users;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable @Min(1) Long userId) {
        log.info("Get user by id {}", userId);
        UserDto user = userService.getUserById(userId);
        log.debug("User: {}", user);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated(UserDto.Create.class) UserDto userDto) {
        log.info("Create user with email {}", userDto.getEmail());
        UserDto createdUser = userService.createUser(userDto);
        log.debug("Created user: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @PathVariable @Positive Long userId,
            @RequestBody @Validated(UserDto.Update.class) UserDto userDto) {
        log.info("Update user with id {}", userId);
        userDto = userDto.toBuilder().id(userId).build();
        UserDto updatedUser = userService.updateUser(userDto);
        log.debug("Updated user: {}", updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUserById(@PathVariable @Min(1) Long userId) {
        log.info("Delete user with id {}", userId);
        boolean deleteResult = userService.deleteUserById(userId);
        log.debug("Deleted user: {}", deleteResult);
        return deleteResult;
    }

}
