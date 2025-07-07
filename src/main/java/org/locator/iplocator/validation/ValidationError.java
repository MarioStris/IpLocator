package org.locator.iplocator.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValidationError implements Serializable {
    private String message;
}
