package com.morethanheroic.swords.item.service.transformer;

import com.google.common.collect.ImmutableList;
import com.morethanheroic.swords.definition.transformer.DefinitionListTransformer;
import com.morethanheroic.swords.item.domain.ItemModifierDefinition;
import com.morethanheroic.swords.item.service.loader.domain.RawItemModifierDefinition;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * Transform a {@link List} of {@link RawItemModifierDefinition} to a {@link List} of {@link ItemModifierDefinition}.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemModifierDefinitionListTransformer implements DefinitionListTransformer<List<ItemModifierDefinition>,
        List<RawItemModifierDefinition>> {

    @NonNull
    private final ItemModifierDefinitionTransformer itemModifierDefinitionTransformer;

    @Override
    public List<ItemModifierDefinition> transform(List<RawItemModifierDefinition> rawItemModifierDefinitionList) {
        if (rawItemModifierDefinitionList == null) {
            return Collections.emptyList();
        }

        return rawItemModifierDefinitionList.stream().map(itemModifierDefinitionTransformer::transform).collect(
                collectingAndThen(toList(), ImmutableList::copyOf)
        );
    }
}
