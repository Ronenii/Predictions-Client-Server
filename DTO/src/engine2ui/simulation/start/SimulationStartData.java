package engine2ui.simulation.start;

import engine2ui.simulation.genral.impl.objects.DTOEntity;

import java.time.LocalDateTime;
import java.util.List;

public class SimulationStartData {
    private final String simId;

    private final String status;

    public SimulationStartData(String simId, String status) {
        this.simId = simId;
        this.status = status;
    }

    public String getSimId() {
        return simId;
    }

    public String getStatus() {
        return status;
    }
}
