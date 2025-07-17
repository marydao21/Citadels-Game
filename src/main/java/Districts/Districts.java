package Districts;

import citadels.Game;
import Players.Player;

/**
 * Base class for all district cards in the Citadels game.
 * Provides common functionality and properties for district cards.
 * Districts are the building blocks that players use to construct their city.
 */
public class Districts {
    private final String name;
    private final DistrictsColor color;
    private final int cost;
    private final String ability;
    protected int numAbilities;  // Number of times ability can be used per turn
    private final int points;    // Victory points awarded by this district

    /**
     * Constructs a new district card.
     * @param name The name of the district
     * @param color The color category of the district
     * @param cost The gold cost to build this district
     * @param ability Description of the district's special ability (if any)
     */
    public Districts(String name, DistrictsColor color, int cost, String ability) {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.ability = ability;
        this.numAbilities = 0;
        
        // Special cases for points
        if (name.equals("Dragon Gate") || name.equals("University")) {
            this.points = 8;
        } else {
            this.points = cost;
        }
    }

    /**
     * Gets the name of the district.
     * @return The district's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the building cost of the district.
     * @return The gold cost to build this district
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the color category of the district.
     * @return The district's color
     */
    public DistrictsColor getColor() {
        return color;
    }

    /**
     * Gets the description of the district's special ability.
     * @return The ability description, or null if no special ability
     */
    public String getAbility() {
        return ability;
    }

    /**
     * Calculates the victory points awarded by this district.
     * @param player The player who owns this district
     * @return The number of victory points
     */
    public int getPoints(Player player) {
        return points;
    }

    /**
     * Checks if this district can be destroyed by the Warlord's ability.
     * @return true if the district can be destroyed, false if protected
     */
    public boolean canBeDestroyed() {
        return !name.equals("Keep");
    }

    /**
     * Checks if this district has a special ability that can be used.
     * @return true if the district has an available special ability
     */
    public boolean hasSpecialAbility() {
        return color == DistrictsColor.PURPLE && ability != null && !ability.isEmpty() && numAbilities > 0;
    }

    /**
     * Checks if this district can be duplicated in a player's city.
     * @return true if the district can be duplicated, false otherwise
     */
    public boolean canBeDuplicated() {
        return false;
    }

    /**
     * Returns a string representation of the district card.
     * @return A string in the format "name [color, cost]"
     */
    @Override
    public String toString() {
        return name + " [" + color.toString() + ", " + cost + "]";
    }

    /**
     * Returns a string representation including points information.
     * @return A string in the format "name [color, points]"
     */
    public String infoPoints() {
        return name + " [" + color.toString() + ", " + points + "]";
    }

    /**
     * Returns a string describing the district's ability.
     * @return A string in the format "name: ability"
     */
    public String printAbility() {
        return name + ": " + ability;
    }

    /**
     * Attempts to use this district's special ability.
     * @param game The current game instance
     * @param player The player attempting to use the ability
     * @param isHuman true if the player is human, false if AI
     * @return true if the ability was used successfully
     */
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        return false;
    }

    /**
     * Resets the number of times this district's ability can be used.
     * Called at the end of each turn.
     */
    public void resetAbility() {
        numAbilities = 0;
    }

    /**
     * Sets the owner of this district card.
     * Base implementation does nothing, override in specific districts that need owner reference.
     * @param player The player who owns this district
     */
    public void setOwner(Player player) {
        // Base implementation does nothing, override in specific districts that need owner reference
    }
}
