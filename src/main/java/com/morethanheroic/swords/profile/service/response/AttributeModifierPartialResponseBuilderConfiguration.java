package com.morethanheroic.swords.profile.service.response;

import com.morethanheroic.swords.attribute.domain.Attribute;
import com.morethanheroic.swords.attribute.service.modifier.domain.AttributeModifierEntry;
import com.morethanheroic.swords.response.service.ResponseBuilderConfiguration;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AttributeModifierPartialResponseBuilderConfiguration implements ResponseBuilderConfiguration {

    private final Attribute attribute;
    private final List<AttributeModifierEntry> modifierData;
}
