# Discount Coupon System

A Java demonstration of a shopping cart coupon and discount engine using design patterns such as Strategy and Chain of Responsibility.

## Overview

This project simulates a retail checkout flow where multiple coupon rules are evaluated and applied to a cart. The system supports:

- Flat amount discounts
- Percentage discounts
- Percentage discounts with a capped maximum
- Loyalty-based discounts
- Seasonal category-based discounts
- Bulk purchase discounts
- Bank-specific payment discounts

## Design Patterns Used

- **Strategy Pattern**: Encapsulates discount calculations in separate strategy classes (`FlatDiscountStrategy`, `PercentageDiscountStrategy`, `PercentageWithCapStrategy`) and chooses the correct strategy at runtime.
- **Chain of Responsibility Pattern**: Models coupon evaluation as a chain of `Coupon` handlers. Each coupon can apply its discount and optionally pass processing to the next coupon.
- **Singleton Pattern**: Uses `CouponManager` and `DiscountStrategyManager` as singletons to ensure centralized management of coupon registration and discount strategy creation.

## Key Classes

- `DiscountCoupon` - Main entry point with sample products and cart configuration.
- `Cart`, `CartItem`, `Product` - Models for cart contents and pricing.
- `Coupon` - Abstract base class for coupon rules.
- `SeasonalOffer`, `LoyaltyDiscount`, `BulkPurchaseDiscount`, `BankingCoupon` - Concrete coupon implementations.
- `CouponManager` - Registers coupons and applies them in order.
- `DiscountStrategyManager` - Builds discount strategies for coupons.

## How to Run

From the `24.DiscountCoupon` directory, compile and run:

```bash
javac DiscountCoupon.java
java DiscountCoupon
```

## Example Behavior

The sample `main` method creates a shopping cart, adds several products, marks the customer as a loyalty member, and applies a bank-specific coupon. It prints:

- Original cart total
- Applicable coupons
- Final cart total after discounts

## Extending the Project

To add a new coupon type:

1. Create a new class extending `Coupon`.
2. Implement `isApplicable`, `getDiscount`, and `name`.
3. Register the coupon in `DiscountCoupon.main` using `CouponManager`.

To add a new discount calculation style:

1. Add a new `DiscountStrategy` implementation.
2. Update `DiscountStrategyManager` to return the strategy for a new `StrategyType`.

## Notes

- Coupon order matters because some coupons may stop further processing when they are not combinable.
- `Cart` tracks both original and current totals so discounts are evaluated correctly.
- The project is designed for clarity and easy extension rather than production-level persistence or UI.
