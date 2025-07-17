package Players;

import citadels.Game;
import Districts.Districts;
import Districts.DistrictsColor;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all players in Citadels.
 * Manages a player's hand of cards, city districts, gold, and game state.
 * Provides common functionality for both human and AI players.
 */
public abstract class Player {
    protected List<Districts> hand;      // List to store district cards
    protected List<Districts> city;      // Districts in each player's city
    protected int gold;                  // Amount of player's gold
    protected boolean killed;            // If player is killed
    protected boolean isPlayer;          // AI or Human
    protected final int id;              // Player ID
    protected boolean isBishop;          // If player is Bishop
    protected int character;             // Character ID
    protected boolean usedCharacter;     // If player has used their character ability
    protected boolean gC;                // If player has used their gold or cards ability
    protected int maxDistricts;          // Maximum number of districts a player can build
    protected int maxCardDrawn;          // Maximum number of cards a player can draw
    protected boolean gameEnded;         // If game has ended
    protected boolean isTurn;            // If it's this player's turn
    protected int numPoorHouse;          // Count of Poor House districts owned
    protected int numPark;               // Count of Park districts owned

    /**
     * Constructs a new player with initial game state.
     * @param isPlayer true if human player, false if AI
     * @param id The player's unique identifier
     */
    public Player(boolean isPlayer, int id) {
        hand = new ArrayList<>();
        city = new ArrayList<>();
        gold = 0;
        killed = false;
        this.isPlayer = isPlayer;
        this.id = id;
        this.isBishop = false;
        this.character = -1;
        this.usedCharacter = false;
        this.gC = false;
        this.maxDistricts = 1;
        this.maxCardDrawn = 2;
        this.gameEnded = false;
        this.isTurn = false;
        this.numPoorHouse = 0;
        this.numPark = 0;
    }

    /**
     * Prints the player's current game status.
     * Shows player ID, number of cards, gold, and built districts.
     */
    public void printStatus() {
        System.out.print("Player " + id + (isPlayer ? " (You): " : ": "));
        System.out.print("card=" + hand.size() + " ");
        System.out.print("gold=" + gold + " ");
        System.out.print("city=");
        for (Districts district : city) {
            System.out.print(district.infoPoints() + " ");
        }
        System.out.println();
    }

    /**
     * Abstract method for playing a turn.
     * Must be implemented differently for human and AI players.
     * @param game The current game instance
     */
    public abstract void playTurn(Game game);

    /**
     * Handles end of turn cleanup and special district abilities.
     * Resets player state and triggers Poor House and Park abilities if applicable.
     * @param game The current game instance
     */
    public void endTurn(Game game) {
        usedCharacter = false;
        gC = false;
        isBishop = false;
        maxDistricts = 1;
        character = -1;
        killed = false;
        isTurn = false;

        // Check Poor House ability
        if (numPoorHouse > 0 && gold == 0) {
            game.giveGold(this, 1);
            System.out.println("Player " + id + " received 1 gold from Poor House");
        }

        // Check Park ability
        if (numPark > 0 && hand.size() == 0) {
            game.drawDistrictsForPlayer(this, 2);
            System.out.println("Player " + id + " drew 2 cards from Park");
        }

        for (Districts district : city) {
            district.resetAbility();
        }
    }

    /**
     * Uses the current character's ability.
     * @param game The current game instance
     */
    public void useCharacterAbility(Game game) {
        game.useCharacterAbility(this, character, true);
        usedCharacter = true;
    }

    /**
     * Sets the player's character for the current round.
     * @param character The character's rank (0-7)
     */
    public void setCharacter(int character) {
        this.character = character;
    }

    /**
     * Gets the player's current character.
     * @return The character's rank, or -1 if no character selected
     */
    public int getCharacter() {
        return character;
    }
    
    /**
     * Adds a district card to the player's hand.
     * @param card The district card to add
     */
    public void addCard(Districts card) {
        hand.add(card);
    }

    /**
     * Removes and returns a card from the player's hand.
     * @param index The index of the card to remove
     * @return The removed district card
     */
    public Districts removeCard(int index) {
        return hand.remove(index);
    }

    /**
     * Discards a card from hand back to the game deck.
     * @param index The index of the card to discard
     * @param game The current game instance
     */
    public void discardCard(int index, Game game) {
        game.discard(removeCard(index));
    }

    /**
     * Gets the player's current hand of cards.
     * @return List of district cards in hand
     */
    public List<Districts> getHand() {
        return hand;
    }

    /**
     * Prints all cards in the player's hand with indices.
     */
    public void printHand() {
        int i = 1;
        for (Districts district : hand) {
            System.out.print(i + ". ");
            System.out.println(district.toString());
            i++;
        }
    }

    /**
     * Gets the number of cards in hand.
     * @return The hand size
     */
    public int getHandsize() {
        return hand.size();
    }

    /**
     * Sets the player's hand to a new list of cards.
     * Used for card exchange effects.
     * @param newHand The new list of district cards
     */
    public void setHand(List<Districts> newHand) {
        hand = newHand;
    }

