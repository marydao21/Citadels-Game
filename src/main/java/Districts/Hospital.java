package Districts;

import citadels.Game;
import Players.Player;

public class Hospital extends Districts {
    private boolean allowActionWhenKilled;  // Whether to allow action when assassinated

    public Hospital() {
        super("Hospital", DistrictsColor.PURPLE, 6,
              "Even if you are assassinated, you may take an action during your turn (but you may not build a district card or use your character's power)");
        this.allowActionWhenKilled = true;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            System.out.println("Hospital ability: You may take an action even if assassinated");
            System.out.println("However, you cannot build districts or use character powers");
            System.out.println("This ability is automatically applied when you are assassinated");
        }
        return true;
    }

    public boolean shouldAllowActionWhenKilled() {
        return allowActionWhenKilled;
    }
} 