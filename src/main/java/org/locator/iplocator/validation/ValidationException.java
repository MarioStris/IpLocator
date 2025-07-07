package org.locator.iplocator.validation;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{

    private final ValidationManager validationManager;

    public ValidationException(ValidationManager validationManager) {
        this.validationManager = validationManager;
    }

    public ValidationException(String message) {
        this.validationManager = new ValidationManager();
        validationManager.add(message);
    }
}