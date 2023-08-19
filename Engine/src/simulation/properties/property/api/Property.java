package simulation.properties.property.api;

/**
 * Property interface - its purpose is to act as a connecting link between the property object and its implementation
 * to its functions and use.
 * Declare the 'Property' interface and then utilize it to create new instance of any property type classes in the 'impl' package  (Integer, Double, String or Boolean).
 */

public interface Property {
    String getName();

    PropertyType getType();

    Object getValue();

    void setValue(Object value);

    Boolean isRandInit();

    void updateValueAndIsRandomInit(Object value, boolean isRandomInit);

    public Property dupProperty();

    public Property generateRandomValueProperty();

}
