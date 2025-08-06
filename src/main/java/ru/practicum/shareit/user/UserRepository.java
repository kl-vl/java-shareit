package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> getById(Long userId);

    Optional<User> getByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user);

    boolean deleteById(Long userId);
}
