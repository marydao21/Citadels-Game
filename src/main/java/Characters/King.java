package Characters;

import citadels.Game;
import Players.Player;
import Districts.DistrictsColor;

/**
 * The King character in Citadels (Rank 4).
 * Takes the crown and becomes first player in the next round.
 * Gains one gold for each noble (yellow) district in their city.
 */
public class King extends Characters {
    
    /**
     * Constructs a new King character.
     * Initializes it with rank 4 and its special ability description.
     */
    public King() {
        super(CharacterName.KING, 4);
        ability = "Gains one gold for each yellow (noble) district in your city. You receive the crown token \n" +
                "and will be the first to choose characters on the next round. ";
    }

    /**
     * Implements the King's ability for AI players.
     * Gives the crown token to the AI player.
     *
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        game.giveCrown(player);
        message = "Player " + player.getID() + " received the crown.";
        return true;
    }

    /**
     * Implements the King's ability for human players.
     * Gives the crown token to the human player.
     *
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        game.giveCrown(player);
        message = "You received the crown.";
        return true;
    }

    /**
     * Implements the King's passive income ability.
     * Awards one gold for each yellow district in the player's city.
     *
     * @param game The current game instance
     * @param player The player receiving gold
     * @param isHuman true if the player is human, false if AI
     */
    @Override
    public void usePassive(Game game, Player player, boolean isHuman) {
        int gold = game.giveDistrictGold(player, DistrictsColor.YELLOW);
        if (isHuman) {
            System.out.println("You received " + gold + " gold for your noble districts.");
        } else {
            System.out.println("Player " + player.getID() + " received " + gold + " gold for their noble districts.");
        }
    }
}
