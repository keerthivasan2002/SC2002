import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class MedicineInventory {
    public ArrayList<Medicines> medicines;
    private ArrayList<MedicalRecord> medicalRecords;
    
    private String medicine_File = "Medicine_List.csv";
    Scanner sc = new Scanner(System.in);
    FileManager medicinesFileManager = new FileManager(medicine_File);
    MedicalRecordManager mrm = new MedicalRecordManager();
    OptionHandling oh = new OptionHandling();

    public MedicineInventory(){
        medicines = new ArrayList<Medicines>();
        loadMedicalInventory();
        this.medicalRecords = mrm.getMedicalRecord();
    }

    public OptionHandling getOh(){
        return oh;
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
            String stockDisplay = String.valueOf(med.stock);
            String lowStockAlertDisplay = (med.stock > med.lowStockAlert) ? "" : String.valueOf(med.lowStockAlert);  // Print blank if lowStockAlert is 0
            if (med.stock <= med.lowStockAlert && med.status1 != Medicines.status.PENDING) med.status1 = Medicines.status.TOREQUEST; // Exception handling
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

    public void viewAvailableMedicines(){
        System.out.println("Available Medicines: ");
        System.out.printf("%-15s %-10s \n", "Medicine", "Stock");
        System.out.println("----------------------------------------");
        for (Medicines med : medicines){
            if (med.stock > 0){
                System.out.printf("%-15s %-10d \n", med.name, med.stock);
            }
        }
    }


    public boolean updateMedicalInventory() {
        try{        
            // Check if there are any medical records
            if (medicalRecords == null || medicalRecords.size() == 0) {
                System.out.println("No medical records found.");
                return false;
            }

            System.out.println("Enter patient ID: ");
            String patientID = sc.next();        
        
            // Get all records for the patient
            ArrayList<MedicalRecord> records =  mrm.getAllRecordsForPatient(patientID);
            if (records == null || records.size() == 0) {
                System.out.println("No medical records found for patient ID [medicine inventory]1" + patientID);
                return false;
            } 
            // Display all records for the patient
            mrm.displayMedicalRecords(records);

            // Get all records for the patient
            ArrayList<MedicalRecord> recordsByStatus = new ArrayList<>();
            for (MedicalRecord record : records) {
                if (record.isPrescriptionStatus() == false) {
                    recordsByStatus.add(record);
                }
            }
            if (recordsByStatus == null || recordsByStatus.size() == 0) {
                System.out.println("No medical records found for patient ID [medicine inventory]2" + patientID);
                return false;
            }
            
            // Display records with pending prescriptions
            mrm.displayMedicalRecords(recordsByStatus);


            // Prompt and accept input for recordID
            ArrayList<MedicalRecord> recordsByID = new ArrayList<>();
            System.out.println("Enter medical record ID: ");
            int recordID = mrm.getValidMedicalRecordID();
            // System.out.println("Record size: " + records.size());
            if (recordID == -1) {
                throw new InvalidPositiveOptionException();
            }else if (recordID == -2){
                throw new IntNonNegativeException();
            }else{
                for (MedicalRecord record : records) {
                    if (record.getMedicalRecordID() == recordID) {
                        recordsByID.add(record);
                    }
                }
            }
            
            if (recordsByID == null || recordsByID.size() == 0) {
                System.out.println("No medical records found for record ID [medicine inventory]3" + recordID);
                return false;
            }

            viewAvailableMedicines();
            System.out.println("Enter medicine name: ");
            String medicineName = sc.next();

            
            System.out.println("Enter quantity to dispense to patient: ");
            int quantity = sc.nextInt();
            // sc.nextLine();

            //update medical record status (dispenesed)
            mrm.updateMedicalRecord(recordID);
            mrm.saveMedicalRecords();

            //update inventory
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
        }catch(IntNonNegativeException e){
            System.out.println("Error: Invalid input. Please enter a positive number.");
            return false;  // Medicine not found
        }catch(InvalidPositiveOptionException e){
            System.out.println("Error: Invalid input. Please enter a valid record ID.");
            return false;  // Medicine not found
        }catch(Exception e){
            System.out.println("Error: Invalid input. Please enter a number.");
            return false;  // Medicine not found
        }
        return false;  // Default return statement
    }

    


    
    public void add() {
        // Prompt and accept input for userID
        System.out.println("Enter name: ");
        String name = sc.nextLine(); //Need to create another execption handling for string names, cannot be more than certain length
        for (Medicines med : medicines){
            if (med.name.equalsIgnoreCase(name)) {
                System.out.println("Medicine is already in inventory.");
                return;
            }
        }

        // Prompt and accept input for stock
        System.out.println("Enter stock: ");
        int stock = oh.getOption(1,999999999);

        // Prompt and accept input for low stock alert
        System.out.println("Enter Low Stock Alert Limit: ");
        int lowStockAlert = oh.getOption(1, 999999999);

        Medicines.status status1;
        if (stock <= lowStockAlert) status1 = Medicines.status.TOREQUEST;
        else status1 = Medicines.status.NIL;

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
            if (medicine.name.equalsIgnoreCase(toberemoved)) { // Check if name matches
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
        System.out.println("Enter name of Medicine to update Stock:");
        String name = sc.nextLine(); // [TRY_EXCEPT]
        for (Medicines medicine : medicines) {
            if (medicine.name.equalsIgnoreCase(name)) {
                //int N = medicines.indexOf(medicine);
                System.out.printf("%-15s %-10d %-15d\n", medicine.name, medicine.stock, medicine.lowStockAlert);
                System.out.println("Enter new Stock Level to be updated:");
                medicine.stock = oh.getOption(1, 99999999);
                if(medicine.stock <= medicine.lowStockAlert) medicine.status1 = Medicines.status.TOREQUEST;
                else medicine.status1 = Medicines.status.NIL;
                System.out.print("updated New Stock Level!\n");
                return;
            }
        }
        System.out.print("Stock not found!");
    }
    public void updateLowStockAlert(){
        System.out.println("Enter name of Medicine to change Low Stock Alert level:");
        String name = sc.nextLine();
        for (Medicines medicine : medicines){
            if(medicine.name.equalsIgnoreCase(name)){
                System.out.printf("%-15s %-10d %-15s\n", medicine.name, medicine.stock, medicine.lowStockAlert);
                System.out.println("Enter new Low Stock Alert Value to be updated:");
                medicine.lowStockAlert = oh.getOption(1, 99999999);
                if (medicine.stock <= medicine.lowStockAlert) medicine.status1 = Medicines.status.TOREQUEST;
                else medicine.status1 = Medicines.status.NIL;
                System.out.println("updated New Stock Level!");
                return;
            }
        }
        System.out.println("Medicine not Found");
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

    public int viewRequestsAdmin(){
        int counter = 0;
        for (Medicines med : medicines) {
            // Check conditions and use conditional expressions for each column
            if(med.status1 == Medicines.status.PENDING) {
                 counter++;
            }
        }
        if (counter == 0) {
            System.out.println("There are no pending Requests");
            return 0;
        }
        if(counter == 1000){
            System.out.println("TOO MANY REQUESTS...EXPLODING");
            return 0;
        }
        System.out.printf("Pending Requests: %1d\n", counter);
        System.out.printf("%-15s %-15s %-15s\n", "Medicine", "RequestStatus", "RequestQuantity");
        System.out.println("-------------------------------------------------");
        for (Medicines medicine : medicines) {
            // Check conditions and use conditional expressions for each column
            if(medicine.status1 == Medicines.status.PENDING) {
                String stockDisplay = medicine.stock == 0 ? "" : String.valueOf(medicine.stock);  // Print blank if stock is 0 [TRY EXECPT: UNECESSARY!]
                String lowStockAlertDisplay = (medicine.stock > medicine.lowStockAlert) ? "" : String.valueOf(medicine.lowStockAlert);  // Print blank if lowStockAlert is 0
                String status1 = medicine.status1.toString();  // prints the negative status
                String requestQuantityDisplay = String.valueOf(medicine.requestQuantity);  // Print blank if requestQuantity is 0
                System.out.printf("%-15s %-15s %-15s\n",
                    medicine.name,
                    status1,
                    requestQuantityDisplay);
            }
        }
        return counter;
    }
    public void approveRequest() {
        System.out.print("Enter name of Medicine to Approve:\n");
        String name = sc.nextLine();
        for (Medicines medicine : medicines) {
            if (medicine.name.equalsIgnoreCase(name)) {
                if(medicine.stock + medicine.requestQuantity <= medicine.lowStockAlert){
                    System.out.println("Not allowed to Approve, requested quantity too low\n");
                    return;
                }
                medicine.status1 = Medicines.status.NIL;
                medicine.stock = medicine.requestQuantity;
                medicine.requestQuantity = 0;
                System.out.println("Request Approved!\n");
                return;
            }
        }
        System.out.println("Request not found\n");
    }
    public void rejectRequest(){
        System.out.print("Enter name of Medicine to reject:\n");
        String name = sc.nextLine();
        for (Medicines medicine : medicines) {
            if (medicine.name.equalsIgnoreCase(name)) {
                if (medicine.stock <= medicine.lowStockAlert) medicine.status1 = Medicines.status.TOREQUEST;
                else medicine.status1 = Medicines.status.NIL;
                medicine.requestQuantity = 0;
                System.out.println("Request Rejected!\n");
                return;
            }
        }
        System.out.println("Request not found\n");
    }
}
