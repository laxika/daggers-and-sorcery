package com.morethanheroic.swords.item.service.loader.domain;

import com.morethanheroic.swords.attribute.domain.CombatAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CombatAttributeRequirementDefinition extends RawAttributeRequirementDefinition {

    private CombatAttribute attribute;

    public CombatAttribute getAttribute() {
        return attribute;
    }
}
