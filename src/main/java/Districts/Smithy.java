package Districts;

import citadels.Game;
import Players.Player;
import java.util.List;

public class Smithy extends Districts {
    public Smithy() {
        super("Smithy", DistrictsColor.PURPLE, 5,
              "Once during your turn, you may pay two gold to draw 3 district cards.");
        this.numAbilities = 1;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (numAbilities == 0) {
            if (isHuman) {
                System.out.println("Smithy ability already used this turn");
            }
            return false;
        }

        if (player.getGold() < 2) {
            if (isHuman) {
                System.out.println("Not enough gold to use Smithy (need 2 gold)");
            }
            return false;
        }

        if (isHuman) {
            System.out.println("Pay 2 gold to draw 3 cards? [y/n]");
            while (true) {
                String input = game.scanner.nextLine().toLowerCase();
                if (input.equals("y")) {
                    break;
                }
                if (input.equals("n")) {
                    return false;
                }
                System.out.println("Invalid input");
            }
        }

        // Pay gold and draw cards
        player.spendGold(2);
        List<Districts> drawCards = game.drawDistricts(3);
        for (Districts card : drawCards) {
            player.addCard(card);
        }
        numAbilities--;

        if (isHuman) {
            System.out.println("You paid 2 gold and drew 3 cards");
            return true;
        }
        System.out.println("Player " + player.getID() + " paid 2 gold and drew 3 cards");
        return true;
    }

    @Override
    public void resetAbility() {
        numAbilities = 1;
    }
} 