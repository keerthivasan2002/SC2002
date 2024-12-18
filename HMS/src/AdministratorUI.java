import java.util.Scanner;
import java.util.ArrayList;

public class AdministratorUI implements UserUI{
        // Need DoctorID, Need patientID,
        // Staff management UI
    /*
        1. Manage hospital staff (doctors and pharmacists) by adding, updating, or
        removing staff members.
        2. Display a list of staff filtered by role, gender, age, etc.
    */
        // Appointment Management UI
    /*
        Access real time updates of scheduled Appts
       Details should include:
        - Patient ID
        - Doctor ID
        - Appointment status (e.g., confirmed, canceled, completed).
        - Date and time of appointment
        - Appointment Outcome Record (for completed appointments)
     */
        // Inventory management UI
    /*
        Administrators can view and manage the inventory of medication including,
        adding, removing or updating stock levels.
    ○ Administrators can update the low stock level alert line of each medicine.
    ○ Administrators can approve replenishment requests from pharmacists. Once the
    request is approved, the stock level will be updated automatically
    */

        private String userID;
        private Staff admin;
        private StaffManager sm;
        private AppointmentManager am;
        private PatientManager pm;

        private MedicineInventory mi = new MedicineInventory();
        
       // private InventoryManager im;
        
        Scanner sc = new Scanner(System.in);
        OptionHandling oh = new OptionHandling();
        /*
         FOR FILE TRANSFER / RECEIVING
         Admin admin;
         AdminManager am;
         ..
          */
        public AdministratorUI(String userID, StaffManager sm, AppointmentManager am, PatientManager pm, MedicineInventory mi) {
            this.userID = userID;
            this.sm = sm;
            this.admin = sm.selectStaff(userID);
            this.am = am;
            this.mi = mi;
            this.pm = pm;
            // System.out.println("AdministratorUI initialized with valid dependencies.[AdministratorUI]");
            // System.out.println("UserID: " + userID);
            // System.out.println("StaffManager is null: [AdministratorUI]" + (sm == null));
            // System.out.println("Admin is null: [AdministratorUI]" + (admin == null));
            // System.out.println("AppointmentManager is null: [AdministratorUI]" + (am == null));
            // System.out.println("PatientManager is null: [AdministratorUI]" + (pm == null));
            //Handling errors with the code
            if (this.admin == null) {
                System.out.println("No administrators found with the given ID:" + userID);
            } else {
                userOption();
            }
        }

    public void userOption() {
        int choice = -1;

        System.out.println("Hello " + admin.getName() + " (UserID: " + userID + ").");
        System.out.println("What would you like to do today?");

        while (true){
            adminMenu();
            choice = oh.getOption(1, 4);

            switch (choice) {
                case 1: // Manage Staff Records
                    viewAndManageStaffRecords();
                    break;
                case 2:
                    //manageAppointment();
                    viewPatientOrDoctorAppointment();
                    break;
                case 3:
                    manageMedicationInventory();
                    break;
                case 4:
                    System.out.println("Thank you! Hope to see you soon :) \n");
                    System.exit(0);
                    return;
                default:
                    break;
            }
        }
    }

