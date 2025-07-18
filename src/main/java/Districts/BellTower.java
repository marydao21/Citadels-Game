package Districts;

import citadels.Game;
import Players.Player;

public class BellTower extends Districts {
    private boolean gameEndAnnounced;  // Whether the game end has been announced
    private boolean canAnnounceEnd;    // Whether player can announce game end

    public BellTower() {
        super("Bell Tower", DistrictsColor.PURPLE, 5,
              "When you place the Bell Tower in your city, you may announce that the game will end after the round in which a player first places his 7th district. You may do this even if the Bell Tower is your 7th district. If the Bell Tower is later destroyed, the game end conditions return to normal");
        this.gameEndAnnounced = false;
        this.canAnnounceEnd = true;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (!canAnnounceEnd) {
            if (isHuman) {
                System.out.println("Bell Tower ability already used");
            }
            return false;
        }

        if (isHuman) {
            System.out.println("Bell Tower ability: You may announce that the game will end after someone reaches 7 districts");
            System.out.println("Do you want to announce the game end? (y/n)");
            
            while (true) {
                String input = game.scanner.nextLine().toLowerCase();
                if (input.equals("y") || input.equals("yes")) {
                    gameEndAnnounced = true;
                    canAnnounceEnd = false;
                    System.out.println("Game will now end after a player reaches 7 districts");
                    return true;
                } else if (input.equals("n") || input.equals("no")) {
                    System.out.println("You chose not to announce the game end");
                    canAnnounceEnd = false;
                    return false;
                } else {
                    System.out.println("Please enter 'y' or 'n'");
                }
            }
        } else {
            // AI behavior: always announce if possible
            gameEndAnnounced = true;
            canAnnounceEnd = false;
            System.out.println("Player " + player.getID() + " announced game will end after 7 districts");
            return true;
        }
    }

    @Override
    public void resetAbility() {
        super.resetAbility();
        canAnnounceEnd = false;  // Can only be used once when placed
    }

    public boolean isGameEndAnnounced() {
        return gameEndAnnounced;
    }

    public void setGameEndAnnounced(boolean announced) {
        this.gameEndAnnounced = announced;
    }
} 