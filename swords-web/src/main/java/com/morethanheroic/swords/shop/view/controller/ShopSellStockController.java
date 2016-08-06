package com.morethanheroic.swords.shop.view.controller;

import com.morethanheroic.response.exception.ConflictException;
import com.morethanheroic.response.exception.NotFoundException;
import com.morethanheroic.session.domain.SessionEntity;
import com.morethanheroic.swords.inventory.domain.InventoryEntity;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.inventory.service.UnidentifiedItemIdCalculator;
import com.morethanheroic.swords.item.domain.ItemDefinition;
import com.morethanheroic.swords.item.domain.ItemType;
import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.money.domain.MoneyType;
import com.morethanheroic.swords.response.domain.CharacterRefreshResponse;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.shop.domain.ShopEntity;
import com.morethanheroic.swords.shop.service.ItemPriceCalculator;
import com.morethanheroic.swords.shop.service.ShopEntityFactory;
import com.morethanheroic.swords.shop.service.cache.ShopDefinitionCache;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopSellStockController {

    @Autowired
    private UnidentifiedItemIdCalculator unidentifiedItemIdCalculator;

    @Autowired
    private ItemDefinitionCache itemDefinitionCache;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private ShopDefinitionCache shopDefinitionCache;

    @Autowired
    private ShopEntityFactory shopEntityFactory;

    @Autowired
    private ItemPriceCalculator itemPriceCalculator;

    @Autowired
    private InventoryFacade inventoryFacade;

    @RequestMapping(value = "/shop/{shopId}/sell/{itemId}", method = RequestMethod.GET)
    public CharacterRefreshResponse sellStock(UserEntity user, SessionEntity sessionEntity, @PathVariable int shopId, @PathVariable int itemId) {
        if (!shopDefinitionCache.isDefinitionExists(shopId)) {
            throw new NotFoundException();
        }

        boolean isIdentifiedItem = unidentifiedItemIdCalculator.isIdentifiedItem(itemId);

        if (!isIdentifiedItem) {
            itemId = unidentifiedItemIdCalculator.getRealItemId(sessionEntity, itemId);
        }

        if (!itemDefinitionCache.isDefinitionExists(itemId)) {
            throw new NotFoundException();
        }

        final InventoryEntity inventoryEntity = inventoryFacade.getInventory(user);

        if (!inventoryEntity.hasItemAmount(itemId, 1, isIdentifiedItem)) {
            throw new ConflictException();
        }

        //TODO: Check that the player is on the shop's position except if its the main shop
        //TODO: Use main shop rates if the player using the main shop

        ItemDefinition itemDefinition = itemDefinitionCache.getDefinition(itemId);

        if (itemDefinition.getType() == ItemType.MONEY) {
            throw new ConflictException();
        }

        ShopEntity shopEntity = shopEntityFactory.getEntity(shopId);

        inventoryEntity.increaseMoneyAmount(MoneyType.MONEY, itemPriceCalculator.getSellPrice(itemDefinition));
        inventoryEntity.removeItem(itemId, 1, isIdentifiedItem);

        shopEntity.buyItem(itemDefinition, 1);

        return responseFactory.newSuccessfulResponse(user);
    }
}
