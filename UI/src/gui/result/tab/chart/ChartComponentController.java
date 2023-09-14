package gui.result.tab.chart;

import gui.result.tab.ResultTabComponentController;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ChartComponentController {
    private ResultTabComponentController mainController;

    @FXML
    private LineChart<Integer, Integer> chart;  // Specify the types for LineChart

    @FXML
    private NumberAxis ticksAxis;

    @FXML
    private NumberAxis entityQuantityAxis;

    public void setMainController(ResultTabComponentController mainController) {
        this.mainController = mainController;
    }

    /**
     * Builds a graph based on the given population list. Each index in the list represents
     * the population in intervals of 20 ticks.
     */
    public void showPopulationData(List<Integer> population) {
        chart.getData().clear();
        int tick = 0;
        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

        // Clear existing data in the series
        series.getData().clear();

        for (Integer i : population) {
            series.getData().add(new XYChart.Data<>(tick, i));
            tick += 20;
        }

        // Set the series as the chart's data
        chart.getData().setAll(series);
    }

    public void clearChart(){
        chart.getData().clear();
    }
}
