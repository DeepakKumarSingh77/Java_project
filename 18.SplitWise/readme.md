# SplitWise Application

## Description
This is a Java-based implementation of a SplitWise-like expense sharing application. It allows users to create groups, add expenses, split bills among members using different strategies (equal, exact amounts, or percentages), track balances, and simplify debts. The application demonstrates various design patterns and object-oriented principles.

## Features
- **User Management**: Create users with names and emails
- **Group Management**: Create groups and add/remove members
- **Expense Splitting**: Support for different split types:
  - Equal split among all involved users
  - Exact amounts for each user
  - Percentage-based splits
- **Balance Tracking**: Track individual and group balances
- **Debt Simplification**: Minimize the number of transactions needed to settle debts
- **Notifications**: Observer pattern for notifying users about new expenses and settlements
- **Settlement**: Record payments between users to clear debts

## Design Patterns Used
- **Singleton Pattern**: `Splitwise` class ensures only one instance of the expense manager
- **Factory Pattern**: `SplitFactory` creates appropriate split strategies
- **Strategy Pattern**: Different splitting algorithms (`EqualSplit`, `ExactSplit`, `PercentageSplit`)
- **Observer Pattern**: Users are notified about group activities
- **Facade Pattern**: `Splitwise` class provides a simplified interface for complex operations

## Class Structure
- `SplitwiseApp`: Main class with demonstration
- `Splitwise`: Singleton facade for managing users, groups, and expenses
- `User`: Represents a user with balances and notification capabilities
- `Group`: Manages group members, expenses, and balances
- `Expense`: Represents an expense with splits
- `Split`: Individual split for a user
- `SplitStrategy` and implementations: Handle different splitting logic
- `DebtSimplifier`: Algorithm to minimize transactions for debt settlement

## How to Run
1. Ensure you have Java installed (JDK 8 or higher)
2. Navigate to the project directory: `18.SplitWise/`
3. Compile the code: `javac SplitwiseApp.java`
4. Run the application: `java SplitwiseApp`

## Sample Output
The application demonstrates:
- Creating users and a group
- Adding expenses with different split types
- Displaying balances
- Debt simplification
- Settlements and user removal from groups

## Technologies Used
- Java
- Design Patterns (Singleton, Factory, Strategy, Observer, Facade)

## Future Enhancements
- Persistence layer for storing data
- Web interface
- Mobile app integration
- Advanced reporting features