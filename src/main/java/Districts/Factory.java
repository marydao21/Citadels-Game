package Districts;

import citadels.Game;
import Players.Player;

public class Factory extends Districts {
    private boolean reducePurpleCost;  // Whether to reduce cost of other purple districts

    public Factory() {
        super("Factory", DistrictsColor.PURPLE, 6,
              "Your cost for building OTHER purple district cards is reduced by one. This does not affect the Warlord's cost for destroying the card");
        this.reducePurpleCost = true;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            System.out.println("Factory ability: Your cost for building other purple districts is reduced by 1 gold");
            System.out.println("This ability is automatically applied when building");
        }
        return true;
    }

    public boolean shouldReducePurpleCost() {
        return reducePurpleCost;
    }

    public int getReducedCost(Districts district) {
        if (reducePurpleCost && district.getColor() == DistrictsColor.PURPLE && !district.getName().equals("Factory")) {
            return Math.max(0, district.getCost() - 1);  // Reduce by 1, minimum 0
        }
        return district.getCost();  // No reduction
    }
} 