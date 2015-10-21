package com.morethanheroic.swords.combat.service.calc.attack;

import com.morethanheroic.swords.attribute.domain.CombatAttribute;
import com.morethanheroic.swords.attribute.domain.SkillAttribute;
import com.morethanheroic.swords.attribute.service.DiceUtil;
import com.morethanheroic.swords.attribute.service.calc.GlobalAttributeCalculator;
import com.morethanheroic.swords.combat.domain.Combat;
import com.morethanheroic.swords.combat.domain.CombatResult;
import com.morethanheroic.swords.combat.domain.Winner;
import com.morethanheroic.swords.combat.service.CombatMessageBuilder;
import com.morethanheroic.swords.combat.service.CombatUtil;
import com.morethanheroic.swords.combat.service.calc.RandomCombatCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerRangedAttackCalculator implements AttackCalculator {

    private final CombatMessageBuilder combatMessageBuilder;
    private final GlobalAttributeCalculator globalAttributeCalculator;
    private final CombatUtil combatUtil;
    private final DiceUtil diceUtil;

    @Autowired
    public PlayerRangedAttackCalculator(CombatMessageBuilder combatMessageBuilder, GlobalAttributeCalculator globalAttributeCalculator, CombatUtil combatUtil, DiceUtil diceUtil) {
        this.combatMessageBuilder = combatMessageBuilder;
        this.globalAttributeCalculator = globalAttributeCalculator;
        this.combatUtil = combatUtil;
        this.diceUtil = diceUtil;
    }

    @Override
    public void calculateAttack(CombatResult result, Combat combat) {
        if (diceUtil.rollValueFromAttributeCalculationResult(globalAttributeCalculator.calculateActualValue(combat.getUserCombatEntity().getUserEntity(), CombatAttribute.AIMING)) > combat.getMonsterCombatEntity().getMonsterDefinition().getDefense()) {
            dealDamage(result, combat);

            if (combat.getMonsterCombatEntity().getActualHealth() <= 0) {
                result.addMessage(combatMessageBuilder.buildMonsterKilledMessage(combat.getMonsterCombatEntity().getMonsterDefinition().getName()));
                result.setWinner(Winner.PLAYER);
            }
        } else {
            result.addMessage(combatMessageBuilder.buildPlayerRangedMissMessage(combat.getMonsterCombatEntity().getMonsterDefinition().getName()));
        }
    }

    private void dealDamage(CombatResult result, Combat combat) {
        int damage = diceUtil.rollValueFromAttributeCalculationResult(globalAttributeCalculator.calculateActualValue(combat.getUserCombatEntity().getUserEntity(), CombatAttribute.RANGED_DAMAGE));

        addAttackXp(result, combat, damage * 2);

        combat.getMonsterCombatEntity().decreaseActualHealth(damage);

        result.addMessage(combatMessageBuilder.buildRangedDamageToMonsterMessage(combat.getMonsterCombatEntity().getMonsterDefinition().getName(), damage));
    }

    private void addAttackXp(CombatResult result, Combat combat, int amount) {
        if (combatUtil.getUserWeaponType(combat.getUserCombatEntity().getUserEntity()) != null) {
            result.addRewardXp(combatUtil.getUserWeaponSkillType(combat.getUserCombatEntity().getUserEntity()), amount);
        } else {
            result.addRewardXp(SkillAttribute.FISTFIGHT, amount);
        }
    }
}
