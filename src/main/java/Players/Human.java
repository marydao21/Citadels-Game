package Players;

import citadels.Game;
import Districts.Districts;
import java.util.List;
import Characters.Characters;

/**
 * Implementation of a human-controlled player in Citadels.
 * Handles user input for character selection, district building,
 * and ability usage through console interaction.
 */
public class Human extends Player {

    /**
     * Constructs a new human player.
     * @param isPlayer Always true for human players
     * @param id The player's unique identifier
     */
    public Human(boolean isPlayer, int id) {
        super(isPlayer, id);
        isTurn = false;
    }

    /**
     * Handles character selection for human players.
     * Displays available characters and prompts for selection.
     * @param game The current game instance
     */
    @Override
    public void selectCharacter(Game game) {
        // Show available characters
        List<Characters> available = game.getAvailableCharacters();
        System.out.println("Choose a character [1-" + available.size() + "]:");
        for (int i = 0; i < available.size(); i++) {
            System.out.println((i+1) + ": " + available.get(i).getName());
        }
        // Get player's choice
        while (true) {
            try {
                int choice = Integer.parseInt(game.scanner.nextLine()) - 1;
                if (choice >= 0 && choice < available.size()) {
                    Characters selectedChar = available.remove(choice);
                    setCharacter(selectedChar.getRank()-1);
                    game.setPlayerTurn(selectedChar.getRank()-1, this);
                    System.out.println("You selected " + selectedChar.getName());
                    if (selectedChar.getRank() == 4) {
                        game.setNextCrown(id-1);
                    }
                    return;
                }
            } catch (NumberFormatException e) {}
            System.out.println("Invalid choice. Please select 1-" + available.size());
        }
    }

    /**
     * Handles the human player's turn.
     * Processes commands for viewing game state, building districts,
     * using character abilities, and managing resources.
     * 
     * @param game The current game instance
     */
    @Override
    public void playTurn(Game game) {
        if (isTurn) {
            System.out.println("Your turn");
            if (killed) {
                System.out.println("Oops! Too bad! You were killed.");
                endTurn(game);
                return;
            }
            game.usePassive(character, this, true);
            if (character == 0 || character == 1) {
                useCharacterAbility(game);
                usedCharacter = true;
            }
            goldOrCards(game);
        }
        // Handle human's input
        String input = "";
        String[] inputArray;
        while (true) {
            System.out.print("> ");
            input = game.scanner.nextLine().trim();
            inputArray = input.split(" ", 2);  // Changed to 2 to properly split commands with arguments
            if (input.equals("hand")) {  // Print hand and gold
                printGold();
                printHand();
                continue;
            }
            if (input.equals("gold")) {  // Print gold
                printGold();
                continue;
            }
            if (inputArray[0].equals("citadel") || inputArray[0].equals("list") || inputArray[0].equals("city")) {  // Print citadel of a player
                if (inputArray.length < 2) {
                    game.printCitadel(id);
                    continue;
                }
                try {
                    int target = Integer.parseInt(inputArray[1]);
                    if (target < 1 || target > game.getPlayerCount()) {
                        System.out.println("Invalid target");
                        continue;
                    }
                    game.printCitadel(target);
                } catch (Exception e) {
                    System.out.println("Invalid target");
                }
                continue;
            }
            if (input.equals("actionCharacter")) {  // Print character's ability
                game.printAction(character);
                continue;
            }
            if (input.equals("actionDistrict")) {  // Print district's abilities
                System.out.println("You have these abilities, use useDistrictAbility <DistrictName> to use them:");
                boolean shown = false;
                for (Districts district : city) {
                    if (district.hasSpecialAbility()) {
                        System.out.println(district.printAbility());
                        shown = true;
                    }
                }
                if (!shown) {
                    System.out.println("Oops! My bad. You don't have any district abilities");
                }
                continue;
            }
            if (inputArray[0].equals("useDistrictAbility")) {  // Use district's ability
                if (!isTurn) {
                    continue;
                }
                if (inputArray.length != 2) {
                    System.out.println("Invalid input");
                    continue;
                }
                game.useDistrictAbility(inputArray[1], this, true);
                continue;
            }
            if (input.equals("useCharacterAbility")) {  // Use character's ability
                if (!isTurn) {
                    continue;
                }
                if (!usedCharacter) {
                    useCharacterAbility(game);
                    usedCharacter = true;
                } else {
                    System.out.println("You have already used your character ability");
                }
                continue;
            }
            if (inputArray[0].equals("info")) {  // Print info about a district or a character
                if (inputArray.length != 2) {
                    System.out.println("Invalid. Specify what you want to know about: info <name>");
                    continue;
                }
                boolean found = false;
                for (Districts district : hand) {
                    if (district.getName().equalsIgnoreCase(inputArray[1])) {
                        System.out.println(district.toString() + ", ability: " + district.getAbility());
                        found = true;
                        break;
                    }
                }
                if (!found && !game.printCharacter(inputArray[1])) {
                    System.out.println("No info found about " + inputArray[1]);
                }
                continue;
            }
            if (input.equals("t")) {  // Process turns
                if (!isTurn) {
                    break;
                }
                System.out.println("This is only to acknowledge other's turn");
                continue;
            }
            if (input.equals("end")) {  // End turn
                if (!isTurn) {
                    continue;
                }
                endTurn(game);
                break;
            }
            if (input.equals("all")) {  // Print all players' status
                game.printAll();
                continue;
            }
            if (input.equals("help")) {  // Print help menu
                printHelp();
                continue;
            }
            if (input.equals("build")) {  // Build a district
                if (!isTurn) {
                    continue;
                }
                if (maxDistricts > 0) {
                    humanBuildDistrict(game);
                } else {
                    System.out.println("You cannot build any more districts this turn");
                }
                continue;
            }
            if (input.equals("debugChar")) {  // Print players' characters
                game.debugCharacter();
                continue;
            }
            if (input.equals("debugHand")) {  // Print players' hands
                game.debugHand();
                continue;
            }
            System.out.println("Invalid command. Type 'help' for available commands.");
        }
    }

