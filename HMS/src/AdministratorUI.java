import java.util.Scanner;
import java.util.ArrayList;

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
        private String userID; // Why is there error here
        private Staff staff, admin, doctor;
        private StaffManager sm;
        private PatientManager pm;
        private Patient patient;
        private AppointmentManager am;
        
        Scanner sc = new Scanner(System.in);

        /*
         FOR FILE TRANSFER / RECEIVING
         Admin admin;
         AdminManager am;
         ..
          */
        public AdministratorUI(String userID, StaffManager sm, AppointmentManager am) {
            this.userID = userID;
            this.sm = sm;
            this.admin = sm.selectStaff(userID);
            this.am = am;


            //Handling errors with the code
            if (this.admin == null) {
                System.out.println("No doctors found with the given ID:" + userID);
            } else {
                adminOption();
            }
        }

        // Function to get the option from the user
    public int getOption(){
        int option = 0;
        boolean valid = false;
        Scanner sc = new Scanner(System.in);
        while (!valid){
            try{
                System.out.println("Enter your option from 1 to 4: ");
                option = sc.nextInt();
                System.out.println("You entered: " + option);
                if (option < 0){
                    throw new IntNonNegativeException();
                }else if (option == 0 || option > 4){
                    throw new InvalidPositiveOptionException();
                }else {
                    valid = true;
                }
            }catch (IntNonNegativeException e){
                //Handle negative numbers
                System.out.println(e.getMessage());
                option = getOption();
            }catch (InvalidPositiveOptionException e){
                //Handle invalid positive numbers
                System.out.println(e.getMessage());
                option = getOption();
            }catch (Exception e){
                //handle non integer numebr
                System.out.println("Invalid input. Please enter a valid number.");
                // option = getOption();
            }
        }
        return option;
        
    }

    public int getOptionAgain(){
        int option = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your option: ");
        option = sc.nextInt();

        if (option < 0){
            System.out.println("Error: your option MUST NOT be negative number!");
            System.out.println("You are out! ");
            System.out.println("Program Terminating! ");
            System.exit(0);
        }else if (option == 0 || option > 4){
            System.out.println("Error: your option MUST be between 1 to 9!");
            System.out.println("You are out! ");
            System.out.println("Program Terminating! ");
            System.exit(0);
        }else{
            System.out.println("Error: your are idiot!");
            System.out.println("You are out! ");
            System.out.println("Program Terminating! ");
            System.exit(0);
        }
        return option;
    }

    public void adminOption() {
        int choice = -1;

        System.out.println("Hello " + admin.getName() + ".");
        System.out.println("What would you like to do today?");

        while (true){
            adminMenu();
            choice = getOption();
            switch (choice) {
                case 1: // Manage Staff Records
                    viewAndManageStaffRecords();
                    break;
                case 2:
                    //manageAppointment();
                    viewPatientOrDoctorAppointment();
                    break;
                case 3:
                    //manageInventory();
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
        System.out.println("6. Approved");
        System.out.println("7. Scheduled");
        System.out.println("8. Back to Main Menu");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public void viewPatientOrDoctorAppointment(){
        int choice = -1;
        while (choice != 3){
            displayAppointmentMenu();
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 3) {
                break; // Exit the main menu loop
            }

            ArrayList<Appointment> filteredAppointments = new ArrayList<>();

            switch (choice){
                case 1:
                    System.out.print("Enter Patient ID: ");
                    String patientID = sc.nextLine().trim();
                    filteredAppointments = am.getAppointmentsByPatientID(patientID);
                    break;
                case 2:
                    System.out.print("Enter Doctor ID: ");
                    String doctorID = sc.nextLine().trim();
                    filteredAppointments = am.getAppointmentsByDoctorID(doctorID);
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
            while (statusChoice != 8){
                statusMenu();
                System.out.print("Enter your choice: ");
                statusChoice = sc.nextInt();
                sc.nextLine();
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
                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments, AppointmentStatus.APPROVED);
                        break;
                    case 7:
                        filteredStatusAppointments = am.getAppointmentsByStatus(filteredAppointments,AppointmentStatus.SCHEDULED);
                        break;
                    case 8:
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
            choice = sc.nextInt();
            sc.nextLine();
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
                    sm.removeStaffMember(staffIDString);
                    break;
                case 4:
                    sm.displayStaffMembers();
                    break;
                case 5:
                    break;
                default:
                    break;
            }
        }
    }



    private void manageAppointment(){
        
    }

}