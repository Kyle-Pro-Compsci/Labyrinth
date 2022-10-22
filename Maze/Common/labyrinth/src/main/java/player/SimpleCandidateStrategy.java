package player;

import game.model.Direction;
import game.model.Position;
import game.model.Tile;
import game.model.Treasure;
import game.view.ExperimentationBoardView;
import game.view.PlayerViewSelf;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static game.model.Direction.*;

/**
 * A category of strategies which search every possible move to find a way to reach a candidate goal,
 * iterating through candidate goals until a reachable goal is found.
 */
public abstract class SimpleCandidateStrategy implements Strategy {

    /**
     * Gets all candidates to be considered in the desired order.
     */
    abstract protected List<Tile> getCandidatesInOrder(ExperimentationBoardView board, Tile spareTile, PlayerViewSelf playerInformation);

    /**
     * Given the current board, spare tile, and player information, produces a plan for the turn which includes all
     * the actions the player wishes to take.
     * Returns Optional.empty() if the player wishes to pass.
     */
    @Override
    public Optional<TurnPlan> createTurnPlan(ExperimentationBoardView board, PlayerViewSelf playerInformation) {
        for (Tile candidate : this.getCandidatesInOrder(board, board.getSpareTile(), playerInformation)) {
            Optional<TurnPlan> planForCandidate = createTurnPlanForCandidate(playerInformation.getAvatarPosition(), board, candidate);
            if (planForCandidate.isPresent()) {
                return planForCandidate;
            }
        }
        return Optional.empty();
    }
    protected boolean spareTileIsGoal(Tile spareTile, Treasure goalTreasure) {
        return spareTile.getTreasure().equals(goalTreasure);
    }

    protected Tile findGoalTile(ExperimentationBoardView board, Treasure goalTreasure) {
        Predicate<Tile> matchesGoalPredicate = (Tile tile) -> tile.getTreasure().equals(goalTreasure);
        Optional<Position> goalTile = board.findFirstTileMatching(matchesGoalPredicate);
        if (goalTile.isEmpty()) {
            if (this.spareTileIsGoal(board.getSpareTile(), goalTreasure)) {
                return board.getSpareTile();
            }
            throw new IllegalStateException("Board does not contain goalTreasure");
        }
        return board.getTileAt(goalTile.get());
    }

    /**
     * Returns a TurnPlan which will result in reaching the given candidate Tile,
     * or returns Optional.empty() if it cannot find such a plan.
     */
    private Optional<TurnPlan> createTurnPlanForCandidate(Position currentPosition,
                                                          ExperimentationBoardView board, Tile candidate) {
        if (board.getTileAt(currentPosition).equals(candidate)) {
            return Optional.empty();
        }

        int boardHeight = board.getHeight();
        int boardWidth = board.getWidth();

        for (int rowIndex = 0; rowIndex < boardHeight; rowIndex += 2) {
            for (int rotations = 3; rotations >= 0; rotations--) {
                for (Direction direction : new Direction[]{LEFT, RIGHT}) {
                    Position avatarAfterSliding = getAvatarPositionAfterSliding(currentPosition,
                            boardWidth, boardHeight, direction, rowIndex);

                    Map<Tile, Position> reachableTilePositionsAfterSlide =
                            board.findReachableTilePositionsAfterSlideAndInsert(direction, rowIndex, rotations, avatarAfterSliding);
                    if (reachableTilePositionsAfterSlide.containsKey(candidate)
                    && reachableTilePositionsAfterSlide.get(candidate) != avatarAfterSliding) {
                        return Optional.of(new TurnPlan(direction, rowIndex, rotations, reachableTilePositionsAfterSlide.get(candidate)));
                    }
                }
            }
        }
        for (int colIndex = 0; colIndex < boardWidth; colIndex += 2) { //TODO: Consider how to split into helpers
            for (int rotations = 3; rotations >= 0; rotations--) {
                for (Direction direction : new Direction[]{UP, DOWN}) {
                    Position avatarAfterSliding = getAvatarPositionAfterSliding(currentPosition,
                            boardWidth, boardHeight, direction, colIndex);

                    Map<Tile, Position> reachableTilePositionsAfterSlide =
                            board.findReachableTilePositionsAfterSlideAndInsert(direction, colIndex, rotations, avatarAfterSliding);
                    if (reachableTilePositionsAfterSlide.containsKey(candidate)
                    && reachableTilePositionsAfterSlide.get(candidate) != avatarAfterSliding) {
                        return Optional.of(new TurnPlan(direction, colIndex, rotations, reachableTilePositionsAfterSlide.get(candidate)));
                    }
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Gets the new position of an avatar after a slide was performed.
     */
    private Position getAvatarPositionAfterSliding(Position originalPosition, int width, int height,
                                                   Direction direction, int index) {
        if ((direction == LEFT || direction == RIGHT) && originalPosition.getRow() == index) {
            if (direction == LEFT) {
                return originalPosition.addDeltaWithBoardWrap(-1, 0, height, width);
            } else {
                return originalPosition.addDeltaWithBoardWrap(1, 0, height, width);
            }
        } else if ((direction == UP || direction == DOWN) && originalPosition.getRow() == index) {
            if (direction == UP) {
                return originalPosition.addDeltaWithBoardWrap(0, 1, height, width);
            } else {
                return originalPosition.addDeltaWithBoardWrap(0, -1, height, width);
            }
        }
        return originalPosition; //TODO: Consider consolidating
    }
}
