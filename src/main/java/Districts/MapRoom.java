package Districts;

import Players.Player;

public class MapRoom extends Districts {
    public MapRoom() {
        super("Map Room", DistrictsColor.PURPLE, 5,
              "At the end of the game, you score one point for each card in your hand");
    }

    @Override
    public int getPoints(Player player) {
        // Base points (5) plus one point for each card in player's hand
        return super.getPoints(player) + player.getHandsize();
    }
} 