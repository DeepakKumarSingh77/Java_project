# Notification System - Low Level Design (LLD)

## Overview
This project demonstrates a Notification System implemented using various design patterns in Java. It showcases how to build a flexible, extensible notification system that can send notifications via multiple channels (Email, SMS, Popup) while logging them, and applying decorators like timestamps and signatures.

The system is designed for educational purposes to illustrate Low-Level Design (LLD) concepts, making it suitable for beginners learning design patterns and for advanced developers to understand pattern integration.

## Design Patterns Used

### 1. **Decorator Pattern**
- **Purpose**: Dynamically adds responsibilities to objects without modifying their structure.
- **Implementation**: Used for notifications to add timestamps and signatures.
- **Classes**: `NotificationDecorator`, `TimestampDecorator`, `SignatureDecorator`.

### 2. **Observer Pattern**
- **Purpose**: Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified.
- **Implementation**: Observers (Logger, NotificationEngine) are notified when a new notification is sent.
- **Classes**: `IObserver`, `IObservable`, `NotificationObservable`, `Logger`, `NotificationEngine`.

### 3. **Strategy Pattern**
- **Purpose**: Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
- **Implementation**: Different notification strategies (Email, SMS, Popup) are encapsulated and can be added dynamically.
- **Classes**: `INotificationStrategy`, `EmailStrategy`, `SMSStrategy`, `PopUpStrategy`.

### 4. **Singleton Pattern**
- **Purpose**: Ensures a class has only one instance and provides a global point of access to it.
- **Implementation**: `NotificationService` is a singleton to manage notifications centrally.
- **Class**: `NotificationService`.

## Key Components

### Interfaces
- `INotification`: Defines the contract for notifications.
- `IObserver`: Defines the contract for observers.
- `IObservable`: Defines the contract for observables.
- `INotificationStrategy`: Defines the contract for notification strategies.

### Classes
- `SimpleNotification`: Basic notification with content.
- `NotificationDecorator`: Abstract decorator for notifications.
- `TimestampDecorator`: Adds timestamp to notification content.
- `SignatureDecorator`: Adds signature to notification content.
- `NotificationObservable`: Concrete observable that notifies observers.
- `NotificationService`: Singleton service to manage notifications.
- `Logger`: Observer that logs notifications.
- `NotificationEngine`: Observer that sends notifications using strategies.
- `EmailStrategy`, `SMSStrategy`, `PopUpStrategy`: Concrete strategies for sending notifications.
- `NotificationSystem`: Main class with demonstration.

## How to Run
1. Ensure you have Java installed (JDK 8 or higher).
2. Compile the code: `javac NotificationSystem.java`
3. Run the program: `java NotificationSystem`

## Example Output
```
Logging New Notification : 
[Sun Apr 06 12:00:00 EDT 2026] Your order has been shipped!
-- Customer Care
Sending email Notification to: random.person@gmail.com
[Sun Apr 06 12:00:00 EDT 2026] Your order has been shipped!
-- Customer Care
Sending SMS Notification to: +91 9876543210
[Sun Apr 06 12:00:00 EDT 2026] Your order has been shipped!
-- Customer Care
Sending Popup Notification: 
[Sun Apr 06 12:00:00 EDT 2026] Your order has been shipped!
-- Customer Care
```

## Learning Points
- **Beginners**: Understand how design patterns work together to solve real-world problems. See how interfaces promote loose coupling.
- **Intermediate**: Learn to combine multiple patterns (Decorator + Observer + Strategy) in a single system.
- **Advanced**: Analyze extensibility - how easy it is to add new decorators, observers, or strategies without changing existing code.

## Extensions
- Add more decorators (e.g., encryption, priority levels).
- Implement more strategies (e.g., Push notifications, Slack).
- Add more observers (e.g., database logging, analytics).

This system demonstrates clean architecture principles and SOLID design in Java.
