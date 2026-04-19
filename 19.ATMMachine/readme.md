# ATM Machine Simulation

## Description
This is a Java-based simulation of an ATM (Automated Teller Machine) system, demonstrating Low-Level Design (LLD) principles. It includes features like card insertion, PIN authentication, balance checking, deposits, and withdrawals with denomination handling.

## Features
- Card insertion and PIN authentication
- Balance inquiry
- Cash withdrawal with denomination dispensing (1000, 500, 100, 50 rupees)
- Cash deposit
- State management for ATM operations
- Chain of Responsibility pattern for money dispensing

## Design Patterns Used
- **State Pattern**: Manages the ATM's states (IDLE, CARD_INSERTED, AUTHENTICATED)
- **Chain of Responsibility**: Handles dispensing money in different denominations

## Classes Overview
- `ATMState`: Enum for ATM states
- `BankService`: Manages user accounts, authentication, and transactions
- `MoneyHandler`: Abstract class for denomination handlers
  - `ThousandHandler`, `FiveHundredHandler`, `OneHundredHandler`, `FiftyHandler`: Concrete handlers for each denomination
- `ATMMachine`: Main ATM class handling user interactions
- `Main`: Entry point with simulation demo

## How to Run
1. Ensure Java is installed on your system.
2. Compile the code: `javac Main.java`
3. Run the program: `java Main`

## Sample Output
```
---- ATM Simulation ----
Card inserted
Authenticated
Balance: 10000

--- Withdraw 3700 ---
Dispensing 3 x 1000
Dispensing 1 x 500
Dispensing 2 x 100
Withdrawal successful
Balance: 6300

--- Deposit 2000 ---
Deposited: 2000
Balance: 8300

--- Withdraw 15000 (should fail) ---
Insufficient balance

--- Withdraw 1250 (check denomination logic) ---
Dispensing 1 x 1000
Dispensing 1 x 100
Dispensing 1 x 50
Dispensing 1 x 100
Withdrawal successful
```