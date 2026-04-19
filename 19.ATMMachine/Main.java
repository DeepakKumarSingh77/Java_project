import java.util.HashMap;
import java.util.Map;

enum ATMState {
    IDLE,
    CARD_INSERTED,
    AUTHENTICATED
}

class BankService {
    private Map<Integer, Integer> pinDB = new HashMap<>();
    private Map<Integer, Integer> balanceDB = new HashMap<>();

    public void addAccount(int card, int pin, int balance) {
        pinDB.put(card, pin);
        balanceDB.put(card, balance);
    }

    public boolean authenticate(int card, int pin) {
        return pinDB.containsKey(card) && pinDB.get(card) == pin;
    }

    public int getBalance(int card) {
        return balanceDB.getOrDefault(card, 0);
    }

    public boolean hasBalance(int card, int amount) {
        return balanceDB.getOrDefault(card, 0) >= amount;
    }

    public void debit(int card, int amount) {
        balanceDB.put(card, balanceDB.get(card) - amount);
    }

    public void credit(int card, int amount) {
        balanceDB.put(card, balanceDB.getOrDefault(card, 0) + amount);
    }
}

abstract class MoneyHandler {
    protected MoneyHandler nextHandler;

    public MoneyHandler(MoneyHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    abstract boolean canDispense(int amount);
    abstract void dispense(int amount);
}

class ThousandHandler extends MoneyHandler {
    private int noteCnt;

    public ThousandHandler(int count, MoneyHandler next) {
        super(next);
        this.noteCnt = count;
    }

    @Override
    boolean canDispense(int amount) {
        int required = amount / 1000;
        int used = Math.min(required, noteCnt);
        int remaining = amount - used * 1000;

        if (remaining == 0) return true;
        if (nextHandler != null) return nextHandler.canDispense(remaining);
        return false;
    }

    @Override
    void dispense(int amount) {
        int required = amount / 1000;
        int used = Math.min(required, noteCnt);

        if (used > 0) {
            System.out.println("Dispensing " + used + " x 1000");
            noteCnt -= used;
        }

        int remaining = amount - used * 1000;
        if (remaining > 0 && nextHandler != null) {
            nextHandler.dispense(remaining);
        }
    }
}

class FiveHundredHandler extends MoneyHandler {
    private int noteCnt;

    public FiveHundredHandler(int count, MoneyHandler next) {
        super(next);
        this.noteCnt = count;
    }

    @Override
    boolean canDispense(int amount) {
        int required = amount / 500;
        int used = Math.min(required, noteCnt);
        int remaining = amount - used * 500;

        if (remaining == 0) return true;
        if (nextHandler != null) return nextHandler.canDispense(remaining);
        return false;
    }

    @Override
    void dispense(int amount) {
        int required = amount / 500;
        int used = Math.min(required, noteCnt);

        if (used > 0) {
            System.out.println("Dispensing " + used + " x 500");
            noteCnt -= used;
        }

        int remaining = amount - used * 500;
        if (remaining > 0 && nextHandler != null) {
            nextHandler.dispense(remaining);
        }
    }
}

class OneHundredHandler extends MoneyHandler {
    private int noteCnt;

    public OneHundredHandler(int count, MoneyHandler next) {
        super(next);
        this.noteCnt = count;
    }

    @Override
    boolean canDispense(int amount) {
        int required = amount / 100;
        int used = Math.min(required, noteCnt);
        int remaining = amount - used * 100;

        if (remaining == 0) return true;
        if (nextHandler != null) return nextHandler.canDispense(remaining);
        return false;
    }

    @Override
    void dispense(int amount) {
        int required = amount / 100;
        int used = Math.min(required, noteCnt);

        if (used > 0) {
            System.out.println("Dispensing " + used + " x 100");
            noteCnt -= used;
        }

        int remaining = amount - used * 100;
        if (remaining > 0 && nextHandler != null) {
            nextHandler.dispense(remaining);
        }
    }
}

class FiftyHandler extends MoneyHandler {
    private int noteCnt;

