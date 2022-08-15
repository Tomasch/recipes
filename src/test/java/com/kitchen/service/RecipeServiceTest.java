package com.kitchen.service;

import com.kitchen.exception.RecipeNotFoundException;
import com.kitchen.model.dto.RecipeInput;
import com.kitchen.model.entity.Recipe;
import com.kitchen.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    SearchRequestService searchRequestService;
    @Mock
    Recipe recipe;

    @InjectMocks
    private RecipeService recipeService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        recipeService = new RecipeService(recipeRepository, searchRequestService);
    }

    @Test
    void add() {
        RecipeInput recipeInput = getValidRecipeInput();
        recipeService.add(recipeInput);
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void get() {
        Recipe recipe = getValidRecipe();
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        Recipe returnedRecipe = recipeService.get(6);
        assertEquals(recipe, returnedRecipe);
    }

    @Test
    void getFailed() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.get(6));
    }

    @Test
    void find() {
        when(recipeRepository.find(any())).thenReturn(List.of(getValidRecipe()));
        assertFalse(recipeService.find(any()).isEmpty());
    }

    @Test
    void findNotFound() {
        when(recipeRepository.find(any())).thenReturn(Collections.emptyList());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.find(any()));
    }

    @Test
    void update() {
        when(recipeRepository.findById(any())).thenReturn(Optional.of(getValidRecipe()));
        when(recipeRepository.save(any())).thenReturn(getValidRecipe());
        Recipe update = recipeService.update(5, getValidRecipeInput());
        assertNotNull(update);
    }

    @Test
    void updateNonexistent() {
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.update(6L, getValidRecipeInput()));
    }

    @Test
    void deleteById() {
        recipeService.deleteById(6);
        verify(recipeRepository, times(1)).deleteById(any());
    }

    private Recipe getValidRecipe() {
        Recipe recipe = new Recipe();
        RecipeInput recipeInput = getValidRecipeInput();
        recipe.setMealType(Recipe.MealType.valueOf(recipeInput.getMealType()));
        recipe.setIngredients(recipeInput.getIngredients());
        recipe.setInstructions(recipeInput.getInstructions());
        recipe.setServingsNumber(Long.parseLong(recipeInput.getServingsNumber()));
        return recipe;
    }

    private RecipeInput getValidRecipeInput() {
        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setMealType("VEGETARIAN");
        recipeInput.setIngredients("potatoes, sour cream, chicken breast");
        recipeInput.setInstructions("Bake sliced potatoes mixed with sour cream and chicken on the top");
        recipeInput.setServingsNumber("2");
        return recipeInput;
    }

    @AfterEach
    void closeService() throws Exception {
        autoCloseable.close();
    }
}