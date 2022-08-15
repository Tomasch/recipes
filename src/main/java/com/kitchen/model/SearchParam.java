package com.kitchen.model;

import lombok.Getter;

@Getter
public enum SearchParam {
    HAVING_INGREDIENT_WORDS("wing"),
    WITHOUT_INGREDIENT_WORDS("ning"),
    HAVING_INSTRUCTION_WORDS("wins"),
    WITHOUT_INSTRUCTION_WORDS("nins"),
    SERVINGS_NUMBER("serv"),
    MEAL_TYPE("t");
    private String searchParamName;
    SearchParam(String searchParamName) {
        this.searchParamName = searchParamName;
    }
}
