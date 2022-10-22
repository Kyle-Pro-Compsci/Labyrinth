package game.model;

import java.awt.*;

/**
 * Represents a single Player in the game.
 */
public class Player {
    private final String name;
    private final Color color;
    private final Treasure goalTreasure;
    private final Position homePosition;
    private Position currentAvatarPosition;

    /**
     * Creates a new Player with the given goal Treasure and Position for their home tile, and sets
     * the player's initial Position to the home tile's Position.
     */
    public Player(String name, Color color, Treasure goalTreasure, Position homePosition) {
        this.name = name;
        this.color = color;
        this.goalTreasure = goalTreasure;
        this.homePosition = homePosition;
        this.currentAvatarPosition = homePosition;
    }

    public Treasure getGoalTreasure() {
        return goalTreasure;
    }

    public Position getHomePosition() {
        return homePosition;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Position getCurrentAvatarPosition() {
        return currentAvatarPosition;
    }

    public void setCurrentAvatarPosition(Position currentAvatarPosition) {
        this.currentAvatarPosition = currentAvatarPosition;
    }

    @Override
    public String toString() {
        return "Player{" +
                "goalTreasure=" + goalTreasure +
                ", homePosition=" + homePosition +
                ", currentAvatarPosition=" + currentAvatarPosition +
                '}';
    }
}
