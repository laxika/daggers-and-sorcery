package com.morethanheroic.swords.explore.service.event.impl.sevgard.whisperingwoods;

import com.google.common.collect.Lists;
import com.morethanheroic.swords.attribute.domain.GeneralAttribute;
import com.morethanheroic.swords.combat.domain.Drop;
import com.morethanheroic.swords.combat.service.calc.drop.DropCalculator;
import com.morethanheroic.swords.explore.domain.ExplorationResult;
import com.morethanheroic.swords.explore.service.event.ExplorationEvent;
import com.morethanheroic.swords.explore.service.event.ExplorationEventDefinition;
import com.morethanheroic.swords.explore.service.event.ExplorationEventLocationType;
import com.morethanheroic.swords.explore.service.event.newevent.ExplorationResultBuilderFactory;
import com.morethanheroic.swords.inventory.domain.InventoryEntity;
import com.morethanheroic.swords.inventory.service.InventoryFacade;
import com.morethanheroic.swords.item.service.cache.ItemDefinitionCache;
import com.morethanheroic.swords.monster.domain.DropAmountDefinition;
import com.morethanheroic.swords.monster.domain.DropDefinition;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ExplorationEvent
public class RobbingTheChieftainExplorationEventDefinition extends ExplorationEventDefinition {

    private static final int GOBLIN_GUARD_MONSTER_ID = 2;
    private static final int GOBLIN_CHIEFTAIN_MONSTER_ID = 5;

    private final List<DropDefinition> chestDropDefinitions;

    @Autowired
    public RobbingTheChieftainExplorationEventDefinition(final ItemDefinitionCache itemDefinitionCache) {
        chestDropDefinitions = Lists.newArrayList(
                DropDefinition.builder()
                        .item(itemDefinitionCache.getDefinition(99))
                        .amount(
                                DropAmountDefinition.builder()
                                        .minimumAmount(1)
                                        .maximumAmount(1)
                                        .build()
                        )
                        .chance(0.3)
                        .identified(false)
                        .build(),
                DropDefinition.builder()
                        .item(itemDefinitionCache.getDefinition(1))
                        .amount(
                                DropAmountDefinition.builder()
                                        .minimumAmount(8)
                                        .maximumAmount(20)
                                        .build()
                        )
                        .chance(100)
                        .identified(true)
                        .build(),
                DropDefinition.builder()
                        .item(itemDefinitionCache.getDefinition(36))
                        .amount(
                                DropAmountDefinition.builder()
                                        .minimumAmount(1)
                                        .maximumAmount(1)
                                        .build()
                        )
                        .chance(1)
                        .identified(true)
                        .build(),
                DropDefinition.builder()
                        .item(itemDefinitionCache.getDefinition(95))
                        .amount(
                                DropAmountDefinition.builder()
                                        .minimumAmount(1)
                                        .maximumAmount(1)
                                        .build()
                        )
                        .chance(1)
                        .identified(true)
                        .build()
        );
    }


    @Autowired
    private ExplorationResultBuilderFactory explorationResultBuilderFactory;

    @Autowired
    private DropCalculator dropCalculator;

    @Autowired
    private InventoryFacade inventoryFacade;

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public ExplorationEventLocationType getLocation() {
        return ExplorationEventLocationType.WHISPERING_WOODS;
    }

    @Override
    public ExplorationResult explore(UserEntity userEntity) {
        final StringBuilder stringBuilder = new StringBuilder();

        return explorationResultBuilderFactory
                .newExplorationResultBuilder(userEntity)
                .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_1")
                .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_2")
                .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_3")
                .newAttributeProbeEntry(GeneralAttribute.DEXTERITY, 8)
                .isSuccess((explorationResultBuilder) -> explorationResultBuilder
                        .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_4")
                        .newCombatEntry(GOBLIN_CHIEFTAIN_MONSTER_ID)
                        .newCustomLogicEntry(() -> {
                            List<Drop> chestDrops = dropCalculator.calculateDrops(chestDropDefinitions);

                            final InventoryEntity inventoryEntity = inventoryFacade.getInventory(userEntity);

                            boolean first = true;
                            for (Drop drop : chestDrops) {
                                if (!first) {
                                    stringBuilder.append(", ");
                                } else {
                                    first = false;
                                }

                                stringBuilder.append(drop.getAmount()).append(" ").append(drop.getItem().getName());

                                inventoryEntity.addItem(drop.getItem(), drop.getAmount(), drop.isIdentified());
                            }
                        })
                        .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_5", stringBuilder)
                        .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_8", stringBuilder)
                        .build())
                .isFailure((explorationResultBuilder) -> explorationResultBuilder
                        .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_6")
                        .newCombatEntry(GOBLIN_GUARD_MONSTER_ID)
                        .newMessageEntry("ROBBING_THE_CHIEFTAIN_EXPLORATION_EVENT_ENTRY_7")
                        .build()
                )
                .build();
    }
}
