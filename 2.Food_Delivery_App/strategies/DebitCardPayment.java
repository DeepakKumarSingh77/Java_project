package strategies;

public class DebitCardPayment implements PaymentStrategy {
    String cardNumber;

    public DebitCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    @Override
    public void pay(double amount) {
        // Logic to process debit card payment
        System.out.println("Processing debit card payment of $" + amount + " using card number: " + cardNumber);
    }
}
