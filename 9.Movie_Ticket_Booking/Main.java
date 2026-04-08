import java.util.concurrent.locks.ReentrantLock;
import java.util.*;


class Seat {
    private final String seatId;  // e.g., A10
    private final int row;
    private final int number;

    public Seat(String seatId, int row, int number) {
        this.seatId = seatId;
        this.row = row;
        this.number = number;
    }

    public String getSeatId() {
        return seatId;
    }
}

enum SeatStatus {
    AVAILABLE,
    LOCKED,
    BOOKED
}



class ShowSeat {
    private final Seat seat;
    private SeatStatus status;

    private long lockedAt;   // 🔥 new
    private static final long LOCK_TIMEOUT = 2 * 60 * 1000; // 2 min

    private final ReentrantLock lock = new ReentrantLock();

    public ShowSeat(Seat seat) {
        this.seat = seat;
        this.status = SeatStatus.AVAILABLE;
    }

    public boolean lockSeat() {
        lock.lock();
        try {
            // 🔥 check expiry
            if (status == SeatStatus.LOCKED &&
                System.currentTimeMillis() - lockedAt > LOCK_TIMEOUT) {
                status = SeatStatus.AVAILABLE;
            }

            if (status != SeatStatus.AVAILABLE) {
                return false;
            }

            status = SeatStatus.LOCKED;
            lockedAt = System.currentTimeMillis();
            return true;

        } finally {
            lock.unlock();
        }
    }

    public boolean bookSeat() {
        lock.lock();
        try {
            if (status != SeatStatus.LOCKED) {
                return false;
            }
            status = SeatStatus.BOOKED;
            return true;

        } finally {
            lock.unlock();
        }
    }

    public void releaseSeat() {
        lock.lock();
        try {
            status = SeatStatus.AVAILABLE;
        } finally {
            lock.unlock();
        }
    }
}

class User {
    private final String userId;
    private final String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }
}

class Movie {
    private final String movieId;
    private final String title;
    private final int duration;

    public Movie(String movieId, String title, int duration) {
        this.movieId = movieId;
        this.title = title;
        this.duration = duration;
    }
    public String getTitle() {
        return title;
    }
}

class Screen {
    private final String screenId;
    private final List<Seat> seats;

    public Screen(String screenId, List<Seat> seats) {
        this.screenId = screenId;
        this.seats = seats;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}


class Show {
    private final String showId;
    private final Movie movie;
    private final Screen screen;

    private final Date startTime;
    private final Date endTime;

    private final List<ShowSeat> showSeats;

    public Show(String showId, Movie movie, Screen screen,
                Date startTime, Date endTime) {

        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = endTime;

        // 🔥 create ShowSeats from Screen seats
        this.showSeats = new ArrayList<>();
        for (Seat seat : screen.getSeats()) {
            showSeats.add(new ShowSeat(seat));
        }
    }

    public List<ShowSeat> getShowSeats() {
        return showSeats;
    }
}


class Theater {
    private final String theaterId;
    private final String name;
    private final List<Screen> screens;

    public Theater(String theaterId, String name, List<Screen> screens) {
        this.theaterId = theaterId;
        this.name = name;
        this.screens = screens;
    }
}

enum BookingStatus {
    CONFIRMED,
    CANCELLED
}


class Booking {
    private final String bookingId;
    private final User user;
    private final Show show;
    private final List<ShowSeat> seats;
    private BookingStatus status;

    public Booking(User user, Show show, List<ShowSeat> seats) {
        this.bookingId = UUID.randomUUID().toString();
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.CONFIRMED;
    }

    public static Booking createBooking(User user, Show show, List<ShowSeat> selectedSeats) {

        List<ShowSeat> lockedSeats = new ArrayList<>();

        // Step 1: lock seats
        for (ShowSeat seat : selectedSeats) {
            if (!seat.lockSeat()) {

                // rollback
                for (ShowSeat locked : lockedSeats) {
                    locked.releaseSeat();
                }

                throw new RuntimeException("Seat not available");
            }

            lockedSeats.add(seat);
        }

        // Step 2: confirm booking
        for (ShowSeat seat : lockedSeats) {
            seat.bookSeat();
        }

        return new Booking(user, show, lockedSeats);
    }
}


interface SearchService {
    List<Movie> searchByTitle(String title);
}


class MovieSearchService implements SearchService {

    private final List<Movie> movies;

    public MovieSearchService(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public List<Movie> searchByTitle(String title) {
        List<Movie> result = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }
}


public class Main {

    public static void main(String[] args) {

        // Step 1: Create Seats
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            seats.add(new Seat("A" + i, 1, i));
        }

        // Step 2: Create Screen
        Screen screen = new Screen("S1", seats);

        // Step 3: Create Movie
        Movie movie = new Movie("M1", "Avengers", 180);

        // Step 4: Create Show
        Show show = new Show(
                "SH1",
                movie,
                screen,
                new Date(),
                new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000)
        );

        // Step 5: Create Users
        User user1 = new User("U1", "Alice");
        User user2 = new User("U2", "Bob");

        // Step 6: Search Movie
        List<Movie> movieList = Arrays.asList(movie);
        SearchService searchService = new MovieSearchService(movieList);

        System.out.println("Search Results:");
        for (Movie m : searchService.searchByTitle("aven")) {
            System.out.println(m.getTitle());
        }

        // Step 7: Get Seats from Show
        List<ShowSeat> showSeats = show.getShowSeats();

        // Pick same seats for both users
        List<ShowSeat> selectedSeatsUser1 = Arrays.asList(showSeats.get(0), showSeats.get(1));
        List<ShowSeat> selectedSeatsUser2 = Arrays.asList(showSeats.get(0), showSeats.get(1));

        // Step 8: Simulate Concurrent Booking
        Runnable task1 = () -> {
            try {
                Booking booking = Booking.createBooking(user1, show, selectedSeatsUser1);
                System.out.println("User1 Booking Success: " + booking);
            } catch (Exception e) {
                System.out.println("User1 Booking Failed: " + e.getMessage());
            }
        };

        Runnable task2 = () -> {
            try {
                Booking booking = Booking.createBooking(user2, show, selectedSeatsUser2);
                System.out.println("User2 Booking Success: " + booking);
            } catch (Exception e) {
                System.out.println("User2 Booking Failed: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}