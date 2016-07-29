package com.morethanheroic.swords.shop.service.response;

import com.morethanheroic.session.domain.SessionEntity;
import com.morethanheroic.swords.inventory.domain.InventoryEntity;
import com.morethanheroic.swords.inventory.repository.dao.ItemDatabaseEntity;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.inventory.service.UnidentifiedItemIdCalculator;
import com.morethanheroic.swords.item.domain.ItemDefinition;
import com.morethanheroic.swords.item.domain.ItemType;
import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.profile.service.response.item.ProfileIdentifiedItemEntryResponseBuilder;
import com.morethanheroic.swords.profile.service.response.item.ProfileUnidentifiedItemEntryResponseBuilder;
import com.morethanheroic.swords.response.domain.CharacterRefreshResponse;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.shop.domain.ShopDefinition;
import com.morethanheroic.swords.shop.domain.ShopEntity;
import com.morethanheroic.swords.shop.domain.ShopItem;
import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShopItemListResponseBuilder {

    @NonNull
    private ResponseFactory responseFactory;

    @NonNull
    private ProfileIdentifiedItemEntryResponseBuilder profileIdentifiedItemEntryResponseBuilder;

    @NonNull
    private ProfileUnidentifiedItemEntryResponseBuilder profileUnidentifiedItemEntryResponseBuilder;

    @NonNull
    private UnidentifiedItemIdCalculator unidentifiedItemIdCalculator;

    @NonNull
    private ItemDefinitionCache itemDefinitionCache;

    @NonNull
    private InventoryFacade inventoryFacade;

    public CharacterRefreshResponse build(UserEntity userEntity, SessionEntity sessionEntity, ShopEntity shopEntity) {
        final CharacterRefreshResponse response = responseFactory.newResponse(userEntity);

        response.setData("shopDefinition", buildShopDefinition(shopEntity.getShopDefinition()));
        response.setData("itemList", buildItemList(shopEntity));
        response.setData("inventoryList", buildInventoryItemList(shopEntity, sessionEntity, inventoryFacade.getInventory(userEntity)));

        return response;
    }

    private Map<String, Object> buildShopDefinition(ShopDefinition shopDefinition) {
        final Map<String, Object> result = new HashMap<>();

        result.put("id", shopDefinition.getId());
        result.put("name", shopDefinition.getName());

        return result;
    }

    private List<Map<String, Object>> buildItemList(ShopEntity shopEntity) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (ShopItem shopItem : shopEntity.getAllItemsInShop()) {
            Map<String, Object> item = new HashMap<>();

            item.put("itemDefinition", profileIdentifiedItemEntryResponseBuilder.buildItemEntry(shopItem.getItem()));
            item.put("itemPrice", shopEntity.getShopSellPrice(shopItem.getItem()));
            item.put("itemAmount", shopItem.getItemAmount());

            result.add(item);
        }

        return result;
    }

    private List<Map<String, Object>> buildInventoryItemList(ShopEntity shopEntity, SessionEntity sessionEntity, InventoryEntity inventoryEntity) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (ItemDatabaseEntity shopItem : inventoryEntity.getItemsLegacy()) {
            ItemDefinition itemDefinition = itemDefinitionCache.getDefinition(shopItem.getItemId());

            if (itemDefinition.getType() == ItemType.MONEY) {
                continue;
            }

            Map<String, Object> item = new HashMap<>();

            if (shopItem.isIdentified()) {
                item.put("itemDefinition", profileIdentifiedItemEntryResponseBuilder.buildItemEntry(itemDefinition));
            } else {
                item.put("itemDefinition", profileUnidentifiedItemEntryResponseBuilder.buildItemEntry(itemDefinition, unidentifiedItemIdCalculator.getUnidentifiedItemId(sessionEntity, itemDefinition.getId())));
            }
            item.put("itemPrice", shopEntity.getShopBuyPrice(itemDefinition));
            item.put("itemAmount", shopItem.getAmount());

            result.add(item);
        }

        return result;
    }
}
