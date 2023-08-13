package simulation.properties.property.random.value.api;

public abstract class AbstractNumericRndValueGen <T> extends AbstractRndValueGen <T> {
    protected final T from;
    protected final T to;

    public AbstractNumericRndValueGen(T from, T to) {
        this.from = from;
        this.to = to;
    }
}
