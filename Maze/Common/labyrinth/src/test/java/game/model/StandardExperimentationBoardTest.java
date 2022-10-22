package game.model;

import game.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static game.model.Direction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StandardExperimentationBoardTest {
    private StandardExperimentationBoard exBoard;

    @BeforeEach
    public void setup() {
        StandardBoard standardBoard = TestUtils.createUniformBoard(false, false, true, true,
                TestUtils.makeTile(true, true, false, false,
                        TestUtils.makeTreasure(Gem.ammolite, Gem.labradorite)));
        exBoard = new StandardExperimentationBoard(standardBoard);
    }

    @Test
    public void testFindReachableTilePositionsAfterSlideAndInsert() {
        Map<Tile, Position> reachable1 = exBoard.findReachableTilePositionsAfterSlideAndInsert(DOWN, 2, 0, new Position(0, 0));
        assertEquals(2, reachable1.keySet().size());
        assertTrue(reachable1.containsKey(exBoard.getTileAt(new Position(0, 0))));
        assertEquals(new Position(0, 0), reachable1.get(exBoard.getTileAt(new Position(0, 0))));
        assertTrue(reachable1.containsKey(exBoard.getTileAt(new Position(0, 1))));
        assertEquals(new Position(0, 1), reachable1.get(exBoard.getTileAt(new Position(0, 1))));

        Map<Tile, Position> reachable2 = exBoard.findReachableTilePositionsAfterSlideAndInsert(LEFT, 6, 1, new Position(6, 4));
        assertEquals(7, reachable2.keySet().size());
        for (int i = 1; i < 7; i++) {
            assertTrue(reachable2.containsKey(exBoard.getTileAt(new Position(6, i))));
            assertEquals(new Position(6, i - 1), reachable2.get(exBoard.getTileAt(new Position(6, i))));
        }
        assertTrue(reachable2.containsKey(exBoard.getSpareTile()));
        assertEquals(new Position(6, 6), reachable2.get(exBoard.getSpareTile()));
    }
}
