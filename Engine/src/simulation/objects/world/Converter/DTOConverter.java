package simulation.objects.world.Converter;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.NonRangedDTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
import simulation.objects.entity.Entity;
import simulation.properties.property.api.Property;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;

import java.util.Map;

/**
 * Responsible for conversion of objects and properties in the engine to DTOs.
 */
public class DTOConverter {

    public DTOEntity[] convertEntities2DTOEntities(Map<String, Entity> entities) {
        Entity[] entityArr = entities.values().toArray(new Entity[0]);
        DTOEntity[] dtoEntities = new DTOEntity[entityArr.length];

        for (int i = 0; i < entityArr.length; i++) {
            dtoEntities[i] = new DTOEntity(entityArr[i].getName(), entityArr[i].getStartingPopulation(), entityArr[i].getCurrentPopulation(), convertProperties2DTOProperties(entityArr[i].getProperties()));
        }

        return dtoEntities;
    }

    private DTOProperty[] convertProperties2DTOProperties(Map<String, Property> properties) {
        Property[] propertyArr = properties.values().toArray(new Property[0]);
        DTOProperty[] dtoProperties = new DTOProperty[propertyArr.length];

        for (int i = 0; i < propertyArr.length; i++) {
            switch (propertyArr[i].getType()) {
                case DECIMAL:
                    IntProperty intProperty = (IntProperty) propertyArr[i];
                    dtoProperties[i] = new RangedDTOProperty(intProperty.getName(), intProperty.getType().toString(), intProperty.isRandInit(), intProperty.getFrom(), intProperty.getTo());
                    break;
                case FLOAT:
                    DoubleProperty doubleProperty = (DoubleProperty) propertyArr[i];
                    dtoProperties[i] = new RangedDTOProperty(doubleProperty.getName(), doubleProperty.getType().toString(), doubleProperty.isRandInit(), doubleProperty.getFrom(), doubleProperty.getTo());
                    break;
                case BOOLEAN:
                case STRING:
                    dtoProperties[i] = new NonRangedDTOProperty(propertyArr[i].getName(), propertyArr[i].getType().toString(), propertyArr[i].isRandInit());
                    break;
            }
        }
        return dtoProperties;
    }
}
