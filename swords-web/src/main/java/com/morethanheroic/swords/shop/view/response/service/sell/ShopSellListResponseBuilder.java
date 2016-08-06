package com.morethanheroic.swords.shop.view.response.service.sell;

import com.morethanheroic.response.domain.Response;
import com.morethanheroic.response.service.ResponseBuilder;
import com.morethanheroic.swords.response.service.ResponseFactory;
import com.morethanheroic.swords.shop.view.response.domain.ShopDefinitionPartialResponseBuilderConfiguration;
import com.morethanheroic.swords.shop.view.response.domain.sell.ShopSellListResponseBuilderConfiguration;
import com.morethanheroic.swords.shop.view.response.domain.sell.ShopSellTypeListPartialResponseBuilderConfiguration;
import com.morethanheroic.swords.shop.view.response.service.ShopDefinitionPartialResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopSellListResponseBuilder implements ResponseBuilder<ShopSellListResponseBuilderConfiguration> {

    private final ResponseFactory responseFactory;
    private final ShopDefinitionPartialResponseBuilder shopDefinitionPartialResponseBuilder;
    private final ShopSellTypeListPartialResponseBuilder shopSellTypeListPartialResponseBuilder;

    @Override
    public Response build(ShopSellListResponseBuilderConfiguration responseBuilderConfiguration) {
        final Response response = responseFactory.newResponse(responseBuilderConfiguration.getUserEntity());

        response.setData("items",
                responseBuilderConfiguration.getItems().entrySet()
                        .stream()
                        .map(itemTypeListEntry ->
                                shopSellTypeListPartialResponseBuilder.build(
                                        ShopSellTypeListPartialResponseBuilderConfiguration.builder()
                                                .itemType(itemTypeListEntry.getKey())
                                                .items(itemTypeListEntry.getValue())
                                                .build()
                                )
                        ).collect(Collectors.toList())
        );

        response.setData("definition", shopDefinitionPartialResponseBuilder.build(
                ShopDefinitionPartialResponseBuilderConfiguration.builder()
                        .shopDefinition(responseBuilderConfiguration.getShopDefinition())
                        .build()
                )
        );

        return response;
    }
}
