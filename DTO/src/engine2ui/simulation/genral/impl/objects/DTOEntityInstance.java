package engine2ui.simulation.genral.impl.objects;

import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;

import java.io.Serializable;
import java.util.Map;

public class DTOEntityInstance implements Serializable {
    private Map<String, DTOProperty> properties;

    public DTOEntityInstance(Map<String, DTOProperty> properties){
        this.properties = properties;
    }

    public DTOProperty getDTOPropertyByName(String propertyName){
        return properties.get(propertyName);
    }


}
