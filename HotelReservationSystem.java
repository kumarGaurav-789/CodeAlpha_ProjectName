import java.io.*;
import java.util.*;

/* ---------- ROOM ---------- */
abstract class Room implements Serializable {
    int roomNo;
    String type;
    double price;
    boolean available = true;

    Room(int roomNo, String type, double price) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
    }
}

/* ---------- ROOM TYPES ---------- */
class StandardRoom extends Room {
    StandardRoom(int no) { super(no, "Standard", 2000); }
}

class DeluxeRoom extends Room {
    DeluxeRoom(int no) { super(no, "Deluxe", 3500); }
}

class SuiteRoom extends Room {
    SuiteRoom(int no) { super(no, "Suite", 6000); }
}

/* ---------- CUSTOMER ---------- */
class Customer implements Serializable {
    String name, phone;
    Customer(String n, String p) {
        name = n;
        phone = p;
    }
}

/* ---------- RESERVATION ---------- */
class Reservation implements Serializable {
    String bookingId;
    Customer customer;
    Room room;

    Reservation(String id, Customer c, Room r) {
        bookingId = id;
        customer = c;
        room = r;
    }

    void show() {
        System.out.println("\nBooking ID : " + bookingId);
        System.out.println("Customer   : " + customer.name);
        System.out.println("Room No    : " + room.roomNo);
        System.out.println("Category   : " + room.type);
        System.out.println("Price      : ₹" + room.price);
    }
}

/* ---------- MAIN CLASS ---------- */
public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<Room>();
    static ArrayList<Reservation> bookings = new ArrayList<Reservation>();
    static final String FILE = "bookings.dat";

    public static void main(String[] args) {

        load();
        initRooms();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- HOTEL RESERVATION SYSTEM ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    viewRooms();
                    break;

                case 2:
                    bookRoom(sc);
                    break;

                case 3:
                    cancelBooking(sc);
                    break;

                case 4:
                    viewBookings();
                    break;

                case 5:
                    save();
                    System.out.println("Thank you!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    /* ---------- METHODS ---------- */

    static void initRooms() {
        if (rooms.isEmpty()) {
            rooms.add(new StandardRoom(101));
            rooms.add(new DeluxeRoom(201));
            rooms.add(new SuiteRoom(301));
        }
    }

    static void viewRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (r.available) {
                System.out.println(r.roomNo + " | " + r.type + " | ₹" + r.price);
            }
        }
    }

    static void bookRoom(Scanner sc) {
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Phone: ");
        String phone = sc.next();

        System.out.print("Enter Room No: ");
        int no = sc.nextInt();

        for (Room r : rooms) {
            if (r.roomNo == no && r.available) {
                System.out.println("Payment of ₹" + r.price + " successful!");
                r.available = false;

                String id = "BKG" + System.currentTimeMillis();
                bookings.add(new Reservation(id, new Customer(name, phone), r));
                save();

                System.out.println("Room Booked! Booking ID: " + id);
                return;
            }
        }
        System.out.println("Room not available!");
    }

    static void cancelBooking(Scanner sc) {
        System.out.print("Enter Booking ID: ");
        String id = sc.next();

        Iterator<Reservation> it = bookings.iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.bookingId.equals(id)) {
                r.room.available = true;
                it.remove();
                save();
                System.out.println("Booking Cancelled");
                return;
            }
        }
        System.out.println("Booking ID not found!");
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for (Reservation r : bookings) {
            r.show();
        }
    }

    /* ---------- FILE I/O ---------- */

    static void save() {
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(FILE));
            oos.writeObject(bookings);
            oos.close();
        } catch (Exception e) {
            System.out.println("Save Error");
        }
    }

    static void load() {
        try {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(FILE));
            bookings = (ArrayList<Reservation>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            bookings = new ArrayList<Reservation>();
        }
    }
}
