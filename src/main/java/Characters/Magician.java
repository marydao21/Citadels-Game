package Characters;

import citadels.Game;
import Players.Player;

/**
 * The Magician character in Citadels (Rank 3).
 * Can either exchange hands with another player or discard any number of cards
 * to draw an equal number of new ones from the deck.
 */
public class Magician extends Characters {

    /**
     * Constructs a new Magician character.
     * Initializes it with rank 3 and its special ability description.
     */
    public Magician() {
        super(CharacterName.MAGICIAN, 3);
        ability = "Can either exchange their hand with another player’s, or discard any number of \n" +
                "district cards face down to the bottom of the deck and draw an equal number of cards from the \n" +
                "district deck (can only do this once per turn).";
    }

    /**
     * Implements the Magician's ability for AI players.
     * Randomly chooses between exchanging hands with another player
     * or discarding and drawing new cards.
     *
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true if the ability was used successfully
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        // AI randomly chooses between exchanging hands (option 1) or discarding cards (option 2)
        if (game.YesNo()) {
            // Exchange hands with random player
            int target = game.choosePlayer(1, game.getPlayerCount());
            while (target == player.getID()) {
                target = game.choosePlayer(1, game.getPlayerCount());
            }
            game.exchangeHands(player, target - 1);
            message = "Player " + player.getID() + " exchanged hands with Player " + target + ".";
        } else {
            // Discard and draw new cards
            int discarded = game.aiDiscard(player);
            message = "Player " + player.getID() + " discarded and drew " + discarded + " new cards.";
        }
        return true;
    }

    /**
     * Implements the Magician's ability for human players.
     * Prompts the player to choose between exchanging hands with another player
     * or discarding and drawing new cards.
     *
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true if the ability was used successfully
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        String action = "Choose your action:\n0. Cancel action\n1. Exchange hands with another player\n2. Discard and draw new district cards";
        System.out.println(action);

        while (true) {
            String choice = game.scanner.nextLine();
            if (choice.equals("0")) {
                message = "You decided not to use your ability.";
                return false;
            } else if (choice.equals("1")) {
                System.out.println("Choose a player to exchange hands with (1-" + game.getPlayerCount() + ") or 0 to change action:");
                while (true) {
                    try {
                        int target = Integer.parseInt(game.scanner.nextLine());
                        if (target == 0) {
                            System.out.println(action);
                            break;
                        }
                        if (target > 1 && target <= game.getPlayerCount()) {
                            game.exchangeHands(player, target - 1);
                            message = "You exchanged hands with Player " + target + ".";
                            return true;
                        }
                        System.out.println("Invalid player number. Choose 1-" + game.getPlayerCount() + " (except 3) or 0 to change action:");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Choose a number.");
                    }
                }
            } else if (choice.equals("2")) {
                System.out.println("Your hand:");
                player.printHand();
                System.out.println("Enter the indices of cards to discard (comma-separated) or 0 to change action:");
                while (true) {
                    String input = game.scanner.nextLine();
                    if (input.equals("0")) {
                        System.out.println(action);
                        break;
                    }
                    try {
                        String[] indices = input.split(",");
                        int count = 0;
                        int handSize = player.getHandsize();
                        for (String index : indices) {
                            int idx = Integer.parseInt(index.trim()) - 1;
                            if (idx >= 0 && idx < handSize) {
                                player.discardCard(idx - count, game);
                                count++;
                            } else {
                                System.out.println("No card number " + index);
                            }
                        }
                        if (count > 0) {
                            // Draw new cards equal to the number discarded
                            game.drawDistrictsForPlayer(player, count);
                            message = "You discarded " + count + " cards and drew " + count + " new ones.";
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Enter comma-separated numbers or 0 to change action.");
                    }
                }
            } else {
                System.out.println("Invalid choice. Choose 0, 1, or 2:");
            }
        }
    }
}
