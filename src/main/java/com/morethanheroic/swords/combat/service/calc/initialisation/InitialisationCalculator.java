package com.morethanheroic.swords.combat.service.calc.initialisation;

import com.morethanheroic.swords.combat.domain.Combat;
import com.morethanheroic.swords.combat.service.calc.CombatEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitialisationCalculator {

    private final HumanInitialisationCalculator humanInitialisationCalculator;
    private final MonsterInitialisationCalculator monsterInitialisationCalculator;

    @Autowired
    public InitialisationCalculator(HumanInitialisationCalculator humanInitialisationCalculator, MonsterInitialisationCalculator monsterInitialisationCalculator) {
        this.humanInitialisationCalculator = humanInitialisationCalculator;
        this.monsterInitialisationCalculator = monsterInitialisationCalculator;
    }

    public CombatEntity calculateInitialisation(Combat combat) {
        int monsterInitialisation = monsterInitialisationCalculator.calculateInitialisation(combat.getMonsterCombatEntity().getMonsterDefinition());
        int playerInitiation = humanInitialisationCalculator.calculateInitialisation(combat.getUserCombatEntity().getUserEntity());

        return monsterInitialisation >= playerInitiation ? CombatEntity.MONSTER : CombatEntity.HUMAN;
    }
}
