package ru.practicum.shareit.user.legacy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation.
 * @deprecated replaced with JPA-implementation {@link UserRepository}
 */
@Deprecated
@Repository
@Profile("legacy")
public class UserRepositoryInMemory implements UserRepositoryDeprecated {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User save(User user) throws UserValidateException {
        if (user == null) {
            throw new UserValidateException("User cannot be null");
        }
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
        } else {
            User existingUser = users.get(user.getId());
            if (existingUser != null && !existingUser.getEmail().equals(user.getEmail())) {
                usersByEmail.remove(existingUser.getEmail());
            }
        }
        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail(), user);

        return user;
    }

    @Override
    public boolean deleteById(Long userId) {
        if (userId == null) {
            return false;
        }
        User removedUser = users.remove(userId);
        if (removedUser == null) {
            return false;
        }
        usersByEmail.remove(removedUser.getEmail());
        return true;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email);
    }
}
