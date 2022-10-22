package game.it.processing;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static game.model.Direction.UP;

/**
 * Deserialization methods for Labyrinth models. Created with a parser and each public method reads a specific kind of
 * data from the head of the parser, moving the head as it reads.
 */
public class MazeJsonParser {
    private final int boardWidth;
    private final int boardHeight;
    private final JsonParser parser;
    private final ObjectMapper mapper;

    public MazeJsonParser(InputStream in, int boardHeight, int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        JsonFactory jsonFactory = new JsonFactory();
        this.mapper = new ObjectMapper();
        try {
            this.parser = jsonFactory.createParser(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Game getGame() throws IOException {
        // we use null here to hold the variables right before assigning them, there will never persist a null value
        // as we check if any remain null afterwards
        Tile[][] tileGrid = null;
        Tile spareTile = null;
        List<Player> playerList = null;
        Optional<SlideAndInsertRecord> previousSlideAndInsert = null;

        while (this.parser.nextToken() != JsonToken.END_OBJECT) {
            String field = this.parser.getCurrentName();
            switch (field) {
                case "board":
                    this.readNext();
                    List<List<String>> rawGrid = this.getRawTileGrid();
                    tileGrid = this.getBoardGrid(rawGrid);
                    break;
                case "spare":
                    this.readNext();
                    spareTile = this.getTile();
                    this.readNext(); // read past the nested END_OBJECT
                    break;
                case "plmt":
                    this.readNext();
                    playerList = this.getPlayerList();
                    break;
                case "last":
                    this.readNext();
                    previousSlideAndInsert = this.getSlideAndInsert();
                    break;
                default:
                    throw new MazeJsonProcessingException("Found invalid field" + field + " in State.");
            }
        }
        if (tileGrid == null || spareTile == null || playerList == null || previousSlideAndInsert == null) {
            throw new MazeJsonProcessingException("Did not find all four expected values for State.");
        }
        StandardBoard board = new StandardBoard(tileGrid, spareTile);
        return new Game(board, playerList, 0, previousSlideAndInsert);
    }

    /** Ignores gems. **/
    public Board getBoardNoSpareTile() throws IOException {
        List<List<String>> rawBoard = this.getRawTileGrid();

        Tile[][] grid = this.getBoardGrid(rawBoard);
        return new StandardBoard(grid, new Tile(Set.of(UP),
                new Treasure(List.of(Gem.ammolite, Gem.alexandrite))));
    }

    public Position getCoordinate() throws IOException {
        return this.mapper.readValue(this.parser, Position.class);
    }

    public void readNext() throws IOException {
        this.parser.nextToken();
    }

    public int getInt() throws IOException {
        return this.parser.getValueAsInt();
    }

    public Direction getDirection() throws IOException {
        return Direction.valueOf(this.parser.getText());
    }

    public int getRotations() throws IOException {
        int degrees = this.parser.getValueAsInt();
        if (degrees % 90 != 0) {
            throw new MazeJsonProcessingException("Parsed a degree rotation that was not a multiple of 90.");
        }
        int counterClockwiseRotations = degrees / 90;
        int clockwiseRotations = 4 - counterClockwiseRotations;
        return clockwiseRotations;
    }

    private List<List<String>> getRawTileGrid() throws IOException {
        List<List<String>> board = new ArrayList<>();

        while(this.parser.nextToken() != JsonToken.END_OBJECT) {
            if (this.parser.getCurrentToken().equals(JsonToken.FIELD_NAME)) {
                String field = this.parser.getCurrentName();

                if (field.equals("connectors")) {
                    this.readNext();
                    while (this.parser.nextToken() != JsonToken.END_ARRAY) {
                        List<String> row = new LinkedList<>();
                        while (this.parser.nextToken() != JsonToken.END_ARRAY) {
                            row.add(this.parser.getText());

                        }
                        board.add(row);
                    }
                }
            }
        }
        return board;
    }

    private Tile[][] getBoardGrid(List<List<String>> rawBoard) {
        Tile[][] grid = new Tile[boardHeight][boardWidth];
        for (int row = 0; row < boardHeight; row++) {
            for (int col = 0; col < boardWidth; col++) {
                String shape = rawBoard.get(row).get(col);
                Tile tile = IntegrationTestUtils.createTileFromShape(shape);
                grid[row][col] = tile;
            }
        }
        return grid;
    }

    /** Ignores gems. **/
    private Tile getTile() throws IOException {
        Tile result = null;
        while (this.parser.nextToken() != JsonToken.END_OBJECT) {
            String field = this.parser.getCurrentName();

            if (field.equals("tilekey")) {
                this.readNext();
                result = IntegrationTestUtils.createTileFromShape(this.parser.getText());
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Did not find an object with a tilekey while trying to read Tile");
        }
        return result;
    }

    private List<Player> getPlayerList() throws IOException {
        List<Player> players = new ArrayList<>();

        while (this.parser.nextToken() != JsonToken.END_ARRAY) {
            players.add(this.getPlayer());
        }

        return players;
    }

    private Player getPlayer() throws IOException {
        Position avatar = null;
        Position home = null;
        Color color = null;
        while (this.parser.nextToken() != JsonToken.END_OBJECT) {
            String field = this.parser.getCurrentName();

            if (field.equals("current")) {
                this.readNext();
                avatar = this.getCoordinate();
            }
            if (field.equals("home")) {
                this.readNext();
                home = this.getCoordinate();
            }
            if (field.equals("color")) {
                this.readNext();
                String value = this.parser.getText();
                if (value.matches("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$")) {
                    color = Color.decode("#" + value);
                }
                else {
                    color = Color.getColor(value);
                }
            }
        }
        Player player = new Player("No-Name Joe", color, new Treasure(List.of(Gem.alexandrite, Gem.ammolite)), home);
        player.setCurrentAvatarPosition(avatar);
        return player;
    }

    private Optional<SlideAndInsertRecord> getSlideAndInsert() throws IOException {
        if (this.parser.getCurrentToken().equals(JsonToken.VALUE_NULL)) {
            return Optional.empty();
        }
        Direction direction;
        int index;
        int rotations = 0;
        this.readNext();
        index = this.parser.getIntValue();
        this.readNext();
        direction = this.getDirection();
        if (this.parser.nextToken() != JsonToken.END_ARRAY) {
            throw new MazeJsonProcessingException("Action array had more than two elements");
        }
        return Optional.of(new SlideAndInsertRecord(direction, index, rotations));
    }

    public static class MazeJsonProcessingException extends JsonProcessingException {
        public MazeJsonProcessingException(String msg) {
            super(msg);
        }
    }
}
