package properties.activition;

public class Activation {
    private int ticks;

    public Activation(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }

    private double probability;

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
