import java.io.*;
import java.util.*;


// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// System State (Inventory + Booking History)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void saveState(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());

// Reservation class
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;
    boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }
}

// Cancellation Service
class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> releasedRooms = new Stack<>();

    public void cancelBooking(String reservationId,
                              Map<String, Reservation> reservations,
                              Map<String, Integer> inventory) {

        // Validate reservation existence
        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation res = reservations.get(reservationId);

        // Prevent duplicate cancellation
        if (res.isCancelled) {
            System.out.println("Cancellation Failed: Booking already cancelled.");
            return;

        }

        // Step 1: Push roomId to stack (rollback tracking)
        releasedRooms.push(res.roomId);

        // Step 2: Restore inventory
        inventory.put(res.roomType, inventory.get(res.roomType) + 1);

        // Step 3: Mark as cancelled
        res.isCancelled = true;

        // Step 4: Update booking history (logical state change)
        System.out.println("Booking Cancelled Successfully for ID: " + reservationId);
        System.out.println("Room " + res.roomId + " released back to inventory.");
    }

    // View rollback stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recently Released Rooms): " + releasedRooms);
    }

    // Load state from file
    public static SystemState loadState() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();


        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }

        // Safe default state
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        return new SystemState(inventory, new ArrayList<>());
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Load previous state
        SystemState state = PersistenceService.loadState();

        Map<String, Integer> inventory = state.inventory;
        List<Reservation> bookings = state.bookings;

        // Display recovered state
        System.out.println("\nRecovered Inventory: " + inventory);
        System.out.println("Recovered Bookings: " + bookings);

        // Step 2: Simulate new booking
        Reservation newBooking = new Reservation("RES" + (bookings.size() + 1),
                "Astha", "Deluxe");

        bookings.add(newBooking);

        // Update inventory safely
        if (inventory.get("Deluxe") > 0) {
            inventory.put("Deluxe", inventory.get("Deluxe") - 1);
            System.out.println("\nNew Booking Added: " + newBooking);
        } else {
            System.out.println("\nNo Deluxe rooms available.");
        }

        // Step 3: Save updated state
        PersistenceService.saveState(new SystemState(inventory, bookings));

        System.out.println("\nSystem shutting down safely...");

        // Inventory (roomType → count)
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Standard", 1);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);

        // Confirmed reservations
        Map<String, Reservation> reservations = new HashMap<>();

        // Simulate confirmed bookings
        reservations.put("RES101", new Reservation("RES101", "Astha", "Deluxe", "D1"));
        reservations.put("RES102", new Reservation("RES102", "Rahul", "Standard", "S1"));

        // Reduce inventory as if rooms were allocated
        inventory.put("Deluxe", 0);
        inventory.put("Standard", 0);

        CancellationService service = new CancellationService();

        // Perform cancellation
        service.cancelBooking("RES101", reservations, inventory);

        // Try invalid cancellation
        service.cancelBooking("RES999", reservations, inventory);

        // Try duplicate cancellation
        service.cancelBooking("RES101", reservations, inventory);

        // Display rollback stack
        service.showRollbackStack();

        // Display updated inventory
        System.out.println("\nUpdated Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }

    }
}