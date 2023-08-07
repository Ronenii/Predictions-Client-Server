package properties.property.random.value.impl;

import properties.property.random.value.api.AbstractRndValueGen;

public class BoolRndValueGen extends AbstractRndValueGen <Boolean> {

    /**
     * Using 'AbstractRndValueGen' random member. random.nextBoolean() generates true or false.
     */
    @Override
    public Boolean generateRandomValue() {
        return random.nextBoolean();
    }
}
