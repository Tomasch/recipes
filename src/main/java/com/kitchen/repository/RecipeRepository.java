package com.kitchen.repository;

import com.kitchen.model.dto.SearchRequest;
import com.kitchen.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RecipeRepository extends
        JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe>, RecipeRepositoryCustom {
    List<Recipe> find(SearchRequest searchRequest);
}