    private void adminMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Patient Menu:");
        System.out.println("1. View and Manage Staff Records");
        System.out.println("2. View if scheduled appointment is updated");
        System.out.println("3. View and manage inventory of medication ");
        System.out.println("4. Logout");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.print("Enter your choice: ");
    }

    public void staffRecordMenu(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Staff Record Menu");
        System.out.println("1. Add Staff Member");
        System.out.println("2. Update Staff Member");
        System.out.println("3. Remove Staff Member");
        System.out.println("4. Display Staff Members");
        System.out.println("5. Back to Main Menu");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public void displayAppointmentMenu(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Filter appointments by:      ");
        System.out.println("1. Patient ID");
        System.out.println("2. Doctor ID");
        System.out.println("3. Back to Main Menu");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public void statusMenu(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Filter appointments by:    ");
        System.out.println("1. Confirmed");
        System.out.println("2. Cancelled");
        System.out.println("3. Completed");
        System.out.println("4. Rejected");
        System.out.println("5. Pending");
        System.out.println("6. Back to Main Menu");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public void viewPatientOrDoctorAppointment(){
        int choice = -1;
        while (choice != 3){
            displayAppointmentMenu();
            System.out.print("Enter your choice: ");
            choice = oh.getOption(1, 3);
            // sc.nextLine();

            if (choice == 3) {
                break; // Exit the main menu loop
            }

            ArrayList<Appointment> filteredAppointments = new ArrayList<>();

            switch (choice){
                case 1:
                    System.out.print("Enter Patient ID: ");
                    String patientID = sc.nextLine().trim().toUpperCase();
                    System.out.println("PatientID = " + patientID);
                    Patient patient = pm.selectPatient(patientID);
                    System.out.println("Patient = " + patient.getName());
                    filteredAppointments = am.getPatientAppointments(patient,0);
                    if (filteredAppointments.isEmpty()) {
                        System.out.println("No appointments found for the given criteria.[AdministratorUI]");
                        continue;
                    }
                    break;
                case 2:
                    System.out.print("Enter Doctor ID: ");
                    String doctorID = sc.nextLine().trim().toUpperCase();
                    Staff doctor = sm.selectStaff(doctorID);
                    filteredAppointments = am.getDoctorAppointments(doctor,0);
                    if (filteredAppointments.isEmpty()) {
                        System.out.println("No appointments found for the given criteria.[AdministratorUI]");
                        continue;
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }

            if (filteredAppointments.isEmpty()) {
                System.out.println("No appointments found for the given criteria.");
                continue;
            }
            am.displayAppointment(filteredAppointments);

            ArrayList<Appointment> filteredStatusAppointments = new ArrayList<>();
            int statusChoice = -1;
            while (statusChoice != 6){
                statusMenu();
                System.out.print("Enter your choice: ");
                statusChoice = oh.getOption(1, 6);
                // sc.nextLine();
                switch (statusChoice){
                    case 1:

                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments, AppointmentStatus.CONFIRMED);
                        break;
                    case 2:
                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments, AppointmentStatus.CANCELLED);
                        break;
                    case 3:
                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments, AppointmentStatus.COMPLETED);
                        break;
                    case 4:
                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments, AppointmentStatus.REJECTED);
                        break;
                    case 5:
                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments, AppointmentStatus.PENDING);
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
                am.displayAppointment(filteredStatusAppointments);
            }
        }
    }

     /*
    1. Manage hospital staff (doctors and pharmacists) by adding, updating, or
    removing staff members.
    2. Display a list of staff filtered by role, gender, age, etc.
    */
            // wait for Gui Shan to manage Staff Records...
    private void viewAndManageStaffRecords(){
        int choice = -1;
        String staffIDString = "";
        while (choice != 5){
            staffRecordMenu();
            choice = oh.getOption(1, 5);
            // sc.nextLine();
            switch (choice){
                case 1:

                    sm.addStaffMember();
                    break;
                case 2:
                    System.out.println("Update Staff Member: ");
                    System.out.println("---------------------------------");
                    System.out.println("Enter the ID of the staff member to Update: ");
                    staffIDString = sc.nextLine();
                    System.out.println("StaffID = " + staffIDString);
                    sm.updateStaffMember(staffIDString);
                    break;
                case 3:
                    System.out.println("Remove Staff Member: ");
                    System.out.println("---------------------------------");
                    System.out.println("Enter the ID of the staff member to remove: ");
                    staffIDString = sc.nextLine();
                    System.out.println("StaffID = " + staffIDString);
                    StaffManager.removeStaffMember(staffIDString);
                    break;
                case 4:
                    sm.displayStaffMembers();
                    break;
                case 5:
                   // break;
                default:
                    break;
            }
        }
    }

    public void MedicationInventoryMenu() {
        System.out.println("-----------------------------------");
        System.out.println("Medication Inventory Menu");
        System.out.println("1. Approve Requests from Pharmacists");
        System.out.println("2. View Medication Inventory");
        System.out.println("3. Add Medication to Inventory");
        System.out.println("4. Remove Medication from Inventory");
        System.out.println("5. Updating Medication Stock Level");
        System.out.println("6. Update value of Low Stock Alert");
        System.out.println("7. Back to Main Menu");
        System.out.println("-----------------------------------");
        System.out.print("Enter your choice: ");
    }

    public void requestMenuDisplay(){
        System.out.println("-------------------------------------------------");
        System.out.println("1. Approve Request");
        System.out.println("2. Reject Request");
        System.out.println("3. Go Back");
        System.out.println("-------------------------------------------------");
    }

    private void requestMenu(){
        int choice2 = -1;
        while(choice2 != 3){
            if(mi.viewRequestsAdmin() == 0){
               System.out.println("Returning to Administrator menu...\n");
               return;
            }
            requestMenuDisplay();
            choice2 = oh.getOption(1,3);
            switch(choice2){
                case 1:
                    mi.approveRequest();
                    mi.saveMedicines();
                    break;
                case 2:
                    mi.rejectRequest();
                    mi.saveMedicines();
                    break;
                default:
                    break;

            }
        }
    }
    public void viewMedicationInventory(){
        mi.viewMedicalInventory();
    } // Case 1

    public void addMedicationInventory(){
        mi.add();
    }

    public void removeMedicationInventory(){
        mi.rm();
        mi.saveMedicines();
    }

    public void updatingMedicationStockLevel(){
        mi.update();
        mi.saveMedicines();
    }
    public void updatelowStockAlertValue(){
        mi.updateLowStockAlert();
        mi.saveMedicines();
    }

    private void manageMedicationInventory(){
        int choice = -1;
        while(choice != 7){
            MedicationInventoryMenu();
            choice = oh.getOption(1, 7);
            switch (choice){
                case 1:
                    requestMenu();
                    break;
                case 2:
                    viewMedicationInventory();
                    break;
                case 3:
                    addMedicationInventory();
                    mi.saveMedicines();
                    break;
                case 4:
                    removeMedicationInventory();
                    mi.saveMedicines();
                    break;
                case 5:
                    updatingMedicationStockLevel();
                    mi.saveMedicines();
                    break;
                case 6:
                    updatelowStockAlertValue();
                    mi.saveMedicines();
                    break;
                case 7:
                    System.out.println("Exiting Medication Inventory Menu");
                    break;
                default:
                    break;
            }
        }
    }

}