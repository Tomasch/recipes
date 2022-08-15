package com.kitchen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.Application;
import com.kitchen.model.SearchParam;
import com.kitchen.model.dto.RecipeInput;
import com.kitchen.model.entity.Recipe;
import com.kitchen.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class RecipeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecipeRepository recipeRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    void postRequest() throws Exception {
        RecipeInput recipeInput = getValidRecipeInput();
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(recipeInput))
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void postRequestInvalid() throws Exception {
        RecipeInput recipeInput = getValidRecipeInput();
        recipeInput.setMealType("InvalidValue");
        String content = objectMapper.writeValueAsString(recipeInput);
        mockMvc.perform(post("/recipes")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().is(400))
                .andReturn().getResolvedException().getMessage().contains("meal type");
    }

    @Test
    void getRequestFailed() throws Exception {
        Recipe recipe = getOtherValidRecipe();
        recipe.setId(999L);
        mockMvc.perform(get("/recipes/{id}", recipe.getId())
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException().getMessage().contains("Not found");
    }

    @Test
    void getRequest() throws Exception {
        recipeRepository.save(getOtherValidRecipe());
        Recipe recipe = recipeRepository.save(getValidRecipe());
        mockMvc.perform(get("/recipes/{id}", recipe.getId())
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(recipe.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mealType").value(recipe.getMealType().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.servingsNumber").value(recipe.getServingsNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients").value(recipe.getIngredients()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions").value(recipe.getInstructions()));

//        mockMvc.perform(get("/recipes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(recipeInput))
//                ).andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId").exists());

//        MvcResult result = mockMvc.perform(get("/recipes/1/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isOk())
//                .andDo(document("recipes"
////                        requestFields(fieldWithPath("weather.whatever"))//,
////                        responseFields(beneathPath("recipe").withSubsectionId("recipeSubs"),
////                                fieldWithPath("id").description("recipe ID"),
////                                fieldWithPath("mealType").description("Type of meal of values VEGETARIAN, NONVEGETARIAN"),
////                                fieldWithPath("ingredients").description("Ingredients for the recipe"))
//                ))
//                .andDo(document("recipes",
//                                responseFields(
//                                        fieldWithPath("id").description("recipe ID"),
//                                        fieldWithPath("mealType").description("Type of meal of values VEGETARIAN, NONVEGETARIAN"),
//                                        fieldWithPath("servingsNumber").description("Type of meal of values VEGETARIAN, NONVEGETARIAN"),
//                                        fieldWithPath("ingredients").description("Ingredients for the recipe"),
//                                        fieldWithPath("instructions").description("Instructions to make a recipe"),
//                                        fieldWithPath("version").description("Version number indicating how many times the recipe was updated")
////
//                                ))
//                )
//                .andDo(print())
//                .andReturn();
////        List<Recipe> recipes = objectMapper.readValue(result.getResponse().getContentAsString(),
////                objectMapper.getTypeFactory().constructCollectionType(List.class, Recipe.class));
////        assertFalse(recipes.isEmpty());
//        Recipe recipe = objectMapper.readValue(result.getResponse().getContentAsString(), Recipe.class);
//        assertTrue(recipe.getId() != '\0');
    }

    @Test
    void updateRequest() throws Exception {
        Recipe recipe = recipeRepository.save(getValidRecipe());
        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setMealType(Recipe.MealType.NONVEGETARIAN.name());
        recipeInput.setInstructions("updatedInstructions");
        recipeInput.setIngredients("updatedIngredients");
        recipeInput.setServingsNumber("5");
        String content = objectMapper.writeValueAsString(recipeInput);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/recipes/{id}", recipe.getId())
                        .content(content)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(recipe.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mealType").value(recipeInput.getMealType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.servingsNumber").value(recipeInput.getServingsNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients").value(recipeInput.getIngredients()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions").value(recipeInput.getInstructions()))
                .andDo((document("recipes-input",
                        pathParameters(parameterWithName("id").description("Recipe ID")),
                        requestFields(
                                fieldWithPath("mealType").description("One of categories: " +
                                        Arrays.stream(Recipe.MealType.values()).map(Enum::name)
                                                .collect(Collectors.joining(", "))),
                                fieldWithPath("servingsNumber").description("Estimated portions amount").type(Number.class.getSimpleName()),
                                fieldWithPath("ingredients").description("Ingredients used. Maximum length of that field is 100 characters").attributes(key("max length").value("100")),
                                fieldWithPath("instructions").description("How to make a recipe. Maximum length of that field is 500 characters").attributes(key("max length").value("500"))
                        ))))
                .andDo(document("recipes-entity",
                                responseFields(
                                        fieldWithPath("id").description("recipe ID"),
                                        fieldWithPath("mealType").description("One of categories: " +
                                                Arrays.stream(Recipe.MealType.values()).map(Enum::name)
                                                        .collect(Collectors.joining(", "))),
                                        fieldWithPath("servingsNumber").description("Estimated portions amount"),
                                        fieldWithPath("ingredients").description("Ingredients used"),
                                        fieldWithPath("instructions").description("How to make a recipe"),
                                        fieldWithPath("version").description("Shows amount of updates")
//
                                ))
                );
    }

    @Test
    void findNoFilters() throws Exception {
        Recipe recipe = recipeRepository.save(getValidRecipe());
        Recipe otherRecipe = recipeRepository.save(getOtherValidRecipe());
        mockMvc.perform(get("/recipes")
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id",
                        containsInAnyOrder(recipe.getId().intValue(), otherRecipe.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].mealType",
                        containsInAnyOrder(recipe.getMealType().name(), otherRecipe.getMealType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].servingsNumber",
                        containsInAnyOrder(recipe.getServingsNumber().intValue(), otherRecipe.getServingsNumber().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients",
                        containsInAnyOrder(recipe.getIngredients(), otherRecipe.getIngredients())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].instructions",
                        containsInAnyOrder(recipe.getInstructions(), otherRecipe.getInstructions())));

    }

    @Test
    void findAllFilters() throws Exception {
        Recipe recipeToFind = recipeRepository.save(getValidRecipe());
        recipeRepository.save(getOtherValidRecipe());
        mockMvc.perform(RestDocumentationRequestBuilders.get("/recipes")
                        .param(SearchParam.HAVING_INGREDIENT_WORDS.getSearchParamName(), getFirstWord(recipeToFind.getIngredients()))
                        .param(SearchParam.WITHOUT_INGREDIENT_WORDS.getSearchParamName(), "leek")
                        .param(SearchParam.HAVING_INSTRUCTION_WORDS.getSearchParamName(), getFirstWord(recipeToFind.getInstructions()))
                        .param(SearchParam.WITHOUT_INSTRUCTION_WORDS.getSearchParamName(), "steam")
                        .param(SearchParam.MEAL_TYPE.getSearchParamName(), Recipe.MealType.VEGETARIAN.name())
                        .param(SearchParam.SERVINGS_NUMBER.getSearchParamName(), String.valueOf(recipeToFind.getServingsNumber()))
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(recipeToFind.getId().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mealType").value(recipeToFind.getMealType().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].servingsNumber").value(recipeToFind.getServingsNumber().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ingredients").value(recipeToFind.getIngredients()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instructions").value(recipeToFind.getInstructions()))
                .andDo(document("search-params", requestParameters(
                        parameterWithName("wing").description("With word inside ingredients"),
                        parameterWithName("ning").description("Excluding word inside ingredients"),
                        parameterWithName("wins").description("With word inside instructions"),
                        parameterWithName("nins").description("Excluding word inside instructions"),
                        parameterWithName("t").description("Having type of meal from values: " + Arrays.stream(Recipe.MealType.values()).map(Enum::name).collect(Collectors.toList())),
                        parameterWithName("serv").description("With amount of servings"),
                        parameterWithName("_csrf").description("CSRF header to secure clients form requests")
                )));
    }

    @Test
    void findWrongFilter() throws Exception {
        mockMvc.perform(get("/recipes")
                        .param("wrongFilterName", "someFilterValue")
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRequest() throws Exception {
        Recipe recipeToDelete = recipeRepository.save(getValidRecipe());
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/{id}", recipeToDelete.getId())
                        .with(csrf()).with(user("test"))
                ).andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(recipeRepository.findById(recipeToDelete.getId()).isEmpty());
    }

    private String getFirstWord(String phrase) {
        String[] result = phrase.split(" ", 1);
        return result[0];
    }

    private RecipeInput getValidRecipeInput() {
        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setMealType("VEGETARIAN");
        recipeInput.setIngredients("potatoes, sour cream, chicken breast");
        recipeInput.setInstructions("Bake sliced potatoes mixed with sour cream and chicken on the top");
        recipeInput.setServingsNumber("2");
        return recipeInput;
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

    private Recipe getOtherValidRecipe() {
        Recipe recipe = new Recipe();
        recipe.setMealType(Recipe.MealType.NONVEGETARIAN);
        recipe.setIngredients("other ingredients");
        recipe.setInstructions("other instructions");
        recipe.setServingsNumber(3L);
        return recipe;
    }
}