package properties.property.random.value.impl;
import properties.property.random.value.api.AbstractNumericRndValueGen;

public class DoubleRndValueGen extends AbstractNumericRndValueGen <Double> {

    public DoubleRndValueGen(Double from, Double to) {
        super(from, to);
    }

    /**
     * Using 'AbstractRndValueGen' random member. random.nextDouble() generates a double between 0.0 to 1.0. The return value
     * is then multiplied by 'from + (to - from)' in order to shift the random number to fit within the requested range.
     */
    @Override
    public Double generateRandomValue() {
        return from + (to - from) * random.nextDouble();
    }
}
