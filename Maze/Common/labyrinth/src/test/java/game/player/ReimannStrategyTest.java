package game.player;

import game.model.*;
import game.TestUtils;
import game.view.ExperimentationBoardView;
import game.view.PlayerViewSelf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import player.ReimannStrategy;
import player.TurnPlan;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ReimannStrategyTest {

    private StandardBoard uniformBoard;
    private ExperimentationBoard experimentationUniformBoard;
    private ExperimentationBoardView experimentationUniformBoardView;
    private ReimannStrategy reimannStrategy;
    private Player player;
    private PlayerViewSelf playerViewSelf;

    @BeforeEach
    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.experimentationUniformBoard = new StandardExperimentationBoard(this.uniformBoard);
        this.experimentationUniformBoardView = new ExperimentationBoardView(this.experimentationUniformBoard);
        this.reimannStrategy = new ReimannStrategy();
        this.player = new Player("Learner", Color.BLUE, TestUtils.makeTreasure(Gem.apatite, Gem.labradorite),
                new Position(3, 4));
        this.playerViewSelf = new PlayerViewSelf(player);
    }

    @Test
    public void testGettingCandidatesInRowColumnOrder() {
//        setup();
        class AnonymousReimannStrategy extends ReimannStrategy {
            public void testCandidateOrder() {
                List<Tile> candidatesInOrder = this.getCandidatesInOrder(experimentationUniformBoardView,
                        experimentationUniformBoardView.getSpareTile(), playerViewSelf);

                assertEquals(playerViewSelf.getGoalTreasure(), candidatesInOrder.get(0).getTreasure());
                int i = 1;
                for (int row = 0; row < experimentationUniformBoardView.getHeight(); row++) {
                    for (int col = 0; col < experimentationUniformBoardView.getWidth(); col++) {
                        if (row == 3 & col == 3) {
                            continue;
                        }
                        assertSame(candidatesInOrder.get(i), experimentationUniformBoardView.getTileAt(new Position(row, col)));
                        i++;
                    }
                }
            }
        }
        new AnonymousReimannStrategy().testCandidateOrder();
    }

    @Test
    public void testReimannGetTurnPlanSkip() {
//        setup();
        StandardBoard emptyBoard = TestUtils.createUniformBoard(false, false, false, false);
        ExperimentationBoard experimentationeEmptyBoard = new StandardExperimentationBoard(emptyBoard);
        ExperimentationBoardView experimentationEmptyBoardView = new ExperimentationBoardView(experimentationeEmptyBoard);
        Player playerSkip = new Player("Learner", Color.BLUE, TestUtils.makeTreasure(Gem.apatite, Gem.labradorite),
                new Position(0, 0));
        PlayerViewSelf playerSkipView = new PlayerViewSelf(playerSkip);
        Optional<TurnPlan> turnPlan = reimannStrategy.createTurnPlan(experimentationEmptyBoardView, playerSkipView);
        assertTrue(turnPlan.isEmpty());
    }

    @Test
    public void testReimannGetTurnPlanRow() {
//        setup();
        Optional<TurnPlan> turnPlan = reimannStrategy.createTurnPlan(experimentationUniformBoardView, this.playerViewSelf);
        assertTrue(turnPlan.isPresent());
        assertEquals(Direction.LEFT, turnPlan.get().getSlideDirection());
        assertEquals(0, turnPlan.get().getSlideIndex());
        assertEquals(3, turnPlan.get().getSpareTileRotations());
        assertEquals(new Position(3, 3), turnPlan.get().getMoveDestination());
    }

    @Test
    public void testReimannGetTurnPlanColMustShift() {
        setup();
        StandardBoard board = TestUtils.createUniformBoard(true, true, false, false);
        StandardExperimentationBoard experimentationBoard = new StandardExperimentationBoard(board);
        ExperimentationBoardView experimentationBoardView = new ExperimentationBoardView(experimentationBoard);

        // Gem position at row6, column2
        Player colPlayer = new Player("Buddy", Color.RED,
                TestUtils.makeTreasure(Gem.orange_radiant, Gem.garnet), new Position(0, 3));
        PlayerViewSelf colPlayerView = new PlayerViewSelf(colPlayer);

        Optional<TurnPlan> turnPlan = reimannStrategy.createTurnPlan(experimentationBoardView, colPlayerView);

        assertTrue(turnPlan.isPresent());
        assertEquals(Direction.RIGHT, turnPlan.get().getSlideDirection());
        assertEquals(0, turnPlan.get().getSlideIndex());
    }
}


