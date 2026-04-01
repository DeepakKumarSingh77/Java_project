import java.util.*;

/*============================
      Notification & Decorators
=============================*/

interface INotification{
       String getContent();
}

// Concrete Notification: simple text notification.
class SimpleNotification implements INotification{
    private String content;

    public SimpleNotification(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}

// Abstract Decorator: wraps a Notification object.
abstract class NotificationDecorator implements INotification{
    protected INotification notification;

    public NotificationDecorator(INotification notification) {
        this.notification = notification;
    }

    @Override
    public String getContent() {
        return notification.getContent();
    }
}

// Decorator to add a timestamp to the content.
class TimestampDecorator extends NotificationDecorator{
    public TimestampDecorator(INotification notification) {
        super(notification);
    }

    @Override
    public String getContent() {
        return "[" + new Date() + "] " + notification.getContent();
    }
}

// Decorator to append a signature to the content.
class SignatureDecorator extends NotificationDecorator{
    private String signature;

    public SignatureDecorator(INotification notification, String signature) {
        super(notification);
        this.signature = signature;
    }

    @Override
    public String getContent() {
        return notification.getContent() + "\n-- " + signature;
    }
}


/*============================
  Observer Pattern Components
=============================*/

// Observer interface: each observer gets an update with a Notification pointer.
interface IObserver{
    void update();
}

interface IObservable{
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers();
}

// Concrete Observable
class NotificationObservable implements IObservable{
    private List<IObserver> observers = new ArrayList<>();
    private INotification notification;

    public void setNotification(INotification notification) {
        this.notification = notification;
        notifyObservers();
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    public INotification getNotification() {
        return notification;
    }

    public void setNotificationContent(INotification notification) {
            this.notification=notification;
            notifyObservers();
    }

    public String getNotificationContent() {
        return notification.getContent();
    }
}

/*============================
       NotificationService
=============================*/

// The NotificationService manages notifications. It keeps track of notifications. 
// Any client code will interact with this service.

//Singleton class
class NotificationService{
    private static NotificationService instance=null;
    private NotificationObservable observable;
    private List<INotification> notifications=new ArrayList<>();
    private NotificationService() {
        observable = new NotificationObservable();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

     // Expose the observable so observers can attach.
    public NotificationObservable getObservable() {
        return observable;
    }

    // Creates a new Notification and notifies observers.
    public void sendNotification(INotification notification) {
        notifications.add(notification);
        observable.setNotification(notification);
    }
}

/*============================
       ConcreteObservers
=============================*/
class Logger implements IObserver {
    private NotificationObservable notificationObservable;

    public Logger() {
        this.notificationObservable = NotificationService.getInstance().getObservable();
        notificationObservable.addObserver(this);
    }

    public Logger(NotificationObservable observable) {
        notificationObservable.addObserver(this);
        this.notificationObservable = observable;
    }

    public void update() {
        System.out.println("Logging New Notification : \n" + notificationObservable.getNotificationContent());
    }
}

/*============================
  Strategy Pattern Components (Concrete Observer 2)
=============================*/

interface INotificationStrategy {
    void sendNotification(String content);
}

class EmailStrategy implements INotificationStrategy {
    private String emailId;

    public EmailStrategy(String emailId) {
        this.emailId = emailId;
    }

    public void sendNotification(String content) {
        // Simulate the process of sending an email notification, 
        // representing the dispatch of messages to users via email.​
        System.out.println("Sending email Notification to: " + emailId + "\n" + content);
    }
}


class SMSStrategy implements INotificationStrategy {
    private String mobileNumber;

    public SMSStrategy(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void sendNotification(String content) {
        // Simulate the process of sending an SMS notification, 
        // representing the dispatch of messages to users via SMS.​
        System.out.println("Sending SMS Notification to: " + mobileNumber + "\n" + content);
    }
}

class PopUpStrategy implements INotificationStrategy {
    public void sendNotification(String content) {
        // Simulate the process of sending popup notification.
        System.out.println("Sending Popup Notification: \n" + content);
    }
}


class NotificationEngine implements IObserver {
    private NotificationObservable notificationObservable;
    private List<INotificationStrategy> notificationStrategies = new ArrayList<>();

    public NotificationEngine() {
        this.notificationObservable = NotificationService.getInstance().getObservable();
        notificationObservable.addObserver(this);
    }

    public NotificationEngine(NotificationObservable observable) {
        this.notificationObservable = observable;
    }

    public void addNotificationStrategy(INotificationStrategy ns) {
        this.notificationStrategies.add(ns);
    }

    // Can have RemoveNotificationStrategy as well.

    public void update() {
        String notificationContent = notificationObservable.getNotificationContent();
        for (INotificationStrategy strategy : notificationStrategies) {
            strategy.sendNotification(notificationContent);
        }
    }
}

public class NotificationSystem {
    public static void main(String[] args) {
          NotificationService notificationService=NotificationService.getInstance();
          Logger logger=new Logger();
          NotificationEngine notificationEngine = new NotificationEngine();

          notificationEngine.addNotificationStrategy(new EmailStrategy("random.person@gmail.com"));
          notificationEngine.addNotificationStrategy(new SMSStrategy("+91 9876543210"));
          notificationEngine.addNotificationStrategy(new PopUpStrategy());

          INotification notification=new SimpleNotification("Your order has been shipped!");
          notification=new TimestampDecorator(notification);
          notification=new SignatureDecorator(notification,"Customer Care");

          notificationService.sendNotification(notification);
    }
}
