package Districts;

import citadels.Game;
import Players.Player;
import Characters.Warlord;

/**
 * The Armory district card in Citadels.
 * A purple district that can be sacrificed to destroy another district,
 * similar to the Warlord's ability but without paying gold.
 */
public class Armory extends Districts {

    /**
     * Constructs a new Armory district card.
     * Initializes it as a purple district with cost 3 and one-use ability.
     */
    public Armory() {
        super("Armory", DistrictsColor.PURPLE, 3,
              "During your turn, you may destroy the Armory in order to destroy any other district card of your choice in another player's city");
        this.numAbilities = 1;
    }

    /**
     * Uses the Armory's special ability to destroy itself and another district.
     * Creates a temporary Warlord to handle the district destruction logic.
     *
     * @param game The current game instance
     * @param player The player using the ability
     * @param isHuman true if the player is human, false if AI
     * @return true if a district was successfully destroyed, false otherwise
     */
    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        // Find this district's index in the player's city
        int armoryIndex = -1;
        for (int i = 0; i < player.getCity().size(); i++) {
            if (player.getCity().get(i) == this) {
                armoryIndex = i;
                break;
            }
        }

        if (armoryIndex == -1) {
            return false;
        }

        // Create a Warlord to use its ability
        Warlord warlord = new Warlord();
        boolean success;
        
        if (isHuman) {
            System.out.println("Using Armory - you will destroy this district to destroy another player's district.");
            success = warlord.useAbilityHuman(game, player);
        } else {
            success = warlord.useAbilityAI(game, player);
        }

        if (success) {
            // Remove the Armory itself
            player.removeDistrict(game, armoryIndex);
            if (isHuman) {
                System.out.println("Armory was destroyed in the process");
            }
            return true;
        }
        return false;
    }
} 