package managers;

import java.util.ArrayList;
import java.util.List;
import models.Cart;
import models.Order;

public class OrderManager {
    private List<Order> orders = new ArrayList<>();
    private static OrderManager instance = null;

    private OrderManager() {
        // Private Constructor
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void listOrders() {
        System.out.println("\n--- All Orders ---");
        for (Order order : orders) {
            System.out.println(order.getOrderType() + " order for " + order.getUser().getName()
                    + " | Total: ₹" + order.getTotalPrice()
                    + " | At: " + order.getScheduled());
        }
    }
}