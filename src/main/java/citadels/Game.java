package citadels;

import java.util.*;

import Characters.Characters;
import Districts.Districts;
import Districts.DistrictsColor;

import Players.Com;
import Players.Human;
import Players.Player;

/**
 * The Game class represents the main game logic for the Citadels card game.
 * It manages the game state, players, cards, and game flow including character selection
 * and action phases. The game supports both human and computer players.
 *
 * @author Mary Dao
 * @version 1.0
 */
public class Game {
    public Scanner scanner;    // Tool to read player input
    public Random random;  // Tool to make random choices

    private int numPlayers;    // Number of players
    private List<Districts> deck;  // List to store district cards
    private List<Characters> characterDeck;  // List of character cards
    private Player[] players; // List of players
    private List<Player> throneRoomOwners; // List of players who own a Throne Room
    private int gameState;  // 0: character selection, 1: play, 2: cs last round, 3: play last round, 4: end
    private int crownHolder;    // Player who holds the crown
    private List<Characters> availableCharacters;  // Characters available for selection
    private int currentPlayer;  // Current player's turn
    private Player[] playerTurns;  // Characters chosen by each player
    private int firstTo8Player = -1;  // Player who was first to 8 districts
    private int[] points;  // Points for each player
    private int count;  // Count of players who have selected a character
    private final boolean interrupted;  // Whether the game has been interrupted
    private Characters face_down;
    private int nextCrown;
    private int thief_target;
    private App app;

    /**
     * Constructs a new Game instance with the specified parameters.
     *
     * @param app The main application instance
     * @param deck The initial deck of district cards
     * @param characterDeck The deck of character cards
     * @param numPlayers The number of players in the game
     */
    public Game(App app, List<Districts> deck, List<Characters> characterDeck, int numPlayers) {
        // Initialize the game
        this.app = app;
        scanner = new Scanner(System.in);
        random = new Random();
        this.deck = deck;
        this.characterDeck = characterDeck;
        this.numPlayers = numPlayers;
        // Initialize arrays
        players = new Player[numPlayers];
        playerTurns = new Player[characterDeck.size()];
        availableCharacters = new ArrayList<>();
        points = new int[numPlayers];
        availableCharacters = new ArrayList<>();
        throneRoomOwners = new ArrayList<>();

        // Set up the game, set starting values
        interrupted = false;
        firstTo8Player = -1;
        thief_target = -1;
        count = 0;
        gameState = 0;
        shuffleDistricts();
        nextCrown = random.nextInt(numPlayers);
        currentPlayer = 8;
    }

    /**
     * Sets up the initial game state by creating players (one human, rest computer).
     */
    public void setUp() {
        // Initialize players
        for (int i = 0; i < numPlayers; i++) {
            if (i == 0) {
                addPlayer(new Human(true, i + 1));
            } else {
                addPlayer(new Com(false, i + 1));
            }
        }
    }

    /**
     * Initiates the character selection phase of the game.
     * This phase allows players to choose their characters for the round.
     */
    public void startSelectionPhase() {
        if (currentPlayer < 8) { // If current player is less than 8, don't start selection
            return;
        }
        if (firstTo8Player == -1 && gameState == 1) {
            gameState = 0; // If no one has 8 districts yet, go back to normal selection
        } else if (firstTo8Player != -1 && gameState == 1) {
            gameState = 2; // If someone has 8 districts, go to last round
        } else if (gameState == 3) {
            gameState++; // If last round, go to end    
            gameEnd();
            app.gameOver();
        }
        System.out.println("===============================================================");
        System.out.println("Selection phase: ");
        System.out.println("===============================================================");
        System.out.println("Player " + (nextCrown+1) + " was the King."); // Show who was King
        thief_target = -1; // Reset thief target
        
        // Check if crown is changing hands
        if (nextCrown != crownHolder) {
            // Give gold to all Throne Room owners
            for (Player player : throneRoomOwners) {
                giveGold(player, 1);
                System.out.println("Player " + player.getID() + " received 1 gold from Throne Room");
            }
        }
        
        crownHolder = nextCrown; // Update who has the crown
        currentPlayer = crownHolder; // Set current player to crown holder  
        availableCharacters.addAll(characterDeck); // Add all characters to available characters
        shuffleCharacters(); // Shuffle the characters
        Arrays.fill(playerTurns, null); // Reset player turns
    }

