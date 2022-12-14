package game.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents a position on a board where row and column are 0-indexed from the top-left of the board.
 */
@JsonPropertyOrder({ "column#", "row#" })
public class Position {
    @JsonProperty("row#")
    private final int row;
    @JsonProperty("column#")
    private final int column;

    public Position(@JsonProperty("row#") int row, @JsonProperty("column#") int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Tried to create a Position with negative coordinates.");
        }
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Get the position that is rowDelta rows and colDelta columns away from this position, modulo the dimensions
     * of the board.
     */
    public Position addDeltaWithBoardWrap(int rowDelta, int colDelta, int boardHeight, int boardWidth) {
        int nextRow = (this.row + rowDelta) % boardHeight;
        if (nextRow < 0) {
            nextRow = nextRow + boardHeight;
        }
        int nextCol = (this.column + colDelta) % boardWidth;
        if (nextCol < 0) {
            nextCol = nextCol + boardWidth;
        }
        return new Position(nextRow, nextCol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ')';
    }

    /**
     * Compares two Positions by row index then by column index.
     */
    public static class RowColumnOrderComparator implements Comparator<Position> {
        @Override
        public int compare(Position p1, Position p2) {
            if (p1.getRow() != p2.getRow()) {
                return Integer.compare(p1.getRow(), p2.getRow());
            } else {
                return Integer.compare(p1.getColumn(), p2.getColumn());
            }
        }
    }
}