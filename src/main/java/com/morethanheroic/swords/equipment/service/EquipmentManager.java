package com.morethanheroic.swords.equipment.service;

import com.morethanheroic.swords.attribute.service.ItemRequirementToAttributeConverter;
import com.morethanheroic.swords.attribute.service.calc.GlobalAttributeCalculator;
import com.morethanheroic.swords.equipment.domain.EquipmentEntity;
import com.morethanheroic.swords.equipment.domain.EquipmentSlotMapper;
import com.morethanheroic.swords.equipment.repository.domain.EquipmentMapper;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.memoize.Memoize;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class EquipmentManager {

    private final InventoryFacade inventoryFacade;
    private final EquipmentMapper equipmentMapper;
    private final EquipmentSlotMapper equipmentSlotMapper;
    private final ItemDefinitionCache itemDefinitionCache;

    @Autowired
    private ItemRequirementToAttributeConverter itemRequirementToAttributeConverter;

    @Autowired
    private GlobalAttributeCalculator globalAttributeCalculator;

    @Autowired
    public EquipmentManager(InventoryFacade inventoryFacade, EquipmentMapper equipmentMapper, EquipmentSlotMapper equipmentSlotMapper, ItemDefinitionCache itemDefinitionCache) {
        this.inventoryFacade = inventoryFacade;
        this.equipmentMapper = equipmentMapper;
        this.equipmentSlotMapper = equipmentSlotMapper;
        this.itemDefinitionCache = itemDefinitionCache;
    }

    //TODO: we should create a @InjectToReturn that automatically resolve @Autowired inside returned object. It should run before @Memoize.
    @Memoize
    public EquipmentEntity getEquipment(UserEntity userEntity) {
        return new EquipmentEntity(userEntity, inventoryFacade.getInventory(userEntity), equipmentMapper, equipmentSlotMapper, globalAttributeCalculator, itemDefinitionCache, itemRequirementToAttributeConverter);
    }

    public void createEquipmentForUser(UserEntity userEntity) {
        equipmentMapper.insert(userEntity.getId());
    }
}
