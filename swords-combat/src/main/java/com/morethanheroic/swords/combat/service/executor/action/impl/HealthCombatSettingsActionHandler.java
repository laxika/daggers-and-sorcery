package com.morethanheroic.swords.combat.service.executor.action.impl;

import com.morethanheroic.math.PercentageCalculator;
import com.morethanheroic.swords.combat.domain.Combat;
import com.morethanheroic.swords.combat.domain.CombatResult;
import com.morethanheroic.swords.combat.service.CombatMessageBuilder;
import com.morethanheroic.swords.combat.service.executor.action.CombatSettingsActionHandler;
import com.morethanheroic.swords.combat.service.executor.action.resolver.CombatSettingsActionHandlerResolverProvider;
import com.morethanheroic.swords.settings.model.TriggerType;
import com.morethanheroic.swords.settings.service.domain.CombatSettingsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCombatSettingsActionHandler extends CombatSettingsActionHandler {

    private final PercentageCalculator percentageCalculator;
    private final CombatMessageBuilder combatMessageBuilder;

    @Autowired
    public HealthCombatSettingsActionHandler(CombatSettingsActionHandlerResolverProvider combatSettingsActionHandlerResolverProvider, PercentageCalculator percentageCalculator, CombatMessageBuilder combatMessageBuilder) {
        super(combatSettingsActionHandlerResolverProvider);

        this.percentageCalculator = percentageCalculator;
        this.combatMessageBuilder = combatMessageBuilder;
    }

    @Override
    public void executeAction(CombatResult combatResult, Combat combat, CombatSettingsEntity combatSettingsEntity) {
        if (percentageCalculator.calculatePercentage(combat.getUserCombatEntity().getActualHealth(), combat.getUserCombatEntity().getMaximumHealth()) < combatSettingsEntity.getTarget()) {
            combatResult.addMessage(combatMessageBuilder.buildHealthSettingTriggeredMessage(combatSettingsEntity.getTarget()));

            executeCombatSettings(combat, combatResult, combatSettingsEntity);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.HEALTH;
    }
}
