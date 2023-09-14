package gui.result.tab;

import engine2ui.simulation.runtime.ResultData;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.tab.chart.ChartComponentController;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

public class ResultTabComponentController {
    private ResultComponentController mainController;

    @FXML
    private Chart chartComponent;

    @FXML
    private ChartComponentController chartComponentController;

    @FXML
    private Label executionResultLabel;

    @FXML
    private TabPane executionResultTP;

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void enableResultComponent() {
        executionResultTP.disableProperty().set(false);
    }

    public void disableResultComponent() {
        executionResultTP.disableProperty().set(true);
    }

    public void updateToChosenSimulation(SimulationRunData simulationRunData){
        if (simulationRunData.isCompleted()) {
            enableResultComponent();
            loadResultComponent(simulationRunData.getResultData());
        } else {
            disableResultComponent();
            unloadResultComponent();
        }
    }

    // TODO: Implement both of these
    private void loadResultComponent(ResultData resultData) {
        unloadResultComponent();
        chartComponentController.showPopulationData(resultData.getPopulationChartData());
    }

    private void unloadResultComponent() {
        chartComponentController.clearChart();
    }
}
