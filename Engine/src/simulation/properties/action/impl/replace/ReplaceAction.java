package simulation.properties.action.impl.replace;

import simulation.objects.entity.Entity;
import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

public class ReplaceAction extends AbstractAction {
    private final Entity newEntity;
    private final ReplaceActionType replaceType;

    public ReplaceAction(Expression property, String contextEntity, Entity newEntity, SecondaryEntity secondaryEntity, ReplaceActionType replaceType) {
        super(ActionType.REPLACE, property, contextEntity, secondaryEntity);
        this.newEntity = newEntity;
        this.replaceType = replaceType;
    }


    public ReplaceActionType getReplaceType() {
        return replaceType;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Expression getValueExpression() {
        return null;
    }

    public void invoke(EntityInstance firstEntityInstance, Grid grid, int lastChangeTickCount) {
        EntityInstance newInstance = newEntity.createNewEntityInstance();

        if(replaceType == ReplaceActionType.DERIVED) {
            newInstance.updateDerivedEntityInstance(firstEntityInstance, lastChangeTickCount);
        }

        grid.setInstanceInGrid(firstEntityInstance.row, firstEntityInstance.column, newInstance);
        firstEntityInstance.kill();
    }

    public void invokeWithSecondary(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, Grid grid, int lastChangeTickCount) {
        if(getContextEntity().equals(firstEntityInstance.getInstanceEntityName())) {
            invoke(firstEntityInstance, grid, lastChangeTickCount);
        }
        else {
            invoke(secondEntityInstance, grid, lastChangeTickCount);
        }
    }
}
