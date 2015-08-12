package com.morethanheroic.swords.item.service.domain;

import com.morethanheroic.swords.attribute.domain.CombatAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CombatAttributeRequirementDefinition extends AttributeRequirementDefinition {

    private CombatAttribute attribute;

    public CombatAttribute getAttribute() {
        return attribute;
    }
}
