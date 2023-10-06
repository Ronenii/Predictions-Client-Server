package gui.app.menu.execution.models;

import server2client.simulation.genral.impl.objects.DTOEntity;

import java.util.Map;

public class EntitiesStartData {
    private final Map<DTOEntity, Integer> entityPopulations;
    private final int entitiesLeft;

    public EntitiesStartData(Map<DTOEntity, Integer> entityPopulations, int entitesLeft) {
        this.entityPopulations = entityPopulations;
        this.entitiesLeft = entitesLeft;
    }

    public Map<DTOEntity, Integer> getEntityPopulations() {
        return entityPopulations;
    }

    public int getEntitiesLeft() {
        return entitiesLeft;
    }
}
