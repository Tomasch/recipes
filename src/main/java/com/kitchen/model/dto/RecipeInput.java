package com.kitchen.model.dto;

import com.kitchen.service.validator.NumberConstraint;
import com.kitchen.service.validator.MealTypeConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@Getter @Setter
@Validated
public class RecipeInput {
    @Valid
    @MealTypeConstraint
    String mealType;
    @NumberConstraint
    String servingsNumber;
    @Length(max = 100)
    String ingredients;
    @Length(max = 500)
    String instructions;
}
