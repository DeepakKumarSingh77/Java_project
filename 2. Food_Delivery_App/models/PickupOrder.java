package models;

public class PickupOrder extends Order {
        private String restaurantAddress;

        public PickupOrder(){
            restaurantAddress="";
        }

        public String getRestaurantAddress() {
            return restaurantAddress;
        }

        public void setRestaurantAddress(String restaurantAddress) {
            this.restaurantAddress = restaurantAddress;
        }

        @Override
        public String getOrderType(){
            return "Pickup";
        }
    
}
