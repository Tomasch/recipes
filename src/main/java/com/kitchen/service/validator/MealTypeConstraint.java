package com.kitchen.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = MealTypeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MealTypeConstraint {
    String message() default "Value out of domain values for meal type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}