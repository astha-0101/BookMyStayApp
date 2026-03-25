import java.util.*;

// Reservation class (represents booking request)
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request added for " + reservation.guestName);
    }

    // View all requests (read-only)
    public void viewRequests() {
        System.out.println("\nBooking Requests in Queue (FIFO Order):\n");

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main class (IMPORTANT)
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Create Booking Queue
        BookingQueue bookingQueue = new BookingQueue();

        // Step 2: Simulate Guest Requests
        bookingQueue.addRequest(new Reservation("Aman", "Single"));
        bookingQueue.addRequest(new Reservation("Riya", "Suite"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double"));

        // Step 3: View Queue (FIFO order)
        bookingQueue.viewRequests();

        // NOTE: No allocation or inventory update here
    }
}