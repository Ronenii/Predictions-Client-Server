package simulation.properties.action.api;

public abstract class AbstractAction implements Action {

    private final ActionType type;
    private final String contextProperty;
    private final String contextEntity;

    private final SecondaryEntity secondaryEntity;

    public AbstractAction(ActionType type, String contextProperty, String contextEntity, SecondaryEntity secondaryEntity) {
        this.type = type;
        this.contextEntity = contextEntity;
        this.contextProperty = contextProperty;
        this.secondaryEntity = secondaryEntity;
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

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public static class SecondaryEntity{
        private final String contextEntity;
        private final int count;
        private final Action Condition;

        public SecondaryEntity(String contextEntity, int count, Action condition) {
            this.contextEntity = contextEntity;
            this.count = count;
            Condition = condition;
        }

        public String getContextEntity() {
            return contextEntity;
        }

        public int getCount() {
            return count;
        }

        public Action getCondition() {
            return Condition;
        }
    }

}
