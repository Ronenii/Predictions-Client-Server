package gui.app.menu.result.models;

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
