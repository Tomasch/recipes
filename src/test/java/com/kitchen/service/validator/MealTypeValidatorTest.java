package com.kitchen.service.validator;

import com.kitchen.model.dto.RecipeInput;
import com.kitchen.model.entity.Recipe;
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
class MealTypeValidatorTest {
    private MealTypeValidator mealTypeValidator = new MealTypeValidator();
    @Mock
    ConstraintValidatorContext context;
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Test
    void emptyValue() {
        boolean valid = mealTypeValidator.isValid(null, context);
        assertTrue(valid);
    }

    @Test
    void incorrectValue() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean valid = mealTypeValidator.isValid("pastafarian", context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(anyString());
        assertFalse(valid);
    }

    @Test
    void correctValue() {
        boolean valid = mealTypeValidator.isValid(Recipe.MealType.VEGETARIAN.name(), context);
        assertTrue(valid);
    }
}