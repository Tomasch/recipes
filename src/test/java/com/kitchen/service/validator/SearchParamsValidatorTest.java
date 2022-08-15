package com.kitchen.service.validator;

import com.kitchen.model.SearchParam;
import com.kitchen.model.entity.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import javax.validation.ConstraintValidatorContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchParamsValidatorTest {
    private SearchParamsValidator searchParamsValidator = new SearchParamsValidator();
    @Mock
    ConstraintValidatorContext context;
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Test
    void emptyValue() {
        boolean valid = searchParamsValidator.isValid(CollectionUtils.toMultiValueMap(Collections.emptyMap()), context);
        assertTrue(valid);
    }

    @Test
    void incorrectValue() {
        MultiValueMap<String, String> multiValueMap = CollectionUtils.toMultiValueMap(Map.of("incorrectSearchParam", List.of("someValue")));
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        boolean valid = searchParamsValidator.isValid(multiValueMap, context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(anyString());
        assertFalse(valid);
    }

    @Test
    void correctValue() {
        MultiValueMap<String, String> multiValueMap = CollectionUtils.toMultiValueMap(Map.of(SearchParam.MEAL_TYPE.getSearchParamName(), List.of(Recipe.MealType.VEGETARIAN.name())));
        boolean valid = searchParamsValidator.isValid(multiValueMap, context);
        assertTrue(valid);
    }
}