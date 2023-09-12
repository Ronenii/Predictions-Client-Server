package gui.result.models;

import javafx.beans.property.SimpleIntegerProperty;

public class PopulationData {
    private String name;
    private SimpleIntegerProperty population;

    public PopulationData(String name, SimpleIntegerProperty population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population.get();
    }

    public SimpleIntegerProperty populationProperty() {
        return population;
    }
}
