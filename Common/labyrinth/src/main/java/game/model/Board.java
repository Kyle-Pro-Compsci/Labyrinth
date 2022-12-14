package game.model;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents Labyrinth's board, which is a grid of Tiles.
 */
public interface Board {
    Tile getTileAt(Position position);

    Tile getSpareTile();

    int getHeight();

    int getWidth();

    /**
     * Validate that sliding a row or column at the given index in the specified direction and inserting the spare tile
     * does not violate game rules or pass invalid arguments.
     */
    boolean isValidSlideAndInsert(Direction direction, int index, int rotations);

    /**
     * Slides the row (for left or right slides) or column (for up or down slides) at the given index in the specified
     * direction, then inserts the spare tile after rotating it the given number of times.
     *
     * @precondition isValidSlideAndInsert(direction, index, rotations) is called and returns True
     */
    void slideAndInsert(Direction direction, int index, int rotations);

    /**
     * Returns a set of all positions that can be reached via a continuous pathway from the given starting position.
     */
    Set<Position> getReachablePositions(Position startingPosition);
}
