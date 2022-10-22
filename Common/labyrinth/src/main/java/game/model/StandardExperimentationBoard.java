package game.model;

import java.util.*;
import java.util.function.Predicate;

/**
 * An ExperimentationBoard for StandardBoard.
 */
public class StandardExperimentationBoard extends StandardBoard implements ExperimentationBoard {

    /**
     * Creates a StandardExperimentationBoard with a deep copy of the given StandardBoard's state.
     */
    public StandardExperimentationBoard(StandardBoard board) {
        super(copyGrid(board), copyTile(board.getSpareTile()));

    }

    static Tile[][] copyGrid(StandardBoard board) {
        Tile[][] newGrid = new Tile[board.getHeight()][board.getWidth()];

        for (int row = 0; row < newGrid.length; row++) {
            for (int col = 0; col < newGrid[row].length; col++) {
                newGrid[row][col] = copyTile(board.getTileAt(new Position(row, col)));
            }
        }
        return newGrid;
    }

    private static Tile copyTile(Tile tile) {
        Set<Direction> newDirections = new HashSet<>();
        if (tile.connects(Direction.UP)) {newDirections.add(Direction.UP);}
        if (tile.connects(Direction.DOWN)) {newDirections.add(Direction.DOWN);}
        if (tile.connects(Direction.LEFT)) {newDirections.add(Direction.LEFT);}
        if (tile.connects(Direction.RIGHT)) {newDirections.add(Direction.RIGHT);}

        return new Tile(newDirections, tile.getTreasure());
    }

    /**
     * Find all the Tiles that would be reachable after a given slide and insert from a given starting
     * position (on the updated board) and their corresponding positions (on the updated board)
     */
    @Override
    public Map<Tile, Position> findReachableTilePositionsAfterSlideAndInsert(Direction direction, int index, int rotations, Position position) {
        this.slideAndInsert(direction, index, rotations);
        Set<Position> reachablePositions = this.getReachablePositions(position);

        Map<Tile, Position> reachableTilesByPosition = new HashMap<>();
        for (Position reachablePosition: reachablePositions) {
            reachableTilesByPosition.put(this.getTileAt(reachablePosition), reachablePosition);
        }
        this.slideAndInsert(Direction.opposite(direction), index, 0);
        this.getSpareTile().rotate(4 - rotations);
        return reachableTilesByPosition;
    }

    /**
     * Gets the position of the first Tile (in row-column order) that matches the given predicate
     */
    @Override
    public Optional<Position> getFirstTileMatching(Predicate<Tile> searchFunction) {
        for (int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for (int colIndex = 0; colIndex < this.getWidth(); colIndex++) {
                Position tilePosition = new Position(rowIndex, colIndex);
                if (searchFunction.test(this.getTileAt(tilePosition))) {
                    return Optional.of(tilePosition);
                }
            }
        }
        return Optional.empty();
    }
}
