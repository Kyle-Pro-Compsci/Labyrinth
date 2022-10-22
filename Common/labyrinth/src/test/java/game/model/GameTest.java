package game.model;

import game.TestUtils;
import game.exceptions.IllegalGameActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.model.Direction.*;
import static game.model.Direction.RIGHT;

public class GameTest {
    private Board uniformBoard;
    private Board nonUniformBoard;
    private Game game1;
    private Game game2;
    private Player player1;
    private Player player2;
    private Player player3;
    private List<Player> players;

    @BeforeEach
    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.players = new ArrayList<>();
        this.player1 = new Player("Luna", Color.blue, this.uniformBoard.getTileAt(new Position(6, 6)).getTreasure(), new Position(0, 0));
        this.player2 = new Player("Mocha", Color.magenta, this.uniformBoard.getTileAt(new Position(5, 6)).getTreasure(), new Position(0, 1));
        this.player3 = new Player("Winston", Color.lightGray, this.uniformBoard.getTileAt(new Position(4, 6)).getTreasure(), new Position(0, 2));
        players.add(player1);
        players.add(player2);
        players.add(player3);
        this.game1 = new Game(this.uniformBoard, this.players);

        this.nonUniformBoard = TestUtils.createNonUniformBoard();
        this.game2 = new Game(this.nonUniformBoard, this.players);
    }

    @Test
    public void testPlayersStartOnHomeTiles() {
        assertEquals(new Position(0, 0), this.player1.getCurrentAvatarPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentAvatarPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentAvatarPosition());
    }

    @Test
    public void testGetActivePlayer() {
        assertEquals(player1, this.game1.getActivePlayer());
        assertNotEquals(player2, this.game1.getActivePlayer());
        assertNotEquals(player3, this.game1.getActivePlayer());
    }

    @Test
    public void testGetPlayerList() {
        List<Player> playerList = this.game1.getPlayerList();
        assertEquals(3, playerList.size());
        assertEquals(player1, playerList.get(0));
        assertEquals(player2, playerList.get(1));
        assertEquals(player3, playerList.get(2));
    }

    @Test
    public void testInsertRotation() {
        this.game1.slideAndInsert(RIGHT, 0, 0);
        Tile insertedTile = this.game1.getBoard().getTileAt(new Position(0,0));
        assertTrue(insertedTile.connects(LEFT));
        assertTrue(insertedTile.connects(RIGHT));
        assertFalse(insertedTile.connects(UP));
        assertFalse(insertedTile.connects(DOWN));

        this.game1.slideAndInsert(RIGHT, 0, 1);
        Tile insertedTile2 = this.game1.getBoard().getTileAt(new Position(0,0));
        assertFalse(insertedTile2.connects(LEFT));
        assertFalse(insertedTile2.connects(RIGHT));
        assertTrue(insertedTile2.connects(UP));
        assertTrue(insertedTile2.connects(DOWN));
    }

    @Test
    public void testSlideRowRight() {
        this.game2.slideAndInsert(RIGHT, 0, 0);
        Board board = this.game2.getBoard();
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex3 = board.getTileAt(new Position(0, 3));
        Tile tileAtIndex4 = board.getTileAt(new Position(0, 4));

        assertTrue(tileAtIndex1.connects(UP));
        assertTrue(tileAtIndex2.connects(DOWN));
        assertTrue(tileAtIndex3.connects(LEFT));
        assertTrue(tileAtIndex4.connects(RIGHT));

        assertEquals(new Position(0, 1), this.player1.getCurrentAvatarPosition());
        assertEquals(new Position(0, 2), this.player2.getCurrentAvatarPosition());
        assertEquals(new Position(0, 3), this.player3.getCurrentAvatarPosition());
    }

    @Test
    public void testSlideRowLeft() {
        this.game2.slideAndInsert(LEFT, 0, 0);
        Board board = this.game2.getBoard();
        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex6 = board.getTileAt(new Position(0, 6));

        assertTrue(tileAtIndex0.connects(DOWN));
        assertTrue(tileAtIndex1.connects(LEFT));
        assertTrue(tileAtIndex2.connects(RIGHT));

        assertFalse(tileAtIndex6.connects(UP));
        assertFalse(tileAtIndex6.connects(DOWN));
        assertTrue(tileAtIndex6.connects(LEFT));
        assertTrue(tileAtIndex6.connects(RIGHT));

        assertEquals(new Position(0, 6), this.player1.getCurrentAvatarPosition());
        assertEquals(new Position(0, 0), this.player2.getCurrentAvatarPosition());
        assertEquals(new Position(0, 1), this.player3.getCurrentAvatarPosition());
    }

    @Test
    public void testSlideColumnDown() {
        this.game2.slideAndInsert(DOWN, 0, 0);
        Board board = this.game2.getBoard();

        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(1, 0));

        assertFalse(tileAtIndex0.connects(UP));
        assertFalse(tileAtIndex0.connects(DOWN));
        assertTrue(tileAtIndex0.connects(LEFT));
        assertTrue(tileAtIndex0.connects(RIGHT));

        assertTrue(tileAtIndex1.connects(UP));
        assertFalse(tileAtIndex1.connects(DOWN));
        assertFalse(tileAtIndex1.connects(LEFT));
        assertFalse(tileAtIndex1.connects(RIGHT));

        assertEquals(new Position(1, 0), this.player1.getCurrentAvatarPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentAvatarPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentAvatarPosition());
    }

    @Test
    public void testSlideColumnUp() {
        this.game2.slideAndInsert(UP, 0, 0);
        this.game2.slideAndInsert(UP, 0, 0);
        Board board = this.game2.getBoard();

        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex5 = board.getTileAt(new Position(5, 0));
        Tile tileAtIndex6 = board.getTileAt(new Position(6, 0));

        assertFalse(tileAtIndex0.connects(UP));
        assertFalse(tileAtIndex0.connects(DOWN));
        assertFalse(tileAtIndex0.connects(LEFT));
        assertFalse(tileAtIndex0.connects(RIGHT));

        assertFalse(tileAtIndex5.connects(UP));
        assertFalse(tileAtIndex5.connects(DOWN));
        assertTrue(tileAtIndex5.connects(LEFT));
        assertTrue(tileAtIndex5.connects(RIGHT));

        assertTrue(tileAtIndex6.connects(UP));
        assertFalse(tileAtIndex6.connects(DOWN));
        assertFalse(tileAtIndex6.connects(LEFT));
        assertFalse(tileAtIndex6.connects(RIGHT));

        assertEquals(new Position(5, 0), this.player1.getCurrentAvatarPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentAvatarPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentAvatarPosition());
    }

    @Test
    public void testSlideReversesPreviousSlideFails() {
        this.game2.slideAndInsert(UP, 0, 0);
        assertThrows(IllegalGameActionException.class, () -> this.game2.slideAndInsert(DOWN, 0, 0));
    }

    @Test
    public void testActivePlayerCanReachUnreachablePosition() {
        assertFalse(this.game1.activePlayerCanReachPosition(new Position(1, 0)));
        assertFalse(this.game1.activePlayerCanReachPosition(new Position(4, 5)));
        assertFalse(this.game1.activePlayerCanReachPosition(new Position(6, 6)));
        assertFalse(this.game2.activePlayerCanReachPosition(new Position(0, 3)));
        assertFalse(this.game2.activePlayerCanReachPosition(new Position(2, 3)));
    }

    @Test
    public void testActivePlayerCanReachReachablePosition() {
        assertTrue(this.game1.activePlayerCanReachPosition(new Position(0, 0)));
        assertTrue(this.game1.activePlayerCanReachPosition(new Position(0, 1)));
        assertTrue(this.game1.activePlayerCanReachPosition(new Position(0, 3)));
        assertTrue(this.game2.activePlayerCanReachPosition(new Position(0, 0)));
    }

    @Test
    public void testNextTurnWithoutWrapping() {
        assertEquals(player1, this.game1.getActivePlayer());
        this.game1.nextTurn();
        assertEquals(player2, this.game1.getActivePlayer());
        this.game1.nextTurn();
        assertEquals(player3, this.game1.getActivePlayer());
    }

    @Test
    public void testNextTurnWithWrapping() {
        this.game1.nextTurn();
        this.game1.nextTurn();
        this.game1.nextTurn();
        assertEquals(player1, this.game1.getActivePlayer());
    }

    @Test
    public void testKickActivePlayerFromTopOfList() {
        this.game1.kickActivePlayer();
        assertEquals(player2, this.game1.getActivePlayer());
        assertEquals(2, this.game1.getPlayerList().size());
        this.game1.kickActivePlayer();
        assertEquals(player3, this.game1.getActivePlayer());
        assertEquals(1, this.game1.getPlayerList().size());
    }

    @Test
    public void testKickActivePlayerFromBottomOfList() {
        this.game1.nextTurn();
        this.game1.nextTurn();
        this.game1.kickActivePlayer();
        assertEquals(player1, this.game1.getActivePlayer());
        assertEquals(2, this.game1.getPlayerList().size());
    }

    @Test
    public void testPlayerHasReachedGoal() {
        Board testBoard = TestUtils.createUniformBoard(true, true, true, true);
        List<Player> testPlayers = new ArrayList<>();
        Player testPlayer = new Player("Tampopo", Color.green, testBoard.getTileAt(new Position(0, 0)).getTreasure(),
                new Position(0, 0));
        testPlayers.add(testPlayer);
        Game testGame = new Game(testBoard, testPlayers);

        assertTrue(testGame.activePlayerHasReachedGoal());
    }

    @Test
    public void testPlayerHasNotReachedGoal() {
        Board testBoard = TestUtils.createUniformBoard(true, true, true, true);
        List<Player> testPlayers = new ArrayList<>();
        Player testPlayer1 = new Player("Otter", Color.red, testBoard.getTileAt(new Position(0, 0)).getTreasure(),
                new Position(1, 1));
        Player testPlayer2 = new Player("Apollo", Color.black, testBoard.getTileAt(new Position(3, 3)).getTreasure(),
                new Position(3, 4));
        Player testPlayer3 = new Player("Lyka", Color.white, testBoard.getTileAt(new Position(4, 4)).getTreasure(),
                new Position(5, 4));

        testPlayers.add(testPlayer1);
        testPlayers.add(testPlayer2);
        testPlayers.add(testPlayer3);
        Game testGame = new Game(testBoard, testPlayers);

        assertFalse(testGame.activePlayerHasReachedGoal());
        testGame.nextTurn();
        assertFalse(testGame.activePlayerHasReachedGoal());
        testGame.nextTurn();
        assertFalse(testGame.activePlayerHasReachedGoal());
    }
}
