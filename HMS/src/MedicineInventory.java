import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class MedicineInventory {
    public ArrayList<Medicines> medicines;
    private String medicine_File = "Medicine_List.csv";
    Scanner sc = new Scanner(System.in);
    FileManager medicinesFileManager = new FileManager(medicine_File);

    public MedicineInventory(){
        medicines = new ArrayList<Medicines>();
        loadMedicalInventory();
    }

    public void loadMedicalInventory(){
        String [][] medicinesArray = medicinesFileManager.readFile();
        if (medicinesArray == null || medicinesArray.length == 0){
            System.out.println("Failed to load medicine data.");
            return;
        }

        for (int i=1; i < medicinesArray.length; i++){
            String[] row = medicinesArray[i];
            if (row.length >= 5){
                String name = row[0];
                int stock = Integer.parseInt(row[1]);
                int lowStockAlert = Integer.parseInt(row[2]);
                Medicines.status status = Medicines.status.valueOf(row[3].toUpperCase());
                int requestQuantity = Integer.parseInt(row[4]);

                Medicines meds = new Medicines(name, stock, lowStockAlert, status, requestQuantity);
                medicines.add(meds);
            } else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row));
            }

        }
    }

    public void viewMedicalInventory(){
//        System.out.println("Medicine Inventory: ");
//        System.out.printf("%-15s %-10s %-15s\n", "Medicine", "Stock", "Low Stock Alert");
//        System.out.println("----------------------------------------");
//        for (Medicines med : medicines){
//            System.out.printf("%-15s %-10d %-15d\n", med.name, med.stock, med.lowStockAlert);
//        }

        System.out.println("Medicine Inventory: ");
        System.out.printf("%-15s %-10s %-15s %-15s %-15s\n", "Medicine", "Stock", "LowStockAlert", "requestStatus", "requestQuantity" );
        System.out.println("--------------------------------------------------------------------------------");
        for (Medicines med : medicines) {
            // Check conditions and use conditional expressions for each column
            String stockDisplay = med.stock == 0 ? "" : String.valueOf(med.stock);  // Print blank if stock is 0
            String lowStockAlertDisplay = (med.stock > med.lowStockAlert) ? "" : String.valueOf(med.lowStockAlert);  // Print blank if lowStockAlert is 0
            String statusDisplay = (med.status1 == null || med.status1 == Medicines.status.NIL) ? "" : med.status1.toString();  // Print blank if status is null or "NIL"
            String requestQuantityDisplay = (med.status1 == Medicines.status.NIL || med.status1 == Medicines.status.TOREQUEST) ? "" : String.valueOf(med.requestQuantity);  // Print blank if requestQuantity is 0

            System.out.printf("%-15s %-10s %-15s %-15s %-15s\n",
                    med.name,
                    stockDisplay,
                    lowStockAlertDisplay,
                    statusDisplay,
                    requestQuantityDisplay);
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public boolean updateMedicalInventory() {
        System.out.println("Enter medicine name: ");
        String medicineName = sc.next();
        System.out.println("Enter quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        for (Medicines med : medicines) {
            if (med.name.equalsIgnoreCase(medicineName)) {
                if (med.stock >= quantity) {
                    med.stock -= quantity;
                    System.out.println(quantity + " units of " + medicineName + " prescribed. New stock: " + med.stock);

                    // Check if stock is below the low stock alert level
                    if (med.stock <= med.lowStockAlert) {
                        System.out.println("Alert: Stock for " + medicineName + " is low. Current stock: " + med.stock);
                    }
                    return true;  // Successfully updated
                } else {
                    System.out.println("Error: Insufficient stock for " + medicineName);
                    return false; // Not enough stock
                }
            }
        }
        System.out.println("Error: Medicine " + medicineName + " not found in inventory.");
        return false;  // Medicine not found
    }
    
    public void add() {
        // Prompt and accept input for userID
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        // Prompt and accept input for password
        System.out.print("Enter stock: ");
        int stock = Integer.parseInt(sc.nextLine());

        // Prompt and accept input for name
        System.out.print("Enter Low Stock Alert Limit");
        int lowStockAlert = Integer.parseInt(sc.nextLine());

        Medicines.status status1;
        if (stock <= lowStockAlert) {
            status1 = Medicines.status.TOREQUEST;
        } else {
            status1 = Medicines.status.NIL;
        }

        int requestQuantity = 0;
        Medicines meds = new Medicines(name, stock, lowStockAlert, status1, requestQuantity);
        // Should add avoid adding duplicates
        for(Medicines Medicine : medicines){
            if(Medicine.name.equals(name)) {
                System.out.print("Cannot add duplicate!");
                return;
            }
        }
        medicines.add(meds);
        String[] appointment = new String[]{name, String.valueOf(stock), String.valueOf(lowStockAlert), String.valueOf(status1), String.valueOf(requestQuantity)};
        FileManager appointmentFM = new FileManager(medicine_File);
        medicinesFileManager.addNewRow(appointment);
    }
    

    public void rm(){
        System.out.print("Enter name of Medicine to be removed: ");
        String toberemoved = sc.nextLine();
        Iterator<Medicines> iterator = medicines.iterator();

        while (iterator.hasNext()) {
            Medicines medicine = iterator.next(); // wah i don't unds this part
            if (medicine.name.equals(toberemoved)) { // Check if name matches
                iterator.remove(); // Remove the medicine from the list
                System.out.println("Medicine " + toberemoved + " removed.");
                return; // Indicate that the removal was successful
            }
        }
        System.out.println("Medicine " + toberemoved + " not found.");
        // Indicate that no matching medicine was found
    }

    /**
     JavaDocs Doesn't Work??

     Gets the particular Medicine Object, sets new Stock level from input, sets it in medicines list
     [QN] Do I need to set the medicines using medicine.set() or will changing Medicine.stock good enough
     */
    public void update() {
        System.out.print("Enter name of Medicine to update Stock");
        String name = sc.nextLine();
        for (Medicines medicine : medicines) {
            if (medicine.name.equals(name)) {
                //int N = medicines.indexOf(medicine);
                System.out.printf("%-15s %-10d %-15d\n", medicine.name, medicine.stock, medicine.lowStockAlert);
                System.out.print("Enter new Stock Level to be updated");
                // [GPT] Need to Error Check
                try {
                    medicine.stock = Integer.parseInt(sc.nextLine());
                    //medicines.set(N, medicine);
                    if(medicine.stock <= medicine.lowStockAlert) medicine.status1 = Medicines.status.TOREQUEST;
                    else medicine.status1 = Medicines.status.NIL;
                    System.out.print("updated New Stock Level!");
                }catch (NumberFormatException e){
                    System.out.println("Invalid input. Please enter a numeric value");
                }

                return;
            }
        }
        System.out.print("Stock not found!");
    }
    public void updateLowStockAlert(){
        System.out.print("Enter name of Medicine to change Low Stock Alert level");
        String name = sc.nextLine();
        for (Medicines medicine : medicines){
            if(medicine.name.equals(name)){
                System.out.printf("%-15s, %-10s, %-15s\n", medicine.name, medicine.stock, medicine.lowStockAlert);
                System.out.print("Enter new Low Stock Alert Value to be updated");
                try {
                    medicine.lowStockAlert = Integer.parseInt(sc.nextLine());
                    //medicines.set(N, medicine);
                    if (medicine.stock <= medicine.lowStockAlert) medicine.status1 = Medicines.status.TOREQUEST;
                    else medicine.status1 = Medicines.status.NIL;
                    System.out.print("updated New Stock Level!");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a numeric value");
                }
                return;
            }
        }
    }

    public void saveMedicines(){
        FileManager patientFileManager = new FileManager(medicine_File);

        // Convert each patient to a string array
        String[][] medicineData = new String[medicines.size() + 1][];
      medicineData[0] = new String[]{"MedicineName", "Initial Stock", "LowStockAlert", "requestStatus", "requestQuantity"}; // Header row

        for (int i = 0; i < medicines.size(); i++) {
            medicineData[i+1] = medicines.get(i).toArray();
        }
        // Write to file
        patientFileManager.writeFile(medicineData, false);
    }
}
