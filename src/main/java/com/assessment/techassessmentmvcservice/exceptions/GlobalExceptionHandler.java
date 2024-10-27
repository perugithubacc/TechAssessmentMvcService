package com.assessment.techassessmentmvcservice.exceptions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDetails>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetails> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> new ErrorDetails(((FieldError) error).getField(), error.getDefaultMessage(),
                        ex.getStatusCode().value()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleGlobalExceptions(GlobalException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false), ex.getStatus().value());
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }
}

