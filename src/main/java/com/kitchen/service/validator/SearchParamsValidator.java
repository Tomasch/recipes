package com.kitchen.service.validator;

import com.kitchen.model.SearchParam;
import com.kitchen.model.entity.Recipe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kitchen.model.SearchParam.MEAL_TYPE;
import static com.kitchen.model.SearchParam.SERVINGS_NUMBER;

public class SearchParamsValidator implements ConstraintValidator<SearchParamsConstraint, MultiValueMap<String, String>> {
    private static final Predicate<String> IS_NUMBER = s -> Pattern.compile("[0-9]+").matcher(s).matches();
    private static final String ALL_MEAL_TYPES = Arrays.stream(Recipe.MealType.values()).map(Enum::name).collect(Collectors.joining(", "));

    public void initialize(SearchParamsConstraint constraint) {
    }

    public boolean isValid(MultiValueMap<String, String> passedSearchParams,
                           ConstraintValidatorContext context) {
        String unknownSearchParams = getUnknownSearchParams(passedSearchParams);
        if (StringUtils.isNotBlank(unknownSearchParams)) {
            createErrorMessage(context, "Unknown search terms passed: " + unknownSearchParams);
            return false;
        }
        if (passedSearchParams.containsKey(SERVINGS_NUMBER.getSearchParamName())) {
            if (passedSearchParams.get(SERVINGS_NUMBER.getSearchParamName()).size() > 1) {
                createErrorMessage(context, "All conditions in search params are to be treated as AND queries," +
                        "therefore multiple servings number would return no result");
                return false;
            }
            String servingNumbersNotValid = getServingsNumberNotValid(passedSearchParams);
            if (StringUtils.isNotBlank(servingNumbersNotValid)) {
                createErrorMessage(context, "Serving number must be a number. Invalid values are: " + servingNumbersNotValid);
                return false;
            }
        }
        if (passedSearchParams.containsKey(MEAL_TYPE.getSearchParamName())) {
            if (passedSearchParams.get(MEAL_TYPE.getSearchParamName()).size() > 1) {
                createErrorMessage(context, "All conditions in search params are to be treated as AND queries," +
                        "therefore multiple meal types would return no result");
                return false;
            }
            String unknownMealTypes = getUnknownMealTypes(passedSearchParams);
            if (StringUtils.isNotBlank(unknownMealTypes)) {
                createErrorMessage(context, "Meal type should be one of " + ALL_MEAL_TYPES
                        + ". Invalid values are: " + unknownMealTypes);
                return false;
            }
        }
        return true;
    }

    private String getServingsNumberNotValid(MultiValueMap<String, String> passedSearchParams) {
        return passedSearchParams.get(SERVINGS_NUMBER.getSearchParamName()).stream()
                .filter(s -> !IS_NUMBER.test(s))
                .collect(Collectors.joining(", "));
    }

    private String getUnknownMealTypes(MultiValueMap<String, String> passedSearchParams) {
        return passedSearchParams.get(MEAL_TYPE.getSearchParamName()).stream()
                .filter(value -> Arrays.stream(Recipe.MealType.values()).map(Enum::name).noneMatch(name -> name.equalsIgnoreCase(value)))
                .collect(Collectors.joining(", "));
    }

    private String getUnknownSearchParams(MultiValueMap<String, String> passedSearchParams) {
        return passedSearchParams.keySet().stream()
                .filter(term -> !"_csrf".equals(term))
                .filter(term -> Arrays.stream(SearchParam.values())
                        .map(SearchParam::getSearchParamName)
                        .noneMatch(term::equals))
                .collect(Collectors.joining(", "));
    }

    private void createErrorMessage(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
    }
}