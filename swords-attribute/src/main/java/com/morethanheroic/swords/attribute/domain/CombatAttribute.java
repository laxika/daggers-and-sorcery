package com.morethanheroic.swords.attribute.domain;

import com.morethanheroic.swords.attribute.domain.type.AttributeType;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The {@link AttributeType#COMBAT} attributes.
 */
@RequiredArgsConstructor
public enum CombatAttribute implements Attribute {

    //TODO: Load these values from an xml file and use an CombatAttributeDefinition class.
    MANA(0, false, new GeneralAttribute[]{GeneralAttribute.INTELLIGENCE, GeneralAttribute.WISDOM, GeneralAttribute.WILLPOWER}, 0.5),
    LIFE(15, false, new GeneralAttribute[]{GeneralAttribute.VITALITY, GeneralAttribute.ENDURANCE}, 0.5),
    INITIATION(10, true, new GeneralAttribute[]{GeneralAttribute.SWIFTNESS, GeneralAttribute.PERCEPTION}, 0.5),
    ATTACK(10, true, new GeneralAttribute[]{GeneralAttribute.DEXTERITY, GeneralAttribute.SWIFTNESS}, 0.5),
    MAGIC_ATTACK(10, true, new GeneralAttribute[]{GeneralAttribute.INTELLIGENCE, GeneralAttribute.WILLPOWER}, 0.5),
    MAGIC_DAMAGE(10,true,new GeneralAttribute[]{GeneralAttribute.WILLPOWER},0.5),
    AIMING(10, true, new GeneralAttribute[]{GeneralAttribute.DEXTERITY, GeneralAttribute.PERCEPTION}, 0.5),
    DEFENSE(10, true, new GeneralAttribute[]{GeneralAttribute.ENDURANCE, GeneralAttribute.DEXTERITY}, 0.5),
    SPELL_RESISTANCE(10, true, new GeneralAttribute[]{GeneralAttribute.WILLPOWER, GeneralAttribute.WISDOM}, 0.5),
    DAMAGE(1, true, new GeneralAttribute[]{GeneralAttribute.STRENGTH}, 0.25),
    RANGED_DAMAGE(1, true, new GeneralAttribute[]{GeneralAttribute.DEXTERITY}, 0.25);

    private final int initialValue;
    private final boolean unlimited;
    private final GeneralAttribute[] bonusAttributes;
    private final double bonusPercentage;

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.COMBAT;
    }

    @Override
    public int getInitialValue() {
        return initialValue;
    }

    @Override
    public boolean isUnlimited() {
        return unlimited;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public List<GeneralAttribute> getBonusAttributes() {
        return Collections.unmodifiableList(Arrays.asList(bonusAttributes));
    }

    public double getBonusPercentage() {
        return bonusPercentage;
    }
}
