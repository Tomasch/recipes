package com.kitchen.service.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class NumberValidator implements ConstraintValidator<NumberConstraint, String> {
    private static final Predicate<String> IS_NUMBER = s -> Pattern.compile("[0-9]*").matcher(s).matches();
    public void initialize(NumberConstraint constraint) {
    }

    public boolean isValid(String numberText,
                           ConstraintValidatorContext context) {
        return StringUtils.isBlank(numberText) || IS_NUMBER.test(numberText);
    }
}
