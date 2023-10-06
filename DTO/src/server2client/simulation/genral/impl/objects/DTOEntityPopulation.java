package server2client.simulation.genral.impl.objects;

public class DTOEntityPopulation {
    private final String entityName;
    private final int population;

    public DTOEntityPopulation(String entityName, int population) {
        this.entityName = entityName;
        this.population = population;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getPopulation() {
        return population;
    }
}
