package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

public class ProximityAction extends AbstractAction {
    private final String targetEntityName;

    private final Expression depth;

    private final ProximitySubActions proximityActions;

    public ProximityAction(Expression property, String contextEntity,SecondaryEntity secondaryEntity, String targetEntityName, Expression depth, ProximitySubActions proximityActions) {
        super(ActionType.PROXIMITY, property, contextEntity, secondaryEntity);
        this.targetEntityName = targetEntityName;
        this.depth = depth;
        this.proximityActions = proximityActions;
    }

    @Override
    public Object getValue() {
        return null;
    }

    public String getTargetEntityName() {
        return targetEntityName;
    }

    public String getDepthString() {
        return depth.toString();
    }

    public ProximitySubActions getProximityActions() {
        return proximityActions;
    }

    public Expression getDepthExpression() {
        return depth;
    }

    @Override
    public Expression getValueExpression() {
        return null;
    }

    public int getSubActionsCount() {
        int ret = 0;

        if(proximityActions != null) {
            ret = proximityActions.getSubActionsCount();
        }

        return ret;
    }

    public void invoke(EntityInstance entityInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        int depthValue = getDepthValue();

        for (int i = -depthValue; i <= depthValue; i++) {
            for (int j = -depthValue; j <= depthValue; j++) {
                int x = adjustCoordinate((entityInstance.row + i), grid.getRows());
                int y = adjustCoordinate((entityInstance.column + j), grid.getColumns());

                if (grid.getInstance(x,y) != null && grid.getInstance(x,y).getInstanceEntityName().equals(targetEntityName)) {
                    // Target entity found within the circle
                    proximityActions.invoke(entityInstance, grid.getInstance(x,y), grid, lastChangeTickCount);
                    return;
                }
            }
        }
    }

    private int getDepthValue() throws CrashException {
        Object depthObject = depth.evaluate();
        int ret = 0;

        if(depthObject instanceof Integer) {
            ret = (int)depthObject;
        } else if (depthObject instanceof Double) {
            double doubleDepthObject = (double)depthObject;
            ret = (int)doubleDepthObject;
        }

        if (ret == 0){
            throw new CrashException("in action 'Proximity', depth value can not be 0!");
        }

        return ret;
    }

    public void invokeWithSecondary(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        if(getContextEntity().equals(firstEntityInstance.getInstanceEntityName())) {
            invoke(firstEntityInstance, grid, lastChangeTickCount);
        }
        else {
            invoke(secondEntityInstance, grid, lastChangeTickCount);
        }
    }

    /**
     * Helper for the 'Invoke' method, adjust the coordinate in case the circle pass the grid length.
     */
    private int adjustCoordinate(int currentCoordinate, int length){
        int ret;

        if(currentCoordinate < 0) {
            ret = length + currentCoordinate;
        }
        else {
            ret = currentCoordinate % length;
        }

        return ret;
    }

    @Override
    public Action dupAction() {
        Expression dupProperty = null;

        if(getContextProperty() != null) {
            dupProperty = getContextProperty().dupExpression();
        }

        return new ProximityAction(dupProperty, getContextEntity(), getSecondaryEntity(), targetEntityName, depth.dupExpression(), proximityActions.dupProximitySubActions());
    }
}
