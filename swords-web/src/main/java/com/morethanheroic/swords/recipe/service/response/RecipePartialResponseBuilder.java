package com.morethanheroic.swords.recipe.service.response;

import com.morethanheroic.response.service.PartialResponseBuilder;
import com.morethanheroic.swords.recipe.domain.RecipeDefinition;
import com.morethanheroic.swords.recipe.service.response.domain.RecipePartialResponse;
import com.morethanheroic.swords.recipe.service.response.domain.configuration.RecipePartialResponseBuilderConfiguration;
import com.morethanheroic.swords.recipe.service.response.experience.CookingRecipeExperienceListPartialResponseBuilder;
import com.morethanheroic.swords.recipe.service.response.experience.domain.RecipeExperienceListPartialResponseBuilderConfiguration;
import com.morethanheroic.swords.recipe.service.response.ingredient.RecipeIngredientListPartialResponseBuilder;
import com.morethanheroic.swords.recipe.service.response.ingredient.domain.RecipeIngredientListPartialResponseBuilderConfiguration;
import com.morethanheroic.swords.recipe.service.response.requirement.RecipeRequirementListPartialResponseBuilder;
import com.morethanheroic.swords.recipe.service.response.requirement.domain.RecipeRequirementListPartialResponseBuilderConfiguration;
import com.morethanheroic.swords.recipe.service.response.reward.RecipeRewardListPartialResponseBuilder;
import com.morethanheroic.swords.recipe.service.response.reward.domain.RecipeRewardListPartialResponseBuilderConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecipePartialResponseBuilder implements PartialResponseBuilder<RecipePartialResponseBuilderConfiguration> {

    private final RecipeIngredientListPartialResponseBuilder recipeIngredientListPartialResponseBuilder;
    private final RecipeRewardListPartialResponseBuilder recipeRewardListPartialResponseBuilder;
    private final CookingRecipeExperienceListPartialResponseBuilder cookingRecipeExperienceListPartialResponseBuilder;
    private final RecipeRequirementListPartialResponseBuilder recipeRequirementListPartialResponseBuilder;

    @Override
    public RecipePartialResponse build(RecipePartialResponseBuilderConfiguration recipePartialResponseBuilderConfiguration) {
        final RecipeDefinition recipeDefinition = recipePartialResponseBuilderConfiguration.getRecipeDefinition();

        return RecipePartialResponse.builder()
                .id(recipeDefinition.getId())
                .name(recipeDefinition.getName())
                .chance(recipeDefinition.getChance())
                .recipeIngredients(
                        recipeIngredientListPartialResponseBuilder.build(
                                RecipeIngredientListPartialResponseBuilderConfiguration.builder()
                                        .recipeIngredients(recipeDefinition.getRecipeIngredients())
                                        .build()
                        )
                )
                .recipeRewards(
                        recipeRewardListPartialResponseBuilder.build(
                                RecipeRewardListPartialResponseBuilderConfiguration.builder()
                                        .recipeRewards(recipeDefinition.getRecipeRewards())
                                        .build()
                        )
                )
                .recipeExperiences(
                        cookingRecipeExperienceListPartialResponseBuilder.build(
                                RecipeExperienceListPartialResponseBuilderConfiguration.builder()
                                        .recipeExperiences(recipeDefinition.getRecipeExperiences())
                                        .build()
                        )
                )
                .recipeRequirements(
                        recipeRequirementListPartialResponseBuilder.build(
                                RecipeRequirementListPartialResponseBuilderConfiguration.builder()
                                .recipeRequirements(recipeDefinition.getRecipeRequirements())
                                .build()
                        )
                )
                .build();
    }
}
