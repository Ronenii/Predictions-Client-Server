package ui2engine.simulation.control.bar;

public class DTOSimulationControlBar {
    private final boolean toStop;
    private final boolean toPause;
    private final boolean toPlay;

    public DTOSimulationControlBar(boolean toStop, boolean toPause, boolean toPlay) {
        this.toStop = toStop;
        this.toPause = toPause;
        this.toPlay = toPlay;
    }

    public boolean isToStop() {
        return toStop;
    }

    public boolean isToPause() {
        return toPause;
    }

    public boolean isToPlay() {
        return toPlay;
    }
}
