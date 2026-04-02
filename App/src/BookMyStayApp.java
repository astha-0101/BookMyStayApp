import java.util.*;

// Add-On Service class
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    // Get services
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }
        return total;
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        int choice;
        do {
            System.out.println("\nSelect Add-On Service:");
            System.out.println("1. Breakfast (₹500)");
            System.out.println("2. Airport Pickup (₹1200)");
            System.out.println("3. Extra Bed (₹800)");
            System.out.println("4. Done");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, new AddOnService("Breakfast", 500));
                    break;
                case 2:
                    manager.addService(reservationId, new AddOnService("Airport Pickup", 1200));
                    break;
                case 3:
                    manager.addService(reservationId, new AddOnService("Extra Bed", 800));
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);

        // Display services
        System.out.println("\nSelected Services:");
        List<AddOnService> services = manager.getServices(reservationId);

        for (AddOnService s : services) {
            System.out.println("- " + s.getName() + " (₹" + s.getCost() + ")");
        }

        // Total cost
        double total = manager.calculateTotalCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + total);

        System.out.println("\nNote: Booking and inventory remain unchanged.");
    }
}