    public FiftyHandler(int count, MoneyHandler next) {
        super(next);
        this.noteCnt = count;
    }

    @Override
    boolean canDispense(int amount) {
        int required = amount / 50;
        int used = Math.min(required, noteCnt);
        int remaining = amount - used * 50;

        if (remaining == 0) return true;
        if (nextHandler != null) return nextHandler.canDispense(remaining);
        return false;
    }

    @Override
    void dispense(int amount) {
        int required = amount / 50;
        int used = Math.min(required, noteCnt);

        if (used > 0) {
            System.out.println("Dispensing " + used + " x 50");
            noteCnt -= used;
        }

        int remaining = amount - used * 50;
        if (remaining > 0 && nextHandler != null) {
            nextHandler.dispense(remaining);
        }
    }
}

class ATMMachine {
    private ATMState state;
    private int currentCard;
    private BankService bankService;
    private MoneyHandler moneyHandler;

    public ATMMachine(BankService bankService, MoneyHandler handler) {
        this.state = ATMState.IDLE;
        this.bankService = bankService;
        this.moneyHandler = handler;
    }

    public void insertCard(int card) {
        if (state != ATMState.IDLE) {
            System.out.println("Machine busy");
            return;
        }
        currentCard = card;
        state = ATMState.CARD_INSERTED;
        System.out.println("Card inserted");
    }

    public void enterPin(int pin) {
        if (state != ATMState.CARD_INSERTED) {
            System.out.println("Insert card first");
            return;
        }

        if (bankService.authenticate(currentCard, pin)) {
            state = ATMState.AUTHENTICATED;
            System.out.println("Authenticated");
        } else {
            System.out.println("Invalid PIN");
        }
    }

    public void checkBalance() {
        if (state != ATMState.AUTHENTICATED) {
            System.out.println("Not authenticated");
            return;
        }

        System.out.println("Balance: " + bankService.getBalance(currentCard));
    }

    public void deposit(int amount) {
        if (state != ATMState.AUTHENTICATED) {
            System.out.println("Not authenticated");
            return;
        }

        bankService.credit(currentCard, amount);
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(int amount) {
        if (state != ATMState.AUTHENTICATED) {
            System.out.println("Not authenticated");
            return;
        }

        if (!bankService.hasBalance(currentCard, amount)) {
            System.out.println("Insufficient balance");
            return;
        }

        if (!moneyHandler.canDispense(amount)) {
            System.out.println("ATM cannot dispense this amount");
            return;
        }

        bankService.debit(currentCard, amount);
        moneyHandler.dispense(amount);

        System.out.println("Withdrawal successful");
    }
}

public class Main {
    public static void main(String[] args) {

        BankService bankService = new BankService();
        bankService.addAccount(1234, 1111, 10000); // card, pin, balance

        MoneyHandler fifty = new FiftyHandler(10, null);
        MoneyHandler hundred = new OneHundredHandler(10, fifty);
        MoneyHandler fiveHundred = new FiveHundredHandler(10, hundred);
        MoneyHandler thousand = new ThousandHandler(10, fiveHundred);

        ATMMachine atm = new ATMMachine(bankService, thousand);

        System.out.println("---- ATM Simulation ----");

        atm.insertCard(1234);
        atm.enterPin(1111);

        atm.checkBalance();

        System.out.println("\n--- Withdraw 3700 ---");
        atm.withdraw(3700);

        atm.checkBalance();

        System.out.println("\n--- Deposit 2000 ---");
        atm.deposit(2000);

        atm.checkBalance();

        System.out.println("\n--- Withdraw 15000 (should fail) ---");
        atm.withdraw(15000);

        System.out.println("\n--- Withdraw 1250 (check denomination logic) ---");
        atm.withdraw(1250);
    }
}