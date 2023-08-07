package properties.action.api;

import objects.Entity;
import properties.property.api.Property;

public abstract class AbstractAction implements Action{
    private final ActionType type;
    private final Property property;
    private final Entity contextEntity;

    public AbstractAction(ActionType type, Property property, Entity contextEntity) {
        this.type = type;
        this.property = property;
        this.contextEntity = contextEntity;
    }

    @Override
    public ActionType getType() {
        return type;
    }

    @Override
    public Property getProperty() {
        return property;
    }

    @Override
    public Entity getContextEntity() {
        return contextEntity;
    }
}
