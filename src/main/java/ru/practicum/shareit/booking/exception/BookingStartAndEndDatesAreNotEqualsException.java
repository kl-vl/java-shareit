package ru.practicum.shareit.booking.exception;

public class BookingStartAndEndDatesAreNotEqualsException extends Exception {
    public BookingStartAndEndDatesAreNotEqualsException(String message) {
        super(message);
    }
}
