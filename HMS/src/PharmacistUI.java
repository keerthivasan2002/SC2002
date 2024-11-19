import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class PharmacistUI implements UserUI{
    private AppointmentStorage as;
    private ArrayList<Appointment> appointments;
    Staff pharmacist;
    StaffManager sm;
    private MedicineInventory mi;
    private ReplenishmentRequest request;
    private String userID;

    Scanner sc = new Scanner(System.in);
    OptionHandling oh = new OptionHandling();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public PharmacistUI(String userID, StaffManager sm, MedicineInventory mi, AppointmentStorage as){
        this.userID = userID;
        this.sm = sm;
        this.pharmacist = sm.selectStaff(userID);
        this.mi = mi;
        this.as = as;
        this.appointments = as.getAppointments();

        if(this.pharmacist == null){
            System.out.println("No pharmacists found with the given ID: " + userID);
        }else{
            userOption();
        }
    }

    public void userOption(){
        int choice = 0;
        System.out.println("Hello " + pharmacist.getName() + ".");
        System.out.println("What would you like to do today?");

        while(true){  //change accordingly
            pharmacistMenu();
            choice = oh.getOption(1, 5);
            switch (choice){
                case 1: //view appointment outcome records
                    viewAppointmentOutcomeRecords();
                    break;
                case 2: //update prescription status
                    mi.updateMedicalInventory();
                    mi.saveMedicines();
                    break;
                case 3: //view medical inventory
                    mi.viewMedicalInventory();
                    mi.saveMedicines();
                    break;
                case 4: //submit replenishment request
                    ReplenishmentRequest.submitReplenishmentRequest(mi);
                    mi.saveMedicines();
                    break;
                case 5: //log out
                    System.out.println("Thank you! Hope to see you soon :)\n");
                    System.exit(0);
                    return;
            }
            // System.out.println("What else would you like to do today?");
            // sc.nextLine();
        }
        

    }

    private void pharmacistMenu(){
        System.out.println("-----------------------------------");
        System.out.println("Pharmacist Menu:");
        System.out.println("1. View Appointment Outcome Records");
        System.out.println("2. Prescribe Medication");
        System.out.println("3. View Medical Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.println("-----------------------------------");
    }

    public void viewAppointmentOutcomeRecords(){
        System.out.println("Viewing Appointment Outcome Records");
        System.out.println("Enter the patient ID to view the outcome: ");
        String patientID = sc.nextLine().toUpperCase();
        System.out.println("Enter the doctor ID to view the outcome: ");
        String doctorID = sc.nextLine().toUpperCase();

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", 
                "Appointment ID", "Patient ID", "Doctor ID", "Date", "Start Time", "End Time", "Outcome", "Status");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    
        // Iterate through appointments and display each matching appointment in table format
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID) && appointment.getDoctor().getUserID().equals(doctorID)) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %n",
                        appointment.getAppointmentID(),
                        appointment.getPatient().getUserID(),
                        appointment.getDoctor().getUserID(),
                        appointment.getStringDate(),
                        appointment.getStringStartTime(),
                        appointment.getStringEndTime(),
                        appointment.getOutcome()
                        );
            }
        }
        // System.out.println("No matching appointment found.");
    }
}
