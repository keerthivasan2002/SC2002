import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class MedicineInventory {
    public ArrayList<Medicines> medicines;
    private String medicine_File = "Medicine_List.csv";
    Scanner sc = new Scanner(System.in);

    public MedicineInventory(){
        medicines = new ArrayList<Medicines>();
        loadMedicalInventory();
    }

    public void loadMedicalInventory(){
        FileManager medicinesFileManager = new FileManager(medicine_File);
        String [][] medicinesArray = medicinesFileManager.readFile();

        if (medicinesArray == null || medicinesArray.length == 0){
            System.out.println("Failed to load medicine data.");
            return;
        }

        for (int i=1; i < medicinesArray.length; i++){
            String[] row = medicinesArray[i];
            if (row.length >= 3){
                String name = row[0];
                int stock = Integer.parseInt(row[1]);
                int lowStockAlert = Integer.parseInt(row[2]);

                Medicines meds = new Medicines(name, stock, lowStockAlert);
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
        System.out.printf("%-15s %-10s\n", "Medicine", "Stock");
        System.out.println("----------------------------------------");
        for (Medicines med : medicines) {
            System.out.printf("%-15s %-10d\n", med.name, med.stock);
        }
    }

    public boolean updateMedicalInventory(String medicineName, int quantity) {
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
        Medicines meds = new Medicines(name, stock, lowStockAlert);
        // Should add avoid adding duplicates
        for(Medicines Medicine : medicines){
            if(Medicine.name.equals(name)) {
                System.out.print("Cannot add duplicate!");
                return;
            }
        }
        medicines.add(meds);
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
                    System.out.print("updated New Stock Level!");
                }catch (NumberFormatException e){
                    System.out.println("Invalid input. Please enter a numeric value");
                }
                return;
            }
        }
        System.out.print("Stock not found!");
    }
}
