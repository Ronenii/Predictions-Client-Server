package simulation.properties.action.api;

public interface Action {
    ActionType getType();

    String getContextEntity();

    String getProperty();

    void Invoke();

}
