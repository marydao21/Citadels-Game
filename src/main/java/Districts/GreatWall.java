package Districts;

import citadels.Game;
import Players.Player;

public class GreatWall extends Districts {
    private boolean increaseDestroyCost;  // Whether to increase Warlord's destroy cost

    public GreatWall() {
        super("Great Wall", DistrictsColor.PURPLE, 6,
              "The cost for the Warlord to destroy any of your other districts is increased by one gold");
        this.increaseDestroyCost = true;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            System.out.println("Great Wall ability: Warlord must pay +1 gold to destroy your other districts");
            System.out.println("This ability is automatically active");
        }
        return true;
    }

    public boolean shouldIncreaseDestroyCost() {
        return increaseDestroyCost;
    }
} 