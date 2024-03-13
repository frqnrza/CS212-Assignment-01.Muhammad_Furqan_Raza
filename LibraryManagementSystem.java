//Muhammad Furqan Raza
//460535
//Library Management System


//Import declaration Statements
import java.sql.*;
import java.util.*;
import java.lang.* ;


// Book class
class Book{
    private int BookID;
    private String Title;
    private String Author;
    private String Genre;
    private boolean AvailabilityStatus ;

    //Getters :
    public int get_BookID(){
        return BookID;
    }
    public String get_Title(){
        return Title;
    }
    public String get_Author(){
        return Author;
    }
    public String get_Genre(){
        return Genre;
    }
    public boolean get_AvailabilityStatus(){
        return AvailabilityStatus;
    }

    //Setters :
    public void set_BookID( int newID){
        BookID = newID ;
    }
    public void set_Title(String newTitle){
        Title = newTitle;
    }
    public void set_Author(String newAuthor){
        Author = newAuthor ;
    }
    public void set_Genre(String newGenre){
        Genre = newGenre ;
    }
    public void set_AvailabilityStatus(boolean newAvailabilityStatus){
        AvailabilityStatus = newAvailabilityStatus ;
    }

    // Constructor of Book
    public Book(int c_ID, String c_title , String c_author, String c_genre, boolean c_availabilityStatus) {
        this.BookID = c_ID;
        this.Title = c_title;
        this.Author = c_author;
        this.Genre = c_genre;
        this.AvailabilityStatus = c_availabilityStatus;
    }
    //End of Class Book
}


// User class :
class User{
    private int User_ID;
    private String User_Name;
    private String Contact_Info;
    private int BorrowedBooks;

    // Getters :
    public int get_UserID(){
        return User_ID;
    }
    public String get_User_name(){
        return User_Name;
    }
    public String get_ContactInfo(){
        return Contact_Info;
    }
    public int get_BorrowedBooks(){
        return BorrowedBooks;
    }

    // Setters :
    public void set_UserID(int newUserID){
        User_ID = newUserID ;
    }
    public void set_User_name(String newUserName ){
        User_Name = newUserName ;
    }
    public void set_ContactInfo(String newUserContact){
        Contact_Info = newUserContact ;
    }
    public void set_BorrowedBooks(int newBorrowedBook){
        BorrowedBooks = newBorrowedBook ;
    }

    // Constructor of User
    public User(int c_ID, String c_name, String c_contactInfo, int c_borrowedBooks) {
        this.User_ID = c_ID;
        this.User_Name = c_name;
        this.Contact_Info = c_contactInfo;
        this.BorrowedBooks = c_borrowedBooks;
    }
//End of User Class
}




// Library class
class Library{
    private Connection connection;

    // Constructor
    public Library(Connection connection) {
        this.connection = connection;
    }


