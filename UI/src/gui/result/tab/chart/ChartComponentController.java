package gui.result.tab.chart;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ChartComponentController {

    @FXML
    private LineChart<Integer, Integer> chart;  // Specify the types for LineChart

    @FXML
    private CategoryAxis ticksAxis;

    @FXML
    private NumberAxis entityQuantityAxis;

    public void showPopulationData(List<Integer> population) {
        int tick = 1;
        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();  // Specify the types for XYChart.Series
        for (Integer i : population) {
            series.getData().add(new XYChart.Data<>(tick++, i));  // Use 'i' instead of 'population'
        }
        chart.getData().setAll(series);  // Use setAll to clear and set the data
    }

    public void clearChart(){
        chart.getData().clear();
    }
}
