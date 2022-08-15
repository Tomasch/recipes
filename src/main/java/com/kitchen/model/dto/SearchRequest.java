package com.kitchen.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter @Setter
public class SearchRequest {
    List<String> withoutInstructionWords = Collections.emptyList();
    List<String> havingInstructionWords = Collections.emptyList();
    List<String> withoutIngredientWords = Collections.emptyList();
    List<String> havingIngredientWords = Collections.emptyList();
    List<String> servingsNumbers = Collections.emptyList();
    List<String> mealTypes = Collections.emptyList();

    @Override
    public String toString() {
        return "FilterDto{" +
                "withoutInstructionWords=" + withoutInstructionWords +
                ", havingInstructionWords=" + havingInstructionWords +
                ", withoutIngredientWords=" + withoutIngredientWords +
                ", havingIngredientsWords=" + havingIngredientWords +
                ", servingsNumber='" + servingsNumbers + '\'' +
                ", mealType='" + mealTypes + '\'' +
                '}';
    }
}
