package com.morethanheroic.swords.money.service.transformer;

import com.morethanheroic.swords.definition.transformer.DefinitionTransformer;
import com.morethanheroic.swords.money.domain.ConversionDefinition;
import com.morethanheroic.swords.money.domain.MoneyDefinition;
import com.morethanheroic.swords.money.service.conversion.ConversionRateComparator;
import com.morethanheroic.swords.money.service.loader.domain.RawConversionDefinition;
import com.morethanheroic.swords.money.service.loader.domain.RawMoneyDefinition;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transform {@link RawMoneyDefinition} to their domain object pair {@link MoneyDefinition}.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MoneyDefinitionTransformer implements DefinitionTransformer<MoneyDefinition, RawMoneyDefinition> {

    @NotNull
    private final ConversionDefinitionTransformer conversionDefinitionTransformer;

    @NotNull
    private final ConversionRateComparator conversionRateComparator;

    public MoneyDefinition transform(RawMoneyDefinition rawMoneyDefinition) {
        return MoneyDefinition.builder()
                .id(rawMoneyDefinition.getId())
                .conversionDefinitions(convertListOfConversionDefinitions(rawMoneyDefinition.getCombatModifiers()))
                .build();
    }

    private List<ConversionDefinition> convertListOfConversionDefinitions(List<RawConversionDefinition> rawConversionDefinitions) {
        final List<ConversionDefinition> result = rawConversionDefinitions.stream().map(conversionDefinitionTransformer::transform)
                .collect(Collectors.toList());

        Collections.sort(result, conversionRateComparator);

        return result;
    }
}
