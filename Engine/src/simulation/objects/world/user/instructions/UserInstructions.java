package simulation.objects.world.user.instructions;

public class UserInstructions {
    public boolean isSimulationRunning;
    public boolean isSimulationPaused;
    public boolean isSimulationStopped;

    public UserInstructions(boolean isSimulationRunning, boolean isSimulationPaused, boolean isSimulationStopped) {
        this.isSimulationRunning = isSimulationRunning;
        this.isSimulationPaused = isSimulationPaused;
        this.isSimulationStopped = isSimulationStopped;
    }
}
