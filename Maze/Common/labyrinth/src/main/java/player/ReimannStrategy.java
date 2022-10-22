package player;

import game.model.Position;
import game.model.Tile;
import game.view.ExperimentationBoardView;
import game.view.PlayerViewSelf;

import java.util.ArrayList;
import java.util.List;

/**
 * A SimpleCandidateStrategy that evaluates candidates in row-column order.
 */
public class ReimannStrategy extends SimpleCandidateStrategy {

    /**
     * Gets all candidates to be considered in row-column order, but always starting with the goal Tile.
     */
    @Override
    protected List<Tile> getCandidatesInOrder(ExperimentationBoardView board, Tile spareTile, PlayerViewSelf playerInformation) {
        List<Tile> candidates = new ArrayList<>();
        Tile goalTile = this.findGoalTile(board, playerInformation.getGoalTreasure());
        candidates.add(goalTile);
        for (int rowIndex = 0; rowIndex < board.getHeight(); rowIndex++) {
            for (int colIndex = 0; colIndex < board.getWidth(); colIndex++) {
                Tile tileAtPosition = board.getTileAt(new Position(rowIndex, colIndex));
                if (!tileAtPosition.equals(goalTile)) {
                    candidates.add(tileAtPosition);
                }
            }
        }
        return candidates;
    }
}
