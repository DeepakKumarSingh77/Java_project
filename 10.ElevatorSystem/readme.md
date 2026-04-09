# Elevator System Simulation

## Overview

This project is a Java-based simulation of a multi-elevator system in a 10-floor building. It demonstrates low-level design (LLD) principles for building scalable and efficient elevator systems. The simulation handles elevator requests from hallways (hall calls) and inside elevators (car calls), using an intelligent dispatcher to assign elevators optimally.

Whether you're a beginner learning about object-oriented programming or an advanced developer exploring system design patterns, this project provides insights into real-world elevator logic, concurrency concepts, and algorithmic decision-making.

## Features

- **Multiple Elevators**: Supports up to multiple elevators operating simultaneously.
- **Hall Requests**: Users can request elevators from floors with up/down buttons.
- **Car Calls**: Users inside elevators can select destination floors.
- **Intelligent Dispatcher**: Assigns the nearest available elevator based on direction and proximity.
- **State Management**: Elevators have states (IDLE, MOVING) and directions (UP, DOWN, NONE).
- **Priority Queues**: Uses heaps to manage floor requests efficiently.
- **Simulation Engine**: Step-by-step simulation with tick-based movement.

## How It Works (Beginner Level)

Imagine a building with 10 floors (0 to 9). Elevators start at different floors and respond to requests:

1. **Hall Call**: A person presses UP or DOWN on a floor. The dispatcher finds the best elevator.
2. **Car Call**: Inside an elevator, a person presses a floor button.
3. **Movement**: Elevators move floor by floor, stopping at requested floors.

The system simulates this in code, printing the state of each elevator after each "tick" (time step).

### Running the Simulation

1. Ensure you have Java installed (JDK 8 or higher).
2. Compile: `javac Main.java`
3. Run: `java Main`

The simulation will run for 10 ticks, showing elevator positions and states.

## Architecture (Intermediate Level)

### Key Classes

- **Elevator**: Represents an individual elevator.
  - Tracks current floor, state, direction.
  - Manages up/down requests using priority queues (min-heap for up, max-heap for down).
  - Moves one floor per tick, serving requests in order.

- **Dispatcher**: Manages hall requests and assigns elevators.
  - Maintains arrays for up/down requests per floor.
  - Assigns elevators based on:
    - Same direction and on the way.
    - Idle elevators (closest first).

- **ElevatorSystem**: Main controller.
  - Holds references to elevators and dispatcher.
  - Provides methods for requests and simulation ticks.

### Enums

- **Direction**: UP, DOWN, NONE
- **State**: IDLE, MOVING

## Advanced Concepts

### Elevator Assignment Algorithm

The dispatcher uses a greedy approach:

1. **Priority 1**: Elevators moving in the same direction and passing the requested floor.
2. **Priority 2**: Idle elevators, assigned to the closest one.

This minimizes wait time and maximizes efficiency.

### Data Structures

- **PriorityQueue<Integer> upHeap**: Min-heap for upward requests (serves lowest floor first).
- **PriorityQueue<Integer> downHeap**: Max-heap for downward requests (serves highest floor first).

Time Complexity: O(log n) for insertions, O(1) for peeks.

### Design Patterns

- **Observer Pattern**: (Implicit) Elevators observe requests.
- **Strategy Pattern**: Dispatcher uses different strategies for assignment.
- **State Pattern**: Elevator states control behavior.

### Scalability Considerations

- For real systems, add concurrency (threads for each elevator).
- Handle more floors/elevators with distributed dispatchers.
- Add features like door open/close, emergency stops, weight limits.

### Potential Improvements

- Implement SCAN or LOOK algorithms for better efficiency.
- Add logging and metrics.
- Simulate multiple buildings or variable speeds.

## Example Output

```
Tick: 0
Elevator 1 Floor: 0 Direction: UP State: MOVING
Elevator 2 Floor: 5 Direction: NONE State: IDLE
Elevator 3 Floor: 9 Direction: NONE State: IDLE
-------------------
Tick: 1
Elevator 1 Floor: 1 Direction: UP State: MOVING
...
```

## Requirements

- Java 8+
- No external dependencies

## Contributing

Feel free to fork and enhance! Add features like GUI visualization, more advanced algorithms, or unit tests.

## License

This project is for educational purposes. Use and modify as needed.