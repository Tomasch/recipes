package com.kitchen.controller;

import com.kitchen.model.dto.RecipeInput;
import com.kitchen.model.entity.Recipe;
import com.kitchen.service.validator.MandatoryContentConstraint;
import com.kitchen.service.validator.NumberConstraint;
import com.kitchen.service.validator.SearchParamsConstraint;
import com.kitchen.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Validated
public class RecipeController {
    private final RecipeService recipeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Recipe create(@RequestBody @Valid @MandatoryContentConstraint RecipeInput recipeInput) {
        return recipeService.add(recipeInput);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Recipe get(@PathVariable("id") long id) {
        return recipeService.get(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Recipe> find(@RequestParam(required = false) @SearchParamsConstraint
                                MultiValueMap<String, String> searchInput) {
        return recipeService.find(searchInput);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Recipe update(@PathVariable("id") long id,
                         @RequestBody @Valid @MandatoryContentConstraint RecipeInput recipeInput) {
        return recipeService.update(id, recipeInput);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        recipeService.deleteById(id);
    }
}
