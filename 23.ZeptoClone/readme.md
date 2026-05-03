# ZeptoClone

A simplified Java-based low-level design of a Zepto-like grocery fulfillment system. This project demonstrates inventory management, dark store selection, order placement, and replenishment strategies using object-oriented design patterns.

## Features

- Inventory management with `InventoryManager` and `DbInventoryStore`
- Support for multiple dark stores and proximity-based store selection
- Automatic order splitting across multiple stores when a single store cannot fulfil all items
- Replenishment strategies via `ReplenishStrategy` interface
- Simple cart and order flow for a user

## Architecture

- `Product` / `ProductFactory`: models product catalog and simplified product creation
- `InventoryStore` / `DbInventoryStore`: inventory persistence abstraction
- `InventoryManager`: coordinates stock operations and product availability
- `DarkStore`: represents a fulfillment center with its own inventory and replenishment policy
- `DarkStoreManager`: singleton manager for nearby store lookup
- `OrderManager`: singleton responsible for placing orders and assigning delivery partners
- `Cart` / `User` / `Order`: basic shopping flow models

## How to Run

1. Open a terminal in `d:\coding\LLD_PROJECTS\23.ZeptoClone`
2. Compile the Java source:

```bash
javac ZeptoClone.java
```

3. Run the application:

```bash
java ZeptoClone
```

## Demo Flow

- Initializes three dark stores with sample stock
- Creates a user located at `(1.0, 1.0)` and shows available products within 5 KM
- Adds items to the user's cart
- Places an order and prints a summary, including partner assignment and fulfillment details

## Notes

- This is a prototype-style implementation meant for educational/LLD purposes.
- The inventory factory and replenishment strategies can be extended for more realistic business rules.
- No external database or UI is used; all data is stored in memory.
