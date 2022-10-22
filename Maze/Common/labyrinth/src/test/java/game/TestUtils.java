package game;

import game.model.*;
import static game.model.Direction.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtils {

    public static Gem[] rowGems = {Gem.alexandrite, Gem.mexican_opal, Gem.aplite, Gem.apatite, Gem.hackmanite, Gem.beryl, Gem.orange_radiant};
    public static Gem[] colGems = {Gem.heliotrope, Gem.hematite, Gem.garnet, Gem.labradorite, Gem.lapis_lazuli, Gem.magnesite, Gem.moonstone};

    public static StandardBoard createUniformBoard(boolean up, boolean down, boolean left, boolean right) {
        Tile[][] grid = new Tile[7][7];
        for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
            for (int colIndex = 0; colIndex < 7; colIndex++) {
                grid[rowIndex][colIndex] = makeTile(up, down, left, right,
                        makeTreasure(rowGems[rowIndex], colGems[colIndex]));
            }
        }
        StandardBoard board = new StandardBoard(grid,
                makeTile(up, down, left, right, makeTreasure(Gem.ammolite, Gem.mexican_opal)));
        return board;
    }

    public static StandardBoard createUniformBoard(boolean up, boolean down, boolean left, boolean right, Tile spare) {
        Tile[][] grid = new Tile[7][7];
        for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
            for (int colIndex = 0; colIndex < 7; colIndex++) {
                grid[rowIndex][colIndex] = makeTile(up, down, left, right,
                        makeTreasure(rowGems[rowIndex], colGems[colIndex]));
            }
        }
        return new StandardBoard(grid, spare);
    }

    public static StandardBoard createNonUniformBoard() {

        Tile[][] grid = new Tile[7][7];

        for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
            for (int colIndex = 0; colIndex < 7; colIndex++) {
                grid[rowIndex][colIndex] = makeTile(false, false, false, false,
                        makeTreasure(rowGems[rowIndex], colGems[colIndex]));
            }
        }

        grid[0][0] = makeTile(true, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][1] = makeTile(false, true, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][2] = makeTile(false, false, true, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][3] = makeTile(false, false, false, true,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][4] = makeTile(false, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][5] = makeTile(false, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][6] = makeTile(false, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));

        StandardBoard board = new StandardBoard(grid,
                makeTile(false, false, true, true, makeTreasure(Gem.ammolite, Gem.mexican_opal)));
        return board;
    }

    public static StandardBoard createNonUniformBoardWithSomeUniqueTreasures() {


        Tile[][] grid = new Tile[7][7];
        grid[0][0] = makeTile(true, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][1] = makeTile(false, true, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][2] = makeTile(false, false, true, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][3] = makeTile(false, false, false, true,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][4] = makeTile(false, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][5] = makeTile(false, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));
        grid[0][6] = makeTile(false, false, false, false,
                makeTreasure(Gem.ammolite, Gem.mexican_opal));

        for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
            for (int colIndex = 0; colIndex < 7; colIndex++) {
                grid[rowIndex][colIndex] = makeTile(false, false, false, false,
                        makeTreasure(Gem.ammolite, Gem.mexican_opal));
            }
        }
        StandardBoard board = new StandardBoard(grid,
                makeTile(false, false, true, true, makeTreasure(Gem.ammolite, Gem.mexican_opal)));
        return board;
    }

    public static Treasure makeTreasure(Gem gem1, Gem gem2) {
        List<Gem> gems = new ArrayList<>();
        gems.add(gem1);
        gems.add(gem2);

        return new Treasure(gems);
    }

    public static Tile makeTile(boolean up, boolean down, boolean left, boolean right, Treasure treasure) {
        Set<Direction> pathwayConnections = new HashSet<>();
        if (up) {
            pathwayConnections.add(UP);
        }
        if (down) {
            pathwayConnections.add(DOWN);
        }
        if (left) {
            pathwayConnections.add(LEFT);
        }
        if (right) {
            pathwayConnections.add(RIGHT);
        }
        return new Tile(pathwayConnections, treasure);
    }
}
