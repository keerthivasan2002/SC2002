import java.util.ArrayList;

public class MedicineInventory {
    private ArrayList<Medicines> medicines;
    private String medicine_File = "Medicine_List.csv";

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


}
