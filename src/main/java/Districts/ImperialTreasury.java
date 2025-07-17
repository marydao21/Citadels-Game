package Districts;

import Players.Player;

public class ImperialTreasury extends Districts {
    public ImperialTreasury() {
        super("Imperial Treasury", DistrictsColor.PURPLE, 4,
              "At the end of the game, you score one point for each gold in your possession. Gold placed on your district cards do not count towards this total");
    }

    @Override
    public int getPoints(Player player) {
        // Base points (4) plus one point for each gold the player has
        return super.getPoints(player) + player.getGold();
    }
} 