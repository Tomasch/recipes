package com.kitchen.service;

import com.kitchen.exception.RecipeNotFoundException;
import com.kitchen.model.dto.RecipeInput;
import com.kitchen.model.dto.SearchRequest;
import com.kitchen.model.entity.Recipe;
import com.kitchen.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final SearchRequestService searchRequestService;

    public Recipe add(RecipeInput recipeInput) {
        return recipeRepository.save(applyValues(recipeInput, new Recipe()));
    }

    public Recipe get(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(
                        "Not found recipe with ID " + id));
    }

    public List<Recipe> find(MultiValueMap<String, String> searchInput) {
        SearchRequest searchRequest = searchRequestService.map(searchInput);
        List<Recipe> recipes = recipeRepository.find(searchRequest);
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipe found for given search terms");
        }
        return recipes;
    }

    public Recipe update(long id, RecipeInput recipeInput) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Found no recipe with id " + id));
        return recipeRepository.save(applyValues(recipeInput, recipe));
    }

    public void deleteById(long id) {
        recipeRepository.deleteById(id);
    }

    /**
     * Applies values from recipeInput to recipe. Mutates the recipe argument.
     * @param recipeInput
     * @param recipe argument is mutated in the method in order not to create detached entity
     * @return recipe entity object
     */
    private Recipe applyValues(RecipeInput recipeInput, Recipe recipe) {
        String mealType = recipeInput.getMealType();
        if (StringUtils.isNotBlank(mealType)) {
            recipe.setMealType(Recipe.MealType.valueOf(mealType));
        }
        String servingsNumber = recipeInput.getServingsNumber();
        if (StringUtils.isNotBlank(servingsNumber)) {
            recipe.setServingsNumber(Long.parseLong(servingsNumber));
        }
        recipe.setIngredients(recipeInput.getIngredients());
        recipe.setInstructions(recipeInput.getInstructions());
        return recipe;
    }
}
