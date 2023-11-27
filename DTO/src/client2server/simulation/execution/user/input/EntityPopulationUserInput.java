package client2server.simulation.execution.user.input;

public class EntityPopulationUserInput {
    private final int reqId;
    private final String name;
    private final int population;

    public EntityPopulationUserInput(int reqId, String name, int population) {
        this.reqId = reqId;
        this.name = name;
        this.population = population;
    }

    public int getReqId() {
        return reqId;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }
}