    /**
     * Starts the action phase of the game.
     */
    private void startActionPhase() {
        System.out.println("===============================================================");
        System.out.println("Player action phase: ");
        if (gameState == 3) {
            System.out.println("This is the last turn");
        }
        System.out.println("===============================================================");
        availableCharacters.clear();  // Clear the list of available characters since they've all been chosen
        currentPlayer = 0; // Reset current player  
    }

    /**
     * Executes a single turn in the game, handling both selection and action phases.
     *
     * @param app The main application instance
     */
    public void play(App app) {
        if (gameState == 4) {
            System.out.println("Game is already over");
            return;
        }

        // Selection phase
        if (gameState == 0 || gameState == 2) {
            players[currentPlayer].selectCharacter(this);
            if (!players[currentPlayer].isHuman()) {
                players[0].playTurn(this);
            }
            // Move to next player
            currentPlayer++;
            if (currentPlayer >= numPlayers) {
                currentPlayer = 0;
            }

            // Keep track of how many players have chosen
            count++;
            if (count == 6) { // After 6 players have chosen, add the face-down character
                availableCharacters.add(face_down);
            }
            if (count >= numPlayers) { // After all players have chosen, go to action phase 
                gameState++;
                count = 0;
                startActionPhase();
            }
            return;
        }

        // Action phase
        if (playerTurns[currentPlayer] == null) { // Skip if no character chosen for this turn
            currentPlayer++;
            startSelectionPhase();
            return;
        }
        Player player = playerTurns[currentPlayer];
        boolean killed = player.isKilled(); // Check if player is killed
        player.setTurn(); // Set player's turn  
        if (!killed) { // If player is not killed, print their character    
            System.out.println("Player " + player.getID() + " is the " + getCharacter(player.getCharacter()+1));
            if (player.getCharacter() == thief_target) { // If player is the thief target, steal the character  
                stealCharacter(thief_target);
            }
        }
        // Let player take their turn
        player.playTurn(this);
        if (!player.isHuman() && !killed) { // If player is not human and not killed, let human take their turn
            players[0].playTurn(this);
        }
        if (interrupted) { // If game is interrupted, go to end 
            gameState = 4;
        }
        currentPlayer++;
        startSelectionPhase();
    }

    /**
     * Records the first player to build eight districts.
     *
     * @param id The player ID who first reached eight districts
     */
    public void setFirstEight(int id) {
        if (firstTo8Player == -1) {
            firstTo8Player = id;
        }
    }

    /**
     * Calculates final scores and determines the winner of the game.
     */
    public void gameEnd() {
        System.out.println("===========================================================================");
        System.out.println("Game has ended. Here are the points of each player:");
        points[firstTo8Player] += 4; // Add 4 points to the player who was first to 8 districts
        int highest_id = -1; // Variables to track highest score
        int highest = 0;
        for (int i=0; i<players.length; i++) {
            points[i] += players[i].countPoints(); // Add points from districts
            if (points[i] > highest) {
                highest = points[i];
                highest_id = i;
            }
            System.out.println("Player " + i + " got " + points[i] + " points.");
        }
        System.out.println("Player " + highest_id + " won the game! Hoorayyyyyy!!!");
    }

    /**
     * Generates a random boolean value for AI decision making.
     *
     * @return true or false randomly
     */
    public boolean YesNo() {
        int YesNo = random.nextInt(2);
        return YesNo == 1;
    }

    /**
     * Sets the next player to receive the crown.
     *
     * @param nextCrown The player ID who will receive the crown
     */
    public void setNextCrown(int nextCrown) {
        this.nextCrown = nextCrown;
    }

    /**
     * Randomly selects a player number within a specified range.
     *
     * @param min The minimum player number (inclusive)
     * @param max The maximum player number (inclusive)
     * @return A random player number within the specified range
     */
    public int choosePlayer(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Awards gold to a specified player.
     *
     * @param player The player receiving the gold
     * @param amount The amount of gold to give
     */
    public void giveGold(Player player, int amount) {
        player.receiveGold(amount);
    }

    /**
     * Draws a specified number of district cards from the deck.
     *
     * @param amount The number of cards to draw
     * @return A list of drawn district cards
     */
    public List<Districts> drawDistricts(int amount) {
        List<Districts> districts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            districts.add(deck.remove(0)); // Take top card from deck
        }
        return districts;
    }

