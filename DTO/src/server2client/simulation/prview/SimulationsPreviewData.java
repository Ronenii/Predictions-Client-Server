package server2client.simulation.prview;

public class SimulationsPreviewData {
    private final PreviewData[] previewDataArray;

    public SimulationsPreviewData(PreviewData[] previewDataArray) {
        this.previewDataArray = previewDataArray;
    }

    public PreviewData[] getPreviewDataArray() {
        return previewDataArray;
    }
}
