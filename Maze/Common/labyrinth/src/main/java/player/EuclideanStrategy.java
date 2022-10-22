package player;

import game.model.Position;
import game.model.Tile;
import game.view.ExperimentationBoardView;
import game.view.PlayerViewSelf;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A SimpleCandidateStrategy that evaluates candidates in order of Euclidean distance from the goal tile.
 */
public class EuclideanStrategy extends SimpleCandidateStrategy {

    /**
     * Gets all candidates to be considered in order of Euclidean distance from the goal Tile, breaking ties
     * by row-column ordering, but always starting with the goal Tile.
     * If the goal Tile is the spare Tile, all candidates are ranked in row-column order.
     */
    @Override
    protected List<Tile> getCandidatesInOrder(ExperimentationBoardView board, Tile spareTile, PlayerViewSelf playerInformation) {
        List<Position> candidatePositions = new ArrayList<>();
        Tile goalTile = this.findGoalTile(board, playerInformation.getGoalTreasure());
        for (int rowIndex = 0; rowIndex < board.getHeight(); rowIndex++) {
            for (int colIndex = 0; colIndex < board.getWidth(); colIndex++) {
                Position position = new Position(rowIndex, colIndex);
                if (!board.getTileAt(position).equals(goalTile)) {
                    candidatePositions.add(position);
                }
            }
        }
        if (!goalTile.equals(spareTile)) {
            Optional<Position> goalPosition = board.findFirstTileMatching((Tile tile) -> tile.equals(goalTile));
            if (goalPosition.isEmpty()) {
                throw new IllegalStateException("Goal tile for player is not the spare tile or on board.");
            }
            Comparator<Position> smallerEuclideanDistanceComparator = (Position p1, Position p2) -> {
                double differenceInEuclideanDistance = this.getEuclideanDistance(p1, goalPosition.get())
                        - this.getEuclideanDistance(p2, goalPosition.get());
                if (differenceInEuclideanDistance < 0) {
                    return -1;
                }
                else if (differenceInEuclideanDistance > 0) {
                    return 1;
                }
                else {
                    return new Position.RowColumnOrderComparator().compare(p1, p2);
                }
            };
            candidatePositions.sort(smallerEuclideanDistanceComparator);
        }
        List<Tile> candidates = candidatePositions.stream().map(board::getTileAt).collect(Collectors.toList());
        candidates.add(0, goalTile);
        return candidates;
    }

    private double getEuclideanDistance(Position position1, Position position2) {
        return Math.sqrt(Math.pow(position1.getRow() - position2.getRow(), 2)
                + Math.pow(position1.getColumn() - position2.getColumn(), 2));
    }
}
