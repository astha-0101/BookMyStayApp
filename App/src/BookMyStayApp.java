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
        }
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
    }
}