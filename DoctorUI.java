import java.util.Scanner;
import java.util.ArrayList;

public class DoctorUI{
    private Scanner sc = new Scanner(System.in);

    public int doctorOption(){
        int choice = -1;
    
        while (true) {
            try {
                System.out.println("-----------------------------------");
                System.out.println("Doctor Menu:");
                System.out.println("1. View Patient Medical Record");
                System.out.println("2. Update Patient Medical Record");
                System.out.println("3. View Personal Schedule");
                System.out.println("4. Set Availablility for Appointment");
                System.out.println("5. Accept or Decline Appointment Request");
                System.out.println("6. View Upcoming Appointment");
                System.out.println("7. Record Appointment Outcome");
                System.out.println("8. Logout");
                System.out.println("-----------------------------------");
                System.out.print("Enter your choice: ");
    
                choice = sc.nextInt(); // Read the input
    
                // Validate if the choice is within the menu range
                if (choice > 0 && choice < 9) {

                    switch (choice) {
                        case 1:
                            viewPatientMedicalRecord();
                            break;
                        case 2:
                            updatePatientMedicalRecord();
                            break;
                        case 3:
                            viewPersonalSchedule();
                            break;
                        case 4:
                            setAvailabilityForAppointment();
                            break;
                        case 5:
                            acceptOrDeclineAppointmentRequest();
                            break;
                        case 6:
                            viewUpcomingAppointment();
                            break;
                        case 7:
                            recordAppointmentOutcome();
                            break;
                        case 8:
                            return choice  = 9;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                    }
                    break; // Exit the loop if the choice is valid
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (Exception e) {
                // Handle non-integer inputs
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine(); // Clear the invalid input from the scanner
            }
        }
        return choice;
    }

    public void viewPatientMedicalRecord(){
        System.out.println("View Patient Medical Record");
    }

    public void updatePatientMedicalRecord(){
        ReadFile reader = new ReadFile();
        ArrayList<String[]> staffList = new ArrayList<>();
        try{
            staffList = reader.readStaffListCSV();
        }catch(Exception e){
            System.out.println("Error reading CSV file" + e.getMessage());
        }

        System.out.println("Update Patient Medical Record");
        System.out.print("before: ");
        reader.printTable(staffList);

        System.out.println("Add Staff");
        sc.nextLine();
        System.out.print("Enter Staff ID: ");
        String patientID = sc.nextLine();
        System.out.print("Enter Staff passwrd: ");
        String password = sc.nextLine();
        System.out.print("Enter Staff Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Staff Role: ");
        String role = sc.nextLine();
        System.out.print("Enter Staff Gender: ");
        String gender = sc.nextLine();
        System.out.print("Enter Staff Age: ");
        String age = sc.nextLine();
        System.out.print("Enter Staff Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Staff Phone number: ");
        String phone = sc.nextLine();

        String[] row = {patientID, password, name, role, gender, age, email, phone};
        staffList.add(row);

        reader.writeStaffListCSV(staffList);
        System.out.print("after: ");
        reader.printTable(staffList);
    }

    public void viewPersonalSchedule(){
        System.out.println("View Personal Schedule");
    }   

    public void setAvailabilityForAppointment(){
        System.out.println("Set Availability for Appointment");
    }

    public void acceptOrDeclineAppointmentRequest(){
        System.out.println("Accept or Decline Appointment Request");
    }

    public void viewUpcomingAppointment(){
        System.out.println("View Upcoming Appointment");
    }

    public void recordAppointmentOutcome(){
        System.out.println("Record Appointment Outcome");
    }

}
class DoctorManager {
   
}


class Doctor extends User {
    public Doctor(String hospitalID, String password) {
        super(hospitalID, password, UserRole.DOCTOR);
    }

    public int menu(Scanner sc){
        return 0;
    }
    @Override
    public void displayRole() {
        System.out.println("Doctor");
    }
}

class PersonalScheduleManager{

}

