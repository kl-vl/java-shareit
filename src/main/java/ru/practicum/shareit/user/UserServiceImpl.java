package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserEmailAlreadyExistsException("User with email %s already exists".formatted(userDto.getEmail()));
        }
        User user = userMapper.toUserFromCreateDto(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.getById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User existingUser = userRepository.getById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userDto.getId())));

        if (userDto.getEmail() != null &&
                !userDto.getEmail().equals(existingUser.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserEmailAlreadyExistsException(userDto.getEmail());
        }

        userMapper.updateUserFromDto(userDto, existingUser);
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public boolean deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        return true;
    }
}
