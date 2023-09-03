package simulation.objects.world.grid;

import simulation.objects.entity.EntityInstance;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The grid of the world containing all living entity instances. If a certain cell does not contain an entity
 * of if the entity in said cell has died, the cell shall be 'null'.
 * This class is also responsible for moving and validating the movement of the entities.
 */
public class Grid {

    private final EntityInstance[][] grid;
    private final int rows;
    private final int columns;

    public Grid(int height, int width) {
        grid = new EntityInstance[height][width];
        this.rows = height;
        this.columns = width;
    }

    /**
     * Iterates through the grid and tries to move around all entities.
     */
    public void moveAllEntities() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] != null) {
                    tryToMoveEntity(grid[i][j]);
                }
            }
        }
    }

    /**
     * Raffles a random direction for the entity to move to. Each time validates said location is available.
     * If the location is available then updates both the grid and the entity.
     * Else leaves the grid and the entity as is.
     * <p>
     * Added Console printing to show what happened.
     *
     * @param entityInstance The entity we try to move
     */
    private void tryToMoveEntity(EntityInstance entityInstance) {
        EnumSet<Direction> directionSet = EnumSet.allOf(Direction.class);
        Random random = new Random();

        while (!directionSet.isEmpty()) {
            int directionIndex = random.nextInt(directionSet.size());
            Direction direction = (Direction) directionSet.toArray()[directionIndex];

            if (isValidMove(entityInstance, direction)) {
                moveEntity(entityInstance, direction);
                System.out.printf("%s has moved %s\n", entityInstance.getInstanceEntityName(), direction.name());
                return;
            }

            directionSet.remove(direction);
        }

        System.out.printf("%s has no where to move %s\n", entityInstance.getInstanceEntityName());
    }

    /**
     * Checks if a certain coordinate we try to move to in the grid is taken by another entity or is empty.
     *
     * @param entityInstance The instance we want to get the location of
     * @param direction      The direction we want to move the instance to
     * @return If the location we try to move the instance to is taken or not.
     */
    private boolean isValidMove(EntityInstance entityInstance, Direction direction) {
        switch (direction) {
            case UP:
                return grid[toRow(entityInstance.getX() - 1)][toColumn(entityInstance.getY())] != null;
            case DOWN:
                return grid[toRow(entityInstance.getX() + 1)][toColumn(entityInstance.getY())] != null;
            case LEFT:
                return grid[toRow(entityInstance.getX())][toColumn(entityInstance.getY() - 1)] != null;
            case RIGHT:
                return grid[toRow(entityInstance.getX())][toColumn(entityInstance.getY() + 1)] != null;
        }
        return false;
    }

    /**
     * Moves the entity to the given direction and updates both grid cells and the entity's inner x,y coordinate members.
     */
    private void moveEntity(EntityInstance entityInstance, Direction direction) {
        Point prevCoords = new Point(entityInstance.getX(), entityInstance.getY());
        Point newCoords = null;
        switch (direction) {
            case UP: {
                int newRow = toRow(entityInstance.getX() - 1);
                entityInstance.setX(newRow);
                newCoords = new Point(newRow, entityInstance.getY());
                break;
            }
            case DOWN: {
                int newRow = toRow(entityInstance.getX() + 1);
                entityInstance.setX(newRow);
                newCoords = new Point(newRow, entityInstance.getY());
                break;
            }
            case LEFT: {
                int newCol = toColumn(entityInstance.getY() - 1);
                entityInstance.setY(newCol);
                newCoords = new Point(entityInstance.getX(), newCol);
                break;
            }
            case RIGHT: {
                int newCol = toColumn(entityInstance.getY() + 1);
                entityInstance.setY(newCol);
                newCoords = new Point(entityInstance.getX(), newCol);
                break;
            }
        }
        grid[newCoords.x][newCoords.y] = grid[prevCoords.x][prevCoords.y];
        grid[prevCoords.x][prevCoords.y] = null;
    }

    /**
     * Scatters all given entity instances across the grid.
     * 1) Creates a Set of all available coordinates on the grid.
     * 2) Iterates over all given entity instances
     * 2.1) Raffles a coordinate for the current entity instance
     * 2.2) Sets grid cell corresponding to the coordinate as the current instance
     * and sets the current instance's inner coordinate accordingly.
     * 2.3) Remove the raffled coordinate from the coordinate set and run loop again.
     *
     * @param entityInstances All Entity instances generated in the beginning of the simulation run.
     */
    public void populateGrid(List<EntityInstance> entityInstances) {
        Random random = new Random();
        Set<Point> coordinateSet = getAllCoordinatesSet();

        for (EntityInstance e : entityInstances
        ) {
            int coordinateIndex = random.nextInt(coordinateSet.size());
            Point coordinate = (Point) coordinateSet.toArray()[coordinateIndex];

            grid[coordinate.x][coordinate.y] = e;
            e.setX(coordinate.x);
            e.setY(coordinate.y);

            coordinateSet.remove(coordinate);
        }
    }

    /**
     * @return A set of all possible coordinates on the grid represented as Points.
     */
    private Set<Point> getAllCoordinatesSet() {
        HashSet<Point> coordinates = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                coordinates.add(new Point(i, j));
            }
        }
        return coordinates;
    }

    /**
     * Converts the given x to a valid row in the grid.
     */
    private int toRow(int x) {
        return x % rows;
    }

    /**
     * Converts the given y to a valid column in the grid.
     */
    private int toColumn(int y) {
        return y % columns;
    }
}
