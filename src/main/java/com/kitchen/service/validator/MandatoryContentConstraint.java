package com.kitchen.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = MandatoryContentValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MandatoryContentConstraint {
    String message() default "No content filled in";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}