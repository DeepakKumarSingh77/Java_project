package models;

public class DeliveryOrder extends Order {
    private String userAddress;

    public DeliveryOrder() {
        userAddress = "";
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    @Override
    public String getOrderType() {
        return "Delivery";
    }
}
