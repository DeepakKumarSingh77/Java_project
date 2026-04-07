# Shopping Cart System

A simple Java shopping cart application built with clean object-oriented design and common software patterns. It demonstrates how to add products to a cart, apply discounts, and process payments using a flexible, extensible architecture.

## Overview

This project shows a basic e-commerce checkout flow using:

- `Product` for product details
- `CartItem` to hold products and quantities
- `Cart` to manage cart items and calculate totals
- `DiscountStrategy` for interchangeable discount algorithms
- `PaymentStrategy` for various payment methods
- `CheckoutService` to validate and process payments
- `User`, `RegisteredUser`, and `GuestUser` to separate user types

## Who this README is for

- **Beginner:** Learn how classes, interfaces, and object composition work in Java.
- **Intermediate:** Understand strategy patterns and how to keep business logic separated from payment processing.
- **Advanced:** See how this design supports extension, testability, and clean dependency boundaries.

## How it works

1. `Main` creates products and a user.
2. The user gets a `Cart` and adds product items.
3. A discount strategy is applied via `Cart#setDiscountStrategy`.
4. `CheckoutService` validates the cart and processes payment using a `PaymentStrategy`.

## Project structure

- `Main.java` — application entry point and example usage
- `Product` — product entity with ID, name, and price
- `CartItem` — holds a product, quantity, and snapshot price
- `Cart` — stores items, updates quantities, removes products, applies discounts
- `DiscountStrategy` / `PercentageDiscount` / `FlatDiscount` — strategy pattern for discounts
- `PaymentStrategy` / `CreditCardPayment` / `DebitCardPayment` / `UpiPayment` — strategy pattern for payments
- `PaymentService` — executes payment through chosen strategy
- `CheckoutService` — validates cart and orchestrates payment processing
- `User` / `RegisteredUser` / `GuestUser` — user types with a cart

## Design patterns used

- **Strategy pattern** for discounts and payment methods
- **Composition over inheritance** by giving `Cart` behavior through interfaces
- **Single Responsibility Principle** by separating payment processing from cart logic

## Run the application

Compile and run with Java:

```sh
javac Main.java
java Main
```

## What you can improve

- Add `Order` and `Invoice` classes for completed transactions
- Store products using an inventory service rather than hardcoded values
- Add input handling for a console UI or GUI
- Add unit tests for `Cart`, discount strategies, and payment processing
- Add taxes, shipping fees, and item removal confirmation

## Notes for advanced developers

- The current `Cart` stores `priceAtAddition`, which is useful for price snapshotting in real orders.
- `Cart#getTotalPrice()` applies discount after summing item totals, so discount always affects the final order total.
- `CheckoutService` depends on `PaymentService` internally; this can be refactored to constructor injection for better testability.
- To support more payment methods, implement `PaymentStrategy` and pass it into `CheckoutService#processPayment`.

## Extension ideas

- Add `TaxStrategy` for different tax rules
- Add `PromoCode` support with validation logic
- Add persistent storage or database integration
- Implement a REST API using Spring Boot or Jakarta EE

## Summary

This project is a small but practical example of object-oriented Java design, suitable for beginners to explore classes and interfaces, and for experienced developers to extend with real-world checkout patterns.
