package simulation.properties.property.random.value.api;

/**
 * RandomValueGenerator interface - its purpose is to act as a connecting link between the Random - Value generator object and its implementation
 * to its functions and use.
 * Declare the 'RandomValueGenerator' interface and then utilize it to create new instance of any random value type classes in the 'impl' package (Integer, Double, String or Boolean).
 * Note: when creating an instance of random integer or double, the constructor should receive a range.
 */

public interface RandomValueGenerator<T> {

    T generateRandomValue();
}

