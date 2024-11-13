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
        System.out.println("Medicine Inventory: ");
        System.out.printf("%-15s %-10s %-15s\n", "Medicine", "Stock", "Low Stock Alert");
        System.out.println("----------------------------------------");
        for (Medicines med : medicines){
            System.out.printf("%-15s %-10d %-15d\n", med.name, med.stock, med.lowStockAlert);
        }
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
    public void update(){
        // Do I only change the stock level (yes because
    }

}
