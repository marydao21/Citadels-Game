package Characters;

import citadels.Game;
import Players.Player;

/**
 * The Architect character in Citadels (Rank 7).
 * Draws two extra district cards at the start of their turn.
 * Can build up to three districts during their turn instead of one.
 */
public class Architect extends Characters {

    /**
     * Constructs a new Architect character.
     * Initializes it with rank 7 and its special ability description.
     */
    public Architect() {
        super(CharacterName.ARCHITECT, 7);
        ability = "Gains two extra district cards. Can build up to 3 districts per turn.";
    }

    /**
     * Implements the Architect's ability for AI players.
     * Draws two extra district cards.
     *
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        game.drawDistrictsForPlayer(player, 2);
        player.setMaxDistricts(3);
        message = "Player " + player.getID() + " drew 2 cards and can build up to 3 districts.";
        return true;
    }

    /**
     * Implements the Architect's ability for human players.
     * Draws two extra district cards.
     *
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        game.drawDistrictsForPlayer(player, 2);
        player.setMaxDistricts(3);
        message = "You drew 2 cards and can build up to 3 districts.";
        return true;
    }

    /**
     * Implements the Architect's passive ability.
     * Sets the player's maximum districts to 3.
     *
     * @param game The current game instance
     * @param player The player receiving the passive effect
     * @param isHuman true if the player is human, false if AI
     */
    @Override
    public void usePassive(Game game, Player player, boolean isHuman) {
        player.setMaxDistricts(3);
        if (isHuman) {
            System.out.println("You can now build 3 districts.");
        } else {
            System.out.println("Player " + player.getID() + " can now build 3 districts.");
        }
    }
}
