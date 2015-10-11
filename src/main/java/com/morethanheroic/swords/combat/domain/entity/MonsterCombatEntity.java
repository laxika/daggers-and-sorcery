package com.morethanheroic.swords.combat.domain.entity;

import com.morethanheroic.swords.monster.domain.MonsterDefinition;

public class MonsterCombatEntity extends CombatEntity {

    private MonsterDefinition monsterDefinition;

    public MonsterCombatEntity(MonsterDefinition monsterDefinition) {
        this.monsterDefinition = monsterDefinition;

        this.setMaximumHealth(monsterDefinition.getHealth());
        this.setActualHealth(monsterDefinition.getHealth());

        this.setMaximumMana(monsterDefinition.getMana());
        this.setActualMana(monsterDefinition.getMana());
    }

    public MonsterDefinition getMonsterDefinition() {
        return monsterDefinition;
    }
}
