package Districts;

import citadels.Game;
import Players.Player;

public class SchoolOfMagic extends Districts {
    private DistrictsColor chosenColor;  // Color chosen for income purposes

    public SchoolOfMagic() {
        super("School Of Magic", DistrictsColor.PURPLE, 6,
              "For the purposes of income, the School Of Magic is considered to be the color of your choice. If you are the King this round, for example, the School is considered to be a noble (yellow) district");
        this.chosenColor = null;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (isHuman) {
            System.out.println("Choose a color for School of Magic income:");
            System.out.println("1. Red (Military)");
            System.out.println("2. Yellow (Noble)");
            System.out.println("3. Green (Trade)");
            System.out.println("4. Blue (Religious)");
            System.out.println("5. Purple (Special)");
            System.out.println("0. Cancel");

            while (true) {
                try {
                    String input = game.scanner.nextLine();
                    int choice = Integer.parseInt(input);

                    if (choice == 0) {
                        return false;
                    }

                    if (choice >= 1 && choice <= 5) {
                        if (choice == 1) {
                            chosenColor = DistrictsColor.RED;
                        } else if (choice == 2) {
                            chosenColor = DistrictsColor.YELLOW;
                        } else if (choice == 3) {
                            chosenColor = DistrictsColor.GREEN;
                        } else if (choice == 4) {
                            chosenColor = DistrictsColor.BLUE;
                        } else if (choice == 5) {
                            chosenColor = DistrictsColor.PURPLE;
                        }
                        System.out.println("School of Magic will be considered " + chosenColor + " for income");
                        return true;
                    }

                    System.out.println("Invalid choice. Please choose 0-5");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        } else {
            // AI behavior: choose randomly
            DistrictsColor[] colors = {DistrictsColor.RED, DistrictsColor.YELLOW, DistrictsColor.GREEN, DistrictsColor.BLUE, DistrictsColor.PURPLE};
            chosenColor = colors[game.random.nextInt(colors.length)];
            System.out.println("Player " + player.getID() + " chose " + chosenColor + " for School of Magic income");
            return true;
        }
    }

    @Override
    public DistrictsColor getColor() {
        if (chosenColor != null) {
            return chosenColor;  // Return chosen color for income purposes
        }
        return super.getColor();  // Return purple if no color chosen
    }

    public void resetChosenColor() {
        this.chosenColor = null;
    }
} 