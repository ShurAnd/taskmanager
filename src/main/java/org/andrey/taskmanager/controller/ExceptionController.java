package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.andrey.taskmanager.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для перехвата и обработки вывода при исключениях
 */
@RestControllerAdvice
public class ExceptionController {

    private final ObjectMapper objectMapper;

    @Autowired
    public ExceptionController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorDetails handleValidationException(
            ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach((error) -> {
            String errorMessage = error.getMessage();
            errors.add(errorMessage);
        });
        return new ErrorDetails(String.join(", ", errors), LocalDateTime.now().toString());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(OperationNotAllowedException.class)
    public ErrorDetails handleOperationNotAllowedException(
            OperationNotAllowedException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now().toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorDetails handleTaskNotFoundException(
            TaskNotFoundException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now().toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorDetails handleUserNotFoundException(
            UserNotFoundException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now().toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchStatusException.class)
    public ErrorDetails handleNoSuchStatusException(
            NoSuchStatusException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now().toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchPriorityException.class)
    public ErrorDetails handleNoSuchPriorityException(
            NoSuchPriorityException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now().toString());
    }
}
