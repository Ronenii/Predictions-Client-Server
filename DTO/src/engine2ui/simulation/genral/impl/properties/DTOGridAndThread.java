package engine2ui.simulation.genral.impl.properties;

public class DTOGridAndThread {
    private final int gridRows;
    private final int gridColumns;
    private final int threadCount;

    public DTOGridAndThread(int gridRows, int gridColumns, int threadCount) {
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
        this.threadCount = threadCount;
    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
