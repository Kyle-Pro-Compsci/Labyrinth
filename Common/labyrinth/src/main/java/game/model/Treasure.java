package game.model;

import java.util.*;

/**
 * Represents an unordered pair of gems.
 */
public class Treasure {
    private final List<Gem> gems;

    /**
     * Creates a new Treasure and enforces that there are exactly 2 gems.
     */
    public Treasure(List<Gem> gems) {
        if (gems.size() != 2) {
            throw new IllegalArgumentException("Tried to create a treasure without exactly 2 gems");
        }
        this.gems = gems;
    }

    /**
     * Convenience constructor for testing which creates a new Treasure with the given two gems.
     */
    Treasure(Gem gem1, Gem gem2) {
        this.gems = new ArrayList<>();
        this.gems.add(gem1);
        this.gems.add(gem2);
    }

    public List<Gem> getGems() {
        return gems;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Treasure otherTreasure = (Treasure) other;
        if (otherTreasure.getGems().size() != this.gems.size()) {
            return false;
        }
        return this.gems.containsAll(otherTreasure.getGems()) && otherTreasure.getGems().containsAll(this.gems);
    }
}
