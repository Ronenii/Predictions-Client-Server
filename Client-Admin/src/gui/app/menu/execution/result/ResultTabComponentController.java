package gui.app.menu.execution.result;

import gui.app.menu.execution.ExecutionComponentController;
import server2client.simulation.runtime.ResultData;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.api.Controller;
import gui.app.menu.execution.result.chart.ChartComponentController;
import gui.app.menu.execution.result.statistics.StatisticsComponentController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ResultTabComponentController implements Controller {

    private int prevTick = 0;
    private Controller mainController;

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

    public void setMainController(ExecutionComponentController controller) {
        this.mainController = controller;
    }

    public void enableResultComponent() {
        enableComponent();
        executionResultTP.disableProperty().set(false);
    }

    public void disableResultComponent() {
        disableComponent();
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

    private void disableComponent(){
            disableComponentFadeTransition();
            isComponentLoaded = false;
            isComponentDisabled = true;
    }

    private  void enableComponent(){
            enableComponentFadeTransition();
            isComponentLoaded = true;
            isComponentDisabled = false;
    }

    /**
     * If the simulation chosen in the execution queue is completed then this function will enable the result tab
     * component and display the results.
     */
    public void updateToChosenSimulation(SimulationRunData simulationRunData){
        if (simulationRunData.isCompleted() || simulationRunData.isSimulationSkipped()) {
            enableResultComponent();
            if(simulationRunData.getTick() != prevTick){
                displayResults(simulationRunData.getResultData());
            }
            prevTick = simulationRunData.getTick();
        } else {
            disableResultComponent();
            clearComponent();
        }
    }

    private void displayResults(ResultData resultData) {
        enableResultComponent();
        clearComponent();
        chartComponentController.showPopulationData(resultData.getPopulationChartData());
        if(resultData.getEntities() != null){
            statisticsComponentController.loadComponent(resultData.getEntities());
        }
    }

    private void enableComponentFadeTransition() {
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

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
