package com.morethanheroic.swords.explore.service.event.newevent;

import com.google.common.collect.Lists;
import com.morethanheroic.swords.attribute.domain.Attribute;
import com.morethanheroic.swords.combat.domain.CombatMessage;
import com.morethanheroic.swords.combat.domain.step.DefaultCombatStep;
import com.morethanheroic.swords.combat.service.newcb.CombatCalculator;
import com.morethanheroic.swords.explore.domain.ExplorationResult;
import com.morethanheroic.swords.explore.domain.event.result.impl.CombatExplorationEventEntryResult;
import com.morethanheroic.swords.explore.service.event.evaluator.AttributeAttemptEventEntryEvaluator;
import com.morethanheroic.swords.explore.service.event.evaluator.CombatEventEntryEvaluator;
import com.morethanheroic.swords.explore.service.event.evaluator.MessageEventEntryEvaluator;
import com.morethanheroic.swords.explore.service.event.evaluator.domain.AttributeAttemptEventEntryEvaluatorResult;
import com.morethanheroic.swords.explore.service.event.evaluator.domain.CombatEventEntryEvaluatorResult;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExplorationResultBuilder {

    @Autowired
    private MessageEventEntryEvaluator messageEventEntryEvaluator;

    @Autowired
    private CombatEventEntryEvaluator combatEventEntryEvaluator;

    @Autowired
    private AttributeAttemptEventEntryEvaluator attributeAttemptEventEntryEvaluator;

    @Autowired
    private CombatCalculator combatCalculator;

    private ExplorationResult explorationResult;
    private UserEntity userEntity;

    private boolean shouldStop;

    public ExplorationResultBuilder initialize(final UserEntity userEntity, final ExplorationResult explorationResult) {
        this.userEntity = userEntity;
        this.explorationResult = explorationResult;

        return this;
    }

    public ExplorationResultBuilder newMessageEntry(final String messageId, final Object... args) {
        if (shouldStop) {
            return this;
        }

        explorationResult.addEventEntryResult(
                messageEventEntryEvaluator.messageEntry(messageId, args)
        );

        return this;
    }

    public ExplorationResultBuilder newCombatEntry(final int opponentId, final int eventId, final int stage) {
        if (shouldStop) {
            return this;
        }

        final CombatEventEntryEvaluatorResult secondCombatEventEntryEvaluatorResult = combatEventEntryEvaluator.calculateCombat(userEntity, combatEventEntryEvaluator.convertMonsterIdToDefinition(opponentId));

        explorationResult.addEventEntryResult(secondCombatEventEntryEvaluatorResult.getResult());

        userEntity.setActiveExploration(eventId, stage);
        return this;
    }

    public ExplorationResultBuilder continueCombatEntry() {
        if (shouldStop) {
            return this;
        }

        final CombatMessage combatMessage = new CombatMessage();
        combatMessage.addData("message", "Continue fighting.");

        explorationResult.addEventEntryResult(
            CombatEventEntryEvaluatorResult.builder()
                .result(
                    CombatExplorationEventEntryResult.builder()
                        .combatSteps(Lists.newArrayList(
                            DefaultCombatStep.builder()
                                .message(combatMessage)
                                .build()
                        ))
                        .combatEnded(!combatCalculator.isCombatRunning(userEntity))
                        .build()
                )
            .build().getResult()
        );

        return this;
    }

    public MultiWayExplorationResultBuilder newAttributeProbeEntry(final Attribute attribute, final int valueToHit) {
        if (shouldStop) {
            return new MultiWayExplorationResultBuilder(this, false);
        }

        final AttributeAttemptEventEntryEvaluatorResult attemptResult = attributeAttemptEventEntryEvaluator.attributeAttempt(userEntity, attribute, valueToHit);

        explorationResult.addEventEntryResult(attemptResult.getResult());

        return new MultiWayExplorationResultBuilder(this, attemptResult.isSuccessful());
    }

    public synchronized ExplorationResultBuilder newCustomLogicEntry(final Runnable runnable) {
        if (shouldStop) {
            return this;
        }

        runnable.run();

        return this;
    }

    public synchronized ExplorationResult build() {
        return explorationResult;
    }
}
