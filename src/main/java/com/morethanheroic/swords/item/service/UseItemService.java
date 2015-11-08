package com.morethanheroic.swords.item.service;

import com.morethanheroic.swords.attribute.service.calc.GlobalAttributeCalculator;
import com.morethanheroic.swords.combat.domain.entity.CombatEntity;
import com.morethanheroic.swords.combat.domain.entity.UserCombatEntity;
import com.morethanheroic.swords.combat.service.CombatEffectApplierService;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.item.domain.ItemDefinition;
import com.morethanheroic.swords.user.domain.UserEntity;
import com.morethanheroic.swords.user.repository.domain.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UseItemService {

    private final CombatEffectApplierService combatEffectApplierService;
    private final InventoryFacade inventoryFacade;
    private final UserMapper userMapper;

    @Autowired
    private GlobalAttributeCalculator globalAttributeCalculator;

    @Autowired
    public UseItemService(CombatEffectApplierService combatEffectApplierService, InventoryFacade inventoryFacade, UserMapper userMapper) {
        this.combatEffectApplierService = combatEffectApplierService;
        this.inventoryFacade = inventoryFacade;
        this.userMapper = userMapper;
    }

    public boolean canUseItem(UserEntity userEntity, ItemDefinition item) {
        return inventoryFacade.getInventory(userEntity).hasItem(item.getId());
    }

    public void useItem(UserCombatEntity combatEntity, ItemDefinition item) {
        if (canUseItem(combatEntity.getUserEntity(), item)) {
            applyItem(combatEntity, item);
        }
    }

    public void useItem(UserEntity userEntity, ItemDefinition item) {
        if (canUseItem(userEntity, item)) {
            applyItem(userEntity, item);
        }
    }

    private void applyItem(CombatEntity userCombatEntity, ItemDefinition item) {
        combatEffectApplierService.applyEffects(userCombatEntity, item.getCombatEffects());
    }

    private void applyItem(UserEntity userEntity, ItemDefinition item) {
        UserCombatEntity userCombatEntity = new UserCombatEntity(userEntity, globalAttributeCalculator);

        combatEffectApplierService.applyEffects(userCombatEntity, item.getCombatEffects());

        userMapper.updateBasicCombatStats(userEntity.getId(), userCombatEntity.getActualHealth(), userCombatEntity.getActualMana(), userEntity.getMovement());
    }
}
