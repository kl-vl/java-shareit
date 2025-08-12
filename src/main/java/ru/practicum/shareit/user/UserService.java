package ru.practicum.shareit.user;

import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId) throws UserNotFoundException;

    UserDto createUser(UserDto userDto) throws UserEmailAlreadyExistsException, UserValidateException;

    UserDto updateUser(UserDto userDto) throws UserEmailAlreadyExistsException, UserNotFoundException, UserValidateException;

    boolean deleteUserById(Long userId);
}
