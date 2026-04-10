# Payment Gateway System

## Overview

This Java-based Payment Gateway System demonstrates the implementation of a scalable and extensible payment processing framework. It simulates real-world payment gateways like Paytm and Razorpay, incorporating various design patterns to handle payment requests, validation, processing, and retries. The system is designed to be easy to understand for beginners while showcasing advanced concepts for experienced developers.

## Features

- **Multiple Payment Gateways**: Support for Paytm and Razorpay with different success rates and validation rules.
- **Template Method Pattern**: Defines a standard payment flow that can be customized by subclasses.
- **Proxy Pattern**: Implements retry logic with configurable strategies (Linear and Exponential Backoff).
- **Factory Pattern**: Creates payment gateways dynamically based on type.
- **Singleton Pattern**: Ensures single instances for services like PaymentService and PaymentController.
- **Retry Mechanisms**: Handles payment failures with configurable retry strategies.
- **Extensible Design**: Easy to add new gateways, banking systems, or retry strategies.

## Design Patterns Explained

### For Beginners
Design patterns are reusable solutions to common problems in software design. This project uses several patterns to make the code organized and flexible.

- **Template Method**: Like a recipe that outlines steps (validate, initiate, confirm), but lets each gateway fill in the details.
- **Proxy**: Acts as a middleman that adds extra behavior (like retries) without changing the original gateway.
- **Factory**: A "factory" that produces the right gateway based on what you ask for.
- **Singleton**: Ensures there's only one instance of certain classes, like a single payment service.

### For Advanced Developers
- **Template Method**: The `PaymentGateway` abstract class defines the skeleton of the payment process (`processPayment`), deferring specific steps to subclasses. This promotes code reuse and enforces a consistent flow while allowing polymorphism.
- **Proxy Pattern**: `PaymentGatewayProxy` wraps the real gateway, intercepting calls to add retry logic. This decouples retry concerns from core payment logic, enabling AOP-like behavior.
- **Factory Pattern**: `GatewayFactory` uses conditional logic to instantiate gateways with associated proxies, encapsulating object creation and promoting loose coupling.
- **Singleton Pattern**: Applied to `PaymentService` and `PaymentController` to ensure global access to shared state, though in a real system, consider thread-safety and dependency injection alternatives.

## Architecture Overview

```
PaymentController (Singleton)
    ↓
GatewayFactory (Singleton)
    ↓
PaymentGatewayProxy (Proxy)
    ↓
PaymentGateway (Template Method)
    ↓
BankingSystem (Interface)
```

- **PaymentRequest**: Data model for payment details.
- **BankingSystem**: Interface for bank integrations (simulated with random success rates).
- **PaymentGateway**: Abstract base with template method.
- **Concrete Gateways**: PaytmGateway, RazorpayGateway implementing specific validations.
- **RetryStrategy**: Interface for retry logic.
- **PaymentGatewayProxy**: Adds retry capability.
- **GatewayFactory**: Creates configured gateways.
- **PaymentService**: Singleton managing the active gateway.
- **PaymentController**: Singleton orchestrating payments.

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed.
- A terminal or command prompt.

### Steps
1. Navigate to the project directory:
   ```
   cd "d:\coding\LLD_PROJECTS\11. Payment_Gateway_System"
   ```

2. Compile the Java file:
   ```
   javac PaymentGatewayApplication.java
   ```

3. Run the application:
   ```
   java PaymentGatewayApplication
   ```

The program will simulate two payment requests: one via Paytm and one via Razorpay, demonstrating success/failure scenarios.

## Code Structure

- `PaymentRequest.java` (inline): Represents a payment request with sender, receiver, amount, and currency.
- `BankingSystem.java` (interface): Defines the contract for processing payments.
- `PaytmBankingSystem.java` and `RazorpayBankingSystem.java`: Concrete implementations with simulated success rates.
- `PaymentGateway.java` (abstract): Template method for payment flow.
- `PaytmGateway.java` and `RazorpayGateway.java`: Concrete gateways with specific validations.
- `RetryStrategy.java` (interface): Defines retry behavior.
- `LinearRetryStrategy.java` and `ExponentialBackoffStrategy.java`: Retry implementations.
- `PaymentGatewayProxy.java`: Proxy adding retry logic.
- `GatewayFactory.java`: Factory for creating gateways.
- `PaymentService.java`: Singleton managing gateway selection.
- `PaymentController.java`: Singleton handling payment requests.
- `PaymentGatewayApplication.java`: Main class with demo.

## Usage Example

```java
// Create a payment request
PaymentRequest request = new PaymentRequest("Alice", "Bob", 100.0, "INR");

// Process via Paytm
boolean success = PaymentController.getInstance().handlePayment(GatewayType.PAYTM, request);
System.out.println("Payment " + (success ? "succeeded" : "failed"));
```

The system automatically applies retries if the initial payment fails, based on the configured strategy.

## Extending the System

### Adding a New Gateway
1. Implement a new `BankingSystem` (e.g., `StripeBankingSystem`).
2. Create a new gateway class extending `PaymentGateway` (e.g., `StripeGateway`).
3. Add a new enum value in `GatewayType`.
4. Update `GatewayFactory.getGateway()` to handle the new type.

### Adding a New Retry Strategy
1. Implement `RetryStrategy` (e.g., `FixedRetryStrategy`).
2. Update `GatewayFactory` to use the new strategy for specific gateways.

### Best Practices for Extension
- Follow SOLID principles: Single responsibility, Open/Closed, etc.
- Use dependency injection instead of hardcoded singletons for better testability.
- Add logging and monitoring for production use.
- Consider thread-safety for concurrent payments.

## Learning Outcomes

- **Beginners**: Understand basic OOP, interfaces, and simple patterns.
- **Intermediate**: Grasp template method, proxy, and factory patterns in action.
- **Advanced**: Analyze extensibility, retry mechanisms, and architectural trade-offs.

This project serves as an excellent example for Low-Level Design (LLD) interviews and real-world payment system design.

## Contributing

Feel free to fork, extend, or suggest improvements. For major changes, please discuss in an issue first.

## License

This project is for educational purposes. Use at your own discretion.