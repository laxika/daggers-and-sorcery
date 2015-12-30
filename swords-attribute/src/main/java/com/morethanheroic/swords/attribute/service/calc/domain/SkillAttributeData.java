package com.morethanheroic.swords.attribute.service.calc.domain;

import com.morethanheroic.swords.attribute.domain.Attribute;
import com.morethanheroic.swords.attribute.service.modifier.domain.AttributeModifierEntry;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Holds all attribute data after a full attribute calculation for a {@link com.morethanheroic.swords.attribute.domain.type.AttributeType#SKILL} attribute.
 * Creating this class is very resource intensive so try to avoid it as much as possible.
 */
@Getter
public class SkillAttributeData extends AttributeData {

    private final long actualXp;
    private final long nextLevelXp;
    private final long xpBetweenLevels;

    @Builder(builderMethodName = "skillAttributeDataBuilder")
    private SkillAttributeData(Attribute attribute, AttributeCalculationResult actual, AttributeCalculationResult maximum, List<AttributeModifierEntry> attributeModifierData, long actualXp, long nextLevelXp, long xpBetweenLevels) {
        super(attribute, actual, maximum, attributeModifierData);

        this.actualXp = actualXp;
        this.nextLevelXp = nextLevelXp;
        this.xpBetweenLevels = xpBetweenLevels;
    }
}
