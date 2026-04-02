import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println("Booking Confirmed for " + guestName + " in " + roomType + " room.");
    }
}

// Validator Class
class InvalidBookingValidator {

    private static final List<String> VALID_ROOM_TYPES =
            Arrays.asList("Standard", "Deluxe", "Suite");

    // Validate input
    public static void validate(String guestName, String roomType, int availableRooms)
            throws InvalidBookingException {

        // Guest name validation
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Room type validation
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Inventory validation
        if (availableRooms <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Simulated inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 0);

        try {
            // User input
            System.out.print("Enter Guest Name: ");
            String guestName = sc.nextLine();

            System.out.print("Enter Room Type (Standard/Deluxe/Suite): ");
            String roomType = sc.nextLine();

            // Get available rooms
            int availableRooms = inventory.getOrDefault(roomType, 0);

            // Validate (Fail-Fast)
            InvalidBookingValidator.validate(guestName, roomType, availableRooms);

            // Proceed only if valid
            Reservation reservation = new Reservation(guestName, roomType);
            reservation.display();

            // Update inventory safely
            inventory.put(roomType, availableRooms - 1);

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking Failed: " + e.getMessage());
        } catch (Exception e) {
            // Unexpected errors
            System.out.println("Unexpected Error: " + e.getMessage());
        }

        // System continues running
        System.out.println("System is still running safely.");
    }
}