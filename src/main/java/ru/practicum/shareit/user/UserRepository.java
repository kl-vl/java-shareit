package ru.practicum.shareit.user;

import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> getById(Long userId);

    Optional<User> getByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user) throws UserValidateException;

    boolean deleteById(Long userId);
}
