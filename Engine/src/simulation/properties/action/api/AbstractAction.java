package simulation.properties.action.api;

public abstract class AbstractAction implements Action{
    private final ActionType type;
    private final String property;
    private final String contextEntity;
    private final String contextValue;

    public AbstractAction(ActionType type, String property, String contextEntity, String contextValue) {
        this.type = type;
        this.property = property;
        this.contextEntity = contextEntity;
        this.contextValue = contextValue;
    }

    @Override
    public ActionType getType() {
        return type;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public String getContextEntity() {
        return contextEntity;
    }

    @Override
    public String getContextValue() {
        return this.contextValue;
    }

    @Override
    public boolean equals(Object obj) {
        Action toCompare = (AbstractAction) obj;
        return (toCompare.getType().equals(this.type)) && (toCompare.getContextEntity().equals(this.contextEntity)) && (toCompare.getProperty().equals(this.property));
    }
}
