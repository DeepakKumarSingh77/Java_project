# Snake and Ladder Game - Low Level Design (LLD)

## Overview
This project implements a comprehensive Snake and Ladder game in Java, designed with Low-Level Design (LLD) principles. It supports multiple board configurations (standard, random with difficulty levels, custom), variable board sizes, and extensible game rules. The system demonstrates advanced pattern usage for building flexible, maintainable game systems.

Ideal for learning complex game logic, multiple pattern integration, and extensible architecture from intermediate to advanced developers.

## Design Patterns Used

### 1. **Observer Pattern**
- **Purpose**: Enables event-driven notifications for game state changes.
- **Implementation**: Game notifies observers about moves, encounters, and game end.
- **Classes**: `IObserver`, `SnakeAndLadderConsoleNotifier`, `SnakeAndLadderGame`.

### 2. **Factory Pattern**
- **Purpose**: Centralizes object creation for different game configurations.
- **Implementation**: `SnakeAndLadderGameFactory` creates games with various setups (standard, random, custom).
- **Classes**: `SnakeAndLadderGameFactory`.

### 3. **Strategy Pattern**
- **Purpose**: Allows interchangeable algorithms for board setup and game rules.
- **Implementation**: Board setup strategies (random, custom, standard) and game rules.
- **Classes**:
  - Board Setup: `BoardSetupStrategy`, `RandomBoardSetupStrategy`, `CustomCountBoardSetupStrategy`, `StandardBoardSetupStrategy`.
  - Game Rules: `SnakeAndLadderRules`, `StandardSnakeAndLadderRules`.

## Key Components

### Core Classes
- `Board`: Manages board size, entities (snakes/ladders), and configuration.
- `SnakeAndLadderPlayer`: Represents players with position and score tracking.
- `SnakeAndLadderGame`: Main game controller handling turns, dice rolls, and win conditions.
- `Dice`: Simulates dice rolls with configurable faces.

### Entities
- `BoardEntity`: Abstract base for snakes and ladders.
- `Snake`: Moves players backward.
- `Ladder`: Moves players forward.

### Strategies
- **Board Setup**: Random (easy/medium/hard), custom counts, standard positions.
- **Game Rules**: Standard movement and win conditions.

### Utilities
- `SnakeAndLadderConsoleNotifier`: Observes and displays game notifications.

## How to Run
1. Ensure you have Java installed (JDK 8 or higher).
2. Compile the code: `javac SnakeAndLadder.java`
3. Run the program: `java SnakeAndLadder`
4. Choose game setup (1-3), follow prompts for configuration and player names.

## Example Output
```
=== SNAKE AND LADDER GAME ===
Choose game setup:
1. Standard Game (10x10 board with traditional positions)
2. Random Game with Difficulty
3. Custom Game
1

Enter number of players: 2
Enter name for player 1: Alice
Enter name for player 2: Bob

=== Board Configuration ===
Board Size: 100 cells
...
Alice's turn. Press Enter to roll dice...
Rolled: 4
[ NOtification ] Alice played. New Position : 4
...
Great! Ladder at 7! Going up to 14
[ NOtification ] Alice encountered ladder at 7 now going up to 14
...
Alice wins!
[ NOtification ] Game Ended. Winner is : Alice
```

## Learning Points
- **Beginners**: Understand basic game flow, dice mechanics, and entity interactions.
- **Intermediate**: Learn multiple strategy implementations and factory usage for configuration.
- **Advanced**: Analyze complex pattern interactions, extensibility for new rules/entities, and observer decoupling.

## Extensions
- Add AI players with different strategies.
- Implement multiplayer networking.
- Add power-ups or special entities.
- Create GUI observer for visual board display.
- Add game statistics and replay functionality.
- Support different dice types (crooked, multiple).

This system showcases sophisticated pattern application and scalable game architecture in Java.
