package game.model;

import game.TestUtils;
import game.exceptions.IllegalGameActionException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static game.model.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

public class StandardBoardTest {

    @Test
    public void testCreatingBoardLargerThan7by7() {
        Tile[][] grid = new Tile[8][8];
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int colIndex = 0; colIndex < 8; colIndex++) {
                grid[rowIndex][colIndex] = new Tile(true, true, true, true,
                        new Treasure(Gem.ammolite, Gem.mexican_opal));
            }
        }

        assertThrows(IllegalArgumentException.class, () -> new StandardBoard(grid,
                new Tile(true, true, true, true, new Treasure(Gem.ammolite, Gem.mexican_opal))));
    }

    @Test
    public void testCreatingBoardSmallerThan7by7() {
        Tile[][] grid = new Tile[5][5];
        for (int rowIndex = 0; rowIndex < 5; rowIndex++) {
            for (int colIndex = 0; colIndex < 5; colIndex++) {
                grid[rowIndex][colIndex] = new Tile(true, true, true, true,
                        new Treasure(Gem.ammolite, Gem.mexican_opal));
            }
        }

        assertThrows(IllegalArgumentException.class, () -> new StandardBoard(grid,
                new Tile(true, true, true, true, new Treasure(Gem.ammolite, Gem.mexican_opal))));
    }

    @Test
    public void testGetTileAtValidPosition() {
        StandardBoard board = TestUtils.createUniformBoard(true, true, true, true);

        assertTrue(board.getTileAt(new Position(0, 0)).connects(UP));
        assertTrue(board.getTileAt(new Position(0, 0)).connects(DOWN));
        assertTrue(board.getTileAt(new Position(0, 0)).connects(LEFT));
        assertTrue(board.getTileAt(new Position(0, 0)).connects(RIGHT));
    }

    @Test
    public void testGetTileAtInvalidPosition() {
        StandardBoard board = TestUtils.createUniformBoard(true, true, true, true);

        assertThrows(IllegalArgumentException.class, () -> board.getTileAt(new Position(7,7)));
        assertThrows(IllegalArgumentException.class, () -> board.getTileAt(new Position(2,15)));
        assertThrows(IllegalArgumentException.class, () -> board.getTileAt(new Position(15,3)));
    }
    
    @Test
    public void testGetReachablePositions() {
        StandardBoard horizontalBoard = TestUtils.createUniformBoard(false, false, true, true);
        Set<Position> horizontalReachableFromTopLeft 
                = horizontalBoard.getReachablePositions(new Position(0, 0));
        
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,0)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,1)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,2)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,3)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,4)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,5)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,6)));
        
        StandardBoard pathlessBoard = TestUtils.createUniformBoard(false, false, false, false);
        Set<Position> pathlessReachable = pathlessBoard.getReachablePositions(new Position(3,3));
        assertEquals(1, pathlessReachable.size());
        
        StandardBoard latticeBoard = TestUtils.createUniformBoard(true, true, true, true);
        Set<Position> allReachable = latticeBoard.getReachablePositions(new Position(0,0));
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                assertTrue(allReachable.contains(new Position(row, col)));
            }
        }
    }

    @Test
    public void testInsertRotation() {
        StandardBoard board = TestUtils.createUniformBoard(false, false, true, true);
        assertTrue(board.isValidSlideAndInsert(RIGHT, 0,0));
        board.slideAndInsert(RIGHT, 0, 0);
        Tile insertedTile = board.getTileAt(new Position(0,0));
        assertTrue(insertedTile.connects(LEFT));
        assertTrue(insertedTile.connects(RIGHT));
        assertFalse(insertedTile.connects(UP));
        assertFalse(insertedTile.connects(DOWN));

        assertTrue(board.isValidSlideAndInsert(RIGHT, 0,1));
        board.slideAndInsert(RIGHT, 0, 1);
        Tile insertedTile2 = board.getTileAt(new Position(0,0));
        assertFalse(insertedTile2.connects(LEFT));
        assertFalse(insertedTile2.connects(RIGHT));
        assertTrue(insertedTile2.connects(UP));
        assertTrue(insertedTile2.connects(DOWN));
    }

    @Test
    public void testSlideRowRight() {
        StandardBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.isValidSlideAndInsert(RIGHT, 0,0));
        board.slideAndInsert(RIGHT, 0, 0);
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex3 = board.getTileAt(new Position(0, 3));
        Tile tileAtIndex4 = board.getTileAt(new Position(0, 4));

        assertTrue(tileAtIndex1.connects(UP));
        assertTrue(tileAtIndex2.connects(DOWN));
        assertTrue(tileAtIndex3.connects(LEFT));
        assertTrue(tileAtIndex4.connects(RIGHT));
    }

    @Test
    public void testSlideRowLeft() {
        StandardBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.isValidSlideAndInsert(LEFT, 0,0));
        board.slideAndInsert(LEFT, 0, 0);
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
    }

    @Test
    public void testSlideColumnDown() {
        StandardBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.isValidSlideAndInsert(DOWN, 0,0));
        board.slideAndInsert(DOWN, 0, 0);

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
    }

    @Test
    public void testSlideColumnUp() {
        StandardBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.isValidSlideAndInsert(UP, 0,0));
        board.slideAndInsert(UP, 0, 0);
        assertTrue(board.isValidSlideAndInsert(UP, 0,0));
        board.slideAndInsert(UP, 0, 0);

        Tile tileAtIndex00 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex50 = board.getTileAt(new Position(5, 0));
        Tile tileAtIndex60 = board.getTileAt(new Position(6, 0));

        assertFalse(tileAtIndex00.connects(UP));
        assertFalse(tileAtIndex00.connects(DOWN));
        assertFalse(tileAtIndex00.connects(LEFT));
        assertFalse(tileAtIndex00.connects(RIGHT));

        assertFalse(tileAtIndex50.connects(UP));
        assertFalse(tileAtIndex50.connects(DOWN));
        assertTrue(tileAtIndex50.connects(LEFT));
        assertTrue(tileAtIndex50.connects(RIGHT));

        assertTrue(tileAtIndex60.connects(UP));
        assertFalse(tileAtIndex60.connects(DOWN));
        assertFalse(tileAtIndex60.connects(LEFT));
        assertFalse(tileAtIndex60.connects(RIGHT));

        assertTrue(board.isValidSlideAndInsert(UP, 2,0));
        board.slideAndInsert(UP, 2, 3);

        Tile tileAtIndex02 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex62 = board.getTileAt(new Position(6, 2));

        assertFalse(tileAtIndex02.connects(UP));
        assertFalse(tileAtIndex02.connects(DOWN));
        assertFalse(tileAtIndex02.connects(LEFT));
        assertFalse(tileAtIndex02.connects(RIGHT));

        assertFalse(tileAtIndex62.connects(UP));
        assertFalse(tileAtIndex62.connects(DOWN));
        assertFalse(tileAtIndex62.connects(LEFT));
        assertFalse(tileAtIndex62.connects(RIGHT));
    }

    @Test
    public void testInvalidSlideAndInsert() {
        StandardBoard board = TestUtils.createNonUniformBoard();
        assertFalse(board.isValidSlideAndInsert(UP, -1,0));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(UP, -1,0));
        assertFalse(board.isValidSlideAndInsert(RIGHT, -10,0));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(RIGHT, -10,0));
        assertFalse(board.isValidSlideAndInsert(LEFT, 0,-1));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(LEFT, 0,-1));
        assertFalse(board.isValidSlideAndInsert(UP, 1,0));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(UP, 1,0));
        assertFalse(board.isValidSlideAndInsert(DOWN, 1,12));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(DOWN, 1,12));
    }
}
