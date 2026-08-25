package com.mnemora.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice makes this class "listen" for exceptions thrown
// by ANY controller in the app, and lets us handle them in one central place
// instead of writing try/catch in every single controller method.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // This method specifically catches EntryNotFoundException, wherever it's thrown
    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntryNotFound(EntryNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        // Returns HTTP 404 with a clean JSON body instead of a stack trace
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // This catches validation failures — e.g. when @NotBlank fields are missing
    // (remember the DiaryEntryRequest DTO validation from earlier)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");

        // Collect all field-specific error messages, e.g. "title: Title is required"
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        body.put("fieldErrors", fieldErrors);

        // Returns HTTP 400 Bad Request with details on exactly which fields failed
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}