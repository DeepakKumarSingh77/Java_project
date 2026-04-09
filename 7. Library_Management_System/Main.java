import java.util.*;


abstract class User {
    int userId;
    String name;
    String email;

    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}


class Member extends User {
    List<Loan> borrowedBooks = new ArrayList<>();

    public Member(int userId, String name, String email) {
        super(userId, name, email);
    }

    public void borrowBook(BookCopy copy, Loan loan) {
        borrowedBooks.add(loan);
        copy.status = Status.ISSUED;
    }

    public void returnBook(BookCopy copy, Loan loan) {
        copy.status = Status.AVAILABLE;
        loan.returnDate = new Date();
    }
}

class Librarian extends User {

    public Librarian(int userId, String name, String email) {
        super(userId, name, email);
    }

    public void addBook(Book book) {
        System.out.println("Book added: " + book.title);
    }

    public void removeBook(Book book) {
        System.out.println("Book removed: " + book.title);
    }

    public void addBookCopy(Book book, BookCopy copy) {
        book.copies.add(copy);
    }
}

class Book {
    int bookId;
    String title;
    String author;
    String isbn;
    List<BookCopy> copies = new ArrayList<>();

    public Book(int bookId, String title, String author, String isbn) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
}

class BookCopy {
    int copyId;
    Status status;

    public BookCopy(int copyId) {
        this.copyId = copyId;
        this.status = Status.AVAILABLE;
    }
}

enum Status {
    AVAILABLE,
    ISSUED
}


class Loan {
    int loanId;
    int userId;
    int copyId;
    Date issueDate;
    Date dueDate;
    Date returnDate;

    double fineAmount;
    boolean isPaid;

    public Loan(int loanId, int userId, int copyId, Date issueDate, Date dueDate) {
        this.loanId = loanId;
        this.userId = userId;
        this.copyId = copyId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public double calculateFine(FineStrategy strategy) {
        fineAmount = strategy.calculateFine(this);
        return fineAmount;
    }
}

interface FineStrategy {
    double calculateFine(Loan loan);
}

class DailyFineStrategy implements FineStrategy {

    public double calculateFine(Loan loan) {
        if (loan.returnDate == null) return 0;

        long diff = loan.returnDate.getTime() - loan.dueDate.getTime();
        long daysLate = diff / (1000 * 60 * 60 * 24);

        if (daysLate <= 0) return 0;

        return daysLate * 10;
    }
}


interface PaymentStrategy {
    void pay(double amount);
}


class UpiPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via UPI: " + amount);
    }
}

class CardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via Card: " + amount);
    }
}

class CashPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via Cash: " + amount);
    }
}

class AuthService {
    Map<String, String> credentials = new HashMap<>();

    public void register(String email, String password) {
        credentials.put(email, password);
    }

    public boolean login(String email, String password) {
        return credentials.containsKey(email) &&
               credentials.get(email).equals(password);
    }
}



class LibraryManagement {

    List<User> users = new ArrayList<>();
    List<Book> books = new ArrayList<>();
    List<Loan> loans = new ArrayList<>();

    //SEARCH FEATURES

    public List<Book> searchByTitle(String title) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.title.toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchByAuthor(String author) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.author.toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public Book searchByISBN(String isbn) {
        for (Book book : books) {
            if (book.isbn.equals(isbn)) {
                return book;
            }
        }
        return null;
    }


    public void issueBook(Member member, BookCopy copy) {
        if (copy.status != Status.AVAILABLE) {
            System.out.println("Book not available");
            return;
        }

        Date issueDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14);

        Loan loan = new Loan(loans.size() + 1, member.userId, copy.copyId, issueDate, cal.getTime());

        loans.add(loan);
        member.borrowBook(copy, loan);

        System.out.println("Book issued successfully");
    }


    public void returnBook(Member member, BookCopy copy, Loan loan) {
        member.returnBook(copy, loan);

        FineStrategy strategy = new DailyFineStrategy();
        double fine = loan.calculateFine(strategy);

        if (fine > 0) {
            System.out.println("Fine: " + fine);

            PaymentStrategy payment = new CashPayment();
            payment.pay(fine);

            loan.isPaid = true;
        } else {
            System.out.println("No fine");
        }
    }
}


public class Main {
    public static void main(String[] args) {

        //System
        LibraryManagement library = new LibraryManagement();

        //Users
        Member member = new Member(1, "John", "john@mail.com");
        Librarian librarian = new Librarian(2, "Admin", "admin@mail.com");

        library.users.add(member);
        library.users.add(librarian);

        //Add Book
        Book book1 = new Book(101, "Java Programming", "James Gosling", "ISBN001");
        librarian.addBook(book1);

        //Add Copies
        BookCopy copy1 = new BookCopy(1);
        BookCopy copy2 = new BookCopy(2);

        librarian.addBookCopy(book1, copy1);
        librarian.addBookCopy(book1, copy2);

        library.books.add(book1);

        //SEARCH TEST
        System.out.println("\nSearch by title 'Java':");
        List<Book> result = library.searchByTitle("Java");
        for (Book b : result) {
            System.out.println(b.title + " by " + b.author);
        }

        //ISSUE BOOK
        System.out.println("\nIssuing book...");
        library.issueBook(member, copy1);

        //Simulate late return (for fine)
        Loan loan = library.loans.get(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -5); // returned 5 days late
        loan.dueDate = cal.getTime();

        //RETURN BOOK
        System.out.println("\nReturning book...");
        library.returnBook(member, copy1, loan);

        //FINAL STATUS
        System.out.println("\nFinal Status:");
        System.out.println("Book Copy Status: " + copy1.status);
        System.out.println("Fine Paid: " + loan.isPaid);
    }
}