package Characters;

/**
 * Enumeration of all character roles in Citadels.
 * Each character has a display name that is shown to players.
 * The order of declaration matches the character ranks (1-8).
 */
public enum CharacterName {
    /** Rank 1: Can assassinate another character, preventing their turn */
    ASSASSIN("Assassin"),
    
    /** Rank 2: Can steal gold from another character */
    THIEF("Thief"),
    
    /** Rank 3: Can swap hands or exchange cards with the deck */
    MAGICIAN("Magician"),
    
    /** Rank 4: Gains gold from noble districts and receives the crown */
    KING("King"),
    
    /** Rank 5: Gains gold from religious districts and protects districts */
    BISHOP("Bishop"),
    
    /** Rank 6: Gains gold from trade districts and receives extra gold */
    MERCHANT("Merchant"),
    
    /** Rank 7: Draws extra cards and can build up to three districts */
    ARCHITECT("Architect"),
    
    /** Rank 8: Gains gold from military districts and can destroy districts */
    WARLORD("Warlord");

    private final String displayName;

    /**
     * Constructs a character name with its display text.
     * @param displayName The name shown to players
     */
    CharacterName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the character.
     * @return The character's name as shown to players
     */
    @Override
    public String toString() {
        return displayName;
    }
}
