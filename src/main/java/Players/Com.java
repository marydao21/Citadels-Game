package Players;

import citadels.Game;
import Districts.Districts;
import java.util.List;
import Characters.Characters;

/**
 * Implementation of an AI-controlled player in Citadels.
 * Makes automated decisions for character selection, district building,
 * and ability usage based on simple heuristics.
 */
public class Com extends Player {

    /**
     * Constructs a new AI player.
     * @param isPlayer Always false for AI players
     * @param id The player's unique identifier
     */
    public Com(boolean isPlayer, int id) {
        super(isPlayer, id);
    }

    /**
     * Handles character selection for AI players.
     * Randomly selects from available characters with special handling for the King.
     * @param game The current game instance
     */
    @Override
    public void selectCharacter(Game game) {
        // Randomly pick a character
        int choice = game.choosePlayer(0, game.getAvailableCharacters().size()-1);
        Characters selectedChar = game.getAvailableCharacters().remove(choice);
        setCharacter(selectedChar.getRank()-1); // Set the player's character to the chosen one
        game.setPlayerTurn(selectedChar.getRank()-1, this); // Set the player turn
        if (selectedChar.getRank() == 4) {
            game.setNextCrown(id-1); // Set the next crown
        }
        System.out.println("Player " + id + " selected a character"); // Show the player's character
    }

    /**
     * Handles the AI player's turn.
     * Makes automated decisions for resource collection, district building,
     * and ability usage based on current game state.
     * 
     * @param game The current game instance
     */
    @Override
    public void playTurn(Game game) {
        if (killed) {
            endTurn(game);
            return;
        }
        game.usePassive(character, this, false);
        game.useCharacterAbility(this, character, false);
        if (game.YesNo()) {
            game.giveGold(this, 2);
            System.out.println("player " + id + " chose gold");
        } else {
            System.out.println("player " + id + " chose cards");
            List<Districts> drawCards = game.drawDistricts(maxCardDrawn);
            int choice = game.choosePlayer(0, drawCards.size()-1);
            addCard(drawCards.remove(choice));
            // Discard remaining cards
            while (!drawCards.isEmpty()) {
                game.discard(drawCards.remove(0));
            }
        }
        // Try to build districts
        for (int i = 0; i < maxDistricts; i++) {
            if (!game.YesNo()) {
                break;
            }
            if (hand.isEmpty()) {
                break;
            }
            // Choose a random district from hand
            int districtIndex = game.choosePlayer(0, hand.size()-1);
            Districts d = hand.get(districtIndex);
            if (buildDistrict(game, districtIndex, false)) {
                System.out.println("player " + id + " built district " + d.getName());
            }
        }
        endTurn(game);
    }
}