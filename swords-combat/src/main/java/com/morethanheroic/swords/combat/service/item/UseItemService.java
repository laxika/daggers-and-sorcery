package com.morethanheroic.swords.combat.service.item;

import com.google.common.collect.Lists;
import com.morethanheroic.swords.attribute.service.calc.GlobalAttributeCalculator;
import com.morethanheroic.swords.combat.domain.CombatEffectDataHolder;
import com.morethanheroic.swords.combat.entity.domain.CombatEntity;
import com.morethanheroic.swords.combat.entity.domain.UserCombatEntity;
import com.morethanheroic.swords.combat.domain.effect.CombatEffectTarget;
import com.morethanheroic.swords.combat.domain.effect.CombatEffectApplyingContext;
import com.morethanheroic.swords.combat.step.domain.CombatStep;
import com.morethanheroic.swords.combat.step.domain.DefaultCombatStep;
import com.morethanheroic.swords.combat.service.CombatEffectApplierService;
import com.morethanheroic.swords.combat.step.message.CombatMessageFactory;
import com.morethanheroic.swords.inventory.service.InventoryEntityFactory;
import com.morethanheroic.swords.item.domain.ItemDefinition;
import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UseItemService {

    private final GlobalAttributeCalculator globalAttributeCalculator;
    private final CombatEffectApplierService combatEffectApplierService;
    private final InventoryEntityFactory inventoryEntityFactory;
    private final CombatMessageFactory combatMessageFactory;

    public boolean canUseItem(UserEntity userEntity, ItemDefinition item) {
        return inventoryEntityFactory.getEntity(userEntity).hasItem(item);
    }

    public List<CombatStep> useItem(UserCombatEntity combatEntity, ItemDefinition item, CombatEffectDataHolder combatEffectDataHolder) {
        final List<CombatStep> combatSteps = new ArrayList<>();

        if (canUseItem(combatEntity.getUserEntity(), item)) {
            combatSteps.add(
                    DefaultCombatStep.builder()
                            .message(combatMessageFactory.newMessage("spell", "COMBAT_MESSAGE_ITEM_USED", item.getName()))
                            .build()
            );

            applyItem(combatEntity, item, combatEffectDataHolder);

            combatEntity.getUserEntity().setBasicStats(combatEntity.getActualHealth(), combatEntity.getActualMana(), combatEntity.getUserEntity().getMovementPoints());
        } else {
            combatSteps.add(
                    DefaultCombatStep.builder()
                            .message(combatMessageFactory.newMessage("spell", "COMBAT_MESSAGE_CANT_USE_ITEM", item.getName()))
                            .build()
            );
        }

        return combatSteps;
    }

    public void useItem(UserEntity userEntity, ItemDefinition item, CombatEffectDataHolder combatEffectDataHolder) {
        if (canUseItem(userEntity, item)) {
            applyItem(userEntity, item, combatEffectDataHolder);
        }
    }

    //TODO: merge the two applyItem together somehow!
    private void applyItem(CombatEntity combatEntity, ItemDefinition item, CombatEffectDataHolder combatEffectDataHolder) {
        final List<CombatEffectApplyingContext> contexts = item.getCombatEffects().stream()
                .map(effectSettingDefinitionHolder -> CombatEffectApplyingContext.builder()
                        .source(new CombatEffectTarget(combatEntity))
                        .destination(new CombatEffectTarget(combatEntity))
                        .combatSteps(Lists.newArrayList())
                        .effectSettings(effectSettingDefinitionHolder)
                        .sessionEntity(combatEffectDataHolder.getSessionEntity())
                        .parameters(combatEffectDataHolder.getParameters())
                        .build()
                )
                .collect(Collectors.toList());

        combatEffectApplierService.applyEffects(contexts, combatEffectDataHolder);
    }

    private void applyItem(UserEntity userEntity, ItemDefinition item, CombatEffectDataHolder combatEffectDataHolder) {
        final UserCombatEntity userCombatEntity = new UserCombatEntity(userEntity, globalAttributeCalculator);

        final List<CombatEffectApplyingContext> contexts = item.getCombatEffects().stream()
                .map(effectSettingDefinitionHolder -> CombatEffectApplyingContext.builder()
                        .source(new CombatEffectTarget(userCombatEntity))
                        .destination(new CombatEffectTarget(userCombatEntity))
                        .combatSteps(Lists.newArrayList())
                        .effectSettings(effectSettingDefinitionHolder)
                        .sessionEntity(combatEffectDataHolder.getSessionEntity())
                        .parameters(combatEffectDataHolder.getParameters())
                        .build()
                )
                .collect(Collectors.toList());

        combatEffectApplierService.applyEffects(contexts, combatEffectDataHolder);

        userEntity.setBasicStats(userCombatEntity.getActualHealth(), userCombatEntity.getActualMana(), userCombatEntity.getActualMovement());
    }
}
