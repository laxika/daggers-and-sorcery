package com.morethanheroic.swords.profile.service.response;

import com.morethanheroic.swords.attribute.domain.Attribute;
import com.morethanheroic.swords.attribute.domain.GeneralAttribute;
import com.morethanheroic.swords.attribute.domain.SkillAttribute;
import com.morethanheroic.swords.attribute.domain.type.AttributeType;
import com.morethanheroic.swords.attribute.service.AttributeUtil;
import com.morethanheroic.swords.attribute.service.cache.SkillAttributeDefinitionCache;
import com.morethanheroic.swords.attribute.service.calc.GlobalAttributeCalculator;
import com.morethanheroic.swords.attribute.service.calc.domain.calculation.AttributeCalculationResult;
import com.morethanheroic.swords.attribute.service.calc.domain.data.AttributeData;
import com.morethanheroic.swords.attribute.service.calc.domain.calculation.CombatAttributeCalculationResult;
import com.morethanheroic.swords.attribute.service.calc.domain.data.GeneralAttributeData;
import com.morethanheroic.swords.attribute.service.calc.domain.data.SkillAttributeData;
import com.morethanheroic.swords.attribute.service.modifier.domain.AttributeModifierEntry;
import com.morethanheroic.swords.attribute.service.modifier.domain.AttributeModifierValue;
import com.morethanheroic.swords.attribute.service.modifier.domain.CombatAttributeModifierValue;
import com.morethanheroic.swords.equipment.domain.EquipmentEntity;
import com.morethanheroic.swords.equipment.domain.EquipmentSlot;
import com.morethanheroic.swords.equipment.service.EquipmentFacade;
import com.morethanheroic.swords.inventory.repository.dao.ItemDatabaseEntity;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.inventory.service.UnidentifiedItemIdCalculator;
import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.profile.service.response.item.ProfileIdentifiedItemEntryResponseBuilder;
import com.morethanheroic.swords.profile.service.response.item.ProfileUnidentifiedItemEntryResponseBuilder;
import com.morethanheroic.swords.race.service.RaceDefinitionCache;
import com.morethanheroic.swords.response.domain.CharacterRefreshResponse;
import com.morethanheroic.swords.response.domain.Response;
import com.morethanheroic.swords.response.service.ResponseBuilder;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.scavenging.service.ScavengingFacade;
import com.morethanheroic.swords.skill.service.SkillFacade;
import com.morethanheroic.swords.spell.domain.SpellDefinition;
import com.morethanheroic.swords.spell.repository.dao.SpellDatabaseEntity;
import com.morethanheroic.swords.spell.repository.domain.SpellMapper;
import com.morethanheroic.swords.spell.service.cache.SpellDefinitionCache;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfileInfoResponseBuilder implements ResponseBuilder<ProfileInfoResponseBuilderConfiguration> {

    private final ItemDefinitionCache itemDefinitionCache;
    private final InventoryFacade inventoryFacade;
    private final EquipmentFacade equipmentFacade;
    private final ResponseFactory responseFactory;
    private final ProfileIdentifiedItemEntryResponseBuilder profileIdentifiedItemEntryResponseBuilder;
    private final SpellDefinitionCache spellDefinitionCache;
    private final SpellMapper spellMapper;

    @Autowired
    private UnidentifiedItemIdCalculator unidentifiedItemIdCalculator;

    @Autowired
    private ProfileUnidentifiedItemEntryResponseBuilder profileUnidentifiedItemEntryResponseBuilder;

    @Autowired
    private RaceDefinitionCache raceDefinitionCache;

    @Autowired
    private ScavengingFacade scavengingFacade;

    @Autowired
    private AttributeValuePartialResponseBuilder attributeValuePartialResponseBuilder;

    @Autowired
    public ProfileInfoResponseBuilder(ItemDefinitionCache itemDefinitionCache, InventoryFacade inventoryFacade, EquipmentFacade equipmentFacade, ResponseFactory responseFactory, ProfileIdentifiedItemEntryResponseBuilder profileIdentifiedItemEntryResponseBuilder, SpellDefinitionCache spellDefinitionCache, SpellMapper spellMapper) {
        this.itemDefinitionCache = itemDefinitionCache;
        this.inventoryFacade = inventoryFacade;
        this.equipmentFacade = equipmentFacade;
        this.responseFactory = responseFactory;
        this.profileIdentifiedItemEntryResponseBuilder = profileIdentifiedItemEntryResponseBuilder;
        this.spellDefinitionCache = spellDefinitionCache;
        this.spellMapper = spellMapper;
    }

    public Response build(ProfileInfoResponseBuilderConfiguration profileInfoResponseBuilderConfiguration) {
        UserEntity user = profileInfoResponseBuilderConfiguration.getUserEntity();
        HttpSession session = profileInfoResponseBuilderConfiguration.getHttpSession();

        CharacterRefreshResponse response = responseFactory.newResponse(user);

        response.setData("attribute", attributeValuePartialResponseBuilder.build(profileInfoResponseBuilderConfiguration));
        response.setData("username", user.getUsername());
        response.setData("race", raceDefinitionCache.getDefinition(user.getRace()).getName());
        response.setData("registrationDate", user.getRegistrationDate());
        response.setData("lastLoginDate", user.getLastLoginDate());
        response.setData("scavengingPoints", scavengingFacade.getEntity(user).getScavengingPoint());
        response.setData("inventory", buildInventoryResponse(inventoryFacade.getInventory(user).getItems(), session));
        response.setData("equipment", buildEquipmentResponse(user, session));
        response.setData("spell", buildSpellResponse(spellMapper.getAllSpellsForUser(user.getId())));

        return response;
    }

    private HashMap<String, HashMap<String, Object>> buildEquipmentResponse(UserEntity userEntity, HttpSession session) {
        HashMap<String, HashMap<String, Object>> equipmentHolder = new HashMap<>();

        EquipmentEntity equipmentEntity = equipmentFacade.getEquipment(userEntity);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            int equipment = equipmentEntity.getEquipmentIdOnSlot(slot);

            HashMap<String, Object> slotData = new HashMap<>();

            if (equipment == 0) {
                slotData.put("empty", true);
            } else {
                if (equipmentEntity.isEquipmentIdentifiedOnSlot(slot)) {
                    slotData.put("description", profileIdentifiedItemEntryResponseBuilder.buildItemEntry(itemDefinitionCache.getDefinition(equipment)));
                } else {
                    slotData.put("description", profileUnidentifiedItemEntryResponseBuilder.buildItemEntry(itemDefinitionCache.getDefinition(equipment), unidentifiedItemIdCalculator.getUnidentifiedItemId(session, equipment)));
                }
            }

            equipmentHolder.put(slot.name(), slotData);
        }

        return equipmentHolder;
    }

    private ArrayList<HashMap<String, Object>> buildInventoryResponse(List<ItemDatabaseEntity> items, HttpSession session) {
        ArrayList<HashMap<String, Object>> inventoryData = new ArrayList<>();

        for (ItemDatabaseEntity item : items) {
            HashMap<String, Object> itemData = new HashMap<>();

            itemData.put("item", convertItemDatabaseEntityToSendableObject(item));

            if (item.isIdentified()) {
                itemData.put("definition", profileIdentifiedItemEntryResponseBuilder.buildItemEntry(itemDefinitionCache.getDefinition(item.getItemId())));
            } else {
                itemData.put("definition", profileUnidentifiedItemEntryResponseBuilder.buildItemEntry(itemDefinitionCache.getDefinition(item.getItemId()), unidentifiedItemIdCalculator.getUnidentifiedItemId(session, item.getItemId())));
            }

            inventoryData.add(itemData);
        }

        return inventoryData;
    }

    private Map<String, Object> convertItemDatabaseEntityToSendableObject(ItemDatabaseEntity itemDatabaseEntity) {
        Map<String, Object> result = new HashMap<>();

        result.put("itemId", itemDatabaseEntity.getItemId());
        result.put("amount", itemDatabaseEntity.getAmount());

        return result;
    }

    private LinkedList<HashMap<String, Object>> buildSpellResponse(List<SpellDatabaseEntity> spells) {
        LinkedList<HashMap<String, Object>> spellList = new LinkedList<>();

        for (SpellDatabaseEntity spell : spells) {
            SpellDefinition spellDefinition = spellDefinitionCache.getSpellDefinition(spell.getSpellId());

            HashMap<String, Object> spellData = new HashMap<>();

            spellData.put("id", spellDefinition.getId());
            spellData.put("name", spellDefinition.getName());
            spellData.put("combatSpell", spellDefinition.isCombatSpell());
            spellData.put("castingCost", spellDefinition.getSpellCosts());
            spellData.put("openPage", spellDefinition.isOpenPage());

            spellList.add(spellData);
        }

        return spellList;
    }
}
