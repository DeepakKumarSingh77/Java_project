# E-Commerce Application

## Overview
This is a Low-Level Design (LLD) implementation of an E-Commerce Application in Java. The system demonstrates core e-commerce functionalities including product management, shopping cart, inventory handling, order processing, and payment integration.

## Features
- **Product Management**: Define products with ID, name, and price
- **Shopping Cart**: Add/remove items, calculate totals
- **Inventory Management**: Stock tracking and reservation with thread-safe operations
- **Order Processing**: Create orders from cart, checkout with payment
- **Payment Strategies**: Support for multiple payment methods (Credit Card, Debit Card)
- **Concurrency Handling**: Basic concurrency simulation for inventory reservation

## Architecture

### Core Classes
- **ProductItem**: Represents a product with ID, name, and price
- **CartItem**: Represents an item in the cart with quantity
- **Cart**: Manages cart items, calculates total
- **Inventory**: Thread-safe stock management using ConcurrentHashMap
- **InventoryManager**: Handles stock reservation
- **Order**: Represents an order with items, total, and status
- **OrderItem**: Individual item in an order
- **OrderManager**: Manages order storage and retrieval
- **PaymentStrategy**: Interface for payment methods
- **CreditCard/DebitCard**: Concrete payment implementations

### Design Patterns Used
- **Strategy Pattern**: For payment methods
- **Factory Pattern**: (Not implemented in this version, but could be extended)
- **Singleton Pattern**: (Not implemented, but inventory could be singleton)

## How to Run
1. Ensure Java is installed on your system
2. Navigate to the project directory: `15. E-Commerce_Application/`
3. Compile the code: `javac Main.java`
4. Run the application: `java Main`

## Sample Output
```
Cart Total: 4000.0
Paid 4000.0 via Credit Card
Order Status: SUCCESS
Total Orders Stored: 1
Out of stock for P2
Second Order Status: FAILED
```

## Assumptions
- Inventory is managed in-memory
- No database persistence
- Basic concurrency handling (not production-ready)
- Payment is simulated (no real transactions)

## Future Enhancements
- Add database integration
- Implement user authentication
- Add more payment methods
- Enhance concurrency with proper locking
- Add order history and tracking
- Implement discount and coupon system