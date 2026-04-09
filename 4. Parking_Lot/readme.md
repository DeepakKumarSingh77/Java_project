# Parking Lot System - Low Level Design (LLD)

## Overview
This project implements a Parking Lot Management System in Java, demonstrating Low-Level Design (LLD) principles. It simulates a multi-floor parking lot where vehicles can enter, park, and exit while calculating fares and processing payments. The system uses various design patterns to ensure flexibility, extensibility, and maintainability.

Ideal for learning object-oriented design, pattern implementation, and system architecture from beginner to advanced levels.

## Design Patterns Used

### 1. **Factory Pattern**
- **Purpose**: Provides an interface for creating objects without specifying their concrete classes.
- **Implementation**: `VehicleFactory` creates different vehicle types (Bike, Car, Truck) based on input.
- **Classes**: `VehicleFactory`, `Bike`, `Car`, `Truck`.

### 2. **Strategy Pattern**
- **Purpose**: Defines a family of algorithms and makes them interchangeable.
- **Implementation**: Used for spot allocation, fare calculation, and payment methods.
- **Classes**:
  - Spot Allocation: `SpotAllocationStrategy`, `FirstAvailableStrategy`, `NearestFirstStrategy`.
  - Fare Calculation: `FareStrategy`, `NormalFare`, `PeakHourFare`.
  - Payment: `PaymentStrategy`, `CreditCardPayment`, `DebitCardPayment`, `UPIPayment`.

## Key Components

### Core Entities
- `Vehicle`: Abstract class with concrete implementations for Bike, Car, Truck.
- `ParkingSpot`: Represents individual parking spots with type and occupancy status.
- `ParkingFloor`: Contains multiple spots.
- `ParkingLot`: Contains multiple floors.
- `Ticket`: Represents a parking session with entry/exit times and assigned spot.

### Services and Managers
- `TicketService`: Handles ticket creation and closure.
- `FareCalculator`: Calculates parking fees based on strategy.
- `PaymentService`: Processes payments using different strategies.
- `ParkingSpotAllocator`: Allocates spots using allocation strategies.

### Gates
- `EntryGate`: Handles vehicle entry and ticket generation.
- `ExitGate`: Handles vehicle exit, fare calculation, payment, and spot freeing.

### Enums
- `VehicleType`: BIKE, CAR, TRUCK.
- `PaymentType`: CREDIT_CARD, DEBIT_CARD, UPI.

## How to Run
1. Ensure you have Java installed (JDK 8 or higher).
2. Compile the code: `javac Client.java`
3. Run the program: `java Client`

## Example Output
```
---- VEHICLE ENTER ----
Ticket Generated: [some-uuid]
---- VEHICLE EXIT ----
Paid using UPI: [calculated-amount]
Exit complete. Amount paid: [calculated-amount]
```

## Learning Points
- **Beginners**: Understand how to model real-world systems with classes and interfaces. Learn basic pattern usage.
- **Intermediate**: See how strategies enable runtime behavior changes. Grasp the flow from entry to exit.
- **Advanced**: Analyze scalability (adding floors/spots), extensibility (new vehicle types, strategies), and SOLID principles.

## Extensions
- Add more allocation strategies (e.g., VIP spots).
- Implement reservation system.
- Add database persistence for tickets and spots.
- Integrate with external payment gateways.
- Add observer pattern for real-time occupancy monitoring.

This system demonstrates clean code practices and modular design in Java.
