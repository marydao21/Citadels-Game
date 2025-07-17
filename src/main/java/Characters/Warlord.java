package Characters;

import citadels.Game;
import Players.Player;
import Districts.DistrictsColor;

/**
 * The Warlord character in Citadels (Rank 8).
 * Can destroy other players' districts by paying one less gold than their cost.
 * Cannot destroy districts in a city with 8 or more districts.
 * Gains one gold for each military (red) district in their city.
 */
public class Warlord extends Characters {
    /** Maximum number of attempts to find a valid target for district destruction */
    private static final int TIME_LIMIT = 20;

    /**
     * Constructs a new Warlord character.
     * Initializes it with rank 8 and its special ability description.
     */
    public Warlord() {
        super(CharacterName.WARLORD, 8);
        ability = "Gains one gold for each red (military) district in their city. You can destroy one district \n" +
                "of your choice by paying one fewer gold than its building cost. You cannot destroy a district in a \n" +
                "city with 8 or more districts. ";
    }

    /**
     * Implements the Warlord's ability for AI players.
     * Randomly chooses a player's district to destroy.
     *
     * @param game The current game instance
     * @param player The AI player using the ability
     * @return true if a district was destroyed or no valid targets found, false if timed out
     */
    @Override
    public boolean useAbilityAI(Game game, Player player) {
        // Choose a player's district to destroy
        int target;
        int limit = 0;
        while (true) {
            target = game.choosePlayer(1, game.getPlayerCount());
            if (game.showDistricts(target, false)) {
                break;
            }
            limit++;
            if (limit >= TIME_LIMIT) {
                message = "Player " + player.getID() + " did not destroy a district.";
                return false;
            }
        }
        int district = game.chooseDistrict(target);
        String districtName = game.getPlayerDistrictName(target, district);
        if (game.destroyDistrict(player, target, district)) {
            message = "Player " + player.getID() + " destroyed the district " + districtName + " of player " + target + ".";
        } else {
            message = "Player " + player.getID() + " did not destroy a district.";
        }
        return true;
    }

    /**
     * Implements the Warlord's ability for human players.
     * Prompts the player to choose a district to destroy.
     *
     * @param game The current game instance
     * @param player The human player using the ability
     * @return true if the ability was used successfully
     */
    @Override
    public boolean useAbilityHuman(Game game, Player player) {
        // Choose a player's district to destroy
        System.out.println("Choose a player (1-" + game.getPlayerCount() + ") or 0 to cancel:");
        while (true) {
            try {
                int target = Integer.parseInt(game.scanner.nextLine());
                if (target == 0) {
                    message = "You decided not to destroy a district.";
                    return false;
                }
                if (target >= 1 && target <= game.getPlayerCount()) {
                    if (game.showDistricts(target, true)) {
                        String input = game.scanner.nextLine();
                        while (true) {
                            try {
                                int district = Integer.parseInt(input);
                                if (district == 0) {
                                    System.out.println("Choose a player (1-" + game.getPlayerCount() + ") or 0 to cancel:");
                                    continue;
                                }
                                if (district >= 1 && district <= game.getPlayerCount()) {
                                    String districtName = game.getPlayerDistrictName(target, district);
                                    if (game.destroyDistrict(player, target, district)) {
                                        message = "You destroyed the district " + districtName + " of player " + target + ".";
                                        return true;
                                    }
                                    System.out.println("The " + districtName + " cannot be destroyed. Try again or enter 0 to choose another player:");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid choice. Try again or enter 0 to choose another player:");
                            }
                        }
                    } else {
                        System.out.println("Player " + target + " has no destroyable district. Try again or enter 0 to cancel:");
                    }
                } else {
                    System.out.println("Invalid player number. Try again or enter 0 to cancel:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again or enter 0 to cancel:");
            }
        }
    }

    /**
     * Implements the Warlord's passive income ability.
     * Awards one gold for each red district in the player's city.
     *
     * @param game The current game instance
     * @param player The player receiving gold
     * @param isHuman true if the player is human, false if AI
     */
    @Override
    public void usePassive(Game game, Player player, boolean isHuman) {
        int gold = game.giveDistrictGold(player, DistrictsColor.RED);
        if (isHuman) {
            System.out.println("You received " + gold + " gold for your military districts.");
        } else {
            System.out.println("Player " + player.getID() + " received " + gold + " gold for their military districts.");
        }
    }
}
