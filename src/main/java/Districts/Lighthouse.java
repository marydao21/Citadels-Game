package Districts;

import citadels.Game;
import Players.Player;
import java.util.List;

public class Lighthouse extends Districts {
    private boolean canSearchDeck;  // Whether player can search the deck

    public Lighthouse() {
        super("Lighthouse", DistrictsColor.PURPLE, 3,
              "When you place the Lighthouse in your city, you may look through the District Deck, choose one card and add it to your hand. Shuffle the deck afterwards");
        this.canSearchDeck = true;
    }

    @Override
    public boolean useAbility(Game game, Player player, boolean isHuman) {
        if (!canSearchDeck) {
            if (isHuman) {
                System.out.println("Lighthouse ability already used");
            }
            return false;
        }

        if (isHuman) {
            System.out.println("Lighthouse ability: You may look through the district deck and choose one card");
            System.out.println("Do you want to search the deck? (y/n)");
            
            while (true) {
                String input = game.scanner.nextLine().toLowerCase();
                if (input.equals("y") || input.equals("yes")) {
                    // Show top few cards from deck
                    List<Districts> topCards = game.drawDistricts(3);
                    System.out.println("Top 3 cards from deck:");
                    for (int i = 0; i < topCards.size(); i++) {
                        System.out.println((i + 1) + ". " + topCards.get(i).toString());
                    }
                    
                    System.out.println("Choose a card to add to your hand (1-" + topCards.size() + ") or 0 to cancel:");
                    
                    while (true) {
                        try {
                            String choiceInput = game.scanner.nextLine();
                            int choice = Integer.parseInt(choiceInput);
                            
                            if (choice == 0) {
                                // Return all cards to deck
                                for (Districts card : topCards) {
                                    game.discard(card);
                                }
                                canSearchDeck = false;
                                return false;
                            }
                            
                            if (choice > 0 && choice <= topCards.size()) {
                                Districts chosenCard = topCards.get(choice - 1);
                                player.addCard(chosenCard);
                                System.out.println("You added " + chosenCard.getName() + " to your hand");
                                
                                // Return other cards to deck
                                for (int i = 0; i < topCards.size(); i++) {
                                    if (i != choice - 1) {
                                        game.discard(topCards.get(i));
                                    }
                                }
                                
                                canSearchDeck = false;
                                return true;
                            }
                            
                            System.out.println("Invalid choice. Please choose 0-" + topCards.size());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }
                } else if (input.equals("n") || input.equals("no")) {
                    System.out.println("You chose not to search the deck");
                    canSearchDeck = false;
                    return false;
                } else {
                    System.out.println("Please enter 'y' or 'n'");
                }
            }
        } else {
            // AI behavior: always search and take first card
            List<Districts> topCards = game.drawDistricts(3);
            if (!topCards.isEmpty()) {
                Districts chosenCard = topCards.get(0);
                player.addCard(chosenCard);
                System.out.println("Player " + player.getID() + " searched deck and took " + chosenCard.getName());
                
                // Return other cards to deck
                for (int i = 1; i < topCards.size(); i++) {
                    game.discard(topCards.get(i));
                }
            }
            canSearchDeck = false;
            return true;
        }
    }

    @Override
    public void resetAbility() {
        super.resetAbility();
        canSearchDeck = false;  // Can only be used once when placed
    }
} 