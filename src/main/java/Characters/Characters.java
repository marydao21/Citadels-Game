package Characters;
import citadels.Game;
import Players.Player;

/**
 * Abstract base class for all character cards in Citadels.
 * Each character has a unique rank (1-8) and special abilities that can be used during the game.
 * Characters provide players with both active abilities (that must be triggered) and passive abilities
 * (that are always in effect during their turn).
 */
public abstract class Characters { // you can't create a character directly from this class
    protected CharacterName name;
    protected final int rank;
    protected boolean isTurn;
    protected boolean isAlive;
    protected String message;
    protected String ability;

    /**
     * Constructs a new character card.
     * @param name The character's name from the CharacterName enum
     * @param rank The character's rank (1-8), determines turn order
     */
    public Characters(CharacterName name, int rank) {
        this.name = name;
        this.rank = rank;
        this.isTurn = false;
        this.isAlive = true;
        this.message = "";
        this.ability = "";
    }

    /**
     * Returns a string representation of the character.
     * @return The character's name as a string
     */
    public String toString() {
        return getName();
    }

    /**
     * Gets the character's name.
     * @return The character's name as a string
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Gets the character's rank.
     * @return The rank number (1-8)
     */
    public int getRank() {
        return rank;
    }

    /**
     * Helper method for AI to choose a character during selection phase.
     * @param game The current game instance
     * @param min Minimum rank to choose from
     * @param max Maximum rank to choose from
     * @return The chosen character rank
     */
    public int chooseCharacter(Game game, int min, int max) {
        return game.random.nextInt((max - min) + 1) + min;
    }

    /**
     * Uses the character's special ability based on whether the player is human or AI.
     * Also handles displaying any messages resulting from the ability use.
     * 
     * @param game The current game instance
     * @param player The player using the ability
     * @param isHuman true if the player is human, false if AI
     */
    public void useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            useAbilityHuman(game, player);
        } else {
            useAbilityAI(game, player);
        }
        if (!message.isEmpty()) {
            System.out.println(message);
            message = "";
        }
    }

    /**
     * Uses the character's passive ability that is always in effect during their turn.
     * Default implementation does nothing, subclasses can override.
     * 
     * @param game The current game instance
     * @param player The player with the passive ability
     * @param isHuman true if the player is human, false if AI
     */
    public void usePassive(Game game, Player player, boolean isHuman) {
        // Default implementation, can be overridden by subclasses
    }

    /**
     * Prints detailed information about the character including rank and ability.
     */
    public void print() {
        System.out.println(name + " (Rank " + rank + ")");
        System.out.println("Ability: " + ability);
    }

    /**
     * Prints just the character's ability description.
     */
    public void printAbility() {
        System.out.println(getName() + ": " + ability);
    }

    /**
     * Implements the character's ability logic for AI players.
     * Must be implemented by each character class.
     * 
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true if the ability was used successfully
     */
    public abstract boolean useAbilityAI(Game game, Player player);

    /**
     * Implements the character's ability logic for human players.
     * Must be implemented by each character class.
     * 
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true if the ability was used successfully
     */
    public abstract boolean useAbilityHuman(Game game, Player player);
}
