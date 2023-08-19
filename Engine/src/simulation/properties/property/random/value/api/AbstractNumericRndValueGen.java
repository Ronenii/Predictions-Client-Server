package simulation.properties.property.random.value.api;

import java.io.Serializable;

public abstract class AbstractNumericRndValueGen <T> extends AbstractRndValueGen <T> implements Serializable {
    protected final T from;
    protected final T to;

    public AbstractNumericRndValueGen(T from, T to) {
        this.from = from;
        this.to = to;
    }
}
