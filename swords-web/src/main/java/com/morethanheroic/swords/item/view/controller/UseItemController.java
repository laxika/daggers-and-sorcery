package com.morethanheroic.swords.item.view.controller;

import com.morethanheroic.session.domain.SessionEntity;
import com.morethanheroic.swords.combat.domain.CombatEffectDataHolder;
import com.morethanheroic.swords.combat.service.item.UseItemService;
import com.morethanheroic.swords.item.service.definition.cache.ItemDefinitionCache;
import com.morethanheroic.swords.item.service.response.UseItemResponseBuilder;
import com.morethanheroic.swords.response.domain.CharacterRefreshResponse;
import com.morethanheroic.swords.user.domain.UserEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UseItemController {

    private final UseItemService useItemService;
    private final ItemDefinitionCache itemDefinitionCache;
    private final UseItemResponseBuilder useItemResponseBuilder;

    @Transactional
    @RequestMapping(value = "/item/use/{itemId}", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public CharacterRefreshResponse useItem(UserEntity userEntity, SessionEntity sessionEntity, @RequestParam Map<String, String> allRequestParams, @PathVariable int itemId) {
        if (useItemService.canUseItem(userEntity, itemDefinitionCache.getDefinition(itemId))) {
            useItemService.useItem(userEntity, itemDefinitionCache.getDefinition(itemId), new CombatEffectDataHolder((Map) allRequestParams, sessionEntity));

            return useItemResponseBuilder.build(userEntity, true);
        }

        return useItemResponseBuilder.build(userEntity, false);
    }
}
