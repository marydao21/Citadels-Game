# Citadels Game

A Java implementation of the classic card game Citadels, featuring a command-line interface for strategic city-building gameplay.

## Game Overview

Citadels is a strategic card game where players compete to build the most prestigious city by constructing districts and using character abilities. The game supports 4-7 players, with one human player (Player 1) and the rest as computer-controlled opponents.

## Features

- **Multi-player Support**: 4-7 players (1 human + 3-6 AI)
- **Character Selection**: 8 unique characters with special abilities
- **District Building**: 5 different district types with unique effects
- **Special Purple Districts**: 20+ special districts with powerful abilities
- **Command-line Interface**: Clean, interactive text-based gameplay
- **AI Opponents**: Intelligent computer players with strategic decision-making

## Game Components

### Characters (Rank 1-8)
1. **Assassin** - Kill a character to prevent them from taking their turn
2. **Thief** - Steal gold from another character
3. **Magician** - Exchange cards with another player or discard and draw new cards
4. **King** - Receive gold for each noble (yellow) district in your city
5. **Bishop** - Receive gold for each religious (blue) district in your city
6. **Merchant** - Receive gold for each trade (green) district in your city
7. **Architect** - Draw 2 extra cards and can build up to 3 districts
8. **Warlord** - Receive gold for each military (red) district and can destroy districts

### District Types
- **Red (Military)**: Military districts
- **Yellow (Noble)**: Noble districts  
- **Green (Trade)**: Trade districts
- **Blue (Religious)**: Religious districts
- **Purple (Special)**: Special districts with unique abilities

### Special Purple Districts
The game includes 20+ special purple districts with powerful abilities:
- **Haunted City**: Choose its color for victory points
- **Graveyard**: Take destroyed districts into your hand
- **Library**: Keep both cards when drawing
- **Great Wall**: Increase Warlord's destroy cost
- **School of Magic**: Choose its color for income
- **Lighthouse**: Search deck and choose a card
- **Quarry**: Build duplicate districts
- **Bell Tower**: Announce game end conditions
- **Factory**: Reduce cost of other purple districts
- **Hospital**: Take action even when assassinated
- And many more!

## Installation & Setup

### Prerequisites
- Java 8 or higher
- Gradle (for building)

### Building the Game
```bash
# Clone the repository
git clone <repository-url>
cd Citadels-Game

# Build the project
./gradlew build

# Run the game
./gradlew run
```

### Running the JAR File
```bash
# After building, run the JAR directly
java -jar build/libs/citadels.jar
```

## How to Play

### Game Setup
1. Start the game and enter the number of players (4-7)
2. Each player starts with 4 district cards and 2 gold
3. The game begins with character selection

### Game Flow
Each round consists of two phases:

#### 1. Character Selection Phase
- Players secretly choose characters in turn order
- One character remains face-down and unassigned
- Character abilities are activated in rank order (1-8)

#### 2. Action Phase
- Characters take actions in rank order
- Each character can:
  - Take income (2 gold or draw 2 cards)
  - Build districts (pay gold cost)
  - Use character ability
  - Use district abilities (if applicable)

### Victory Conditions
The game ends when:
- A player builds 8 districts, OR
- The Bell Tower announces early game end

**Scoring:**
- Base points: Sum of district costs
- Bonus points: Special district abilities
- First to 8 districts: +4 points
- All 5 district types: +3 points

## Game Rules

### Building Districts
- Pay the gold cost shown on the card
- Districts must be unique (unless Quarry allows duplicates)
- Maximum of 1 district per turn (unless Architect allows more)

### Character Abilities
- Each character has a unique ability
- Abilities activate in character rank order
- Some abilities can be blocked (Assassin, Thief)

### Special District Abilities
- Purple districts have special abilities
- Some activate automatically, others require player choice
- Abilities reset each turn

### Income and Resources
- **Gold**: Used to build districts
- **Cards**: District cards in hand
- **City**: Built districts in play

## Technical Details

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   ├── citadels/          # Main game logic
│   │   ├── Characters/        # Character implementations
│   │   ├── Districts/         # District implementations
│   │   └── Players/           # Player classes (Human/AI)
│   └── resources/
│       └── citadels/
│           ├── cards.tsv      # District card definitions
│           └── cards_to_implement.tsv  # Additional districts
├── test/                      # Unit tests
└── build.gradle              # Build configuration
```

### Key Classes
- **App**: Main application entry point
- **Game**: Core game logic and state management
- **Player**: Abstract base class for all players
- **Human**: Human player implementation
- **Com**: Computer player AI implementation
- **Characters**: Character ability implementations
- **Districts**: District card implementations

### AI Behavior
Computer players use strategic decision-making:
- Prioritize building valuable districts
- Use character abilities effectively
- Manage resources (gold/cards) efficiently
- Make tactical decisions based on game state

## Contributing

This is an educational project implementing the Citadels card game. The codebase demonstrates:
- Object-oriented design principles
- Game state management
- AI decision-making algorithms
- File I/O and resource management
- Unit testing practices

## License

This project is for educational purposes. Citadels is a trademark of its respective owners.

## Game Credits

Original Citadels game design by Bruno Faidutti.
Java implementation created for educational purposes.