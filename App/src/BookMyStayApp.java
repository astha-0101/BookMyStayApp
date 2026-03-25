import java.util.HashMap;

// Abstract Room class
abstract class Room {
    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price: Rs." + price);
    }
}

// Concrete room types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3000);
    }
}

// Inventory management
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with initial count
    public void addRoomType(Room room, int count) {
        inventory.put(room.getType(), count);
    }

    // Retrieve availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (e.g., booking or cancellation)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type) + " rooms available");
        }
        System.out.println();
    }
}

// Main class (Use Case 3)
public class BookMyStayApp {
    public static void main(String[] args) {

        // Initialize rooms
        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single, 5);
        inventory.addRoomType(doub, 3);
        inventory.addRoomType(suite, 2);

        // Display room details and inventory
        System.out.println("===== Welcome to Book My Stay =====\n");

        single.displayDetails();
        System.out.println();

        doub.displayDetails();
        System.out.println();

        suite.displayDetails();
        System.out.println();

        inventory.displayInventory();

        // Example: book a Single Room
        System.out.println("Booking 1 Single Room...");
        inventory.updateAvailability("Single Room", -1);
        inventory.displayInventory();

        // Example: add more Suite Rooms
        System.out.println("Adding 1 Suite Room...");
        inventory.updateAvailability("Suite Room", 1);
        inventory.displayInventory();

        System.out.println("===== Thank You =====");
    }
}