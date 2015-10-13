package com.morethanheroic.swords.spell.service.domain;

import com.morethanheroic.swords.attribute.domain.SkillAttribute;
import com.morethanheroic.swords.item.service.loader.domain.RawAttributeRequirementDefinition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SkillAttributeRequirementDefinition extends RawAttributeRequirementDefinition {

    private SkillAttribute attribute;

    public SkillAttribute getAttribute() {
        return attribute;
    }
}
