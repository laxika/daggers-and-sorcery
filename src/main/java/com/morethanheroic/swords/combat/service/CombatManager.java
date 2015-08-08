package com.morethanheroic.swords.combat.service;

import com.morethanheroic.swords.combat.domain.CombatMessage;
import com.morethanheroic.swords.combat.domain.CombatResult;
import com.morethanheroic.swords.combat.service.calc.CombatCalculator;
import com.morethanheroic.swords.map.repository.domain.MapObjectDatabaseEntity;
import com.morethanheroic.swords.monster.service.MonsterDefinitionManager;
import com.morethanheroic.swords.monster.service.domain.MonsterDefinition;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CombatManager {

    private final CombatCalculator combatCalculator;
    private final MonsterDefinitionManager monsterDefinitionManager;

    @Autowired
    public CombatManager(CombatCalculator combatCalculator, MonsterDefinitionManager monsterDefinitionManager) {
        this.combatCalculator = combatCalculator;
        this.monsterDefinitionManager = monsterDefinitionManager;
    }

    public CombatResult initiateCombat(UserEntity user, int monsterId) {
        MapObjectDatabaseEntity spawn = user.getMap().getSpawnAt(user.getXPosition(), user.getYPosition(), monsterId);


        if (spawn != null) {
            return combatCalculator.doFight(user, monsterDefinitionManager.getMonsterDefinition(monsterId), spawn);
        } else {
            //TODO: do this better, with real error handling on user side
            CombatResult combatResult = new CombatResult();

            CombatMessage combatMessage = new CombatMessage();
            combatMessage.addData("result", "The target monster is not found.");

            combatResult.addMessage(combatMessage);

            return combatResult;
        }
    }
}
