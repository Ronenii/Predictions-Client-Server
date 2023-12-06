package server2client.simulation.genral.impl.properties;

public class DTOGrid {
    private final int gridRows;
    private final int gridColumns;

    public DTOGrid(int gridRows, int gridColumns) {
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

}
