package game.view;

import game.model.ExperimentationBoard;
import game.model.Direction;
import game.model.Position;
import game.model.Tile;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A View of ExperimentationBoard that provides access to the necessary methods to preview slide and inserts.
 */
public class ExperimentationBoardView {
    ExperimentationBoard experimentationBoard;

    public ExperimentationBoardView(ExperimentationBoard experimentationBoard) {
        this.experimentationBoard = experimentationBoard;
    }

    public Map<Tile, Position> findReachableTilePositionsAfterSlideAndInsert(
            Direction direction, int index, int rotations, Position position) {
        return experimentationBoard.findReachableTilePositionsAfterSlideAndInsert(direction, index, rotations, position);
    }

    public Optional<Position> findFirstTileMatching(Predicate<Tile> searchFunction) {
        return experimentationBoard.getFirstTileMatching(searchFunction);
    }

    public int getHeight() {
        return experimentationBoard.getHeight();
    }

    public int getWidth() {
        return experimentationBoard.getWidth();
    }

    public Tile getSpareTile() {
        return experimentationBoard.getSpareTile();
    }

    public Tile getTileAt(Position position) {
        return experimentationBoard.getTileAt(position);
    }
}
