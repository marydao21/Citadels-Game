package Districts;

import citadels.Game;
import Players.Player;

public class Graveyard extends Districts {
    private Districts destroyedDistrict;  // District that was destroyed by Warlord
    private boolean canTakeDestroyed;     // Whether player can take the destroyed district

    public Graveyard() {
        super("Graveyard", DistrictsColor.PURPLE, 5,
              "When the Warlord destroys a district, you may pay one gold to take the destroyed district into your hand. You may not do this if you are the Warlord");
        this.destroyedDistrict = null;
        this.canTakeDestroyed = false;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (!canTakeDestroyed || destroyedDistrict == null) {
            if (isHuman) {
                System.out.println("No district was destroyed by Warlord this turn");
            }
            return false;
        }

        if (player.getGold() < 1) {
            if (isHuman) {
                System.out.println("Not enough gold to take the destroyed district");
            }
            return false;
        }

        if (isHuman) {
            System.out.println("A district was destroyed by Warlord: " + destroyedDistrict.getName());
            System.out.println("Pay 1 gold to take it into your hand? (y/n)");
            
            while (true) {
                String input = game.scanner.nextLine().toLowerCase();
                if (input.equals("y") || input.equals("yes")) {
                    player.spendGold(1);
                    player.addCard(destroyedDistrict);
                    System.out.println("You paid 1 gold and took " + destroyedDistrict.getName() + " into your hand");
                    canTakeDestroyed = false;
                    destroyedDistrict = null;
                    return true;
                } else if (input.equals("n") || input.equals("no")) {
                    System.out.println("You chose not to take the destroyed district");
                    canTakeDestroyed = false;
                    destroyedDistrict = null;
                    return false;
                } else {
                    System.out.println("Please enter 'y' or 'n'");
                }
            }
        } else {
            // AI behavior: always take if possible
            player.spendGold(1);
            player.addCard(destroyedDistrict);
            System.out.println("Player " + player.getID() + " paid 1 gold and took " + destroyedDistrict.getName() + " into hand");
            canTakeDestroyed = false;
            destroyedDistrict = null;
            return true;
        }
    }

    public void setDestroyedDistrict(Districts district) {
        this.destroyedDistrict = district;
        this.canTakeDestroyed = true;
    }

    public void resetAbility() {
        super.resetAbility();
        this.canTakeDestroyed = false;
        this.destroyedDistrict = null;
    }
} 