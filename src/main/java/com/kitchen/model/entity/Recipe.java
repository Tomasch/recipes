package com.kitchen.model.entity;

import com.kitchen.model.entity.converter.RecipeTypeConverter;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "RECIPE")
@NoArgsConstructor
@Getter @Setter
public class Recipe {
    @Id
    @GeneratedValue
    @Column(name = "RECIPE_ID")
    public Long id;

    @Convert(converter = RecipeTypeConverter.class)
    @Column(name = "MEAL_TYPE")
    public MealType mealType;

    @Column(name = "SERVINGS_NUMBER")
    public Long servingsNumber;

    @Column(name = "INGREDIENTS")
    public String ingredients;

    @Column(name = "INSTRUCTIONS")
    public String instructions;

    @Version
    @Column(name = "VERSION")
    public Long version;

    @Getter
    public enum MealType {
        VEGETARIAN,
        NONVEGETARIAN
    }
}
