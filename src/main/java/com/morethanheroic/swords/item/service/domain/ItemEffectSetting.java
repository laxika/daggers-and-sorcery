package com.morethanheroic.swords.item.service.domain;

import com.morethanheroic.swords.effect.domain.EffectSetting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ItemEffectSetting extends EffectSetting{

    @XmlElement(name = "setting-name")
    private String name;

    @XmlElement(name = "setting-value")
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
