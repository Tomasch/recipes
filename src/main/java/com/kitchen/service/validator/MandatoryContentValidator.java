package com.kitchen.service.validator;

import com.kitchen.model.dto.RecipeInput;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class MandatoryContentValidator implements ConstraintValidator<MandatoryContentConstraint, RecipeInput> {
    public void initialize(MandatoryContentConstraint constraint) {
    }

    public boolean isValid(RecipeInput recipeInput,
                           ConstraintValidatorContext context) {
        if (StringUtils.isBlank(recipeInput.getIngredients()) && StringUtils.isBlank(recipeInput.getInstructions())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Either ingredients or instructions field must be filled in").addConstraintViolation();
            return false;
        }
        return true;
    }
}
