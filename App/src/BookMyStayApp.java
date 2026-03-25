import java.util.*;

// Reservation (Booking Request)
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service
class Inventory {
    private Map<String, Integer> rooms;

    public Inventory() {
        rooms = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void decreaseRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

// Booking Service (Allocation Logic)
class BookingService {
    private Queue<Reservation> queue;
    private Inventory inventory;

    // Track allocated rooms
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomTypeMap;

    private int roomCounter = 1;

    public BookingService(Queue<Reservation> queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
        allocatedRoomIds = new HashSet<>();
        roomTypeMap = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String type) {
        return type.substring(0, 1).toUpperCase() + roomCounter++;
    }

    public void processBookings() {
        while (!queue.isEmpty()) {

            Reservation r = queue.poll(); // FIFO
            String type = r.roomType;

            System.out.println("\nProcessing request for " + r.guestName);

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                String roomId = generateRoomId(type);

                // Ensure uniqueness
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    // Map room type to allocated IDs
                    roomTypeMap.putIfAbsent(type, new HashSet<>());
                    roomTypeMap.get(type).add(roomId);

                    // Update inventory
                    inventory.decreaseRoom(type);

                    // Confirm booking
                    System.out.println("Booking Confirmed!");
                    System.out.println("Guest: " + r.guestName);
                    System.out.println("Room Type: " + type);
                    System.out.println("Room ID: " + roomId);

                } else {
                    System.out.println("Error: Duplicate Room ID!");
                }

            } else {
                System.out.println("Booking Failed! No rooms available for " + type);
            }
        }
    }
}

// Main Class (IMPORTANT)
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Inventory setup
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        // Step 2: Booking Queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.add(new Reservation("Aman", "Single"));
        queue.add(new Reservation("Riya", "Single"));
        queue.add(new Reservation("Rahul", "Single")); // should fail
        queue.add(new Reservation("Neha", "Double"));

        // Step 3: Booking Service
        BookingService service = new BookingService(queue, inventory);

        // Step 4: Process Bookings
        service.processBookings();
    }
}