package simulation.properties.property.random.value.impl;

import simulation.properties.property.random.value.api.AbstractNumericRndValueGen;

public class IntRndValueGen extends AbstractNumericRndValueGen<Integer> {

    public IntRndValueGen(Integer from, Integer to) {
        super(from, to);
    }

    /**
     * Using 'AbstractRndValueGen' random member. random.nextInt(x) generates an integer between 0 to x-1. The return value
     * is then added with 'from' in order to ensure the range is correct.
     */
    @Override
    public Integer generateRandomValue() {
        return random.nextInt(to - from + 1) + from;
    }
}
