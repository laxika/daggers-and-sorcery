package com.morethanheroic.swords.attribute.service.modifier.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.morethanheroic.swords.attribute.domain.Attribute;
import com.morethanheroic.swords.attribute.domain.BasicAttribute;
import com.morethanheroic.swords.attribute.domain.CombatAttribute;
import com.morethanheroic.swords.attribute.domain.GeneralAttribute;
import com.morethanheroic.swords.attribute.domain.SkillAttribute;
import com.morethanheroic.swords.attribute.service.modifier.domain.AttributeModifierEntry;
import com.morethanheroic.swords.user.domain.UserEntity;

@Service
public class GlobalAttributeModifierCalculator {

    @Autowired
    private SkillAttributeModifierCalculator skillAttributeModifierCalculator;

    @Autowired
    private GeneralAttributeModifierCalculator generalAttributeModifierCalculator;

    @Autowired
    private CombatAttributeModifierCalculator combatAttributeModifierCalculator;

    @Autowired
    private BasicAttributeModifierCalculator basicAttributeModifierCalculator;

    @Autowired
    @Qualifier("equipmentAttributeModifierCalculator")
    private AttributeModifierCalculator equipmentAttributeModifierCalculator;

    @Autowired
    private RaceAttributeModifierCalculator raceAttributeModifierCalculator;
    
    @Autowired
    private List<AttributeModifierProvider> attributeModifierProviders;

    private Map<Class<? extends Attribute>, AttributeModifierCalculator> modifierCalculatorMap;

    @PostConstruct
    public void initialize() {
        modifierCalculatorMap = ImmutableMap.of(
                SkillAttribute.class, skillAttributeModifierCalculator,
                GeneralAttribute.class, generalAttributeModifierCalculator,
                CombatAttribute.class, combatAttributeModifierCalculator,
                BasicAttribute.class, basicAttributeModifierCalculator
        );
    }

    @SuppressWarnings("unchecked")
    public List<AttributeModifierEntry> calculateModifierData(UserEntity user, Attribute attribute) {
        final ArrayList<AttributeModifierEntry> attributeModifierEntryList = new ArrayList<>();

        attributeModifierEntryList.addAll(modifierCalculatorMap.get(attribute.getClass()).calculate(user, attribute));
        attributeModifierEntryList.addAll(equipmentAttributeModifierCalculator.calculate(user, attribute));
        attributeModifierEntryList.addAll(raceAttributeModifierCalculator.calculate(user, attribute));
        attributeModifierEntryList.addAll(attributeModifierProviders.stream()
                .flatMap(attributeModifierProvider -> attributeModifierProvider.calculateModifiers(user, attribute).stream())
                .collect(Collectors.toList())
        );

        return ImmutableList.copyOf(attributeModifierEntryList);
    }
}
