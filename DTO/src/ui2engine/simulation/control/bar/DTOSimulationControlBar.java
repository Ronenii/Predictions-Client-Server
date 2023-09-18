package ui2engine.simulation.control.bar;

public class DTOSimulationControlBar {
    private final boolean toStop;
    private final boolean toPause;
    private final boolean toPlay;
    private final boolean toSkipForward;

    public DTOSimulationControlBar(boolean toStop, boolean toPause, boolean toPlay, boolean toSkipForward) {
        this.toStop = toStop;
        this.toPause = toPause;
        this.toPlay = toPlay;
        this.toSkipForward = toSkipForward;
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

    public boolean isToSkipForward() {
        return toSkipForward;
    }
}

