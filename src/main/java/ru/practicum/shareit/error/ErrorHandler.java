package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import ru.practicum.shareit.booking.exception.BookingAccessDeniedException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingStartAndEndDatesAreNotEqualsException;
import ru.practicum.shareit.booking.exception.NoItemsAvailableForBookingException;
import ru.practicum.shareit.item.exception.ItemNotBelongsToUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidateException;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleUserEmailAlreadyExists(UserEmailAlreadyExistsException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("User with same email already exists {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "EMAIL_NOT_UNIQUE", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Invalid value")
                ));

        log.warn("Method argument validation error in {} : {}", request.getDescription(false), errors);

        return new ErrorResponseDto("Validation failed", "VALIDATION_ERROR", errors);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMissingRequestHeader(MissingRequestHeaderException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Missing request header{}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "MISSING_REQUEST_HEADER", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUserNotFound(UserNotFoundException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Missing request header{}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "USER_NOT_FOUND", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(BookingAccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBookingAccessDenied(BookingAccessDeniedException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Booking access denied {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "BOOKING_ACCESS_DENIED", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleBookingNotFound(BookingNotFoundException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Booking not found {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "BOOKING_NOT_FOUND", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(NoItemsAvailableForBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleNoItemsAvailableForBooking(NoItemsAvailableForBookingException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("No items available for booking {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "NO_ITEMS_FOR_BOOKING", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(ItemNotBelongsToUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleItemNotBelongsToUser(ItemNotBelongsToUserException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Item not belongs to user {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "ITEM_NOT_BELONGS_TO_USER", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleItemNotFound(ItemNotFoundException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Item not found {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "ITEM_NOT_FOUND", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(BookingStartAndEndDatesAreNotEqualsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBookingStartAndEndDatesAreNotEquals(BookingStartAndEndDatesAreNotEqualsException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Item not found {}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "BOOKING_START_END_DATES_ARE_EQUALS", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(ItemValidateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleItemValidate(ItemValidateException ex, WebRequest request, HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.warn("Item Validate Error{}: {}", request.getDescription(false), ex.getMessage());

        return new ErrorResponseDto(ex.getMessage(), "ITEM_VALIDATION", Map.of(
                "controller", controllerName,
                "method", methodName
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Internal error in {}: {}", request.getDescription(true), ex.getMessage(), ex);

        return new ErrorResponseDto("Internal server error", "INTERNAL_ERROR", Map.of(ex.getClass().getSimpleName(), ex.getMessage()));
    }

}
