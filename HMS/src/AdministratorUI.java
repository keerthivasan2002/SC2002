//import java.util.ArrayList;
import java.util.Scanner;

public class AdministratorUI {

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
    /*
    FOR FILE TRANSFER / RECEIVING
    Admin admin;
    AdminManager am;
    ..
     */

    public void AdminOption(){
        int choice;
        System.out.println("Hello " + admin.getName() + ".");
        System.out.println("What would you like to do today?");
        adminMenu();
        choice = sc.nextInt();

        while(choice > 0 && choice < 5){
            switch(choice){
                case 1: // Manage Staff Records
                   // manageStaffRecords();
                    break;
                case 2:
                    //manageAppointment();
                    break;
                case 3:
                    //manageInventory();
                    break;
                case 4:
                    System.out.println("Thank you! Hope to see you soon :) \n");
                    System.exit(0);
                    return;
            }

        }
    }

    private void adminMenu(){
        System.out.println("-----------------------------------");
        System.out.println("Patient Menu:");
        System.out.println("1. Manage Staff Records");
        System.out.println("2. View if scheduled appointment is updated");
        System.out.println("3. View and manage inventory of medication ");
        System.out.println("4. Logout");
        System.out.println("-----------------------------------");
        System.out.print("Enter your choice: ");
    }

}


