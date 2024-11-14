import java.util.ArrayList;

public class ReplenishmentRequest {
    private String medicineName;
    private int requestedQuantity;
    private String status;  // Status can be "Pending", "Approved", or "Rejected"

    // A list to store all replenishment requests (for demonstration purposes)
    private static ArrayList<ReplenishmentRequest> requests = new ArrayList<>();

    // Constructor
    public ReplenishmentRequest(String medicineName, int requestedQuantity) {
        this.medicineName = medicineName;
        this.requestedQuantity = requestedQuantity;
        this.status = "Pending";  // Initial status of the request
    }

    // Method to submit a replenishment request
    public static void submitReplenishmentRequest(String medicineName, int requestedQuantity) {         // for pharmacist
        ReplenishmentRequest request = new ReplenishmentRequest(medicineName, requestedQuantity);
        requests.add(request);
        System.out.println("Replenishment request submitted for " + requestedQuantity + " units of " + medicineName + ".");
    }

    // Method to approve a replenishment request
    public static void approveReplenishmentRequest(String medicineName, MedicineInventory inventory) {  //for administrator
        for (ReplenishmentRequest request : requests) {
            if (request.medicineName.equalsIgnoreCase(medicineName) && request.status.equals("Pending")) {
                request.status = "Approved";
                inventory.updateMedicalInventory(medicineName, request.requestedQuantity);
                System.out.println("Replenishment request approved for " + request.requestedQuantity + " units of " + medicineName + ".");
                return;
            }
        }
        System.out.println("No pending replenishment request found for " + medicineName + ".");
    }

    // Additional method to print all requests (optional, for tracking purposes)
    public static void viewAllRequests() {          //for administrator
        System.out.println("All Replenishment Requests:");
        System.out.printf("%-15s %-10s %-10s\n", "Medicine", "Quantity", "Status");
        System.out.println("-------------------------------------");
        for (ReplenishmentRequest request : requests) {
            System.out.printf("%-15s %-10d %-10s\n", request.medicineName, request.requestedQuantity, request.status);
        }
    }
}
