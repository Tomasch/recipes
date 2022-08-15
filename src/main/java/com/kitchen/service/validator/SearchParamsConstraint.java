package com.kitchen.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = SearchParamsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchParamsConstraint {
    String message() default "Bad search term";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}