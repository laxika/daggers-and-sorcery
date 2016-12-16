package com.morethanheroic.swords.skill.service.factory;

import com.morethanheroic.entity.service.factory.EntityFactory;
import com.morethanheroic.swords.dependency.InjectAtReturn;
import com.morethanheroic.swords.memoize.Memoize;
import com.morethanheroic.swords.skill.domain.SkillEntity;
import com.morethanheroic.swords.user.domain.UserEntity;
import com.morethanheroic.swords.user.service.UserEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * A factory to create {@link SkillEntityFactory}s.
 */
@Service
@RequiredArgsConstructor
public class SkillEntityFactory implements EntityFactory<SkillEntity, UserEntity> {

    private final UserEntityFactory userEntityFactory;

    /**
     * @deprecated Use #getEntity(int) instead.
     */
    @Deprecated
    @Memoize
    @InjectAtReturn
    public SkillEntity getSkillEntity(UserEntity userEntity) {
        return new SkillEntity(userEntity);
    }

    /**
     * @deprecated Use {@link #getEntity(UserEntity)} instead.
     */
    @Memoize
    @InjectAtReturn
    @Deprecated
    public SkillEntity getEntity(int id) {
        return getEntity(userEntityFactory.getEntity(id));
    }

    /**
     * Return the {@link SkillEntity} of a given user.
     *
     * @param userEntity the user we are returning the skills for
     * @return the created skills
     */
    @Override
    public SkillEntity getEntity(UserEntity userEntity) {
        return new SkillEntity(userEntity);
    }
}
