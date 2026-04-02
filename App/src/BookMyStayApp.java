import java.util.*;

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
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

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