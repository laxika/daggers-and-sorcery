package com.morethanheroic.swords.item.service.cache;

import com.morethanheroic.swords.item.domain.ItemDefinition;
import com.morethanheroic.swords.item.service.loader.ItemDefinitionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class ItemDefinitionCache {

    @Autowired
    private ItemDefinitionLoader itemDefinitionLoader;

    private HashMap<Integer, ItemDefinition> itemDefinitionMap = new HashMap<>();

    @PostConstruct
    public void init() throws JAXBException, IOException, SAXException {
        List<ItemDefinition> rawItemDefinitionList = itemDefinitionLoader.loadItemDefinitions();

        for (ItemDefinition itemDefinition : rawItemDefinitionList) {
            itemDefinitionMap.put(itemDefinition.getId(), itemDefinition);
        }
    }

    public ItemDefinition getItemDefinition(int itemId) {
        return itemDefinitionMap.get(itemId);
    }
}
