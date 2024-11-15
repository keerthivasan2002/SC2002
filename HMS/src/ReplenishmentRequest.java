import java.util.ArrayList;
import java.util.Scanner;

public class ReplenishmentRequest {
    private String medicineName;
    private int requestedQuantity;
    private String status;  // Status can be "Pending", "Approved", or "Rejected"
    private MedicineInventory mi;
    // A list to store all replenishment requests (for demonstration purposes)
    //private static ArrayList<ReplenishmentRequest> requests = new ArrayList<>();

    // Constructor
    public ReplenishmentRequest(String medicineName, int requestedQuantity, MedicineInventory mi) {
        this.medicineName = medicineName;
        this.requestedQuantity = requestedQuantity;
        this.status = "Pending";  // Initial status of the request
        this.mi = mi;
    }

    // Method to submit a replenishment request
    public static void submitReplenishmentRequest() {         // for pharmacist
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter medicine name: ");
        String medicineName = sc.next();
        System.out.println("Enter quantity: ");
        int quantity = sc.nextInt();
       // sc.nextLine();

       // ReplenishmentRequest request = new ReplenishmentRequest(medicineName, quantity, mi);
        //requests.add(request);
        //System.out.println("Replenishment request submitted for " + quantity + " units of " + medicineName + ".");
    }

    // Method to approve a replenishment request
    public void approveReplenishmentRequest() {  //for administrator
        mi.viewMedicalInventory();
        // Filter if status = pending printfile
        // input name of medicine to be approved

    }
    // Additional method to print all requests (optional, for tracking purposes)
    public static void viewAllRequests() {          //for administrator
        System.out.println("All Replenishment Requests:");
        System.out.printf("%-15s %-10s %-10s\n", "Medicine", "Stock", "lowStockAlert");
        System.out.println("-------------------------------------");
    }
}

