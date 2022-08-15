package com.kitchen.service.validator;

import com.kitchen.model.dto.RecipeInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NumberValidatorTest {
    private NumberValidator numberValidator = new NumberValidator();
    @Mock
    ConstraintValidatorContext context;
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Test
    void emptyValue() {
        boolean valid = numberValidator.isValid(null, context);
        assertTrue(valid);
    }

    @Test
    void invalidNumber() {
        boolean valid = numberValidator.isValid("notANumber", context);
        assertFalse(valid);
    }

    @Test
    void instructionsPresent() {
        boolean valid = numberValidator.isValid("99", context);
        assertTrue(valid);
    }
}