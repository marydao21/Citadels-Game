package Districts;

import Players.Player;
import java.util.List;

public class WishingWell extends Districts {
    public WishingWell() {
        super("Wishing Well", DistrictsColor.PURPLE, 5,
              "At the end of the game, you score one point for every OTHER purple district in your city");
    }

    @Override
    public int getPoints(Player player) {
        // Count other purple districts (excluding this Wishing Well)
        int otherPurpleCount = 0;
        List<Districts> city = player.getCity();
        for (Districts district : city) {
            if (district.getColor() == DistrictsColor.PURPLE && district != this) {
                otherPurpleCount++;
            }
        }
        // Base points (5) plus one point for each other purple district
        return super.getPoints(player) + otherPurpleCount;
    }
} 