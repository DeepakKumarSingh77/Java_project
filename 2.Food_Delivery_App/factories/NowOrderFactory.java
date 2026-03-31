package factories;

import java.util.List;

import models.*;
import strategies.*;

public class NowOrderFactory implements OrderFactory {
    @Override
    public Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
                             PaymentStrategy paymentStrategy, double totalCost, String orderType) {
        Order order=null;
        if(orderType.equalsIgnoreCase("Delivery")) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserAddress(user.getAddress());
            order = deliveryOrder;
        } else if(orderType.equalsIgnoreCase("Pickup")) {
            PickupOrder pickupOrder = new PickupOrder();
            pickupOrder.setRestaurantAddress(restaurant.getAddress());
            order = pickupOrder;
        }
        order.setUser(user);
        order.setItems(menuItems);
        order.setRestaurant(restaurant);
        order.setTotalPrice(totalCost);
        order.setOrderType(orderType);
        order.setPaymentStrategy(paymentStrategy);
        return order;
    }   
    
}
