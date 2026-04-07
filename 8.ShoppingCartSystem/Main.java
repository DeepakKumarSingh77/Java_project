import java.util.HashMap;
import java.util.Map;

class Product {
    private String productId;
    private String name;
    private double price;

    public Product(String productId, String name, double price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

class CartItem {
    private Product product;
    private int quantity;
    private double priceAtAddition;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtAddition = product.getPrice(); // snapshot
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return priceAtAddition * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}


interface DiscountStrategy {
    double applyDiscount(double totalAmount);
}

class PercentageDiscount implements DiscountStrategy {
    private double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - (totalAmount * percentage / 100);
    }
}


class FlatDiscount implements DiscountStrategy {
    private double discountAmount;

    public FlatDiscount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - discountAmount;
    }
}


class Cart {
    private Map<String, CartItem> items;
    private DiscountStrategy discountStrategy;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void addItem(Product product, int quantity) {
        String productId = product.getProductId();

        if (items.containsKey(productId)) {
            CartItem item = items.get(productId);
            item.updateQuantity(item.getQuantity() + quantity);
        } else {
            items.put(productId, new CartItem(product, quantity));
        }
    }

    public void removeItem(Product product) {
        items.remove(product.getProductId());
    }

    public void updateItem(Product product, int quantity) {
        if (items.containsKey(product.getProductId())) {
            items.get(product.getProductId()).updateQuantity(quantity);
        }
    }

    public double getTotalPrice() {
        double total = 0;

        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }

        if (discountStrategy != null) {
            total = discountStrategy.applyDiscount(total);
        }

        return total;
    }

    public Map<String, CartItem> getItems() {
        return items;
    }
}


abstract class User {
    protected String userId;
    protected Cart cart;

    public User(String userId) {
        this.userId = userId;
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }
}

class RegisteredUser extends User {

    public RegisteredUser(String userId) {
        super(userId);
    }
}


class GuestUser extends User {
    private String sessionId;

    public GuestUser(String sessionId) {
        super(null);
        this.sessionId = sessionId;
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class DebitCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Debit Card");
    }
}

class UpiPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}


class PaymentService {
    public void processPayment(PaymentStrategy strategy, double amount) {
        strategy.pay(amount);
    }
}

class CheckoutService {

    private PaymentService paymentService;

    public CheckoutService() {
        this.paymentService = new PaymentService();
    }

    public void validateCart(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }
    }

    public double calculateFinalPrice(Cart cart) {
        return cart.getTotalPrice();
    }

    public void processPayment(Cart cart, PaymentStrategy strategy) {
        validateCart(cart);
        double amount = calculateFinalPrice(cart);
        paymentService.processPayment(strategy, amount);
    }
}


public class Main {
    public static void main(String[] args) {

        Product p1 = new Product("1", "Laptop", 1000);
        Product p2 = new Product("2", "Mouse", 50);

        User user = new RegisteredUser("user1");

        Cart cart = user.getCart();
        cart.addItem(p1, 1);
        cart.addItem(p2, 2);

        cart.setDiscountStrategy(new PercentageDiscount(10));

        CheckoutService checkoutService = new CheckoutService();

        checkoutService.processPayment(cart, new CreditCardPayment());
    }
}