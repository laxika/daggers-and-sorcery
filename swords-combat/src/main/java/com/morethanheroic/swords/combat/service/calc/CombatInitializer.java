package com.morethanheroic.swords.combat.service.calc;

import com.morethanheroic.swords.combat.domain.Combat;
import com.morethanheroic.swords.combat.domain.CombatResult;
import com.morethanheroic.swords.combat.service.CombatMessageFactory;
import com.morethanheroic.swords.journal.model.JournalType;
import com.morethanheroic.swords.journal.service.JournalManager;
import com.morethanheroic.swords.monster.domain.MonsterDefinition;
import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CombatInitializer {

    private final JournalManager journalManager;
    private final CombatMessageFactory combatMessageFactory;

    public void initialize(final UserEntity userEntity, final MonsterDefinition monsterDefinition) {
        journalManager.createJournalEntry(userEntity, JournalType.MONSTER, monsterDefinition.getId());
    }

    @Deprecated
    public void initialize(Combat combat, CombatResult combatResult) {
        journalManager.createJournalEntry(combat.getUserCombatEntity().getUserEntity(), JournalType.MONSTER, combat.getMonsterCombatEntity().getMonsterDefinition().getId());

        combatResult.addMessage(
                combatMessageFactory.newMessage("start", "COMBAT_MESSAGE_NEW_FIGHT", combat.getMonsterCombatEntity().getMonsterDefinition().getName())
        );
    }
}
