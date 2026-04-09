package strategies;

public class UpiPaymentStrategy implements PaymentStrategy {
    int upiId;

    public UpiPaymentStrategy(int upiId) {
        this.upiId = upiId;
    }

    @Override
    public void pay(double amount) {
        // Logic to process UPI payment
        System.out.println("Processing UPI payment of $" + amount + " using UPI ID: " + upiId);
    }
}
