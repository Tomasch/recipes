package com.kitchen.service;

import com.kitchen.model.dto.SearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import static com.kitchen.model.SearchParam.HAVING_INGREDIENT_WORDS;
import static com.kitchen.model.SearchParam.HAVING_INSTRUCTION_WORDS;
import static com.kitchen.model.SearchParam.MEAL_TYPE;
import static com.kitchen.model.SearchParam.SERVINGS_NUMBER;
import static com.kitchen.model.SearchParam.WITHOUT_INGREDIENT_WORDS;
import static com.kitchen.model.SearchParam.WITHOUT_INSTRUCTION_WORDS;

@Service
public class SearchRequestService {
    public SearchRequest map(MultiValueMap<String, String> searchTerms) {
        SearchRequest searchRequest = new SearchRequest();
        if (searchTerms.containsKey(HAVING_INSTRUCTION_WORDS.getSearchParamName())) {
            searchRequest.setHavingInstructionWords(searchTerms.get(HAVING_INSTRUCTION_WORDS.getSearchParamName()));
        }
        if (searchTerms.containsKey(WITHOUT_INSTRUCTION_WORDS.getSearchParamName())) {
            searchRequest.setWithoutInstructionWords(searchTerms.get(WITHOUT_INSTRUCTION_WORDS.getSearchParamName()));
        }
        if (searchTerms.containsKey(HAVING_INGREDIENT_WORDS.getSearchParamName())) {
            searchRequest.setHavingIngredientWords(searchTerms.get(HAVING_INGREDIENT_WORDS.getSearchParamName()));
        }
        if (searchTerms.containsKey(WITHOUT_INGREDIENT_WORDS.getSearchParamName())) {
            searchRequest.setWithoutIngredientWords(searchTerms.get(WITHOUT_INGREDIENT_WORDS.getSearchParamName()));
        }
        if (searchTerms.containsKey(SERVINGS_NUMBER.getSearchParamName())) {
            searchRequest.setServingsNumbers(searchTerms.get(SERVINGS_NUMBER.getSearchParamName()));
        }
        if (searchTerms.containsKey(MEAL_TYPE.getSearchParamName())) {
            searchRequest.setMealTypes(searchTerms.get(MEAL_TYPE.getSearchParamName()));
        }
        return searchRequest;
    }
}
