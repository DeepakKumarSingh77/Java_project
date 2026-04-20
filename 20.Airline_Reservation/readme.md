# Airline Reservation System

## Description
This is a Java-based Low-Level Design (LLD) implementation of an Airline Reservation System. The system allows users to search for flights, book seats, make payments, and receive notifications. It demonstrates object-oriented design principles, design patterns, and basic concurrency concepts through seat locking.

## Features
- **User Management**: Create and retrieve user profiles.
- **Flight Search**: Search flights by source, destination, and date.
- **Seat Booking**: Lock and book seats with different statuses (Available, Locked, Booked).
- **Payment Processing**: Support multiple payment methods using the Strategy pattern.
- **Notifications**: Send booking confirmations and cancellations.
- **Booking Management**: Create, confirm, and cancel bookings.

## Design Patterns Used
- **Strategy Pattern**: For payment methods (CreditCard, DebitCard).
- **Interface Segregation**: IFlightSeat interface for different seat types.
- **Service Layer Pattern**: Separate services for users, flights, bookings, and notifications.

## Key Classes
- **User**: Represents a user with ID, name, email, and phone.
- **Flight**: Contains flight details and a list of seats.
- **IFlightSeat**: Interface for seat operations.
- **EconomySeat**: Implementation of IFlightSeat for economy class.
- **Booking**: Manages booking details, seats, and status.
- **PaymentStrategy**: Interface for payment methods.
- **UserService**: Handles user creation and retrieval.
- **FlightService**: Manages flight search and retrieval.
- **FlightBookingService**: Core service for booking operations.
- **NotificationService**: Handles sending notifications.

## How to Run
1. Ensure Java is installed on your system.
2. Compile the code: `javac MainApp.java`
3. Run the application: `java MainApp`

The main method in `MainApp.java` demonstrates the system's functionality by creating a user, flight, booking seats, and processing payment.

## Assumptions
- Only economy seats are implemented; can be extended for business/premium classes.
- Seat locking is simple and not thread-safe; in a real system, use proper concurrency controls.
- Payment strategies are mocked; integrate with real payment gateways.
- No database; uses in-memory HashMaps for simplicity.
- Notifications are printed to console; extend to email/SMS.

## Future Enhancements
- Add more seat types (Business, First Class).
- Implement thread-safe seat locking.
- Integrate with a database.
- Add user authentication.
- Implement flight scheduling and real-time updates.