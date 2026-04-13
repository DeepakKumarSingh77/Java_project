import java.util.*;

enum RideStatus {
    REQUESTED,
    ACCEPTED,
    STARTED,
    COMPLETED,
    CANCELLED
}

enum CaptainStatus {
    AVAILABLE,
    BUSY
}

class Location {
    double lat;
    double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double distance(Location other) {
        double dx = this.lat - other.lat;
        double dy = this.lon - other.lon;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

class User {
    String name;
    String email;
    String phoneNo;

    public User(String name, String email, String phoneNo) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }
}

class Rider extends User {
    List<Ride> rideHistory = new ArrayList<>();

    public Rider(String name, String email, String phoneNo) {
        super(name, email, phoneNo);
    }

    public void addRide(Ride ride) {
        rideHistory.add(ride);
    }
}

class Vehicle {
    String type;
    String vehicleNo;

    public Vehicle(String type, String vehicleNo) {
        this.type = type;
        this.vehicleNo = vehicleNo;
    }
}

class Captain extends User {
    int driverId;
    Location currentLocation;
    CaptainStatus status;
    Vehicle vehicle;

    public Captain(int driverId, String name, String email, String phoneNo, Location loc, Vehicle vehicle) {
        super(name, email, phoneNo);
        this.driverId = driverId;
        this.currentLocation = loc;
        this.vehicle = vehicle;
        this.status = CaptainStatus.AVAILABLE;
    }

    public void updateLocation(Location loc) {
        this.currentLocation = loc;
    }

    public void acceptRide(Ride ride) {
        System.out.println("Driver " + driverId + " accepted ride " + ride.rideId);
    }

    public void rejectRide(Ride ride) {
        System.out.println("Driver " + driverId + " rejected ride " + ride.rideId);
    }
}


class RideRequest {
    int requestId;
    Rider rider;
    Location source;
    Location destination;
    String vehicleType;

    public RideRequest(int requestId, Rider rider, Location source, Location destination, String vehicleType) {
        this.requestId = requestId;
        this.rider = rider;
        this.source = source;
        this.destination = destination;
        this.vehicleType = vehicleType;
    }
}

class Ride {
    int rideId;
    Rider rider;
    Captain driver;
    Location sourceLocation;
    Location destinationLocation;
    RideStatus status;
    double fare;

    public Ride(int rideId, Rider rider, Location src, Location dest) {
        this.rideId = rideId;
        this.rider = rider;
        this.sourceLocation = src;
        this.destinationLocation = dest;
        this.status = RideStatus.REQUESTED;
    }

    public boolean assignDriver(Captain driver) {
        if (status != RideStatus.REQUESTED) return false;

        this.driver = driver;
        this.status = RideStatus.ACCEPTED;
        driver.status = CaptainStatus.BUSY;
        return true;
    }

    public void startRide() {
        status = RideStatus.STARTED;
    }

    public void completeRide() {
        status = RideStatus.COMPLETED;
        driver.status = CaptainStatus.AVAILABLE;
    }

    public void cancel() {
        status = RideStatus.CANCELLED;
    }
}

interface MatchingStrategy {
    List<Captain> findNearbyDrivers(Location location);
}

class NearestDriverStrategy implements MatchingStrategy {

    List<Captain> allDrivers;

    public NearestDriverStrategy(List<Captain> drivers) {
        this.allDrivers = drivers;
    }

    @Override
    public List<Captain> findNearbyDrivers(Location location) {
        List<Captain> result = new ArrayList<>();

        for (Captain driver : allDrivers) {
            if (driver.status == CaptainStatus.AVAILABLE) {
                result.add(driver);
            }
        }

        // sort by distance
        result.sort((d1, d2) -> 
            Double.compare(
                d1.currentLocation.distance(location),
                d2.currentLocation.distance(location)
            )
        );

        return result;
    }
}

class RideManager {

    private static RideManager instance;

    Map<Integer, Ride> activeRides = new HashMap<>();
    Map<Integer, List<Ride>> rideHistory = new HashMap<>();

