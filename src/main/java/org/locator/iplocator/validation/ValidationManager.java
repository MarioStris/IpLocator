package org.locator.iplocator.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationManager implements Serializable {

    private final List<ValidationError> errors;

    public ValidationManager() {
        this.errors = new ArrayList<>();
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void add(String message) {
        errors.add(new ValidationError(message));
    }
}
