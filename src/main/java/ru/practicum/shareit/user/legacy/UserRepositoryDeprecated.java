package ru.practicum.shareit.user.legacy;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.List;
import java.util.Optional;

/**
 * In-memory interface.
 * @deprecated replaced with JPA-implementation {@link UserRepository}
 */
@Deprecated
public interface UserRepositoryDeprecated {

    List<User> findAll();

    Optional<User> findById(Long userId);

    Optional<User> getByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user) throws UserValidateException;

    boolean deleteById(Long userId);
}
