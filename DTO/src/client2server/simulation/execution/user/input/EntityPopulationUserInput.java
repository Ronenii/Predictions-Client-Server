package client2server.simulation.execution.user.input;

public class EntityPopulationUserInput {
    private final String name;
    private final int population;

    public EntityPopulationUserInput(String name, int population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }
}
