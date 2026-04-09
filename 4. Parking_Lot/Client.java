import java.util.*;

enum VehicleType {
    BIKE,
    CAR,
    TRUCK
}

enum PaymentType {
    CREDIT_CARD,
    DEBIT_CARD,
    UPI
}

// Vehicle
abstract class Vehicle {
    String number;
    VehicleType type;

    Vehicle(String number, VehicleType type) {
        this.number = number;
        this.type = type;
    }
}

class Bike extends Vehicle {
    Bike(String number) {
        super(number, VehicleType.BIKE);
    }
}

class Car extends Vehicle {
    Car(String number) {
        super(number, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    Truck(String number) {
        super(number, VehicleType.TRUCK);
    }
}

// VehicleFactory
class VehicleFactory {

    public Vehicle createVehicle(VehicleType type, String number) {
        switch (type) {
            case BIKE:
                return new Bike(number);
            case CAR:
                return new Car(number);
            case TRUCK:
                return new Truck(number);
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
    }
}

// SpotAllocationStrategy
interface SpotAllocationStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle);
}


// FirstAvailableStrategy
class FirstAvailableStrategy implements SpotAllocationStrategy {

    public ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.spots) {
                if (spot.canFitVehicle(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }
}

// NearestFirstStrategy
class NearestFirstStrategy implements SpotAllocationStrategy {

    public ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.spots) {
                if (spot.canFitVehicle(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }
}


// ParkingSpotAllocator
class ParkingSpotAllocator {
    SpotAllocationStrategy strategy;
    ParkingLot parkingLot;

    ParkingSpotAllocator(SpotAllocationStrategy strategy, ParkingLot parkingLot) {
        this.strategy = strategy;
        this.parkingLot = parkingLot;
    }

    ParkingSpot allocateSpot(Vehicle vehicle) {
        ParkingSpot spot = strategy.findSpot(parkingLot.floors, vehicle);

        if (spot != null) {
            spot.assignVehicle(vehicle);
        }

        return spot;
    }
}

// Ticket
class Ticket {
    String id;
    long entryTime;
    long exitTime;
    ParkingSpot spot;
    Vehicle vehicle;

    Ticket(String id, long entryTime, Vehicle vehicle, ParkingSpot spot) {
        this.id = id;
        this.entryTime = entryTime;
        this.vehicle = vehicle;
        this.spot = spot;
    }
}


// TicketService
class TicketService {

    ParkingSpotAllocator allocator;

    TicketService(ParkingSpotAllocator allocator) {
        this.allocator = allocator;
    }

    Ticket createTicket(Vehicle vehicle) {
        ParkingSpot spot = allocator.allocateSpot(vehicle);

        if (spot == null) {
            throw new RuntimeException("No parking spot available");
        }

        String ticketId = UUID.randomUUID().toString();
        long entryTime = System.currentTimeMillis();

        return new Ticket(ticketId, entryTime, vehicle, spot);
    }

    void closeTicket(Ticket ticket) {
        ticket.exitTime = System.currentTimeMillis();
    }
}

// FareStrategy
interface FareStrategy {
    double calculate(Ticket ticket);
}

class NormalFare implements FareStrategy {

    public double calculate(Ticket ticket) {
        long duration = (ticket.exitTime - ticket.entryTime) / 1000; // seconds
        return duration * 1.0; // simple rate
    }
}

class PeakHourFare implements FareStrategy {

    public double calculate(Ticket ticket) {
        long duration = (ticket.exitTime - ticket.entryTime) / 1000;
        return duration * 2.0; // higher rate
    }
}

class FareCalculator {
    FareStrategy strategy;

    FareCalculator(FareStrategy strategy) {
        this.strategy = strategy;
    }

    double calculateFare(Ticket ticket) {
        return strategy.calculate(ticket);
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid using Credit Card: " + amount);
    }
}

class DebitCardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid using Debit Card: " + amount);
    }
}

class UPIPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}

// PaymentService
class PaymentService {
    void pay(double amount, PaymentStrategy strategy) {
        strategy.pay(amount);
    }
}

