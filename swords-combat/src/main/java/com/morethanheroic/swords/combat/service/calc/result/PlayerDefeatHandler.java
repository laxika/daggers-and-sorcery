package com.morethanheroic.swords.combat.service.calc.result;

import com.morethanheroic.swords.combat.domain.Combat;
import com.morethanheroic.swords.combat.domain.CombatResult;
import com.morethanheroic.swords.combat.domain.entity.CombatEntity;
import com.morethanheroic.swords.combat.domain.entity.UserCombatEntity;
import com.morethanheroic.swords.combat.service.CombatMessageBuilder;
import com.morethanheroic.swords.skill.domain.SkillEntity;
import com.morethanheroic.swords.skill.domain.SkillType;
import com.morethanheroic.swords.skill.service.HighestSkillCalculator;
import com.morethanheroic.swords.skill.service.factory.SkillEntityFactory;
import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PlayerDefeatHandler {

    private static final double PERCENTAGE_TO_REMOVE = 0.25;

    private final SkillEntityFactory skillEntityFactory;
    private final HighestSkillCalculator highestSkillCalculator;
    private final CombatMessageBuilder combatMessageBuilder;

    public void handleDefeat(Combat combat, CombatResult combatResult) {
        //Combat timeout happened...
        if (!combatResult.isPlayerDied()) {
            return;
        }

        final UserEntity userEntity = combat.getUserCombatEntity().getUserEntity();

        handleDeath(userEntity, combatResult);
        handleResurrection(userEntity, combat.getUserCombatEntity(), combatResult);
    }

    private void handleDeath(UserEntity userEntity, CombatResult combatResult) {
        final SkillEntity skillEntity = skillEntityFactory.getSkillEntity(userEntity);

        final List<SkillType> highestTreeSkill = highestSkillCalculator.getHighestSkills(skillEntity);
        for (SkillType skillType : highestTreeSkill) {
            final int experienceToRemove = calculateExperienceToRemove(skillType, skillEntity);

            combatResult.addMessage(combatMessageBuilder.buildExperienceLossByDeathMessage(skillType, experienceToRemove));

            skillEntity.decreaseExperience(skillType, experienceToRemove);
        }
    }

    private void handleResurrection(UserEntity userEntity, UserCombatEntity userCombatEntity, CombatResult combatResult) {
        combatResult.addMessage(combatMessageBuilder.buildResurrectionMessage());

        userEntity.setBasicStats(userCombatEntity.getMaximumHealth(), userCombatEntity.getMaximumMana(), userEntity.getMovementPoints());
    }

    private int calculateExperienceToRemove(SkillType skillType, SkillEntity skillEntity) {
        return (int) (skillEntity.getExperienceBetweenNextLevel(skillType) * PERCENTAGE_TO_REMOVE);
    }
}
