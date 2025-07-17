package Districts;

import citadels.Game;
import Players.Player;

public class Museum extends Districts {
    private int cardsUnder;  // Number of cards placed under the Museum

    public Museum() {
        super("Museum", DistrictsColor.PURPLE, 4,
              "On your turn, you may place a district card from your hand face down under the Museum. At the end of the game, you score one extra point for every card under the Museum");
        this.numAbilities = 1;  // Can use once per turn
        this.cardsUnder = 0;    // Start with no cards under
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (numAbilities == 0) {
            if (isHuman) {
                System.out.println("Museum ability already used this turn");
            }
            return false;
        }

        // Check if player has any cards in hand
        if (player.getHandsize() == 0) {
            if (isHuman) {
                System.out.println("No cards in hand to place under Museum");
            }
            return false;
        }

        if (isHuman) {
            // Show player's hand
            System.out.println("Your hand:");
            player.printHand();
            System.out.println("\nChoose a card to place under Museum (1-" + player.getHandsize() + ") or 0 to cancel:");
            
            while (true) {
                try {
                    String input = game.scanner.nextLine();
                    int choice = Integer.parseInt(input);
                    
                    if (choice == 0) {
                        return false;
                    }
                    
                    if (choice > 0 && choice <= player.getHandsize()) {
                        // Remove the chosen card and increment cards under Museum
                        player.discard(choice - 1);
                        cardsUnder++;
                        numAbilities--;
                        System.out.println("Card placed under Museum. Museum now has " + cardsUnder + " cards.");
                        return true;
                    }
                    
                    System.out.println("Invalid choice. Please choose 0-" + player.getHandsize());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        } else {
            // AI behavior: always use if possible, choose first card
            player.discard(0);
            cardsUnder++;
            numAbilities--;
            System.out.println("Player " + player.getID() + " placed a card under Museum. Museum now has " + cardsUnder + " cards.");
            return true;
        }
    }

    @Override
    public void resetAbility() {
        numAbilities = 1;
    }

    @Override
    public int getPoints(Player player) {
        // Base points (4) plus one point for each card under the Museum
        return super.getPoints(player) + cardsUnder;
    }
} 