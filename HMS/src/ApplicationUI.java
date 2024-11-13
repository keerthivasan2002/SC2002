import java.util.Scanner;


public class ApplicationUI {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Scanner sc = new Scanner(System.in);
        String exit = "0";

        while(!exit.equals("1")){
            System.out.println("Welcome to the login page");
            System.out.println("Please enter your Hospital ID and Password");

            System.out.println();
            System.out.print("Username: ");
            String userName = sc.next();
            // String userName = "D001"; // speed up testing purpose
            
            System.out.print("Password: ");
            // String password = sc.next();
            String password = "password"; // speed up testing purpose

            LogInManager login = new LogInManager(userName, password);
            boolean accept = login.authoriseLogin();
            if (accept) {
                System.out.println("Welcome");
                userOption(userName);
            } else {
                System.out.println("Login failed.");
                System.out.println("If you would like to exit. Press 1.");
                System.out.println("If you would like to try again. press any other key.");
                exit = sc.next();
            }
        }

    }

    public static void userOption(String userName) {
        //initialise outside to avoid initialising multiple times
        // Schedule schedule = new Schedule();
        ScheduleManager scheduleManager = new ScheduleManager();
        AppointmentManager am = new AppointmentManager();

        if((userName.charAt(0) == 'p' || userName.charAt(0) == 'P') && userName.length() == 5){
            PatientManager pm = new PatientManager(); // Create new object from class
            MedicalRecordManager mrm = new MedicalRecordManager(); // Create new object from class
            PatientUI patientUI = new PatientUI(userName, pm, mrm, scheduleManager, am); //
        }else if((userName.charAt(0) == 'p' || userName.charAt(0) == 'P') && userName.length() == 4){
            //pharmacist UI
            StaffManager sm = new StaffManager();
            PharmacistUI pharmacistUI = new PharmacistUI(userName, sm);
        }else if(userName.charAt(0) == 'd' || userName.charAt(0) == 'D'){
            MedicalRecordManager mrm = new MedicalRecordManager();
            StaffManager sm = new StaffManager();
            DoctorUI doctorUI = new DoctorUI(userName, sm, mrm, am, scheduleManager);
            //doctorUI
        }else if(userName.charAt(0) == 'a' || userName.charAt(0) == 'A'){
            //adminUI
        }else{
            System.out.println("Invalid user ID");
        }
    }
}

