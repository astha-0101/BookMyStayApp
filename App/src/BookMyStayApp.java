import java.util.*;

// Reservation class
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized add
    public synchronized void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request Added: " + r.guestName);
    }

    // synchronized remove
    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Shared Inventory (Critical Section)
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Standard", 1);
        rooms.put("Deluxe", 1);
        rooms.put("Suite", 1);
    }

    // synchronized allocation (critical section)
    public synchronized boolean allocateRoom(String roomType, String guestName) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocated " + roomType + " room to " + guestName);
            rooms.put(roomType, available - 1);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED for " + guestName + " (No " + roomType + " rooms)");
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + rooms);
    }
}

// Worker Thread (Concurrent Booking Processor)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(String name, BookingQueue queue, Inventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            // synchronized queue access
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            // critical section
            inventory.allocateRoom(r.roomType, r.guestName);

            try {
                Thread.sleep(100); // simulate processing delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Astha", "Deluxe"));
        queue.addRequest(new Reservation("Rahul", "Deluxe"));
        queue.addRequest(new Reservation("Neha", "Standard"));
        queue.addRequest(new Reservation("Amit", "Standard"));
        queue.addRequest(new Reservation("Sara", "Suite"));

        // Create multiple threads
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();
    }
}
