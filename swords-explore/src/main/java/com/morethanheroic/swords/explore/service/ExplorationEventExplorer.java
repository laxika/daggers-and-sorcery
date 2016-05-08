package com.morethanheroic.swords.explore.service;

import com.morethanheroic.session.domain.SessionEntity;
import com.morethanheroic.swords.explore.domain.ExplorationResult;
import com.morethanheroic.swords.explore.domain.context.ExplorationContext;
import com.morethanheroic.swords.explore.service.cache.ExplorationEventDefinitionCache;
import com.morethanheroic.swords.explore.service.context.ExplorationContextFactory;
import com.morethanheroic.swords.explore.service.event.ExplorationResultFactory;
import com.morethanheroic.swords.explore.service.event.MultiStageExplorationEventDefinition;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create and explore an event if the user doesn't have any event running, or run the event to the next stage is the user
 * has an event already in progress.
 */
@Service
public class ExplorationEventExplorer {

    private static final int NOT_STARTED = 0;
    private static final int NO_EVENT = 0;
    private static final int MINIMUM_MOVEMENT_POINTS = 0;

    @Autowired
    private ExplorationResultFactory explorationResultFactory;

    @Autowired
    private ExplorationContextFactory explorationContextFactory;

    @Autowired
    private ExplorationEventDefinitionCache explorationEventDefinitionCache;

    @Transactional
    public ExplorationResult explore(final UserEntity userEntity, final SessionEntity sessionEntity, final int nextState) {
        if (!canExplore(userEntity, nextState)) {
            return buildFailedExplorationResult();
        }

        return buildSuccessfulExplorationResult(userEntity, explorationContextFactory.newExplorationContext(userEntity, sessionEntity, nextState));
    }

    private boolean canExplore(final UserEntity userEntity, int nextState) {
        if (userEntity.getMovementPoints() < MINIMUM_MOVEMENT_POINTS) {
            return false;
        }

        if (nextState > NOT_STARTED && userEntity.getActiveExplorationEvent() == NO_EVENT) {
            return false;
        }

        if (nextState > NOT_STARTED && !((MultiStageExplorationEventDefinition) explorationEventDefinitionCache.getDefinition(userEntity.getActiveExplorationEvent())).isValidNextStageAtStage(userEntity.getActiveExplorationState(), nextState)) {
            return false;
        }

        return true;
    }

    //TODO: Do a better response than this!
    private ExplorationResult buildFailedExplorationResult() {
        return explorationResultFactory.newExplorationResult();
    }

    private ExplorationResult buildSuccessfulExplorationResult(final UserEntity userEntity, final ExplorationContext explorationContext) {
        if (explorationContext.getStage() == NO_EVENT) {
            return exploreNewEvent(userEntity, explorationContext);
        }

        return continueEvent(userEntity, explorationContext);
    }

    private ExplorationResult exploreNewEvent(final UserEntity userEntity, final ExplorationContext explorationContext) {
        return explorationContext.getEvent().explore(userEntity);
    }

    private ExplorationResult continueEvent(final UserEntity userEntity, final ExplorationContext explorationContext) {
        return ((MultiStageExplorationEventDefinition) explorationContext.getEvent()).explore(userEntity, explorationContext.getStage());
    }
}
