package com.morethanheroic.swords.combat.domain.effect.entry;

import com.morethanheroic.swords.combat.domain.CombatEffectDataHolder;
import com.morethanheroic.swords.combat.domain.CombatEffectServiceAccessor;
import com.morethanheroic.swords.combat.domain.effect.CombatEffectApplyingContext;
import com.morethanheroic.swords.combat.domain.effect.CombatEffectDefinition;
import com.morethanheroic.swords.combat.domain.entity.UserCombatEntity;
import com.morethanheroic.swords.effect.domain.EffectSettingDefinitionHolder;
import com.morethanheroic.swords.inventory.domain.InventoryEntity;

public class LesserIdentifyEffectDefinition extends CombatEffectDefinition {

    public LesserIdentifyEffectDefinition(EffectSettingDefinitionHolder effectSettingDefinitionHolder) {
        super(effectSettingDefinitionHolder);
    }

    @Override
    public void apply(CombatEffectApplyingContext effectApplyingContext, CombatEffectDataHolder combatEffectDataHolder, CombatEffectServiceAccessor combatEffectServiceAccessor) {
        final InventoryEntity inventoryEntity = combatEffectServiceAccessor.getInventoryFacade().getInventory(((UserCombatEntity) effectApplyingContext.getDestination().getCombatEntity()).getUserEntity());

        final int realItem = combatEffectServiceAccessor.getUnidentifiedItemIdCalculator().getRealItemId(combatEffectDataHolder.getSessionEntity(), Integer.parseInt((String) combatEffectDataHolder.getParameters().get("itemId")));

        if (inventoryEntity.hasItem(realItem, false)) {
            inventoryEntity.removeItem(realItem, 1, false);
            inventoryEntity.addItem(realItem, 1, true);
        }
    }

    @Override
    public String getId() {
        return "lesser_identify";
    }
}
