package server2client.simulation.status;

public class SimulationsStatusData {
    private final StatusData[] statusDataArray;

    public SimulationsStatusData(StatusData[] statusDataArray) {
        this.statusDataArray = statusDataArray;
    }

    public StatusData[] getStatusDataArray() {
        return statusDataArray;
    }
}
