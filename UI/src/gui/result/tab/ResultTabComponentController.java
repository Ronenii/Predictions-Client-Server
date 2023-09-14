package gui.result.tab;

import engine2ui.simulation.runtime.ResultData;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.tab.chart.ChartComponentController;
import gui.result.tab.statistics.StatisticsComponentController;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class ResultTabComponentController {
    private ResultComponentController mainController;

    @FXML
    private GridPane statisticsComponent;

    @FXML
    private StatisticsComponentController statisticsComponentController;

    @FXML
    private Chart chartComponent;

    @FXML
    private ChartComponentController chartComponentController;

    @FXML
    private Label executionResultLabel;

    @FXML
    private TabPane executionResultTP;

    @FXML
    public void initialize() {
        if (statisticsComponentController != null && chartComponentController != null) {
            statisticsComponentController.setMainController(this);
            chartComponentController.setMainController(this);
        }
    }

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void enableResultComponent() {
        executionResultTP.disableProperty().set(false);
    }

    public void disableResultComponent() {
        executionResultTP.disableProperty().set(true);
    }

    /**
     * If the simulation chosen in the execution queue is completed then this function will enable the result tab
     * component and display the results.
     */
    public void updateToChosenSimulation(SimulationRunData simulationRunData){
        if (simulationRunData.isCompleted()) {
            enableResultComponent();
            loadComponent(simulationRunData.getResultData());
        } else {
            disableResultComponent();
            clearComponent();
        }
    }

    private void loadComponent(ResultData resultData) {
        clearComponent();
        chartComponentController.showPopulationData(resultData.getPopulationChartData());
        statisticsComponentController.loadComponent(resultData.getEntities());
    }

    private void clearComponent() {
        chartComponentController.clearChart();
    }
}
