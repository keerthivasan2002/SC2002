
import java.util.ArrayList;
import java.util.Scanner;

public class ReplenishmentRequest {
    private String medicineName;
    private int requestedQuantity;
    private String status;  // Status can be "Pending", "Approved", or "Rejected"
    private MedicineInventory mi;
    // A list to store all replenishment requests (for demonstration purposes)
    private static ArrayList<ReplenishmentRequest> requests = new ArrayList<>();
    OptionHandling oh = new OptionHandling();

    // Getter for medicineName (for use in other classes)
    public String getMedicineName() {
        return medicineName;
    }

    // Getter for requestedQuantity (for use in other classes)
    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    // Constructor
    public ReplenishmentRequest(String medicineName, int requestedQuantity, MedicineInventory mi) {
        this.medicineName = medicineName;
        this.requestedQuantity = requestedQuantity;
        this.status = "Pending";  // Initial status of the request
        this.mi = mi;
    }

    // Method to submit a replenishment request
    public static void submitReplenishmentRequest(MedicineInventory mi) {         // for pharmacist
        Scanner sc = new Scanner(System.in);
        OptionHandling oh = mi.getOh();
        System.out.println("Enter medicine name: ");
        String medicineName = sc.next();
        sc.nextLine();

        for (Medicines medicines: mi.medicines){
            if(medicines.name.equalsIgnoreCase(medicineName)){
//                System.out.println("Enter request quantity: ");
//                medicines.requestQuantity = sc.nextInt();   //cldnt do oh bcos this is static method
                medicines.requestQuantity = getQuantityFromUser(oh, mi, medicineName);
                medicines.status1 = Medicines.status.PENDING;
                System.out.println("Submitted Replenishment Request.");
                return;
            }
        }
        System.out.println("Error: Medicine " + medicineName + " not found in inventory.");
    }

    private static int getQuantityFromUser(OptionHandling oh, MedicineInventory mi, String medicineName) {
        Scanner sc = new Scanner(System.in);
        int minQuantity = 1; // Minimum quantity allowed
        int maxQuantity = 9999999; // Assuming no upper limit initially; this can be adjusted for any business rules
        int requestedQuantity = 1;

        System.out.println("Enter request quantity: ");

        try{
            requestedQuantity = oh.getOption(minQuantity, maxQuantity); // Use OptionHandling here
            if(requestedQuantity <= 0){
                throw new IntNonNegativeException();
            }
        } catch (Exception e){
            System.out.println("Invalid input. Please enter a number.");
            return 0; //Or handle the error in a more sophisticated manner.
        }
        //sc.close(); //Important to prevent resource leak
        return requestedQuantity;
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

