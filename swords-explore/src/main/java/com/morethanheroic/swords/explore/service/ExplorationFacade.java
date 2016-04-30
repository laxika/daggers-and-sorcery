package com.morethanheroic.swords.explore.service;

import com.morethanheroic.swords.explore.domain.ExplorationResult;
import com.morethanheroic.swords.explore.service.cache.ExplorationEventDefinitionCache;
import com.morethanheroic.swords.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class ExplorationFacade {

    @Autowired
    private ExplorationEventDefinitionCache explorationEventDefinitionCache;

    @Autowired
    private Random random;

    @Transactional
    public ExplorationResult explore(UserEntity userEntity) {
        if (userEntity.getMovementPoints() <= 0) {
            //TODO: Do a better response than this!
            return ExplorationResult.builder().build();
        }

        //TODO: make this random
        return explorationEventDefinitionCache.getDefinition(random.nextInt(explorationEventDefinitionCache.size()) + 1).explore(userEntity);
    }
}
