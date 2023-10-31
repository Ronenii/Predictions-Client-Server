package server2client.simulation.genral.impl.objects;

import server2client.simulation.genral.impl.properties.DTOProperty;

import java.io.Serializable;
import java.util.Map;

public class DTOEntityInstance implements Serializable {
    private final Map<String, DTOProperty> properties;

    public DTOEntityInstance(Map<String, DTOProperty> properties){
        this.properties = properties;
    }

    public DTOProperty getDTOPropertyByName(String propertyName){
        return properties.get(propertyName);
    }


}
