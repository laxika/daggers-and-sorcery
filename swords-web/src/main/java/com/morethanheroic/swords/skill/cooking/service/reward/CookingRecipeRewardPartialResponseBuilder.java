package com.morethanheroic.swords.skill.cooking.service.reward;

import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.recipe.domain.RecipeReward;
import com.morethanheroic.swords.response.service.PartialResponseBuilder;
import com.morethanheroic.swords.skill.cooking.service.reward.domain.CookingRecipeRewardPartialResponse;
import com.morethanheroic.swords.skill.cooking.service.reward.domain.CookingRecipeRewardPartialResponseBuilderConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookingRecipeRewardPartialResponseBuilder implements PartialResponseBuilder<CookingRecipeRewardPartialResponseBuilderConfiguration> {

    private final ItemDefinitionCache itemDefinitionCache;

    @Override
    public CookingRecipeRewardPartialResponse build(CookingRecipeRewardPartialResponseBuilderConfiguration responseBuilderConfiguration) {
        final RecipeReward recipeReward = responseBuilderConfiguration.getRecipeReward();

        return CookingRecipeRewardPartialResponse.builder()
                .name(itemDefinitionCache.getDefinition(recipeReward.getId()).getName())
                .amount(recipeReward.getAmount())
                .build();
    }
}