    /**
     * Attempts to build a district from hand.
     * Checks for sufficient gold and duplicate districts.
     * 
     * @param game The current game instance
     * @param index The index of the district in hand to build
     * @param showPrompt Whether to show error messages
     * @return true if the district was built successfully
     */
    public boolean buildDistrict(Game game, int index, boolean showPrompt) {
        Districts district = hand.get(index);
        for (Districts d : city) {
            if (d.getName().equals(district.getName())) {
                if (showPrompt) {
                    System.out.println("You already have " + district.getName());
                }
                return false;
            }
        }
        if (gold >= district.getCost()) {
            city.add(district);
            district.setOwner(this);  // Set the owner when building
            gold -= district.getCost();
            hand.remove(index);
            maxDistricts--;
            
            // Check for special districts
            String districtName = district.getName();
            if (districtName.equals("Observatory")) {
                maxCardDrawn = 3;
            } else if (districtName.equals("Throne Room")) {
                game.addThroneRoomOwner(this);
            } else if (districtName.equals("Poor House")) {
                numPoorHouse++;
            } else if (districtName.equals("Park")) {
                numPark++;
            }
            
            if (city.size() == 8) {
                game.setFirstEight(id-1);
            }
            return true;
        }
        if (showPrompt) {
            System.out.println("Not enough gold to build " + district.getName());
        }
        return false;
    }

    /**
     * Gets the list of districts in the player's city.
     * @return List of built districts
     */
    public List<Districts> getCity() {
        return city;
    }

    /**
     * Gets a specific district from the city.
     * @param index The index of the district
     * @return The district at the specified index
     */
    public Districts getDistrict(int index) {
        return city.get(index);
    }

    /**
     * Gets the number of districts in the city.
     * @return The number of built districts
     */
    public int getDistrictCount() {
        return city.size();
    }

    /**
     * Counts districts of a specific color in the city.
     * Used for character abilities and scoring.
     * @param color The color to count
     * @return Number of districts of that color
     */
    public int countDistrictColor(DistrictsColor color) {
        int count = 0;
        for (Districts district : city) {
            if (district.getColor() == color) {
                count++;
            }
        }
        return count;
    }

    /**
     * Displays all districts in the city.
     */
    public void displayCity() {
        for (Districts district : city) {
            System.out.println(district.infoPoints());
        }
    }

    /**
     * Removes a district from the city.
     * Handles special district cleanup.
     * @param game The current game instance
     * @param index The index of the district to remove
     */
    public void removeDistrict(Game game, int index) {
        Districts district = city.get(index);
        String districtName = district.getName();
        
        if (districtName.equals("Throne Room")) {
            game.removeThroneRoomOwner(this);
        } else if (districtName.equals("Poor House")) {
            numPoorHouse--;
        } else if (districtName.equals("Park")) {
            numPark--;
        }
        
        city.remove(index);
    }

    /**
     * Adds gold to the player's treasury.
     * @param amount The amount of gold to add
     */
    public void receiveGold(int amount) {
        gold += amount;
    }

    /**
     * Removes gold from the player's treasury.
     * @param amount The amount of gold to spend
     */
    public void spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
        }
    }

    /**
     * Gets the player's current gold amount.
     * @return The amount of gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * Marks the player as killed by the Assassin.
     */
    public void killed() {
        if (!killed) {
            killed = true;
        }
    }

    /**
     * Checks if the player was killed.
     * @return true if player was killed
     */
    public boolean isKilled() {
        return killed;
    }

    /**
     * Sets the player as the Bishop.
     */
    public void setBishop() {
        isBishop = true;
    }

    /**
     * Checks if the player is the Bishop.
     * @return true if player is the Bishop
     */
    public boolean isBishop() {
        return isBishop;
    }

    /**
     * Checks if this is a human player.
     * @return true if human, false if AI
     */
    public boolean isHuman() {
        return isPlayer;
    }

    /**
     * Gets the player's ID number.
     * @return The player ID
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the maximum number of districts that can be built this turn.
     * @param maxDistricts The new maximum
     */
    public void setMaxDistricts(int maxDistricts) {
        this.maxDistricts = maxDistricts;
    }

    /**
     * Prints the player's current gold amount.
     */
    public void printGold() {
        System.out.println("Gold: " + gold);
    }

    /**
     * Abstract method for character selection.
     * Must be implemented differently for human and AI players.
     * @param game The current game instance
     */
    public abstract void selectCharacter(Game game);

    /**
     * Calculates the player's total victory points.
     * Includes district points, color bonuses, and completion bonus.
     * @return The total points
     */
    public int countPoints() {
        int points = 0;
        int yellow = 0;
        int blue = 0;
        int green = 0;
        int red = 0;
        int purple = 0;
        for (Districts district : city) {
            points += district.getPoints(this);
            if (district.getColor() == DistrictsColor.YELLOW) {
                yellow++;
            } else if (district.getColor() == DistrictsColor.BLUE) {
                blue++;
            } else if (district.getColor() == DistrictsColor.GREEN) {
                green++;
            } else if (district.getColor() == DistrictsColor.RED) {
                red++;
            } else if (district.getColor() == DistrictsColor.PURPLE) {
                purple++;
            }
        }
        if (yellow > 0 && blue > 0 && green > 0 && red > 0 && purple > 0) {
            points += 3;
        }
        if (city.size() >= 8) {
            points += 2;
        }
        return points;
    }

    /**
     * Gets all districts in the city.
     * @return List of built districts
     */
    public List<Districts> getDistricts() {
        return city;
    }

    /**
     * Sets the player's districts.
     * @param districts The new list of districts
     */
    public void setDistricts(List<Districts> districts) {
        this.hand = districts;
    }

    /**
     * Discards a card from hand.
     * @param index The index of the card to discard
     */
    public void discard(int index) {
        hand.remove(index);
    }

    /**
     * Sets this player's turn as active.
     */
    public void setTurn() {
        isTurn = true;
    }
}
