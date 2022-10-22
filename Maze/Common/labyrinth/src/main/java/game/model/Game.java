package game.model;

import game.exceptions.IllegalGameActionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static game.model.Direction.*;

/**
 * Represents the game state.
 */
public class Game {
    private final Board board;

    /** Players are stored in this List in terms of turn order. **/
    private final List<Player> playerList;
    /** An index into playerList. **/
    private int activePlayer;
    /** A record of the slide and insert action that was performed last turn. Empty on the first turn. **/
    private Optional<SlideAndInsertRecord> previousSlideAndInsert;

    /**
     * Creates a new State with the given list of Players and board. The turn order is defined by the Player list
     * order, with the first Player in the list starting first.
     */
    public Game(Board board, List<Player> playerList) {
        this.playerList = new ArrayList<>();
        this.playerList.addAll(playerList);
        this.board = board;
        this.activePlayer = 0;
        this.previousSlideAndInsert = Optional.empty();
    }

    /**
     * Creates a new State representing a game in progress with the given list of Players and board, as well as the
     * given active player index and an optional previous slide and insert. The turn order is defined by the Player list
     * order, with the first Player in the list starting first.
     */
    public Game(Board board, List<Player> playerList, int activePlayer, Optional<SlideAndInsertRecord> previousSlideAndInsert) {
        this.playerList = new ArrayList<>();
        this.playerList.addAll(playerList);
        this.board = board;
        this.activePlayer = activePlayer;
        this.previousSlideAndInsert = previousSlideAndInsert;
    }

    /**
     * Slides the row (for left or right slides) or column (for up or down slides) at the given index in the specified
     * direction, then inserts the spare tile after rotating it the given number of times.
     * If a Player is moved off the board, move the Player to the newly inserted tile.
     */
    public void slideAndInsert(Direction direction, int index, int rotations) {
        this.validateSlideDoesNotUndoPrevious(direction, index);
        this.board.slideAndInsert(direction, index, rotations);

        this.slidePlayers(direction, index);
        this.previousSlideAndInsert = Optional.of(new SlideAndInsertRecord(direction, index, rotations));
    }

    public boolean activePlayerCanReachPosition(Position positionToReach) {
        Set<Position> reachableTiles = this.board.getReachablePositions(
                this.getActivePlayer().getCurrentAvatarPosition());

        return reachableTiles.contains(positionToReach);
    }

    public boolean activePlayerHasReachedGoal() {
        Treasure playerTreasure = this.getActivePlayer().getGoalTreasure();
        Tile tileContainingPlayer = this.board.getTileAt(this.getActivePlayer().getCurrentAvatarPosition());

        return playerTreasure.equals(tileContainingPlayer.getTreasure());
    }

    /**
     * Kicks the currently active Player by removing it from the list of Players.
     * The next Player in the list will become the active Player as its index has been reduced by 1.
     * If the kicked Player was the last Player in the list then the active Player is set to the first Player
     * in the list.
     */
    public void kickActivePlayer() {
        if (this.playerList.size() == 0) {
            throw new IllegalGameActionException("Tried to kick the active player when there were no" +
                    "players in the game.");
        }
        this.playerList.remove(this.activePlayer);
        if (this.activePlayer == this.playerList.size()) {
            this.activePlayer = 0;
        }
    }

    /**
     * Moves the game to the next turn by updating the active Player.
     */
    public void nextTurn() {
        if (this.activePlayer < 0 || this.activePlayer >= this.playerList.size()) {
            throw new IllegalStateException(String.format("The active Player index %d is " +
                    "outside the bounds of the player List.", this.activePlayer));
        }
        if (this.activePlayer == this.playerList.size() - 1) {
            this.activePlayer = 0;
        }
        else {
            this.activePlayer++;
        }
    }

    public Player getActivePlayer() {
        return this.playerList.get(this.activePlayer);
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    private void validateSlideDoesNotUndoPrevious(Direction direction, int index) {
        if (this.previousSlideAndInsert.isPresent()) {
            if (this.previousSlideAndInsert.get().getDirection() == Direction.opposite(direction)
                    && this.previousSlideAndInsert.get().getIndex() == index) {
                throw new IllegalGameActionException("Attempted to perform a slide which undoes the previous slide.");
            }
        }
    }

    /**
     * Update the positions of each of the players who were on a tile that was moved during a Slide.
     */
    private void slidePlayers(Direction direction, int index) {
        int boardHeight = this.board.getHeight();
        int boardWidth = this.board.getWidth();

        for (Player player : this.playerList) {

            Position playerPosition = player.getCurrentAvatarPosition();

            if (direction == UP && playerPosition.getColumn() == index) {
                player.setCurrentAvatarPosition(playerPosition.addDeltaWithBoardWrap(-1, 0, boardHeight, boardWidth));
            }
            else if (direction == DOWN && playerPosition.getColumn() == index) {
                player.setCurrentAvatarPosition(playerPosition.addDeltaWithBoardWrap(1, 0, boardHeight, boardWidth));
            }
            else if (direction == LEFT && playerPosition.getRow() == index) {
                player.setCurrentAvatarPosition(playerPosition.addDeltaWithBoardWrap(0, -1, boardHeight, boardWidth));
            }
            else if (direction == RIGHT && playerPosition.getRow() == index) {
                player.setCurrentAvatarPosition(playerPosition.addDeltaWithBoardWrap(0, 1, boardHeight, boardWidth));
            }
        }
    }
}
