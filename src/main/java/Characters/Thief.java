package Characters;

import citadels.Game;
import Players.Player;

/**
 * The Thief character in Citadels (Rank 2).
 * Can steal all gold from another character, except the Assassin or a killed character.
 * The Thief's ability must be used at the start of their turn.
 */
public class Thief extends Characters {

    /**
     * Constructs a new Thief character.
     * Initializes it with rank 2 and its special ability description.
     */
    public Thief() {
        super(CharacterName.THIEF, 2);
        ability = "Select another character whom you wish to rob. When that character's turn comes up, you take all of their gold.";
    }

    /**
     * Implements the Thief's ability for AI players.
     * Randomly chooses a character (ranks 3-8) to steal from.
     *
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true if gold was stolen, false if no valid target was found
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        int target = chooseCharacter(game, 3, 8);
        if (target == -1) {
            return false;
        }
        game.setStolen(target-1);
        return true;
    }

    /**
     * Implements the Thief's ability for human players.
     * Prompts the player to choose a character (ranks 3-8) to steal from.
     * Cannot steal from the Assassin (rank 1) or a killed character.
     *
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true if gold was stolen, false if the action was cancelled
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        System.out.println("Who do you want to steal from? Choose a character from 3-8 or 0 to cancel:");
        int target;
        while (true) {
            String targetStr = game.scanner.nextLine();
            try {
                target = Integer.parseInt(targetStr);
                if (target == 0) {
                    message = "You decided not to steal from anyone.";
                    return false;
                }
                if (target < 3 || target > 8) {
                    System.out.println("Invalid input: 3-8 only");
                    continue;
                }
                game.setStolen(target-1);
                message = "You will steal from the " + game.getCharacter(target).toString() + ".";
                return true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: 3-8 only");
            }
        }
    }
}
