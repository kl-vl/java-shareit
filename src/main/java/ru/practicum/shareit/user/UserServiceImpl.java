package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) throws UserEmailAlreadyExistsException, UserValidateException {
        log.info("CreateUser input: email={}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserEmailAlreadyExistsException("User with email %s already exists".formatted(userDto.getEmail()));
        }
        User user = userMapper.toUserFromCreateDto(userDto);
        User savedUser = userRepository.save(user);

        log.info("CreateUser succes: id={}", savedUser.getId());

        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("GetAllUsers");

        List<UserDto> users = userRepository.findAll().stream().map(userMapper::toDto).toList();

        log.debug("GetAllUsers success: size: {}", users.size());

        return users;

    }

    @Override
    public UserDto getUserById(Long userId) throws UserNotFoundException {
        log.info("GetUser input: id={}", userId);

        UserDto userDto = userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));

        log.debug("GetUser success: id={}", userId);

        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws UserEmailAlreadyExistsException, UserNotFoundException, UserValidateException {
        log.info("UpdateUser input: id={}, email={}", userDto.getId(), userDto.getEmail());
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userDto.getId())));

        if (userDto.getEmail() != null &&
                !userDto.getEmail().equals(existingUser.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserEmailAlreadyExistsException(userDto.getEmail());
        }

        userMapper.updateUserFromDto(userDto, existingUser);
        User updatedUser = userRepository.save(existingUser);

        log.info("UpdateUser success: id={}", updatedUser.getId());

        return userMapper.toDto(updatedUser);
    }

    @Override
    public boolean deleteUserById(Long userId) {
        log.info("DeleteUser input: id={}", userId);

        userRepository.deleteById(userId);

        log.info("DeleteUser success: id={}", userId);

        return true;
    }
}
