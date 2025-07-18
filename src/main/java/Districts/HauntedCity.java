package Districts;

import citadels.Game;
import Players.Player;
import java.util.List;

public class HauntedCity extends Districts {
    private boolean builtLastRound;  // Track if built in last round
    private DistrictsColor chosenColor;  // Color chosen for victory points

    public HauntedCity() {
        super("Haunted City", DistrictsColor.PURPLE, 2,
              "For the purposes of victory points, the Haunted City is considered to be of the color of your choice. You cannot use this ability if you built it during the last round of the game");
        this.builtLastRound = false;
        this.chosenColor = null;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (builtLastRound) {
            if (isHuman) {
                System.out.println("Haunted City ability cannot be used if built in the last round");
            }
            return false;
        }

        if (isHuman) {
            System.out.println("Choose a color for Haunted City victory points:");
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
                        System.out.println("Haunted City will be considered " + chosenColor + " for victory points");
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
            System.out.println("Player " + player.getID() + " chose " + chosenColor + " for Haunted City");
            return true;
        }
    }

    @Override
    public int getPoints(Player player) {
        if (chosenColor != null && !builtLastRound) {
            // Count districts of the chosen color (including this Haunted City)
            int colorCount = 0;
            List<Districts> city = player.getCity();
            for (Districts district : city) {
                if (district.getColor() == chosenColor) {
                    colorCount++;
                }
            }
            return colorCount;  // Return points based on chosen color count
        }
        return super.getPoints(player);  // Return base points (2) if no color chosen
    }

    public void setBuiltLastRound(boolean builtLastRound) {
        this.builtLastRound = builtLastRound;
    }

    public void resetChosenColor() {
        this.chosenColor = null;
    }
} 