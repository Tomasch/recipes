package com.kitchen.model.entity.converter;

import com.kitchen.exception.InvalidRecipeTypeException;
import com.kitchen.model.entity.Recipe;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class RecipeTypeConverter implements AttributeConverter<Recipe.MealType, String> {
    @Override
    public String convertToDatabaseColumn(Recipe.MealType attribute) {
        return attribute.name();
    }

    @Override
    public Recipe.MealType convertToEntityAttribute(String dbData) {
        return Arrays.stream(Recipe.MealType.values())
                .filter(mealType -> mealType.name().equalsIgnoreCase(dbData))
                .findFirst().orElseThrow(() -> new InvalidRecipeTypeException("Found unknown meal type in DB: " + dbData));
    }
}
