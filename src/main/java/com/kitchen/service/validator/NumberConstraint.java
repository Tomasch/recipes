package com.kitchen.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = NumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberConstraint {
    String message() default "must be a number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}