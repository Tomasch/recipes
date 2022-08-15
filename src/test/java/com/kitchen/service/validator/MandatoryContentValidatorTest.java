package com.kitchen.service.validator;

import com.kitchen.model.dto.RecipeInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MandatoryContentValidatorTest {
    private MandatoryContentValidator mandatoryContentValidator = new MandatoryContentValidator();
    @Mock
    ConstraintValidatorContext context;
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Test
    void mandatoryMissing() {
        RecipeInput recipeInput = new RecipeInput();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean valid = mandatoryContentValidator.isValid(recipeInput, context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(anyString());
        assertFalse(valid);
    }

    @Test
    void ingredientsPresent() {
        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setIngredients("someIngredients");
        boolean valid = mandatoryContentValidator.isValid(recipeInput, context);
        assertTrue(valid);
    }

    @Test
    void instructionsPresent() {
        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setInstructions("someInstructionss");
        boolean valid = mandatoryContentValidator.isValid(recipeInput, context);
        assertTrue(valid);
    }
}