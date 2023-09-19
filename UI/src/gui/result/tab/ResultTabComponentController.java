package gui.result.tab;

import engine2ui.simulation.runtime.ResultData;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.tab.chart.ChartComponentController;
import gui.result.tab.statistics.StatisticsComponentController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

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

    private boolean isComponentLoaded;
    private boolean isComponentDisabled;

    @FXML
    public void initialize() {
        if (statisticsComponentController != null && chartComponentController != null) {
            statisticsComponentController.setMainController(this);
            chartComponentController.setMainController(this);
        }

        this.isComponentDisabled = true;
    }

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void enableResultComponent() {
        executionResultTP.disableProperty().set(false);
    }

    public void disableResultComponent() {
        disableComponentFadeTransition();
        executionResultTP.disableProperty().set(true);
    }

    private void disableComponentFadeTransition() {
        if (!isComponentDisabled) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), executionResultTP);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0.2);
            fadeTransition.play();
            executionResultTP.setOpacity(0.2);
            isComponentLoaded = false;
            isComponentDisabled = true;
        }
    }

    /**
     * If the simulation chosen in the execution queue is completed then this function will enable the result tab
     * component and display the results.
     */
    public void updateToChosenSimulation(SimulationRunData simulationRunData){
        if (simulationRunData.isCompleted() || simulationRunData.isSimulationSkipped()) {
            enableResultComponent();
            loadComponent(simulationRunData.getResultData());
        } else {
            disableResultComponent();
            clearComponent();
        }
    }

    private void loadComponent(ResultData resultData) {
        loadComponentFadeTransition();
        clearComponent();
        chartComponentController.showPopulationData(resultData.getPopulationChartData());
        if(resultData.getEntities() != null){
            statisticsComponentController.loadComponent(resultData.getEntities());
        }
    }

    private void loadComponentFadeTransition() {
        if(!isComponentLoaded){
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), executionResultTP);
            fadeTransition.setFromValue(0.2);
            fadeTransition.setToValue(1);
            fadeTransition.play();
            executionResultTP.setOpacity(1);
            isComponentLoaded = true;
            isComponentDisabled = false;
        }
    }

    public void clearComponent() {
        chartComponentController.clearChart();
        statisticsComponentController.clearComponent();
    }

    public int getSimulationCurrentTicks() {
       return mainController.getSimulationCurrentTicks();
    }
}
