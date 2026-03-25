import java.util.*;

// Room class (Domain Model)
class Room {
    String type;
    double price;
    String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("--------------------------");
    }
}

// Inventory class (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomData;

    public SearchService(Inventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {
            int available = inventory.getAvailability(type);

            // Validation: only show available rooms
            if (available > 0) {
                Room room = roomData.get(type);
                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println("==========================");
            }
        }
    }
}

// Main App
public class UseCase4RoomSearch {
    public static void main(String[] args) {

        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0); // unavailable
        inventory.addRoom("Suite", 2);

        // Step 2: Room Data (Domain Model)
        Map<String, Room> roomData = new HashMap<>();
        roomData.put("Single", new Room("Single", 1500, "WiFi, TV"));
        roomData.put("Double", new Room("Double", 2500, "WiFi, TV, AC"));
        roomData.put("Suite", new Room("Suite", 5000, "WiFi, TV, AC, Mini Bar"));

        // Step 3: Search Service
        SearchService searchService = new SearchService(inventory, roomData);

        // Step 4: Guest searches rooms
        searchService.searchAvailableRooms();
    }
}
