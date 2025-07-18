package Districts;

import citadels.Game;
import Players.Player;

public class Library extends Districts {
    private boolean keepBothCards;  // Whether to keep both drawn cards

    public Library() {
        super("Library", DistrictsColor.PURPLE, 6,
              "If you choose to draw cards when you take an action, you keep both of the cards you have drawn");
        this.keepBothCards = false;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            System.out.println("Library ability: When you draw cards, you will keep both cards instead of choosing one");
            System.out.println("This ability is automatically applied when you draw cards");
        }
        keepBothCards = true;
        return true;
    }

    @Override
    public void resetAbility() {
        super.resetAbility();
        keepBothCards = false;
    }

    public boolean shouldKeepBothCards() {
        return keepBothCards;
    }
} 