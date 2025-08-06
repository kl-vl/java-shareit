package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryInMemory implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new UserValidateException("User cannot be null");
        }
        synchronized (users) {
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
    }

    @Override
    public boolean deleteById(Long userId) {
        if (userId == null) {
            return false;
        }
        synchronized (users) {
            User removedUser = users.remove(userId);
            if (removedUser == null) {
                return false;
            }
            usersByEmail.remove(removedUser.getEmail());
            return true;
        }
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
