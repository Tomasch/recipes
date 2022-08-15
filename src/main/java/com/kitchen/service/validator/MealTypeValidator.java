package com.kitchen.service.validator;

import com.kitchen.model.entity.Recipe;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MealTypeValidator implements ConstraintValidator<MealTypeConstraint, String> {
    private static final String ALL_MEAL_TYPES = Arrays.stream(Recipe.MealType.values()).map(Enum::name).collect(Collectors.joining(", "));

    @Override
    public void initialize(MealTypeConstraint constraint) {
    }

    @Override
    public boolean isValid(String MealTypeText,
                           ConstraintValidatorContext context) {
        if (Objects.isNull(MealTypeText)) {
            return true;
        }
        if (isAnyMealType(MealTypeText)) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        String errorMessage = "Given meal type " + MealTypeText + " not matching domain values " + ALL_MEAL_TYPES;
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        return false;
    }

    private boolean isAnyMealType(String vegetarianText) {
        return Arrays.stream(Recipe.MealType.values())
                .anyMatch(mealType -> mealType.name().equals(vegetarianText));
    }
}
