package org.locator.iplocator.controllers;

import lombok.extern.slf4j.Slf4j;
import org.locator.iplocator.validation.ValidationError;
import org.locator.iplocator.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(ValidationException e) {
        List<ValidationError> errors = e.getValidationManager().getErrors().stream().distinct().toList();
        return ResponseEntity.badRequest().body(errors);
    }
}
