package Characters;

import citadels.Game;
import Players.Player;

/**
 * The Assassin character in Citadels (Rank 1).
 * Can target and kill another character, preventing them from taking their turn.
 * The Assassin's ability must be used at the start of their turn.
 */
public class Assassin extends Characters {

    /**
     * Constructs a new Assassin character.
     * Initializes it with rank 1 and its special ability description.
     */
    public Assassin() {
        super(CharacterName.ASSASSIN, 1);
        ability = "Select another character whom you wish to kill. The killed character loses their turn.";
    }

    /**
     * Implements the Assassin's ability for AI players.
     * Randomly chooses a character (ranks 2-8) to kill.
     *
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true as the ability always succeeds
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        int target = chooseCharacter(game, 2, 8);
        game.killCharacter(target-1);
//        message = "Player " + player.getID() + " killed the " + game.getCharacter(target) + ".";
        return true;
    }

    /**
     * Implements the Assassin's ability for human players.
     * Prompts the player to choose a character (ranks 2-8) to kill.
     *
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true if a character was killed, false if the action was cancelled
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        System.out.println("Who do you want to kill? Choose a character from 2-8 or 0 to cancel:");
        int target;
        while (true) {
            String targetStr = game.scanner.nextLine();
            try {
                target = Integer.parseInt(targetStr);
                if (target == 0) {
                    message = "You decided not to kill anyone.";
                    return false;
                }
                if (target < 2 || target > 8) {
                    System.out.println("Invalid input: 2-8 only");
                    continue;
                }
                game.killCharacter(target-1);
                message = "You killed the " + game.getCharacter(target).toString() + ".";
                return true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: 2-8 only");
            }
        }
    }
}
