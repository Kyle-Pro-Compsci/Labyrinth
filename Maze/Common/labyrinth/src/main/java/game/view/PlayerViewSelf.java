package game.view;

import game.model.Player;
import game.model.Position;
import game.model.Treasure;

public class PlayerViewSelf {

    private final Player player;

    public PlayerViewSelf(Player player) {
        this.player = player;
    }

    public Position getAvatarPosition() {
        return player.getCurrentAvatarPosition();
    }

    public Treasure getGoalTreasure() {
        return player.getGoalTreasure();
    }
}
