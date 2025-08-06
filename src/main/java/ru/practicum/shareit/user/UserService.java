package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    boolean deleteUserById(Long userId);
}
