import java.util.*;

enum BookingStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

enum SeatStatus {
    AVAILABLE,
    LOCKED,
    BOOKED
}

class User {
    String userId;
    String name;
    String email;
    String phone;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}


class Flight {
    String flightId;
    String source;
    String destination;
    Date departureTime;
    Date arrivalTime;

    List<IFlightSeat> seats;

    public List<IFlightSeat> getAvailableSeats() {
        List<IFlightSeat> available = new ArrayList<>();
        for (IFlightSeat seat : seats) {
            if (seat.isAvailable()) {
                available.add(seat);
            }
        }
        return available;
    }
}

interface IFlightSeat {
    boolean isAvailable();
    void lockSeat();
    void bookSeat();
}

class EconomySeat implements IFlightSeat {
    String seatId;
    String seatNumber;
    SeatStatus status;
    double price;

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public void lockSeat() {
        if (status == SeatStatus.AVAILABLE) {
            status = SeatStatus.LOCKED;
        }
    }

    public void bookSeat() {
        if (status == SeatStatus.LOCKED) {
            status = SeatStatus.BOOKED;
        }
    }
}

class Booking {
    String bookingId;
    User user;
    Flight flight;

    List<IFlightSeat> seats;

    BookingStatus status;
    double totalAmount;

    public void calculateTotalAmount() {
        totalAmount = 0;
        for (IFlightSeat seat : seats) {
            if (seat instanceof EconomySeat) {
                totalAmount += ((EconomySeat) seat).price;
            }
        }
    }

    public void confirmBooking() {
        this.status = BookingStatus.COMPLETED;
        for (IFlightSeat seat : seats) {
            seat.bookSeat();
        }
    }

    public void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
    }
}

interface PaymentStrategy {
    boolean pay(double amount);
}


class CreditCard implements PaymentStrategy {

    public boolean pay(double amount) {
        System.out.println("Paid using Credit Card: " + amount);
        return true;
    }
}

class DebitCard implements PaymentStrategy {

    public boolean pay(double amount) {
        System.out.println("Paid using Debit Card: " + amount);
        return true;
    }
}


class UserService {
    Map<String, User> userDb = new HashMap<>();

    public User createUser(String name, String email, String phone) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, phone);
        userDb.put(id, user);
        return user;
    }

    public User getUserById(String userId) {
        return userDb.get(userId);
    }
}

class FlightService {
    Map<String, Flight> flightDb = new HashMap<>();

    public List<Flight> searchFlights(String source, String destination, Date date) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : flightDb.values()) {
            if (f.source.equals(source) && f.destination.equals(destination)) {
                result.add(f);
            }
        }
        return result;
    }

    public Flight getFlightById(String id) {
        return flightDb.get(id);
    }
}

class FlightBookingService {

    public Booking createBooking(User user, Flight flight, List<IFlightSeat> seats) {
        Booking booking = new Booking();
        booking.bookingId = UUID.randomUUID().toString();
        booking.user = user;
        booking.flight = flight;
        booking.seats = seats;
        booking.status = BookingStatus.PENDING;

        lockSeats(seats);
        booking.calculateTotalAmount();

        return booking;
    }

    public void lockSeats(List<IFlightSeat> seats) {
        for (IFlightSeat seat : seats) {
            seat.lockSeat();
        }
    }

    public void releaseSeats(List<IFlightSeat> seats) {
        for (IFlightSeat seat : seats) {
            // simple reset
            if (seat instanceof EconomySeat) {
                ((EconomySeat) seat).status = SeatStatus.AVAILABLE;
            }
        }
    }

    public boolean makePayment(Booking booking, PaymentStrategy strategy) {
        boolean success = strategy.pay(booking.totalAmount);

        if (success) {
            confirmBooking(booking);
        } else {
            releaseSeats(booking.seats);
        }

        return success;
    }

    public void confirmBooking(Booking booking) {
        booking.confirmBooking();
    }

    public void cancelBooking(Booking booking) {
        booking.cancelBooking();
        releaseSeats(booking.seats);
    }
}

class NotificationService {

    public void sendBookingConfirmation(User user, Booking booking) {
        System.out.println("Booking confirmed for user: " + user.name);
    }

    public void sendCancellation(User user, Booking booking) {
        System.out.println("Booking cancelled for user: " + user.name);
    }
}


public class MainApp {

    public static void main(String[] args) {

        UserService userService = new UserService();
        FlightService flightService = new FlightService();
        FlightBookingService bookingService = new FlightBookingService();
        NotificationService notificationService = new NotificationService();

        User user = userService.createUser("John", "john@mail.com", "123456");

        Flight flight = new Flight();
        flight.flightId = "F1";
        flight.source = "Delhi";
        flight.destination = "Mumbai";
        flight.seats = new ArrayList<>();

        EconomySeat seat1 = new EconomySeat();
        seat1.seatId = "S1";
        seat1.seatNumber = "1A";
        seat1.status = SeatStatus.AVAILABLE;
        seat1.price = 1000;

        EconomySeat seat2 = new EconomySeat();
        seat2.seatId = "S2";
        seat2.seatNumber = "1B";
        seat2.status = SeatStatus.AVAILABLE;
        seat2.price = 1200;

        flight.seats.add(seat1);
        flight.seats.add(seat2);

        flightService.flightDb.put(flight.flightId, flight);

        List<IFlightSeat> selectedSeats = new ArrayList<>();
        selectedSeats.add(seat1);
        selectedSeats.add(seat2);

        Booking booking = bookingService.createBooking(user, flight, selectedSeats);

        System.out.println("Booking created. Status: " + booking.status);
        System.out.println("Total amount: " + booking.totalAmount);

        PaymentStrategy payment = new CreditCard();
        boolean paymentStatus = bookingService.makePayment(booking, payment);

        System.out.println("Payment success: " + paymentStatus);
        System.out.println("Booking status: " + booking.status);

        if (paymentStatus) {
            notificationService.sendBookingConfirmation(user, booking);
        } else {
            notificationService.sendCancellation(user, booking);
        }
    }
}