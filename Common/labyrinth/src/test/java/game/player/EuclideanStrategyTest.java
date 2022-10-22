package game.player;

import game.TestUtils;
import game.model.*;
import game.view.ExperimentationBoardView;
import game.view.PlayerViewSelf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import player.EuclideanStrategy;
import player.ReimannStrategy;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EuclideanStrategyTest {
    private StandardBoard uniformBoard;
    private ExperimentationBoard experimentationUniformBoard;
    private ExperimentationBoardView experimentationUniformBoardView;
    private ReimannStrategy reimannStrategy;
    private Player player;
    private PlayerViewSelf playerViewSelf;

    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.experimentationUniformBoard = new StandardExperimentationBoard(this.uniformBoard);
        this.experimentationUniformBoardView = new ExperimentationBoardView(this.experimentationUniformBoard);
        this.reimannStrategy = new ReimannStrategy();
        this.player = new Player("Learner", Color.BLUE, TestUtils.makeTreasure(Gem.apatite, Gem.labradorite),
                new Position(0, 0));
        this.playerViewSelf = new PlayerViewSelf(player);
    }

    @Test
    public void testGettingCandidatesInEuclideanOrder() {
        setup();
        TestEuclideanStrategy strategy = new TestEuclideanStrategy();

        List<Tile> candidatesInOrder = strategy.getCandidatesInOrderExposed();
        assertSame(experimentationUniformBoardView.getTileAt(new Position(3, 3)), candidatesInOrder.get(0));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(2, 3)), candidatesInOrder.get(1));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(3, 2)), candidatesInOrder.get(2));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(3, 4)), candidatesInOrder.get(3));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(4, 3)), candidatesInOrder.get(4));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(2, 2)), candidatesInOrder.get(5));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(2, 4)), candidatesInOrder.get(6));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(4, 2)), candidatesInOrder.get(7));
        assertSame(experimentationUniformBoardView.getTileAt(new Position(4, 4)), candidatesInOrder.get(8));

        for (int candIndex = 1; candIndex < candidatesInOrder.size(); candIndex++) {
            final int prevIndex = candIndex - 1;
            final int curIndex = candIndex;
            Position previousCandidate = experimentationUniformBoard.getFirstTileMatching(
                    (Tile tile) -> tile.equals(candidatesInOrder.get(prevIndex))).get();
            Position currentCandidate = experimentationUniformBoard.getFirstTileMatching(
                    (Tile tile) -> tile.equals(candidatesInOrder.get(curIndex))).get();
            double distance1 = this.getEuclideanDistance(previousCandidate, new Position(3, 3));
            double distance2 = this.getEuclideanDistance(currentCandidate, new Position(3, 3));
            if (distance1 == distance2) {
                assertTrue(new Position.RowColumnOrderComparator().compare(previousCandidate, currentCandidate) <= 0);
            }
            else {
                assertTrue(distance1 < distance2);
            }
        }
    }

    private double getEuclideanDistance(Position position1, Position position2) {
        return Math.sqrt(Math.pow(position1.getRow() - position2.getRow(), 2)
                + Math.pow(position1.getColumn() - position2.getColumn(), 2));
    }

    private class TestEuclideanStrategy extends EuclideanStrategy {
        public List<Tile> getCandidatesInOrderExposed() {
            return this.getCandidatesInOrder(experimentationUniformBoardView,
                    experimentationUniformBoardView.getSpareTile(), playerViewSelf);
        }
    }
}
