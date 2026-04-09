import java.util.*;

enum Direction {
    UP,
    DOWN,
    NONE
}

enum State {
    IDLE,
    MOVING
}

class Elevator {
    int id;
    int currFloor;
    State state;
    Direction direction;

    PriorityQueue<Integer> upHeap;
    PriorityQueue<Integer> downHeap;

    Elevator(int id, int startFloor) {
        this.id = id;
        this.currFloor = startFloor;
        state = State.IDLE;
        direction = Direction.NONE;
        upHeap = new PriorityQueue<>(); // min-heap
        downHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    void addTarget(int floor) {
        if (floor == currFloor) {
            System.out.println("Please Come inside the lift");
            return;
        }
        ;

        if (currFloor > floor) {
            downHeap.add(floor);
        } else {
            upHeap.add(floor);
        }

        // If elevator was idle, decide direction
        if (state == State.IDLE) {
            if (floor > currFloor)
                direction = Direction.UP;
            else
                direction = Direction.DOWN;

            state = State.MOVING;
        }
    }

    void move() {
        if (state == State.IDLE)
            return;

        if (direction == Direction.UP) {

            // Prevent going out of bounds
            if (currFloor == 9) {
                direction = Direction.DOWN;
                return;
            }

            currFloor++;

            if (!upHeap.isEmpty() && currFloor == upHeap.peek()) {
                upHeap.poll();
            }

            if (upHeap.isEmpty()) {
                if (!downHeap.isEmpty()) {
                    direction = Direction.DOWN;
                } else {
                    direction = Direction.NONE;
                    state = State.IDLE;
                }
            }
        }

        else if (direction == Direction.DOWN) {

            if (currFloor == 0) {
                direction = Direction.UP;
                return;
            }

            currFloor--;

            if (!downHeap.isEmpty() && currFloor == downHeap.peek()) {
                downHeap.poll();
            }

            if (downHeap.isEmpty()) {
                if (!upHeap.isEmpty()) {
                    direction = Direction.UP;
                } else {
                    direction = Direction.NONE;
                    state = State.IDLE;
                }
            }
        }
    }
}

class Dispatcher {
    List<Elevator> elevators;
    boolean[] upRequests;
    boolean[] downRequests;

    Dispatcher() {
        upRequests = new boolean[10];
        downRequests = new boolean[10];
    }

    void submitRequest(int floor, Direction dir) {
        if (floor < 0 || floor > 9)
            return;

        if (dir == Direction.UP) {
            upRequests[floor] = true;
        } else {
            downRequests[floor] = true;
        }

        assignElevator(floor, dir);
    }

    void assignElevator(int floor, Direction dir) {
    Elevator best = null;
    int minDistance = Integer.MAX_VALUE;

    for (Elevator e : elevators) {

        // Case 1: Same direction and on the way
        if (e.direction == dir) {
            if ((dir == Direction.UP && e.currFloor <= floor) ||
                (dir == Direction.DOWN && e.currFloor >= floor)) {

                int dist = Math.abs(e.currFloor - floor);
                if (dist < minDistance) {
                    best = e;
                    minDistance = dist;
                }
            }
        }

        // Case 2: Idle elevator
        else if (e.direction == Direction.NONE) {
            int dist = Math.abs(e.currFloor - floor);
            if (dist < minDistance) {
                best = e;
                minDistance = dist;
            }
        }
    }

    // Assign if found
    if (best != null) {
        best.addTarget(floor);

        if (dir == Direction.UP) upRequests[floor] = false;
        else downRequests[floor] = false;
    }
}
}


class ElevatorSystem {
    Dispatcher dispatcher;
    Map<Integer, Elevator> elevatorMap;

    ElevatorSystem(List<Elevator> elevators) {
        this.dispatcher = new Dispatcher();
        this.dispatcher.elevators = elevators;

        this.elevatorMap = new HashMap<>();
        for (Elevator e : elevators) {
            elevatorMap.put(e.id, e);
        }
    }

    // Hall request
    void requestElevator(int floor, Direction dir) {
        dispatcher.submitRequest(floor, dir);
    }

    // Inside elevator request
    void selectFloor(int elevatorId, int floor) {
        if (!elevatorMap.containsKey(elevatorId)) return;

        Elevator e = elevatorMap.get(elevatorId);

        if (floor < 0 || floor > 9) return;

        e.addTarget(floor);
    }

    // Simulation step
    void tick() {
        for (Elevator e : dispatcher.elevators) {
            e.move();
        }
    }
}


public class Main {
    public static void main(String[] args) {

        // Create 3 elevators
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(new Elevator(1, 0));
        elevators.add(new Elevator(2, 5));
        elevators.add(new Elevator(3, 9));

        ElevatorSystem system = new ElevatorSystem(elevators);

        // Hall requests
        system.requestElevator(3, Direction.UP);
        system.requestElevator(7, Direction.DOWN);

        // Simulate ticks
        for (int i = 0; i < 10; i++) {
            System.out.println("Tick: " + i);

            system.tick();

            // Print state
            for (Elevator e : elevators) {
                System.out.println(
                    "Elevator " + e.id +
                    " Floor: " + e.currFloor +
                    " Direction: " + e.direction +
                    " State: " + e.state
                );
            }

            System.out.println("-------------------");
        }

        // Inside request example
        system.selectFloor(1, 8);
    }
}