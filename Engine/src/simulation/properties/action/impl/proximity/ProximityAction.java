package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.AbstractAction;
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
        return depth.evaluate().toString();
    }

    @Override
    public Expression getValueExpression() {
        return null;
    }

    public int getSubActionsCount() {
        return proximityActions.getSubActionsCount();
    }

    public void invoke(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, Grid grid, int lastChangeTickCount) {
        int depthValue = (int) depth.evaluate();

        for (int i = -depthValue; i <= depthValue; i++) {
            for (int j = -depthValue; j <= depthValue; j++) {
                int x = adjustCoordinate((firstEntityInstance.row + i), grid.getRows());
                int y = adjustCoordinate((firstEntityInstance.column + j), grid.getColumns());

                if (grid.getInstance(x,y) == secondEntityInstance) {
                    // Target entity found within the circle
                    proximityActions.invoke(firstEntityInstance, secondEntityInstance, lastChangeTickCount);
                    break;
                }
            }
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

}
