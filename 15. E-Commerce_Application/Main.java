import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;

class ProductItem {
    private String productId;
    private String name;
    private double price;

    public ProductItem(String productId, String name, double price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}

class CartItem {
    private ProductItem productItem;
    private int quantity;

    public CartItem(ProductItem productItem, int quantity) {
        this.productItem = productItem;
        this.quantity = quantity;
    }

    public ProductItem getProductItem() { return productItem; }
    public int getQuantity() { return quantity; }

    public void addQuantity(int qty) {
        this.quantity += qty;
    }

    public double getTotalPrice() {
        return productItem.getPrice() * quantity;
    }
}

class Cart {
    private String cartId;
    private List<CartItem> items = new ArrayList<>();

    public void addItem(ProductItem productItem, int quantity) {
        for (CartItem item : items) {
            if (item.getProductItem().getProductId()
                    .equals(productItem.getProductId())) {
                item.addQuantity(quantity);
                return;
            }
        }
        items.add(new CartItem(productItem, quantity));
    }

    public void removeItem(String productId) {
        items.removeIf(item ->
            item.getProductItem().getProductId().equals(productId)
        );
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}

class Inventory {
    private ConcurrentHashMap<String, Integer> stockMap = new ConcurrentHashMap<>();

    public synchronized boolean reserveStock(String productId, int quantity) {
        int currentStock = stockMap.getOrDefault(productId, 0);

        if (currentStock < quantity) {
            return false;
        }

        stockMap.put(productId, currentStock - quantity);
        return true;
    }

    public void addStock(String productId, int quantity) {
        stockMap.put(productId,
            stockMap.getOrDefault(productId, 0) + quantity);
    }
}

class InventoryManager {
    private Inventory inventory;

    public InventoryManager(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean reserve(String productId, int quantity) {
        return inventory.reserveStock(productId, quantity);
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCard implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via Credit Card");
    }
}

class DebitCard implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via Debit Card");
    }
}


class Order {
    private String orderId;
    private List<OrderItem> items = new ArrayList<>();
    private double totalAmount;
    private OrderStatus status;

    public Order(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            items.add(new OrderItem(
                cartItem.getProductItem().getProductId(),
                cartItem.getQuantity(),
                cartItem.getProductItem().getPrice()
            ));
        }

        totalAmount = cart.getTotal();
        status = OrderStatus.CREATED;
    }

    public boolean checkout(PaymentStrategy paymentStrategy,
                            InventoryManager inventoryManager) {

        //Reserve stock atomically
        for (OrderItem item : items) {
            boolean success = inventoryManager.reserve(
                item.getProductId(),
                item.getQuantity()
            );

            if (!success) {
                System.out.println("Out of stock for " + item.getProductId());
                return false;
            }
        }

        //Payment
        paymentStrategy.pay(totalAmount);

        //Confirm
        status = OrderStatus.CONFIRMED;
        return true;
    }
}


class OrderManager {
    private Map<String, Order> orders = new HashMap<>();

    public void saveOrder(String orderId, Order order) {
        orders.put(orderId, order);
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getOrdersByUser() {
        return new ArrayList<>(orders.values());
    }
}

class OrderItem {
    private String productId;
    private int quantity;
    private double priceAtPurchase;

    public OrderItem(String productId, int quantity, double priceAtPurchase) {
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public double getTotalPrice() {
        return quantity * priceAtPurchase;
    }
}

enum OrderStatus {
    CREATED,
    CONFIRMED,
    FAILED
}

public class Main {
    public static void main(String[] args) {

        //Create Products
        ProductItem p1 = new ProductItem("P1", "iPhone", 1000);
        ProductItem p2 = new ProductItem("P2", "Laptop", 2000);

        //Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addStock("P1", 5);
        inventory.addStock("P2", 2);

        InventoryManager inventoryManager = new InventoryManager(inventory);

        //Create Cart
        Cart cart = new Cart();
        cart.addItem(p1, 2);
        cart.addItem(p2, 1);

        System.out.println("Cart Total: " + cart.getTotal());

        //Create Order
        Order order = new Order(cart);

        //Payment Method
        PaymentStrategy payment = new CreditCard();

        //Checkout
        boolean success = order.checkout(payment, inventoryManager);

        System.out.println("Order Status: " + (success ? "SUCCESS" : "FAILED"));

        //Save Order
        OrderManager orderManager = new OrderManager();
        orderManager.saveOrder("O1", order);

        //Fetch Orders
        System.out.println("Total Orders Stored: " +
                orderManager.getOrdersByUser().size());

        //Try Overselling (Concurrency Simulation Basic)
        Cart cart2 = new Cart();
        cart2.addItem(p2, 5); // more than stock

        Order order2 = new Order(cart2);
        boolean success2 = order2.checkout(payment, inventoryManager);

        System.out.println("Second Order Status: " +
                (success2 ? "SUCCESS" : "FAILED"));
    }
}
