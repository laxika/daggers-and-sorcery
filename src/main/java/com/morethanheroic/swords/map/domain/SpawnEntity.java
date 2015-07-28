package com.morethanheroic.swords.map.domain;

import com.morethanheroic.swords.map.service.domain.MapSpawnEntryDefinition;

public class SpawnEntity {

    private final MapSpawnEntryDefinition mapSpawnEntryDefinition;

    public SpawnEntity(MapSpawnEntryDefinition mapSpawnEntryDefinition) {
        this.mapSpawnEntryDefinition = mapSpawnEntryDefinition;
    }

    public int getMonsterId() {
        return this.mapSpawnEntryDefinition.getMonsterId();
    }

    /**
     * @return The chance that this monster will spawn on the map compared to the other monsters chances.
     */
    public int getChance() {
        return this.mapSpawnEntryDefinition.getChance();
    }
}
