package models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<MenuItem> items;
    private Restaurant restaurant;

    public Cart() {
        this.items = new ArrayList<>();
        this.restaurant = null;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }    

    public void clear() {
        items.clear();
        restaurant = null;
    }

    public boolean isEmpty() {
        return restaurant == null || items.isEmpty();
    }
}