    private RideManager() {}

    public static RideManager getInstance() {
        if (instance == null) {
            instance = new RideManager();
        }
        return instance;
    }

    public void addRide(Ride ride) {
        activeRides.put(ride.rideId, ride);
    }

    public Ride getRide(int rideId) {
        return activeRides.get(rideId);
    }

    public void completeRide(int rideId) {
        Ride ride = activeRides.remove(rideId);

        if (ride != null) {
            rideHistory
                .computeIfAbsent(ride.rider.hashCode(), k -> new ArrayList<>())
                .add(ride);
        }
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCard implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class DebitCard implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Debit Card");
    }
}

class UPI implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

class PaymentService {
    PaymentStrategy strategy;

    public PaymentService(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void processPayment(Ride ride) {
        strategy.pay(ride.fare);
    }
}


class RideService {

    MatchingStrategy matchingStrategy;
    RideManager rideManager;

    public RideService(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
        this.rideManager = RideManager.getInstance();
    }

    public Ride requestRide(RideRequest request) {

        // 1. Create Ride
        Ride ride = new Ride(request.requestId, request.rider, request.source, request.destination);

        // 2. Store Ride
        rideManager.addRide(ride);

        // 3. Find nearby drivers
        List<Captain> drivers = matchingStrategy.findNearbyDrivers(request.source);

        // 4. Try assigning driver
        for (Captain driver : drivers) {
            if (assignDriver(ride, driver)) {
                System.out.println("Ride assigned to driver " + driver.driverId);
                return ride;
            }
        }

        // 5. No driver found
        ride.cancel();
        System.out.println("No drivers available");
        return null;
    }

    public boolean assignDriver(Ride ride, Captain driver) {

        synchronized (ride) {  //lock ride

            if (ride.status != RideStatus.REQUESTED) return false;

            synchronized (driver) {  //lock driver

                if (driver.status != CaptainStatus.AVAILABLE) return false;

                // assign
                ride.assignDriver(driver);
                return true;
            }
        }
    }

    public void startRide(Ride ride) {
        ride.startRide();
    }

    public void completeRide(Ride ride, PaymentService paymentService) {
        ride.completeRide();
        paymentService.processPayment(ride);
        rideManager.completeRide(ride.rideId);
    }

    public void cancelRide(Ride ride) {
        ride.cancel();
    }
}

public class Main {
    public static void main(String[] args) {

        // 1. Create Rider
        Rider rider = new Rider("John", "john@mail.com", "9999999999");

        // 2. Create Drivers
        List<Captain> drivers = new ArrayList<>();

        drivers.add(new Captain(1, "Driver1", "d1@mail.com", "111",
                new Location(1, 1), new Vehicle("Car", "ABC123")));

        drivers.add(new Captain(2, "Driver2", "d2@mail.com", "222",
                new Location(5, 5), new Vehicle("Car", "XYZ789")));

        drivers.add(new Captain(3, "Driver3", "d3@mail.com", "333",
                new Location(2, 2), new Vehicle("Bike", "BIKE001")));

        // 3. Matching Strategy
        MatchingStrategy strategy = new NearestDriverStrategy(drivers);

        // 4. Ride Service
        RideService rideService = new RideService(strategy);

        // 5. Create Ride Request
        RideRequest request = new RideRequest(
                101,
                rider,
                new Location(0, 0),   // source
                new Location(10, 10), // destination
                "Car"
        );

        // 6. Request Ride
        Ride ride = rideService.requestRide(request);

        if (ride == null) {
            System.out.println("Ride booking failed");
            return;
        }

        // 7. Start Ride
        System.out.println("Starting ride...");
        rideService.startRide(ride);

        // 8. Complete Ride
        System.out.println("Completing ride...");
        ride.fare = 250.0;  // set fare manually for testing

        PaymentService paymentService = new PaymentService(new UPI());
        rideService.completeRide(ride, paymentService);

        // 9. Final Status
        System.out.println("Ride Status: " + ride.status);
    }
}