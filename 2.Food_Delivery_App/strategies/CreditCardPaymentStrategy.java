package strategies;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    String cardNumber;

    public CreditCardPaymentStrategy(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        // Logic to process credit card payment
        System.out.println("Processing credit card payment of $" + amount + " using card number: " + cardNumber);
    }
}
