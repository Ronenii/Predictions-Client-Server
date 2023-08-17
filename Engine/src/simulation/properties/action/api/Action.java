package simulation.properties.action.api;

public interface Action {
    ActionType getType();

    String getContextEntity();

    String getProperty();

    String getContextValue();

    void Invoke();

}
