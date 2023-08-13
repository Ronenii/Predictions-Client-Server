package simulation.properties.property.random.value.api;

import java.util.Random;

public abstract class AbstractRndValueGen <T> implements RandomValueGenerator<T> {
    protected final Random random;

    public AbstractRndValueGen() {
        this.random = new Random();
    }
}
