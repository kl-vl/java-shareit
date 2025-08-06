package ru.practicum.shareit.user.exception;

public class UserEmailAlreadyExistsException extends Exception {
    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