    /**
     * Draws district cards and adds them to a player's hand.
     *
     * @param player The player receiving the cards
     * @param amount The number of cards to draw
     */
    public void drawDistrictsForPlayer(Player player, int amount) {
        List<Districts> drawn = drawDistricts(amount);
        for (Districts district : drawn) {
            player.addCard(district);
        }
    }

    /**
     * Discards a district card back to the deck.
     *
     * @param district The district card to discard
     */
    public void discard(Districts district) {
        deck.add(district);
    }

    /**
     * Gets the character name by their rank.
     *
     * @param rank The rank of the character (1-8)
     * @return The name of the character
     */
    public String getCharacter(int rank) {
        return characterDeck.get(rank-1).toString();
    }

    /**
     * Prints information about a specific character.
     *
     * @param name The name of the character to print
     * @return true if the character was found and printed, false otherwise
     */
    public boolean printCharacter(String name) {
        for (Characters character : characterDeck) {
            if (character.getName().toLowerCase().equals(name.toLowerCase())) {
                character.print();
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the status of all players in the game.
     */
    public void printAll() {
        for (Player player : players) {
            player.printStatus();
        }
    }

    /**
     * Prints the built districts (citadel) of a specific player.
     *
     * @param playerIndex The index of the player (1-based)
     */
    public void printCitadel(int playerIndex) {
        if (playerIndex < 1 || playerIndex > numPlayers) {
            System.out.println("Invalid player index");
            return;
        }
        // Show player's built districts
        Player player = players[playerIndex - 1];
        System.out.println("Player " + playerIndex + "'s citadel:");
        List<Districts> districts = player.getCity();
        for (int i = 0; i < districts.size(); i++) {
            System.out.println((i + 1) + ": " + districts.get(i).toString());
        }
    }

    /**
     * Prints the action/ability of a specific character.
     *
     * @param characterIndex The index of the character
     */
    public void printAction(int characterIndex) {
        if (characterIndex < 0 || characterIndex >= characterDeck.size()) {
            System.out.println("Invalid character index");
            return;
        }
        Characters character = characterDeck.get(characterIndex);
        character.printAbility();
    }

    /**
     * Gets the total number of players in the game.
     *
     * @return The number of players
     */
    public int getPlayerCount() {
        return numPlayers;
    }

    /**
     * Adds points to a player's score.
     *
     * @param playerIndex The index of the player
     * @param points The number of points to add
     */
    public void countPoints(int playerIndex, int points) {
        this.points[playerIndex] += points;
    }

    /**
     * Marks a character as killed by the Assassin.
     *
     * @param target The rank of the character to kill
     */
    public void killCharacter(int target) {
        if (playerTurns[target] == null) {
            return;
        }
        playerTurns[target].killed();
    }

    /**
     * Checks if a character has been killed.
     *
     * @param target The rank of the character to check
     * @return true if the character is killed, false otherwise
     */
    public boolean isKilled(int target) {
        return playerTurns[target] != null && playerTurns[target].isKilled();
    }

    /**
     * Activates a character's special ability.
     *
     * @param player The player using the ability
     * @param character The character whose ability is being used
     * @param isHuman Whether the player is human or AI
     */
    public void useCharacterAbility(Player player, int character, boolean isHuman) {
        characterDeck.get(character).useAbility(this, player, isHuman); // Get the character from the deck and use their ability
    }

    /**
     * Sets the target for the Thief's ability.
     *
     * @param thief_target The rank of the character to steal from
     */
    public void setStolen(int thief_target) {
        this.thief_target = thief_target;
    }

    /**
     * Executes the Thief's stealing action.
     *
     * @param target The rank of the character to steal from
     */
    public void stealCharacter(int target) {
        if (playerTurns[target] == null) {
            return;
        }
        int gold = playerTurns[target].getGold();
        playerTurns[target].spendGold(gold);
        giveGold(playerTurns[1], gold);
        System.out.println("Player " + (playerTurns[target].getID()) + " was stolen " + gold + " gold.");
    }

    /**
     * Exchanges hands between two players (Magician's ability).
     *
     * @param player The player initiating the exchange
     * @param target The target player's index
     */
    public void exchangeHands(Player player, int target) {
        List<Districts> cards1 = players[target].getHand();
        List<Districts> cards2 = player.getHand();
        players[target].setHand(cards2);
        player.setHand(cards1);
    }

    /**
     * Handles AI discarding and drawing of cards.
     *
     * @param player The AI player
     * @return The number of cards discarded
     */
    public int aiDiscard(Player player) {
        int handSize = player.getHandsize(); // Get number of cards in hand
        int numDiscard = choosePlayer(0, handSize); // Choose number of cards to discard    
        int cardDiscarded = 0; // Variable to track card discarded
        for (int i = 0; i < numDiscard; i++) {
            cardDiscarded = random.nextInt(handSize); // Randomly choose a card to discard
            player.discard(cardDiscarded); // Discard the card
            handSize--; // Decrement hand size
        }
        drawDistrictsForPlayer(player, numDiscard); // Draw the same number of cards as discarded
        return numDiscard; // Return number of cards discarded
    }

    /**
     * Transfers the crown to a new player.
     *
     * @param player The player receiving the crown
     */
    public void giveCrown(Player player) {
        // Implementation will be added later
        nextCrown = player.getID()-1;
    }

    /**
     * Awards gold based on districts of a specific color.
     *
     * @param player The player receiving the gold
     * @param color The color of districts to count
     * @return The amount of gold awarded
     */
    public int giveDistrictGold(Player player, DistrictsColor color) {
        int gold = player.countDistrictColor(color);
        giveGold(player, gold);
        return gold;
    }

    /**
     * Randomly selects a district to destroy (for Warlord ability).
     *
     * @param target The target player's index
     * @return The index of the chosen district
     */
    public int chooseDistrict(int target) {
        Player player = players[target-1];
        int numDistricts = player.getDistrictCount();  // Get number of districts they have
        return random.nextInt(numDistricts) + 1; // Return a random district index
    }

    /**
     * Gets the name of a specific district owned by a player.
     *
     * @param playerIndex The index of the player
     * @param districtIndex The index of the district
     * @return The name of the district
     */
    public String getPlayerDistrictName(int playerIndex, int districtIndex) {
        Player player = players[playerIndex-1];
        Districts district = player.getDistrict(districtIndex-1);
        return district.getName();
    }

    /**
     * Displays districts that can be destroyed by the Warlord.
     *
     * @param playerIndex The index of the target player
     * @param showPrompt Whether to show the selection prompt
     * @return true if there are districts that can be destroyed, false otherwise
     */
    public boolean showDistricts(int playerIndex, boolean showPrompt) {
        Player target = players[playerIndex - 1];
        
        // Can't destroy if player has 8 or more districts or no districts
        if (target.getDistrictCount() >= 8 || target.getDistrictCount() == 0) {
            return false;
        }

        // If player is Bishop and alive, cannot destroy districts
        if (target.isBishop() && !target.isKilled()) {
            return false;
        }
        // Show available districts to destroy
        List<Districts> districts = target.getDistricts();
        if (showPrompt) {
            System.out.println("Choose a district to destroy or 0 to cancel:");
        }
        // List each district and its cost
        for (int i = 0; i < districts.size(); i++) {
            Districts d = districts.get(i);
            if (showPrompt) {
                System.out.println((i + 1) + ": " + d.getName() + " (Cost: " + (d.getCost() - 1) + " gold)");
            }
        }

        return true;
    }

    /**
     * Destroy a district (Warlord ability).
     *
     * @param player The player destroying the district
     * @param target The target player's index
     * @param districtIndex The index of the district to destroy
     * @return true if the district was successfully destroyed, false otherwise
     */
    public boolean destroyDistrict(Player player, int target, int districtIndex) {
        Player targetPlayer = players[target - 1];

        Districts district = targetPlayer.getDistrict(districtIndex - 1);
        if (!district.canBeDestroyed()) {
            return false;
        }

        int cost = district.getCost() - 1;  // Warlord pays one less
        
        // Check if player has enough gold to destroy the district
        if (player.getGold() < cost) {
            System.out.println("Not enough gold to destroy this district.");
            return false;
        }

        player.spendGold(cost); // Spend the gold
        targetPlayer.removeDistrict(this, districtIndex - 1);
        return true;
    }

    /**
     * Shuffles the district deck.
     */
    public void shuffleDistricts() {
        Collections.shuffle(deck, random);
    }

    /**
     * Shuffles the character deck and handles cards.
     */
    public void shuffleCharacters() {
        Collections.shuffle(availableCharacters, random);
        face_down = availableCharacters.remove(0);
        System.out.println("Face down: "+ face_down.getName());
        int number_card_discard = 8 - numPlayers - 2; // Calculate how many cards to discard
        if (number_card_discard < 0) {
            number_card_discard = 0;
        }
        // Discard cards (keeping King if drawn)
        Characters card_discard;
        for (int i = 0; i < number_card_discard; i++) {
            card_discard = availableCharacters.remove(0);
            if (card_discard.getName().equals("King")) {
                availableCharacters.add(card_discard);
                card_discard = availableCharacters.remove(0);
            }
            System.out.println("Discarded: " + card_discard.getName());
        }
    }

    /**
     * Adds a new player to the game and initializes their starting resources.
     *
     * @param player The player to add
     */
    public void addPlayer(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                break;
            }
        }
        giveGold(player, 2);
        drawDistrictsForPlayer(player, 4);
    }

    /**
     * Gets the list of available characters for selection.
     *
     * @return List of available characters
     */
    public List<Characters> getAvailableCharacters() {
        return availableCharacters;
    }

    /**
     * Sets the turn order for a player based on their character rank.
     *
     * @param rank The character rank
     * @param player The player to set
     */
    public void setPlayerTurn(int rank, Player player) {
        playerTurns[rank] = player;
    }

    /**
     * Activates a character's passive ability.
     *
     * @param character The character whose passive ability is being used
     * @param player The player using the ability
     * @param isHuman Whether the player is human or AI
     */
    public void usePassive(int character, Player player, boolean isHuman) {
        characterDeck.get(character).usePassive(this, player, isHuman);
    }

    /**
     * Activates a district's special ability.
     *
     * @param districtName The name of the district
     * @param player The player using the ability
     * @param isHuman Whether the player is human or AI
     */
    public void useDistrictAbility(String districtName, Player player, boolean isHuman) {
        List<Districts> districts = player.getCity();
        for (Districts district : districts) {
            if (district.getName().equalsIgnoreCase(districtName)) {
                district.useAbility(this, player, isHuman);
                return;
            }
        }
        System.out.println("District not found or no ability available");
    }

    /**
     * Debug method to print all players' characters.
     */
    public void debugCharacter() {
        System.out.println("_____________________________________________________________________");
        System.out.println("Debugging character for Players: ");
        for (Player player : players) {
            int characterNum = player.getCharacter();
            String characterName = characterNum == -1 ? "Null" : getCharacter(characterNum + 1);
            System.out.println("Player " + (player.getID()) + " is " + characterName);
        }
        System.out.println("_____________________________________________________________________");
    }

    /**
     * Debug method to print all players' hands.
     */
    public void debugHand() {
        System.out.println("_____________________________________________________________________");
        System.out.println("Debugging hand for Players: ");
        for (Player player : players) {
            System.out.println("Player " + player.getID() + ": ");
            player.printHand();
            System.out.println();
        }
        System.out.println("_____________________________________________________________________");
    }

    /**
     * Adds a player to the list of Throne Room owners.
     *
     * @param player The player who owns a Throne Room
     */
    public void addThroneRoomOwner(Player player) {
        if (!throneRoomOwners.contains(player)) {
            throneRoomOwners.add(player);
        }
    }

    /**
     * Removes a player from the list of Throne Room owners.
     *
     * @param player The player to remove
     */
    public void removeThroneRoomOwner(Player player) {
        throneRoomOwners.remove(player);
    }
}