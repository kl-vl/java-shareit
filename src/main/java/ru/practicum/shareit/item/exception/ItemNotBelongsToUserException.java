package ru.practicum.shareit.item.exception;

public class ItemNotBelongsToUserException extends Exception {
    public ItemNotBelongsToUserException(String message) {
        super(message);
    }
}
