package game.it;

import game.model.Game;

/**
 * The entry point for all test harnesses. Takes in an argument for which harness to run and executes it.
 */
public class TestRunner {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Tried to run TestRunner without passing in exactly one set of tests.");
        }

        switch (args[0]) {
            case "board":
                StandardBoardIntegrationTest.executeTest();
                break;
            case "game":
                GameIntegrationTest.executeTest();
                break;
            default:
                throw new IllegalArgumentException("test set not recognized");
        }
    }
}
