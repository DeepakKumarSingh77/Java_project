# Tic-Tac-Toe Game - Low Level Design (LLD)

## Overview
This project implements a flexible Tic-Tac-Toe game in Java, designed with Low-Level Design (LLD) principles. It supports customizable board sizes and can be extended for different game rules. The system demonstrates clean architecture with separation of concerns, making it educational for understanding design patterns and game development.

Suitable for beginners learning game logic and for advanced developers exploring pattern-based extensible systems.

## Design Patterns Used

### 1. **Observer Pattern**
- **Purpose**: Allows objects to subscribe to events and get notified when state changes.
- **Implementation**: Game notifies observers (e.g., console notifier) about moves, wins, and draws.
- **Classes**: `IObserver`, `ConsoleNotifier`, `TicTacToeGame`.

### 2. **Factory Pattern**
- **Purpose**: Encapsulates object creation logic.
- **Implementation**: `TicTacToeGameFactory` creates game instances based on type.
- **Classes**: `TicTacToeGameFactory`, `GameType` enum.

### 3. **Strategy Pattern**
- **Purpose**: Defines interchangeable algorithms.
- **Implementation**: Game rules (valid moves, win/draw conditions) are implemented via strategies.
- **Classes**: `TicTacToeRules`, `StandardTicTacToeRules`.

## Key Components

### Core Classes
- `Board`: Manages the game grid, placement, and display.
- `TicTacToePlayer`: Represents players with name, symbol, and score.
- `TicTacToeGame`: Main game controller handling turns, validation, and win/draw checks.
- `Symbol`: Represents player marks (X, O, etc.).

### Interfaces
- `TicTacToeRules`: Defines game rule contracts.
- `IObserver`: Defines observer contracts.

### Utilities
- `ConsoleNotifier`: Observes game events and prints notifications.
- `TicTacToeGameFactory`: Factory for creating games.

## How to Run
1. Ensure you have Java installed (JDK 8 or higher).
2. Compile the code: `javac TicTacToeMain.java`
3. Run the program: `java TicTacToeMain`
4. Enter board size (e.g., 3) and follow prompts for moves.

## Example Output
```
=== TIC TAC TOE GAME ===
Enter board size (e.g., 3 for 3x3): 3
[Notification] Tic Tac Toe Game Started!

  0 1 2
0 - - - 
1 - - - 
2 - - - 

Aditya (X) - Enter row and column: 0 0
[Notification] Aditya played (0,0)

  0 1 2
0 X - - 
1 - - - 
2 - - - 

Harshita (O) - Enter row and column: 1 1
...
Aditya wins!
[Notification] Aditya wins!
```

## Learning Points
- **Beginners**: Learn basic game loop, input handling, and win condition logic.
- **Intermediate**: Understand how patterns enable extensibility (e.g., adding new rules or notifiers).
- **Advanced**: Analyze separation of concerns (Board vs. Game), observer decoupling, and factory for instantiation.

## Extensions
- Add AI player with Minimax strategy.
- Implement different rule sets (e.g., Connect-4 style).
- Add GUI observer for graphical display.
- Support multiple players or teams.
- Add game persistence and replay functionality.

This system showcases modular design and pattern application in Java.
