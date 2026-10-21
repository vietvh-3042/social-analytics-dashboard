package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> badRequest(IllegalArgumentException exception) {
    return error(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, MaxUploadSizeExceededException.class})
  public ResponseEntity<Map<String, Object>> invalidInput(Exception exception) {
    return error(HttpStatus.BAD_REQUEST, "Request validation failed");
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> notFound(ResourceNotFoundException exception) {
    return error(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> internalError(RuntimeException exception) {
    log.error("Unhandled API exception", exception);
    return error(HttpStatus.INTERNAL_SERVER_ERROR, "The request could not be completed");
  }

  private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(
            Map.of(
                "status",
                status.value(),
                "error",
                status.getReasonPhrase(),
                "message",
                message,
                "timestamp",
                Instant.now().toString()));
  }
}
