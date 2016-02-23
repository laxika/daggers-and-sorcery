package com.morethanheroic.swords.profile.service.response;

import com.morethanheroic.swords.attribute.domain.Attribute;
import com.morethanheroic.response.service.ResponseBuilderConfiguration;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttributeDefinitionPartialResponseBuilderConfiguration implements ResponseBuilderConfiguration {

    private final Attribute attribute;
}
