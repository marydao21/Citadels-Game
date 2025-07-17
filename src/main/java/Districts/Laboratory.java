package Districts;

import citadels.Game;
import Players.Player;

public class Laboratory extends Districts {

    public Laboratory() {
        super("Laboratory", DistrictsColor.PURPLE, 5, 
              "Once during your turn, you may discard a district card from your hand and receive one gold from the bank");
        this.numAbilities = 1;
    }

    // Use the laboratory's ability to discard a card for gold
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (numAbilities == 0) {
            if (isHuman) {
                System.out.println("Laboratory ability already used this turn");
            }
            return false;
        }

        if (player.getHandsize() == 0) {
            if (isHuman) {
                System.out.println("No cards in hand to discard");
            }
            return false;
        }

        if (isHuman) {
            System.out.println("Choose a card to discard [1-" + player.getHandsize() + "], or 0 to cancel:");
            player.printHand();
            
            try {
                int choice = Integer.parseInt(game.scanner.nextLine());
                if (choice == 0) {
                    return false;
                }
                if (choice < 1 || choice > player.getHandsize()) {
                    System.out.println("Invalid choice");
                    return false;
                }
                player.removeCard(choice - 1);
                player.receiveGold(1);
                numAbilities--;
                System.out.println("Discarded card and received 1 gold");
                return true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                return false;
            }
        } else {
            // AI always uses ability if possible
            player.removeCard(0);  // Remove first card
            player.receiveGold(1);
            numAbilities--;
            return true;
        }
    }

    // Reset ability usage for next turn
    public void resetAbility() {
        numAbilities = 1;
    }
}