    /**
     * Handles the resource collection phase of a turn.
     * Prompts the player to choose between taking 2 gold or drawing cards.
     * When drawing cards, shows the drawn cards and lets player choose one to keep.
     * 
     * @param game The current game instance
     */
    public void goldOrCards(Game game) {
        System.out.println("Collect 2 gold or draw " + maxCardDrawn + " cards and pick one [gold/cards]:");
        boolean choice;
        String input;
        while (true) {
            input = game.scanner.nextLine().toLowerCase();
            if (input.equals("gold")) {
                choice = true;
                break;
            } else if (input.equals("cards")) {
                choice = false;
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
        gC = true;
        if (choice) {
            game.giveGold(this, 2);
            System.out.println("You received 2 golds");
            return;
        }
        List<Districts> drawCards = game.drawDistricts(maxCardDrawn);
        System.out.println("Pick one of the following cards [1/" + maxCardDrawn + "]:");
        for (int i = 0; i < maxCardDrawn; i++) {
            Districts d = drawCards.get(i);
            System.out.println((i + 1) + ": " + d.getName() + " [color=" + d.getColor() + 
                           ", cost=" + d.getCost() + ", points=" + d.getPoints(this) + 
                           ", ability=" + d.getAbility() + "]");
        }
        int card;
        while (true) {
            input = game.scanner.nextLine();
            try {
                card = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }
            if (card < 1 || card > maxCardDrawn) {
                System.out.println("Invalid input");
                continue;
            }
            Districts d = drawCards.remove(card - 1);
            addCard(d);
            System.out.println("You chose " + d.getName());
            // Discard remaining cards
            while (!drawCards.isEmpty()) {
                game.discard(drawCards.remove(0));
            }
            return;
        }
    }

    /**
     * Handles the district building process.
     * Shows available districts in hand that can be built,
     * prompts for selection, and handles the building process.
     * 
     * @param game The current game instance
     */
    public void humanBuildDistrict(Game game) {
        int handSize = hand.size();
        System.out.println("Build one of the following districts [1-" + handSize + "], 0 to cancel:");
        for (int i = 0; i < handSize; i++) {
            System.out.println((i + 1) + ": " + hand.get(i).toString());
        }
        String input;
        int choice;
        while (true) {
            input = game.scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }
            if (choice == 0) {
                System.out.println("Build cancelled");
                return;
            }
            if (choice > 0 && choice <= handSize) {
                Districts d = hand.get(choice-1);
                if (buildDistrict(game, choice-1, true)) {
                    System.out.println("Built district " + d.getName());
                }
                return;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    /**
     * Displays the help menu showing all available commands.
     * Lists each command with a description of its function,
     * including game actions, information display, and turn management.
     */
    public void printHelp() {
        System.out.println("==================================================================================");
        System.out.println("Available commands:");
        System.out.println("t - Process turns – proceed to the next sequence in the game, eg the next computer \nplayer makes their turn ");
        System.out.println("hand - Show your hand and amount of gold you have");
        System.out.println("gold - Show your gold");
        System.out.println("citadel/list/city [player] - Show a player's citadel");
        System.out.println("actionCharacter - Show your character's action");
        System.out.println("actionDistrict - Show your district's abilities");
        System.out.println("useCharacterAbility - Use your character's ability");
        System.out.println("useDistrictAbility - Use one of your districts' ability (The cards in cards_to_implement.tsv are not implemented)");
        System.out.println("info [name] - Show info about a district in your hand or a character");
        System.out.println("build - Build a district");
        System.out.println("end - End your turn");
        System.out.println("all - Display info about all players, including number of cards in hand, gold and districts \n" +
                "built in their city");
        System.out.println("help - Show this help menu");
        System.out.println("save [filename] - Save the game (Not implemented. This is hard af)");
        System.out.println("load [filename] - Load a game (Not implemented. This is hard af)");
        System.out.println("==================================================================================");
    }
}