package com.kitchen;

import com.kitchen.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTest {
    @Autowired
    private RecipeService recipeService;
    @Test
    public void contextLoads() {
        assertNotNull(recipeService);
    }
}