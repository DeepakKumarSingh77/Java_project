# Library Management System - Low Level Design (LLD)

## Overview
This project models a simple Library Management System in Java. It demonstrates a clean object-oriented design with support for members, librarians, books, book copies, issuing/returning books, fines, and payment processing.

The system is intended for learning how real-world library workflows can be represented using classes, interfaces, and design patterns.

## Design Patterns Used

### 1. **Strategy Pattern**
- **Purpose**: Encapsulates algorithms behind a common interface and allows them to be swapped easily.
- **Implementation**: Used for fine calculation and payment.
- **Classes**: `FineStrategy`, `DailyFineStrategy`, `PaymentStrategy`, `UpiPayment`, `CardPayment`, `CashPayment`.

### 2. **Factory-like Roles**
- **Purpose**: Separates responsibilities between types of users and operations.
- **Implementation**: `Librarian` adds books and copies, while `Member` borrows and returns books.
- **Classes**: `Member`, `Librarian`, `AuthService`.

## Key Components

### Entities
- `User`: Abstract base for `Member` and `Librarian`.
- `Member`: Holds borrowed loans and performs borrow/return operations.
- `Librarian`: Adds/removes books and book copies.
- `Book`: Represents book metadata and its copies.
- `BookCopy`: Represents an individual physical copy with a status.
- `Loan`: Tracks issued books, issue date, due date, return date, fines, and payment status.
- `Status`: Enum with `AVAILABLE` and `ISSUED`.

### Services
- `LibraryManagement`: Core service that manages users, books, loans, search, issuing, and returns.
- `AuthService`: Simple authentication service for registering and logging in users.

### Payment & Fine
- `FineStrategy`: Calculates fine amount for late returns.
- `DailyFineStrategy`: Charges a flat daily late fee.
- `PaymentStrategy`: Supports multiple payment methods.
- `UpiPayment`, `CardPayment`, `CashPayment`: Concrete payment implementations.

## How to Run
1. Ensure Java is installed (JDK 8 or higher).
2. Open terminal in `7.Library_Management_System`.
3. Compile the project:
   ```bash
   javac Main.java
   ```
4. Run the program:
   ```bash
   java Main
   ```

## Example Output
```
Book added: Java Programming

Search by title 'Java':
Java Programming by James Gosling

Issuing book...
Book issued successfully

Returning book...
Fine: 50.0
Paid via Cash: 50.0

Final Status:
Book Copy Status: AVAILABLE
Fine Paid: true
```

## Learning Points
- **Beginners**: Understand how library operations map to classes and methods.
- **Intermediate**: Learn how strategies decouple fine calculation and payment processing.
- **Advanced**: Explore how the system can be extended with authentication, inventory tracking, and persistence.

## Potential Extensions
- Add a proper authentication flow using `AuthService`.
- Store books, users, and loans in a database instead of in-memory lists.
- Add UI/CLI menus for user interaction.
- Support multiple copies per book with copy-level tracking.
- Add reservation and renewal functionality.
- Add membership plans with different fine rules.

This README is designed to help both beginners and advanced developers understand the architecture and behavior of the library management system.