package citadels;

import Characters.*;
import Districts.*;

import java.io.*;
import java.util.*;

/**
 * Main application class for the Citadels game.
 * This class handles game initialization, player setup, and the main game loop.
 */
public class App {
	private List<Districts> districtDeck;
	private List<Characters> characterDeck;
	private Game game;
	private int numPlayers;
	private boolean gameRunning;
	private Scanner scanner;

	/**
	 * Constructs a new App instance and initializes the game.
	 * Prompts for number of players and sets up the game components.
	 */
	public App() {
		scanner = new Scanner(System.in);
		numPlayers = askNumPlayers();
		makeGame();
		gameRunning = true;
	}

	/**
	 * Prompts the user to enter the number of players.
	 * @return The number of players (between 4 and 7)
	 */
	public int askNumPlayers() {
		System.out.println("How many players? (4-7)");
		String input;
		while (true) {
			try {
				input = scanner.nextLine();
				int num = Integer.parseInt(input);
				if (num >= 4 && num <= 7) {
					return num;
				}
				System.out.println("Please enter a number between 4 and 7.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number between 4 and 7.");
			}
		}
	}

	/**
	 * Creates and initializes the game components.
	 * Sets up the district deck, character deck, and initializes the game state.
	 */
	public void makeGame() {
		districtDeck = createDistrictDeck();
		characterDeck = createCharacterDeck();
		game = new Game(this, districtDeck, characterDeck, numPlayers);
		game.setUp();
	}

	/**
	 * Sets the game running state.
	 * @param gameRunning true to keep the game running, false to end it
	 */
	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	/**
	 * Starts the game by displaying welcome message and initiating character selection.
	 */
	public void startGame() {
		System.out.println("Welcome to Citadels!");
		game.startSelectionPhase();
	}

	/**
	 * Main game loop that continues until the game is over.
	 */
	public void playGame() {
		while (gameRunning) {
			game.play(this);
		}
	}
	
	/**
	 * Handles game over state by closing resources and displaying final message.
	 */
	public void gameOver() {
		gameRunning = false;
		System.out.println("Game over!");
		scanner.close();
	}

	/**
	 * Creates the district deck by reading from a configuration file.
	 * Initializes special district cards with their unique abilities.
	 * @return List of district cards for the game
	 * @throws RuntimeException if the cards file cannot be read or no valid cards are loaded
	 */
	private List<Districts> createDistrictDeck() { // Create district deck	
		List<Districts> deck = new ArrayList<>();
		
		// Get input stream from the resource file
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("citadels/cards.tsv");
		if (inputStream == null) {
			throw new RuntimeException("Could not find cards.tsv in resources");
		}
		// Read each line of the file
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
			String line = reader.readLine(); // Skip header
			while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
				// Split with -1 to keep empty fields
				String[] parts = line.split("\t", -1);  // Split line into parts
				if (parts.length < 4) {
					System.err.println("Warning: Skipping invalid line: " + line);
					continue;
				}

				// Create each card
				try {
					String name = parts[0].trim(); // Get card name
					int quantity = Integer.parseInt(parts[1].trim()); // Get quantity
					DistrictsColor color = DistrictsColor.valueOf(parts[2].trim().toUpperCase()); // Get card color	
					int cost = Integer.parseInt(parts[3].trim()); // Get cost
					// Handle the optional ability field
					String ability = (parts.length > 4) ? parts[4].trim() : ""; // Special ability

					// Create the specified quantity of each district
					for (int i = 0; i < quantity; i++) {
						Districts district;
						// Create special districts
						switch (name) {
							case "Laboratory":
								district = new Laboratory();
								break;
							case "Smithy":
								district = new Smithy();
								break;
							case "Museum":
								district = new Museum();
								break;
							case "Imperial Treasury":
								district = new ImperialTreasury();
								break;
							case "Map Room":
								district = new MapRoom();
								break;
							case "Wishing Well":
								district = new WishingWell();
								break;
							case "Armory":
								district = new Armory();
								break;
							default:
								district = new Districts(name, color, cost, ability);
						}
						deck.add(district); // Add card to deck
					}
				} catch (IllegalArgumentException e) {
					System.err.println("Warning: Error parsing line: " + line + " - " + e.getMessage());
                }
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read cards file: " + e.getMessage(), e);
		}

		if (deck.isEmpty()) { // Check if deck is empty
			throw new RuntimeException("No valid district cards were loaded");
		}
		
		return deck; // Return deck
	}

	/**
	 * Creates the character deck with one of each character in rank order.
	 * @return List of character cards for the game
	 */
	private List<Characters> createCharacterDeck() { // Create character deck	
		List<Characters> deck = new ArrayList<>();
		// Add one of each character in rank order
		deck.add(new Assassin());
		deck.add(new Thief());
		deck.add(new Magician());
		deck.add(new King());
		deck.add(new Bishop());
		deck.add(new Merchant());
		deck.add(new Architect());
		deck.add(new Warlord());
		return deck;
	}

	/**
	 * Gets the current game instance.
	 * @return The current Game object
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Main entry point for the Citadels game.
	 * Creates a new game instance and starts gameplay.
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {
		App app = new App();
		app.startGame();
		app.playGame();
	}
}