    // Method to Add New Book
    public void addBook(Book book) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO book(Book_ID , Title , Author , Genre , Availability_Status) VALUES (? , ? , ? , ? , ?)");
            statement.setInt(1, book.get_BookID());
            statement.setString(2, book.get_Title());
            statement.setString(3, book.get_Author());
            statement.setString(4, book.get_Genre());
            statement.setBoolean(5, book.get_AvailabilityStatus());
            statement.executeUpdate();
            System.out.println("Book Added Successfully !");
        }
        catch(SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to Add New Record ! ");
        }
     // method end
    }



    // Method to add new user
    public void addUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user(User_ID , Name , Contact_Information , Borrowed_Books) VALUES (?,?,?,?)");
            statement.setInt(1, user.get_UserID());
            statement.setString(2, user.get_User_name());
            statement.setString(3, user.get_ContactInfo());
            statement.setInt(4, user.get_BorrowedBooks());
            statement.executeUpdate();
            System.out.println("\tUser Added Successfully !\n");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("\tFailed to Add New User !\n");
        }
    }


    // Method to check out book to user
    public void checkOutBook(int userID, int bookID) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE book SET Availability_Status = FALSE WHERE book_ID = ?");
            statement.setInt(1, bookID);
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE user SET Borrowed_Books = Borrowed_Books + 1 WHERE user_ID = ?");
            statement.setInt(1, userID);
            statement.executeUpdate();

            System.out.println("Book checked out successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to check out book.");
        }
    }


    // Method to return book
    public void returnBook(int bookID) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE book SET Availability_Status = TRUE WHERE book_ID = ?");
            statement.setInt(1, bookID);
            statement.executeUpdate();

            System.out.println("Book returned successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to return book.");
        }
    }


    // Method to search for books by title or author
    public void searchBooks(String searchQuery) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM book WHERE title LIKE ? OR author LIKE ?");
            statement.setString(1, "%" + searchQuery + "%");
            statement.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Search Results :");
            while (resultSet.next()) {
                int bookID = resultSet.getInt("Book_ID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String genre = resultSet.getString("Genre");
                boolean availabilityStatus = resultSet.getBoolean("Availability_Status");

                System.out.println("Book ID : " + bookID);
                System.out.println("Title : " + title);
                System.out.println("Author : " + author);
                System.out.println("Genre : " + genre);
                System.out.println("Availability Status : " + (availabilityStatus ? "Available" : "Not Available"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to search for books.");
        }
    }

}


                                  // Main System class

public class LibraryManagementSystem {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Connect to the database
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library_Management_System", "root", "Root@123");
            Library library = new Library(connection);

            // Implement menu-driven interface
            int op;
            System.out.println("Welcome to Java Based Library Management System !");
            do {
                System.out.println("\tMenu : ");
                System.out.println("\t\t1 . Add Book");
                System.out.println("\t\t2 . Add User");
                System.out.println("\t\t3 . Check Out Book");
                System.out.println("\t\t4 . Return Book");
                System.out.println("\t\t5 . Search Books");
                System.out.println("\t\t6 . Display Books");
                System.out.println("\t\t7 . Exit");
                System.out.print("Enter the Operation Number : ") ;
                op = scanner.nextInt();
                scanner.nextLine();  // Consume newline character

                switch (op) {
                    case 1 -> addBook(library);
                    case 2 -> addUser(library);
                    case 3 -> checkOutBook(library);
                    case 4 -> returnBook(library);
                    case 5 -> searchBooks(library);
                    case 6 -> {
                    }
                    case 7 -> System.out.println("\tExiting...!\n\tThanks for using our System\n\tGoodBye !");
                    default -> System.out.println("\tInvalid choice !\n\tPlease enter a number between 1 and 10");
                }
            } while (op!= 7);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(" Failed to connect to the database ! ");
        }
    }


    //Implementation of Methods
    private static void addBook(Library library) {
        System.out.println("Enter Book Details :");
        System.out.print("ID : ");
        int book_ID = scanner.nextInt();
        System.out.print("Title : ");
        scanner.nextLine();
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Availability Status (true/false): ");
        boolean availabilityStatus = scanner.nextBoolean();
        scanner.nextLine();  // Consume newline character

        Book book = new Book(book_ID, title, author, genre, availabilityStatus);
        library.addBook(book);
    }

    private static void addUser(Library library) {
        System.out.println("Enter user details:");
        System.out.print("ID: ");
        int ID = scanner.nextInt();
        System.out.print("Name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Contact Info: ");
        String contactInfo = scanner.nextLine();
        System.out.print("Borrowed_Books: ");
        int borrowed_books = scanner.nextInt();

        User user = new User(ID, name, contactInfo, borrowed_books);
        library.addUser(user);
    }

    private static void checkOutBook(Library library) {
        System.out.print("Enter User ID: ");
        int userID = scanner.nextInt();
        System.out.print("Enter Book ID: ");
        int bookID = scanner.nextInt();
        scanner.nextLine();  // Consume newline character

        library.checkOutBook(userID, bookID);
    }

    private static void returnBook(Library library) {
        System.out.print("Enter Book ID: ");
        int bookID = scanner.nextInt();
        scanner.nextLine();  // Consume newline character

        library.returnBook(bookID);
    }

    private static void searchBooks(Library library) {
        System.out.print("Enter search query (title or author): ");
        String searchQuery = scanner.nextLine();
        library.searchBooks(searchQuery);
    }
}