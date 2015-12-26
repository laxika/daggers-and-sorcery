package com.morethanheroic.swords.equipment.view.controller;

import com.morethanheroic.swords.equipment.domain.EquipmentSlot;
import com.morethanheroic.swords.equipment.service.EquipmentManager;
import com.morethanheroic.swords.equipment.service.EquipmentResponseBuilder;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.inventory.service.UnidentifiedItemIdCalculator;
import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.response.domain.Response;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Lazy
public class EquipmentController {

    private final EquipmentManager equipmentManager;
    private final InventoryFacade inventoryFacade;
    private final ItemDefinitionCache itemDefinitionCache;
    private final EquipmentResponseBuilder equipmentResponseBuilder;

    @Autowired
    private UnidentifiedItemIdCalculator unidentifiedItemIdCalculator;

    @Autowired
    public EquipmentController(InventoryFacade inventoryFacade, ItemDefinitionCache itemDefinitionCache, EquipmentManager equipmentManager, EquipmentResponseBuilder equipmentResponseBuilder) {
        this.inventoryFacade = inventoryFacade;
        this.itemDefinitionCache = itemDefinitionCache;
        this.equipmentManager = equipmentManager;
        this.equipmentResponseBuilder = equipmentResponseBuilder;
    }

    @RequestMapping(value = "/equip/{itemId}", method = RequestMethod.GET)
    public Response equip(UserEntity user, HttpSession session, @PathVariable int itemId) {
        boolean identifiedItem = unidentifiedItemIdCalculator.isIdentifiedItem(itemId);

        if (!identifiedItem) {
            itemId = unidentifiedItemIdCalculator.getRealItemId(session, itemId);
        }

        if (inventoryFacade.getInventory(user).hasItem(itemId, identifiedItem) && itemDefinitionCache.getDefinition(itemId).isEquipment()) {
            if (equipmentManager.getEquipment(user).equipItem(itemDefinitionCache.getDefinition(itemId), identifiedItem)) {
                return equipmentResponseBuilder.build(user, EquipmentResponseBuilder.SUCCESSFULL_REQUEST);
            }
        }

        return equipmentResponseBuilder.build(user, EquipmentResponseBuilder.UNSUCCESSFULL_REQUEST);
    }


    @RequestMapping(value = "/unequip/{slotId}", method = RequestMethod.GET)
    public Response unequip(UserEntity user, @PathVariable String slotId) {
        //TODO has enough inventory slots
        equipmentManager.getEquipment(user).unequipItem(EquipmentSlot.valueOf(slotId));

        return equipmentResponseBuilder.build(user, EquipmentResponseBuilder.SUCCESSFULL_REQUEST);
    }
}
