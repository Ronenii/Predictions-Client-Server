package manager.event;

import engine2ui.simulation.runtime.SimulationRunData;

public interface SimulationUpdatedEvent {
    void onSimulationUpdated(SimulationRunData runData);
}
