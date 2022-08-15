package com.kitchen.repository;

import com.kitchen.model.dto.SearchRequest;
import com.kitchen.model.entity.Recipe;

import java.util.List;

public interface RecipeRepositoryCustom {
    List<Recipe> find(SearchRequest searchRequest);
}
