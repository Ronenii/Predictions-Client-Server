package engine2ui.simulation.execution;

import engine2ui.simulation.start.SimulationStartData;

public class StartResponse {
    private final boolean success;

    private final String message;

    private SimulationStartData simStartData;

    public StartResponse(boolean success, String message, SimulationStartData simStartData){
        this.message = message;
        this.success = success;
        this.simStartData = simStartData;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public SimulationStartData getSimStartData() {
        return simStartData;
    }
}
