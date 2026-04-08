# Movie Ticket Booking

## Overview

This is a Java-based movie ticket booking simulation built as a learning project. It demonstrates a simple domain model for theaters, screens, shows, seats, users, and bookings. It also includes a basic concurrency example where two users try to book the same seats at the same time.

## Key Concepts

- `Seat` represents a physical seat in a screen.
- `ShowSeat` wraps a `Seat` with booking state: `AVAILABLE`, `LOCKED`, or `BOOKED`.
- `Movie` stores movie metadata.
- `Screen` groups seats together for a single theater screen.
- `Show` represents a movie screening and creates `ShowSeat` instances from screen seats.
- `User` represents a customer.
- `Booking` coordinates locking seats and confirming the booking.
- `SearchService` is an interface for searching movies by title.

## Features

- Seat state management with lock and book transitions.
- A timeout mechanism for locked seats (`LOCK_TIMEOUT` set to 2 minutes).
- Thread-safe seat locking and booking using `ReentrantLock`.
- A basic search service implementation for movie title search.

## Who should read this?

- Beginners: learn how a small object model is organized, and how to run a Java `main` method.
- Intermediate developers: understand seat locking and a simple domain-driven design.
- Advanced developers: explore concurrency handling and consider how to improve the seat locking system.

## Project Structure

- `Main.java` — sample application entry point and scenario simulation.
- `Seat` — immutable seat value object.
- `ShowSeat` — manages seat status and thread-safe operations.
- `Movie`, `Screen`, `Show`, `User`, `Booking` — domain entities.
- `SearchService`, `MovieSearchService` — search abstraction and implementation.

## How it works

1. `Main` creates sample seats, screen, movie, show, and users.
2. It searches movies using a title fragment.
3. It selects the same two seats for both users.
4. Two threads simulate concurrent booking attempts.
5. `Booking.createBooking(...)` locks seats first, then confirms the booking.

## Concurrency and safety

- `ShowSeat.lockSeat()` and `ShowSeat.bookSeat()` use `ReentrantLock` to prevent race conditions.
- If a seat is already locked, the method will check whether the lock has expired and reset it to `AVAILABLE` if needed.
- If one thread locks seats successfully, the other thread fails because the seats are no longer available.
- If any seat lock fails during booking, the already locked seats are released.

## Running the project

1. Open the project folder in an IDE such as IntelliJ IDEA or VS Code.
2. Compile and run `Main.java`.
3. Observe output showing search results and booking success/failure for each user.

Example output:

```
Search Results:
Avengers
User1 Booking Success: Booking@<hash>
User2 Booking Failed: Seat not available
```

## Design notes for learners

### Beginner
- Each class is small and focused on a single responsibility.
- `enum` is used to represent seat and booking states.
- `UUID.randomUUID()` is used to create a unique booking ID.

### Intermediate
- `Show` creates a fresh `ShowSeat` list for each show, isolating seat state to each screening.
- `Booking.createBooking(...)` performs a two-phase operation: lock then book.
- The `SearchService` interface allows swapping search implementations later.

### Advanced
- The locking strategy is coarse-grained at a single seat level; a real system may use distributed locks or database transactions.
- `LOCK_TIMEOUT` is a simple expiration mechanism, but it does not clean expired locks proactively.
- `BookSeat` currently does not verify the lock timestamp again before booking; a production version should confirm the lock is still valid.
- A real-world system would likely separate business logic into service classes and persist state in a database.

## Potential improvements

- Add `ShowSeat.isExpired()` and a periodic cleanup task.
- Support partial booking and seat hold expiration notifications.
- Add a `PaymentService` abstraction and payment state.
- Replace `Date` with `LocalDateTime` from `java.time`.
- Add unit tests for `ShowSeat` locking and `Booking` rollback behavior.

## Learning outcomes

After exploring this project, you should understand:

- basic Java class design and encapsulation
- enums for state modeling
- thread-safe access with `ReentrantLock`
- the importance of rollback when a booking fails
- how to simulate a concurrency scenario in a simple sample application
