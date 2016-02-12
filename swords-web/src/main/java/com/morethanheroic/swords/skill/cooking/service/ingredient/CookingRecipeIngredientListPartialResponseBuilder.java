package com.morethanheroic.swords.skill.cooking.service.ingredient;

import com.morethanheroic.swords.recipe.domain.RecipeIngredient;
import com.morethanheroic.swords.response.service.PartialResponseCollectionBuilder;
import com.morethanheroic.swords.skill.cooking.service.ingredient.domain.CookingRecipeIngredientListPartialResponseBuilderConfiguration;
import com.morethanheroic.swords.skill.cooking.service.ingredient.domain.CookingRecipeIngredientPartialResponse;
import com.morethanheroic.swords.skill.cooking.service.ingredient.domain.CookingRecipeIngredientPartialResponseBuilderConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookingRecipeIngredientListPartialResponseBuilder implements PartialResponseCollectionBuilder<CookingRecipeIngredientListPartialResponseBuilderConfiguration> {

    private final CookingRecipeIngredientPartialResponseBuilder cookingRecipeIngredientPartialResponseBuilder;

    @Override
    public List<CookingRecipeIngredientPartialResponse> build(CookingRecipeIngredientListPartialResponseBuilderConfiguration responseBuilderConfiguration) {
        final List<CookingRecipeIngredientPartialResponse> result = new ArrayList<>();

        final List<RecipeIngredient> recipeIngredients = responseBuilderConfiguration.getRecipeIngredients();
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            result.add(
                    cookingRecipeIngredientPartialResponseBuilder.build(
                            CookingRecipeIngredientPartialResponseBuilderConfiguration.builder()
                                    .recipeIngredient(recipeIngredient)
                                    .build()
                    )
            );
        }

        return result;
    }
}
