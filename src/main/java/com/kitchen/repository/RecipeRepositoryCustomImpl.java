package com.kitchen.repository;

import com.kitchen.model.dto.SearchRequest;
import com.kitchen.model.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    @PersistenceContext
    EntityManager em;
    @Override
    public List<Recipe> find(SearchRequest searchRequest) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        CriteriaQuery<Recipe> all = cq.select(root).where(cb.and(searchInputToPredicates(searchRequest, root, cb)));
        TypedQuery<Recipe> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    private Predicate[] searchInputToPredicates(SearchRequest searchParams, Root<Recipe> root, CriteriaBuilder cb) { //todo case insensitive
        Stream<Predicate> havingIngredients = searchParams.getHavingIngredientWords().stream()
                .map(keyword -> getLikePredicate(cb, root, "ingredients", keyword));
        Stream<Predicate> havingInstructions = searchParams.getHavingInstructionWords().stream()
                .map(keyword -> getLikePredicate(cb, root, "instructions", keyword));
        Stream<Predicate> havingServingsNumbers = searchParams.getServingsNumbers().stream()
                .map(keyword -> cb.equal(root.get("servingsNumber"), keyword));
        Stream<Predicate> havingMealTypes = searchParams.getMealTypes().stream()
                .map(keyword -> cb.equal(root.get("mealType"), Recipe.MealType.valueOf(keyword)));
        Stream<Predicate> withoutIngredients = searchParams.getWithoutIngredientWords().stream()
                .map(keyword -> cb.not(getLikePredicate(cb, root, "ingredients", keyword)));
        Stream<Predicate> withoutInstructions = searchParams.getWithoutInstructionWords().stream()
                .map(keyword -> cb.not(getLikePredicate(cb, root, "instructions", keyword)));

        return Stream.of(havingIngredients, havingInstructions,
                        withoutIngredients, withoutInstructions,
                        havingServingsNumbers, havingMealTypes)
                .flatMap(Function.identity()).toArray(Predicate[]::new);
    }

    private Predicate getLikePredicate(CriteriaBuilder cb, Root<Recipe> root, String instructions, String keyword) {
        return cb.like(root.get(instructions), "%" + keyword + "%");
    }
}
