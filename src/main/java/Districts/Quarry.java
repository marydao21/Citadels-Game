package Districts;

import citadels.Game;
import Players.Player;
import java.util.List;

public class Quarry extends Districts {
    private boolean canBuildDuplicate;  // Whether player can build a duplicate district

    public Quarry() {
        super("Quarry", DistrictsColor.PURPLE, 5,
              "When building, you may play a district already found in your city. You may only have one such duplicate district in your city at any one time");
        this.canBuildDuplicate = true;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            System.out.println("Quarry ability: You may build a duplicate district from your city");
            System.out.println("This ability is automatically applied when building");
        }
        return true;
    }

    @Override
    public boolean canBeDuplicated() {
        return canBuildDuplicate;
    }

    public boolean canBuildDuplicateDistrict(Player player) {
        // Check if player already has a duplicate district
        List<Districts> city = player.getCity();
        for (Districts district : city) {
            if (district != this) {  // Don't count this Quarry
                int count = 0;
                for (Districts otherDistrict : city) {
                    if (otherDistrict.getName().equals(district.getName())) {
                        count++;
                    }
                }
                if (count > 1) {
                    return false;  // Already has a duplicate
                }
            }
        }
        return true;  // No duplicates found, can build one
    }
} 