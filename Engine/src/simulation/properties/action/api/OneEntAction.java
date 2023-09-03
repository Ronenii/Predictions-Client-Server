package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;

import java.io.Serializable;

public abstract class OneEntAction implements Action, Serializable {
    private final ActionType type;
    private final String contextProperty;
    private final String contextEntity;

    public OneEntAction(ActionType type, String property, String contextEntity) {
        this.type = type;
        this.contextProperty = property;
        this.contextEntity = contextEntity;
    }

    @Override
    public ActionType getType() {
        return type;
    }

    @Override
    public String getContextProperty() {
        return contextProperty;
    }

    @Override
    public String getContextEntity() {
        return contextEntity;
    }

    abstract public void Invoke(EntityInstance entityInstance, int lastChangeTickCount);

    @Override
    public boolean equals(Object obj) {
        Action toCompare = (OneEntAction) obj;
        return (toCompare.getType().equals(this.type)) && (toCompare.getContextEntity().equals(this.contextEntity)) && (toCompare.getContextProperty().equals(this.contextProperty));
    }
}
