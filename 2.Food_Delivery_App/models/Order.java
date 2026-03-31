package models;

import java.util.List;
import strategies.PaymentStrategy;

public abstract class Order {

    protected User user;
    protected List<MenuItem> items;
    protected Restaurant restaurant;
    protected double totalPrice;
    protected String orderType; // e.g., "Delivery" or "Pickup"
    protected PaymentStrategy paymentStrategy;
    protected String scheduled;

    public Order() {
        this.user = null;
        this.items = null;
        this.restaurant = null;
        this.totalPrice = 0;
        this.orderType = null;
        this.paymentStrategy = null;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public abstract String getOrderType();

     public void setScheduled(String s) {
        scheduled = s;
    }

    public String getScheduled() {
        return scheduled;
    }

    public User getUser() {
        return user;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

      public boolean processPayment() {
        if (paymentStrategy != null) {
            paymentStrategy.pay(totalPrice);
            return true;
        } else {
            System.out.println("Please choose a payment mode first");
            return false;
        }
    }
}
