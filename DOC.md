# Citadels Game Implementation Documentation

## Overview
This document provides a comprehensive explanation of the Citadels card game implementation. The game is a strategic card game where players take on different character roles to build districts in their city while using special abilities to gain advantages.

## Project Structure

### Main Components
1. **App.java** - Entry point and game initialization, (player setup, district and character deck creation, main game loop)
2. **Game.java** - Core game logic and state management: Manages game state (selection phase, action phase); Handles player turns; Implements character abilities; Manages scoring and game end conditions
3. **Characters/** - Character implementations
4. **Districts/** - District card implementations
5. **Players/** - Player implementations (Human and Computer)

## Detailed Component Breakdown

### 2. Character System

The game features 8 unique character classes, each with distinct abilities:

1. **Assassin** (Rank 1)
   - Can eliminate another character for the round
   - Strategic choice to remove powerful characters

2. **Thief** (Rank 2)
   - Can steal gold from another character
   - Cannot steal from Assassin or eliminated characters

3. **Magician** (Rank 3)
   - Can exchange cards with another player
   - Can discard cards to draw new ones
   - Flexible card management

4. **King** (Rank 4)
   - Receives the crown
   - Gets bonus for noble districts
   - First pick in next round's character selection

5. **Bishop** (Rank 5)
   - Protected from Warlord's destruction
   - Gets bonus for religious districts
   - Strategic defensive choice

6. **Merchant** (Rank 6)
   - Gets bonus for trade districts
   - Receives extra gold each turn
   - Economic advantage

7. **Architect** (Rank 7)
   - Can build multiple districts
   - Draws extra cards
   - Accelerates city building

8. **Warlord** (Rank 8)
   - Can destroy districts
   - Gets bonus for military districts
   - Offensive capabilities

### 3. District System

Districts are the building blocks of each player's city. They come in different types and colors:

#### District Colors
- Noble (Yellow)
- Religious (Blue)
- Trade (Green)
- Military (Red)

#### Special Districts
1. **Laboratory**
   - Ability: Discard cards for gold
   - Strategic resource management

2. **Smithy**
   - Ability: Spend gold to draw cards
   - Card advantage generation

3. **Museum**
   - Ability: Keep discarded cards
   - Resource preservation

4. **Imperial Treasury**
   - Bonus points at game end
   - Victory point generation

5. **Map Room**
   - Ability: Draw extra cards
   - Card advantage

6. **Wishing Well**
   - Bonus points at game end
   - Victory point generation

7. **Armory**
   - Protection against destruction
   - Defensive utility

### 4. Game Flow

#### 1. Setup Phase
- Create players (one human, rest computer)
- Shuffle district and character decks
- Distribute initial gold
- Set up game state

#### 2. Character Selection Phase
- Players choose characters in order
- Crown holder gets first pick
- Some characters remain face-down
- Strategic character selection

#### 3. Action Phase
Players take turns based on character rank:
- Take gold
- Draw district cards
- Build districts
- Use character abilities
- Use district abilities

#### 4. End Game
Game ends when a player builds 8 districts:
- Points for districts
- Bonus for first to 8 districts
- Special district bonuses
- Character-specific bonuses

### 5. Player System

#### Player Types
1. **Human Player**
   - Interactive decision making
   - Manual character selection
   - Strategic choices

2. **Computer Player (AI)**
   - Automated decision making
   - Basic strategy implementation
   - Character selection logic

#### Player Components
Each player has:
- Hand of district cards
- Gold resources
- Built districts
- Character abilities
- Special district abilities

### 6. Special Mechanics

#### Throne Room
- Special district that provides gold when crown changes hands
- Strategic defensive choice

#### Character Interactions
- Assassination: Remove character for round
- Theft: Steal gold from characters
- Card Exchange: Magician's ability
- District Destruction: Warlord's ability

#### Scoring System
- Base points from districts
- Color bonuses
- Special district bonuses
- First to 8 districts bonus
- Character-specific bonuses

## Technical Implementation Details

### File Structure
```
src/
├── main/
│   ├── java/
│   │   ├── citadels/
│   │   │   ├── App.java
│   │   │   └── Game.java
│   │   ├── Characters/
│   │   │   ├── Characters.java
│   │   │   ├── CharacterName.java
│   │   │   └── [Character Classes].java
│   │   ├── Districts/
│   │   │   ├── Districts.java
│   │   │   ├── DistrictsColor.java
│   │   │   └── [District Classes].java
│   │   └── Players/
│   │       ├── Player.java
│   │       ├── Human.java
│   │       └── Com.java
│   └── resources/
│       └── citadels/
│           └── cards.tsv
```

### Key Design Patterns
1. **Object-Oriented Design**
   - Clear class hierarchy
   - Encapsulation of game components
   - Polymorphic character and district implementations

2. **State Management**
   - Game state tracking
   - Player state management
   - Turn-based progression

3. **Resource Management**
   - Card deck management
   - Gold economy
   - Player resources

## Conclusion
This implementation provides a complete and playable version of the Citadels card game, featuring all core mechanics and special abilities. The code is structured to be maintainable and extensible, allowing for potential future enhancements or modifications. 