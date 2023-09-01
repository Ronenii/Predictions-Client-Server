package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.api.TwoEntAction;
import simulation.properties.action.expression.api.Expression;

public class ProximityAction extends TwoEntAction {
    private final String targetEntityName;

    private final Expression depth;

    private final ProximitySubActions proximityActions;

    public ProximityAction(String property, String contextEntity, String targetEntityName, Expression depth, ProximitySubActions proximityActions) {
        super(ActionType.PROXIMITY, property, contextEntity);
        this.targetEntityName = targetEntityName;
        this.depth = depth;
        this.proximityActions = proximityActions;
    }

    @Override
    public Object getValue() {
        return null;
    }

    public void Invoke(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, EntityInstance[][] grid, int lastChangeTickCount) {
        int depthValue = (int) depth.evaluate();
        int sourceEntRow = 0, sourceEntColumn = 0, startingRowIndex = sourceEntRow - depthValue, startingColIndex = sourceEntColumn;
        int circleIndexRow = startingRowIndex, circleIndexCol = startingColIndex;
        boolean firstIteration = true;

        //Todo: think of something else.
        while (firstIteration || (circleIndexRow != startingRowIndex && circleIndexCol != startingColIndex)) {
            firstIteration = false;

            if (grid[circleIndexRow][circleIndexCol] == secondEntityInstance) {
                proximityActions.invoke(firstEntityInstance, secondEntityInstance, lastChangeTickCount);
                break;
            }

            if (circleIndexCol == sourceEntColumn + depthValue) {
                if (circleIndexRow == sourceEntRow + depthValue) {
                    circleIndexCol--;
                } else {
                    circleIndexRow--;
                }
            } else if (circleIndexRow == sourceEntRow - depthValue) {
                circleIndexCol++;
            } else if (circleIndexRow == sourceEntRow + depthValue) {
                if(circleIndexCol == sourceEntColumn - depthValue) {
                    circleIndexRow--;
                }
                else {
                    circleIndexCol--;
                }
            }
            else {
                circleIndexRow--;
            }
        }
    }
}
