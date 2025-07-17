package Characters;

import citadels.Game;
import Players.Player;
import Districts.DistrictsColor;

/**
 * The Bishop character in Citadels (Rank 5).
 * Protects their districts from the Warlord's ability.
 * Gains one gold for each religious (blue) district in their city.
 */
public class Bishop extends Characters {

    /**
     * Constructs a new Bishop character.
     * Initializes it with rank 5 and its special ability description.
     */
    public Bishop() {
        super(CharacterName.BISHOP, 5);
        ability = "Gains one gold for each blue (religious) district in your city. Your buildings cannot be \n" +
                "destroyed by the Warlord, unless you are killed by the Assassin.";
    }

    /**
     * Implements the Bishop's ability for AI players.
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        return true;
    }

    /**
     * Implements the Bishop's ability for human players.
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        message = "Bishop has no active ability.";
        return true;
    }

    /**
     * Implements the Bishop's passive income ability.
     * Awards one gold for each blue district in the player's city.
     *
     * @param game The current game instance
     * @param player The player receiving gold
     * @param isHuman true if the player is human, false if AI
     */
    @Override
    public void usePassive(Game game, Player player, boolean isHuman) {
        player.setBishop();
        int gold = game.giveDistrictGold(player, DistrictsColor.BLUE);
        if (isHuman) {
            System.out.println("You received " + gold + " gold for your religious districts.");
        } else {
            System.out.println("Player " + player.getID() + " received " + gold + " gold for their religious districts.");
        }
    }
}