// EntryGate
class EntryGate {
    TicketService ticketService;

    EntryGate(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    Ticket enter(Vehicle vehicle) {
        return ticketService.createTicket(vehicle);
    }
}

// ExitGate
class ExitGate {
    TicketService ticketService;
    FareCalculator fareCalculator;
    PaymentService paymentService;

    ExitGate(TicketService ticketService,
             FareCalculator fareCalculator,
             PaymentService paymentService) {
        this.ticketService = ticketService;
        this.fareCalculator = fareCalculator;
        this.paymentService = paymentService;
    }

    void exit(Ticket ticket, PaymentStrategy paymentStrategy) {

        // 1. Close ticket
        ticketService.closeTicket(ticket);

        // 2. Calculate fare
        double amount = fareCalculator.calculateFare(ticket);

        // 3. Pay
        paymentService.pay(amount, paymentStrategy);

        // 4. Free spot
        ticket.spot.freeSpot();

        System.out.println("Exit complete. Amount paid: " + amount);
    }
}

// ParkingSpot
class ParkingSpot {
    String id;
    boolean isOccupied;
    VehicleType type;
    Vehicle vehicle;

    ParkingSpot(String id, VehicleType type) {
        this.id = id;
        this.type = type;
        this.isOccupied = false;
    }

    boolean canFitVehicle(Vehicle v) {
        return !isOccupied && this.type == v.type;
    }

    void assignVehicle(Vehicle v) {
        this.vehicle = v;
        this.isOccupied = true;
    }

    void freeSpot() {
        this.vehicle = null;
        this.isOccupied = false;
    }
}

// ParkingFloor
class ParkingFloor {
    String id;
    List<ParkingSpot> spots;

    ParkingFloor(String id, List<ParkingSpot> spots) {
        this.id = id;
        this.spots = spots;
    }

    List<ParkingSpot> getAvailableSpots(Vehicle vehicle) {
        List<ParkingSpot> result = new ArrayList<>();

        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                result.add(spot);
            }
        }
        return result;
    }
}

class ParkingLot{
     List<ParkingFloor> floors;

    ParkingLot(List<ParkingFloor> floors) {
        this.floors = floors;
    }
}
public class Client {
    public static void main(String[] args) {
        // 1. Create Parking Spots
        ParkingSpot s1 = new ParkingSpot("S1", VehicleType.CAR);
        ParkingSpot s2 = new ParkingSpot("S2", VehicleType.BIKE);
        ParkingSpot s3 = new ParkingSpot("S3", VehicleType.TRUCK);

        List<ParkingSpot> spots = Arrays.asList(s1, s2, s3);

        // 2. Create Parking Floor
        ParkingFloor floor1 = new ParkingFloor("F1", spots);

        // 3. Create Parking Lot
        ParkingLot parkingLot = new ParkingLot(Arrays.asList(floor1));

        // 4. Strategy + Allocator
        SpotAllocationStrategy strategy = new FirstAvailableStrategy();
        ParkingSpotAllocator allocator = new ParkingSpotAllocator(strategy, parkingLot);

        // 5. Services
        TicketService ticketService = new TicketService(allocator);
        FareStrategy fareStrategy = new NormalFare();
        FareCalculator fareCalculator = new FareCalculator(fareStrategy);
        PaymentService paymentService = new PaymentService();

        // 6. Gates
        EntryGate entryGate = new EntryGate(ticketService);
        ExitGate exitGate = new ExitGate(ticketService, fareCalculator, paymentService);

        // 7. Create Vehicle
        VehicleFactory factory = new VehicleFactory();
        Vehicle car = factory.createVehicle(VehicleType.CAR, "MH12AB1234");

        // 8. ENTRY FLOW
        System.out.println("---- VEHICLE ENTER ----");
        Ticket ticket = entryGate.enter(car);
        System.out.println("Ticket Generated: " + ticket.id);

        // 9. EXIT FLOW
        System.out.println("---- VEHICLE EXIT ----");
        PaymentStrategy paymentStrategy = new UPIPayment();
        exitGate.exit(ticket, paymentStrategy);
    }
}
