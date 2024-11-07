import java.util.Scanner;

public class DoctorUI {
    private String userID;
    Staff doctor;
    StaffManager sm;

    Scanner sc = new Scanner(System.in);


    public DoctorUI(String userID, StaffManager sm){
        this.userID = userID;
        this.sm = sm;
        this.doctor = sm.selectStaff(userID);

        //Handling errors with the code
        if(this.doctor == null){
            System.out.println("No doctors found with the given ID:" + userID);
        }else{
            doctorOption();
        }


    }

    public void doctorOption(){
        int choice;
        System.out.println("Hello " + doctor.getName() + ".");
        System.out.println("What would you like to do today?");
        doctorMenu();
        choice = sc.nextInt();

        while(choice < 9){
            switch (choice){
                case 1: //view Patient Medical Record
                    break;
                case 2: //Update patient Medical Record
                    break;
                case 3: //view Personal Schedule
                    Schedule schedule = new Schedule();
                    schedule.viewTodaySchedule();
                    break;
                case 4: //Set availability for appointment
                    break;
                case 5: //Accept or Decline Appointment Request
                    break;
                case 6: //View Upcoming Appointment
                    break;
                case 7: //Record Appointment Outcome
                    break;
                case 8: //Log out
                    System.out.println("Thank you! Hope to see you soon :)\n");
                    System.exit(0);
                    return;
                default:
                    break;
            }

            System.out.println("What else would you like to do today?");
            doctorMenu();
            choice = sc.nextInt();
        }
    }



    private void doctorMenu(){
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
    }

}