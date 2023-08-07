package jaxb.unmarshal.converter.api;

import jaxb.schema.generated.PRDWorld;
import objects.world.World;

public interface Converter {

    World PRDWorld2World(PRDWorld prdWorld);
}
