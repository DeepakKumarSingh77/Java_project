import java.util.List;
import models.*;
import managers.RestaurantManager;
import managers.OrderManager;
import strategies.*;
import factories.*;
import services.NotificationService;

public class FoodDeliveryApp {

        public FoodDeliveryApp() {
            initializeRestaurants();
        }

        void initializeRestaurants() {
                    RestaurantManager manager = RestaurantManager.getInstance();

    Restaurant restaurant1 = new Restaurant("Pasta Palace", "Delhi", "555-1234");
    restaurant1.addMenuItem(new MenuItem("P1", "Spaghetti Carbonara", 12.99, "Classic Italian pasta"));
    restaurant1.addMenuItem(new MenuItem("P2", "Fettuccine Alfredo", 11.99, "Creamy Alfredo"));
    manager.addRestaurant(restaurant1);   // ✅ ADD THIS

    Restaurant restaurant2 = new Restaurant("Gourmet Haven", "Delhi", "555-5678");
    restaurant2.addMenuItem(new MenuItem("P1","Sushi", 15.99, "Fresh sushi"));
    manager.addRestaurant(restaurant2);   // ✅ ADD THIS

    Restaurant restaurant3 = new Restaurant("Vegan Delights", "Delhi", "555-9012");
    restaurant3.addMenuItem(new MenuItem("V1", "Vegan Burger", 9.99, "Plant-based burger"));
    manager.addRestaurant(restaurant3); 
        }

        List<Restaurant> searchRestaurants(String loc) {
            return RestaurantManager.getInstance().searchRestaurant(loc);
        }

        public void selectRestaurant(User user, Restaurant restaurant) {
            Cart cart=user.getCart();
            cart.setRestaurant(restaurant);
        }

        public void addToCart(User user, String itemCode) {
        Restaurant restaurant = user.getCart().getRestaurant();
        if (restaurant == null) {
            System.out.println("Please select a restaurant first.");
            return;
        }
        for (MenuItem item : restaurant.getMenuItems()) {
            if (item.getCode().equals(itemCode)) {
                user.getCart().addItem(item);
                break;
            }
        }
    }

    public Order checkoutNow(User user, String orderType, PaymentStrategy paymentStrategy) {
        return checkout(user, orderType, paymentStrategy, new NowOrderFactory());
    }

    public Order checkoutScheduled(User user, String orderType, PaymentStrategy paymentStrategy, String scheduleTime) {
        return checkout(user, orderType, paymentStrategy, new ScheduledOrderFactory(scheduleTime));
    }

    public Order checkout(User user, String orderType, PaymentStrategy paymentStrategy, OrderFactory orderFactory) {
        if (user.getCart().isEmpty()) return null;

        Cart userCart = user.getCart();
        Restaurant orderedRestaurant = userCart.getRestaurant();
        List<MenuItem> itemsOrdered = userCart.getItems();
        double totalCost = userCart.getTotalPrice();

        Order order = orderFactory.createOrder(user, userCart, orderedRestaurant, itemsOrdered, paymentStrategy, totalCost, orderType);
        OrderManager.getInstance().addOrder(order);
        return order;
    }

     public void payForOrder(User user, Order order) {
        boolean isPaymentSuccess = order.processPayment();

        if (isPaymentSuccess) {
            NotificationService.notify(order);
        }
    }

    public void printUserCart(User user) {
        System.out.println("Items in cart:");
        System.out.println("------------------------------------");
        for (MenuItem item : user.getCart().getItems()) {
            System.out.println(item.getCode() + " : " + item.getName() + " : ₹" + item.getPrice());
        }
        System.out.println("------------------------------------");
        System.out.println("Grand total : ₹" + user.getCart().getTotalPrice());
    }
        
}
