package com.morethanheroic.swords.monster.service.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DropDefinition {

    private int item;
    private int amount;
    private double chance;

    public int getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public double getChance() {
        return chance;
    }
}
