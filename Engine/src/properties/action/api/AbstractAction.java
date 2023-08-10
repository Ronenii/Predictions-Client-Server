package properties.action.api;

import objects.entity.Entity;
import properties.property.api.Property;

public abstract class AbstractAction implements Action{
    private final ActionType type;
    private final String property;
    private final String contextEntity;

    public AbstractAction(ActionType type, String property, String contextEntity) {
        this.type = type;
        this.property = property;
        this.contextEntity = contextEntity;
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
}
