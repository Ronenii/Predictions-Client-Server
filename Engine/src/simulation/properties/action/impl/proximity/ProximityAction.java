package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

public class ProximityAction extends AbstractAction {
    private final String targetEntityName;

    private final Expression depth;

    private final ProximitySubActions proximityActions;

    public ProximityAction(String property, String contextEntity,SecondaryEntity secondaryEntity, String targetEntityName, Expression depth, ProximitySubActions proximityActions) {
        super(ActionType.PROXIMITY, property, contextEntity, secondaryEntity);
        this.targetEntityName = targetEntityName;
        this.depth = depth;
        this.proximityActions = proximityActions;
    }

    @Override
    public Object getValue() {
        return null;
    }

    public void invoke(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, EntityInstance[][] grid, int lastChangeTickCount) {
        int depthValue = (int) depth.evaluate();

        for (int i = -depthValue; i <= depthValue; i++) {
            for (int j = -depthValue; j <= depthValue; j++) {
                int x = adjustCoordinate((firstEntityInstance.xGridCoordinate + i), grid.length);
                int y = adjustCoordinate((firstEntityInstance.yGridCoordinate + j), grid[0].length);

                if (grid[x][y] == secondEntityInstance) {
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
