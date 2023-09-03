package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;

import java.io.Serializable;

public abstract class TwoEntAction implements Action, Serializable {
    private final ActionType type;
    private final String contextProperty;
    private final String contextEntity;

    public TwoEntAction(ActionType type, String contextProperty, String contextEntity) {
        this.type = type;
        this.contextProperty = contextProperty;
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

    @Override
    public boolean equals(Object obj) {
        Action toCompare = (OneEntAction) obj;
        return (toCompare.getType().equals(this.type)) && (toCompare.getContextEntity().equals(this.contextEntity)) && (toCompare.getContextProperty().equals(this.contextProperty));
    }
}
