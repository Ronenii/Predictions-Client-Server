package gui.app.menu.execution.result.chart;

import gui.app.menu.execution.result.ResultTabComponentController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;

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
    public void showPopulationData(Map<String, List<Integer>> population) {
        // Clear the chart.
        chart.getData().clear();

        for (String s : population.keySet()) {
            // Initialize a new line chart.
            XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

            // Population documentation happens every tick and is limited to 1000 ticks
            // since populations converge at most after hundreds of ticks.
            for (int tick = 0; tick < population.get(s).size() && tick < 1000; tick++) {
                // Add to the line at index 'tick' the population at said index.
                series.getData().add(new XYChart.Data<>(tick, population.get(s).get(tick)));
            }

            // Set the name of the chart
            series.setName(s);

            // Add the chart to the graph.
            chart.getData().add(series);
        }

        // Display legend
        chart.setLegendVisible(true);
    }

    public void clearChart() {
        chart.getData().clear();
    }
}
