package game.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static game.model.Direction.*;

/**
 * Represents a single tile on a Labyrinth board.
 */
public class Tile {
    private Set<Direction> pathwayConnections;

    private final Treasure treasure;

    /**
     * Creates a new Tile with the given treasure and pathways leading in the given Directions.
     */
    public Tile(Set<Direction> pathwayConnections, Treasure treasure) {
        this.pathwayConnections = new HashSet<>();
        this.pathwayConnections.addAll(pathwayConnections);
        this.treasure = treasure;
    }

    /**
     * Convenience constructor used for testing purposes.
     */
    Tile(boolean up, boolean down, boolean left, boolean right, Treasure treasure) {
        this.pathwayConnections = new HashSet<>();
        if (up) {
            this.pathwayConnections.add(UP);
        }
        if (down) {
            this.pathwayConnections.add(DOWN);
        }
        if (left) {
            this.pathwayConnections.add(LEFT);
        }
        if (right) {
            this.pathwayConnections.add(RIGHT);
        }
        this.treasure = treasure;
    }

    /**
     * @return whether the pathway on this Tile connects to edge of the tile in the given direction
     * from the center of the Tile.
     */
    public boolean connects(Direction direction) {
        return this.pathwayConnections.contains(direction);
    }

    /**
     * Rotates the Tile 90 degrees clockwise the given number of rotations.
     * @param rotations The number of clockwise rotations.
     */
    public void rotate(int rotations) {
        if (rotations < 0) {
            throw new IllegalArgumentException("Tried to rotate a tile a negative number of times.");
        }
        for (int i = 0; i < rotations; i++) {
            rotateOnce();
        }
    }

    public Treasure getTreasure() {
        return treasure;
    }

    /**
     * Checks equality of Tiles based on the equality of their Treasures, relying on the fact that
     * no two Tiles share a Treasure.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile otherTile = (Tile) o;
        return treasure.equals(otherTile.treasure);
    }

    /**
     * Hashes a Tile based on its Treasure, relying on the fact that no two Tiles share a Treasure.
     */
    @Override
    public int hashCode() {
        return Objects.hash(treasure);
    }

    /**
     * Rotates the Tile 90 degrees clockwise.
     */
    private void rotateOnce() {
        Set<Direction> nextPathwayConnections = new HashSet<>();

        if (this.pathwayConnections.contains(LEFT)) {
            nextPathwayConnections.add(UP);
        }
        if (this.pathwayConnections.contains(UP)) {
            nextPathwayConnections.add(RIGHT);
        }
        if (this.pathwayConnections.contains(RIGHT)) {
            nextPathwayConnections.add(DOWN);
        }
        if (this.pathwayConnections.contains(DOWN)) {
            nextPathwayConnections.add(LEFT);
        }
        this.pathwayConnections = nextPathwayConnections;
    }
}
