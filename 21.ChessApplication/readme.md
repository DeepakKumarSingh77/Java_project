# Chess Application

A comprehensive chess game system implemented in Java, demonstrating various design patterns and object-oriented principles. This project simulates a multiplayer chess environment with user management, game matching, move validation, and real-time chat functionality.

## Features

- **Complete Chess Game Logic**: Full implementation of chess rules including piece movements, check, checkmate, and stalemate detection
- **User Management**: Player profiles with scoring system
- **Game Matching**: Automated matchmaking based on player scores
- **Real-time Chat**: In-game messaging between players during matches
- **Move Validation**: Ensures all moves follow standard chess rules
- **Game History**: Tracks move history for each game
- **Score Tracking**: Elo-style scoring system with rewards and penalties

## Design Patterns Used

This project demonstrates several key design patterns:

- **Strategy Pattern**: Used for piece movement logic and game rules
- **Factory Pattern**: Piece creation through `PieceFactory`
- **Mediator Pattern**: Chat functionality between players
- **Singleton Pattern**: `GameManager` for centralized game management
- **Observer Pattern**: Implicit in user notifications

## Architecture

### Core Classes

- **Piece Hierarchy**: Abstract `Piece` class with concrete implementations (King, Queen, Rook, Bishop, Knight, Pawn)
- **Board**: Manages piece positions and board state
- **Move**: Represents chess moves with validation
- **ChessRules**: Interface for game rule validation
- **User**: Player representation with scoring
- **Match**: Game session management
- **GameManager**: Singleton managing all active games and matchmaking

### Key Components

- Position system with chess notation conversion
- Comprehensive move validation including check prevention
- Automated game end detection
- Chat mediator for player communication

## How to Run

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Command line interface

### Compilation and Execution

1. Navigate to the project directory:
   ```bash
   cd 21.ChessApplication
   ```

2. Compile the Java file:
   ```bash
   javac Chess.java
   ```

3. Run the application:
   ```bash
   java Chess
   ```

## Usage

The application includes a demonstration of the chess system:

- **Scholar's Mate Demo**: Shows a 4-move checkmate sequence
- **Game Manager Demo**: Demonstrates user matching and game creation

### Example Output

```
=== Chess System with Design Patterns Demo ===

=== Scholar's Mate Demo (4-move checkmate) ===
Match started between Aditya (White) and Rohit (Black)
[Board display]
Move 1: White e2-e4
[Board after move]
...
Game ended - Aditya wins by checkmate!
```

## Demo Features

- **Scholar's Mate**: Classic 4-move checkmate demonstration
- **User Matching**: Score-based matchmaking system
- **Chat System**: Real-time messaging between players
- **Score Updates**: Automatic score adjustments based on game outcomes

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is for educational purposes. Feel free to use and modify as needed.

## Future Enhancements

- GUI interface for the chess board
- Network multiplayer support
- Tournament system
- Advanced AI opponent
- Game replay functionality
- Database persistence for user data
4. Add tests if applicable
5. Submit a pull request

## License

This project is for educational purposes. Feel free to use and modify as needed.

## Future Enhancements

- GUI interface for the chess board
- Network multiplayer support
- Tournament system
- Advanced AI opponent
- Game replay functionality
- Database persistence for user data
4. Add tests if applicable
5. Submit a pull request

## License

This project is for educational purposes. Feel free to use and modify as needed.

## Future Enhancements

- GUI interface for the chess board
- Network multiplayer support
- Tournament system
- Advanced AI opponent
- Game replay functionality
- Database persistence for user data