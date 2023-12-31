package gui.app.menu.execution.result.data;

public class HistogramData {
    private final String value;
    private final int quantity;

    public HistogramData(String value, int quantity) {
        this.value = value;
        this.quantity = quantity;
    }

    public String getValue() {
        return value;
    }

    public int getQuantity() {
        return quantity;
    }
}
