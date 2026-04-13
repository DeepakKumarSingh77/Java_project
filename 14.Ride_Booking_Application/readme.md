# Ride Booking Application

## Overview
This is a Low-Level Design (LLD) implementation of a Ride Booking Application, similar to Uber or Lyft. The system allows riders to request rides, matches them with nearby drivers (captains), and handles the ride lifecycle from booking to completion with payment processing.

## Features
- **User Management**: Support for Riders and Captains (Drivers)
- **Ride Request**: Riders can request rides with source, destination, and vehicle type
- **Driver Matching**: Uses a nearest driver matching strategy
- **Ride Lifecycle**: Request → Accept → Start → Complete → Payment
- **Payment Processing**: Multiple payment methods (Credit Card, Debit Card, UPI)
- **Thread Safety**: Synchronized ride and driver assignment to prevent race conditions

## Design Patterns Used
- **Singleton Pattern**: RideManager for managing active rides
- **Strategy Pattern**: MatchingStrategy for driver matching, PaymentStrategy for payments
- **Factory Pattern**: (Can be extended for different ride types)
- **Observer Pattern**: (Can be extended for notifications)

## Architecture
The application follows object-oriented principles with separation of concerns:
- **Models**: User, Rider, Captain, Vehicle, Ride, Location
- **Services**: RideService for ride operations, PaymentService for payments
- **Strategies**: MatchingStrategy, PaymentStrategy
- **Managers**: RideManager for ride state management

## Classes Overview

### Core Classes
- `User`: Base class for users
- `Rider`: Extends User, manages ride history
- `Captain`: Extends User, represents drivers with location and status
- `Ride`: Represents a ride with status, locations, and fare
- `Vehicle`: Represents driver vehicles

### Services
- `RideService`: Handles ride requests, assignment, and lifecycle
- `PaymentService`: Processes payments using different strategies

### Strategies
- `MatchingStrategy`: Interface for driver matching algorithms
- `NearestDriverStrategy`: Finds nearest available drivers
- `PaymentStrategy`: Interface for payment methods
- `CreditCard`, `DebitCard`, `UPI`: Concrete payment implementations

### Managers
- `RideManager`: Singleton for managing active rides and history

## How to Run

### Prerequisites
- Java 8 or higher
- JDK installed

### Compilation
```bash
javac Main.java
```

### Execution
```bash
java Main
```

### Sample Output
```
Driver 1 accepted ride 101
Ride assigned to driver 1
Starting ride...
Completing ride...
Paid 250.0 using UPI
Ride Status: COMPLETED
```

## Usage Example
The `Main.java` file contains a demonstration:
1. Creates a rider and multiple captains
2. Sets up a nearest driver matching strategy
3. Requests a ride from (0,0) to (10,10)
4. Assigns the nearest available driver
5. Starts and completes the ride
6. Processes payment

## Future Enhancements
- Add more matching strategies (e.g., rating-based, price-based)
- Implement notification system for real-time updates
- Add fare calculation based on distance and time
- Support for multiple vehicle types with different pricing
- Add database persistence
- Implement REST API for web/mobile integration

## Contributing
This is an LLD project for learning purposes. Feel free to extend with additional features or improve the design.

## License
This project is for educational purposes only.