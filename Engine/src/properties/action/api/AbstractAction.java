package properties.action.api;

import objects.entity.Entity;
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

    @Override
    public int hashCode() {
        return property.getName().length() * type.name().length() * contextEntity.getName().length();
    }

    @Override
    public boolean equals(Object obj) {
        Action toCompare = (AbstractAction) obj;
        return (toCompare.getType().equals(this.type)) && (toCompare.getContextEntity().equals(this.contextEntity)) && (toCompare.getProperty().equals(this.property));
    }
}
