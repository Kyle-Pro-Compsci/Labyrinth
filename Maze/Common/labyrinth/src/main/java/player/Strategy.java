package player;

import game.model.Tile;
import game.view.ExperimentationBoardView;
import game.view.PlayerViewSelf;

import java.util.Optional;

/**
 * Classes which implement this interface can create a TurnPlan given the information about the board and the player.
 */
public interface Strategy {
    /**
     * Given the current board, spare tile, and player information, produces a plan for the turn which includes all
     * the actions the player wishes to take.
     * Returns Optional.empty() if the player wishes to pass.
     */
    Optional<TurnPlan> createTurnPlan(ExperimentationBoardView board, PlayerViewSelf playerInformation);
}
