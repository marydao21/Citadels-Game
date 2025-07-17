package Characters;

import citadels.Game;
import Players.Player;
import Districts.DistrictsColor;

/**
 * The Merchant character in Citadels (Rank 6).
 * Receives one extra gold when taking gold as an action.
 * Gains one gold for each trade (green) district in their city.
 */
public class Merchant extends Characters {

    /**
     * Constructs a new Merchant character.
     * Initializes it with rank 6 and its special ability description.
     */
    public Merchant() {
        super(CharacterName.MERCHANT, 6);
        ability = "Gains one gold for each green (trade) district in their city. Gains one extra gold.";
    }

    @Override
    public boolean useAbilityAI(Game game, Player player) {
        return true;
    }

    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        message = "Merchant has no active ability.";
        return true;
    }

    /**
     * Implements the Merchant's passive income ability.
     * Awards one gold for each green district in the player's city and one extra gold.
     *
     * @param game The current game instance
     * @param player The player receiving gold
     * @param isHuman true if the player is human, false if AI
     */
    @Override
    public void usePassive(Game game, Player player, boolean isHuman) {
        game.giveGold(player, 1);
        int gold = game.giveDistrictGold(player, DistrictsColor.GREEN);
        if (isHuman) {
            System.out.println("You received 1 extra gold and " + gold + " gold for your trade districts.");
        } else {
            System.out.println("Player " + player.getID() + " received 1 extra gold and " + gold + " gold for their trade districts.");
        }
    }
}
