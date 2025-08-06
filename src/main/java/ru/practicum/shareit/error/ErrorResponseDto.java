package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private final String message;
    private final String error;
    private final LocalDateTime timestamp;
    private final Map<String, String> details;

    public ErrorResponseDto(String message, String error) {
        this(message, error, LocalDateTime.now(), null);
    }

    public ErrorResponseDto(String message, String error, Map<String, String> details) {
        this(message, error, LocalDateTime.now(), details);
    }

}
