package simulation.properties.activition;

import java.io.Serializable;

public class Activation implements Serializable {
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

    @Override
    public int hashCode() {
        return ticks * (int)Math.ceil(probability* 100);
    }

    @Override
    public boolean equals(Object obj) {
        Activation toCompare = (Activation) obj;
        return (toCompare.ticks == this.ticks) && (toCompare.probability == this.probability);
    }
}
