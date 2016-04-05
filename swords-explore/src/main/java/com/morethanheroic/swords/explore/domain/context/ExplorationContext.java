package com.morethanheroic.swords.explore.domain.context;

import com.morethanheroic.swords.user.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExplorationContext {

    private final UserEntity userEntity;
}
