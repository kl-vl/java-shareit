package ru.practicum.shareit.booking.exception;

public class NoItemsAvailableForBookingException extends Exception {
    public NoItemsAvailableForBookingException(String message) {
        super(message);
    }
}
