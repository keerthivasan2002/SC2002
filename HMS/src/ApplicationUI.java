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
            String password = sc.next();
            //String password = "password";

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
        String useString = userName.toUpperCase();

        if (useString.length() == 5){
            //Patient
            if (useString.startsWith("p") || useString.startsWith("P")){
                PatientManager pm = new PatientManager();
                MedicalRecordManager mrm = new MedicalRecordManager();
                PatientUI patientUI = new PatientUI(useString, pm, mrm, scheduleManager, am);
            }
        } else if(useString.length() == 4){
            //Doctor
            if (useString.startsWith("d") || useString.startsWith("D")){
                MedicalRecordManager mrm = new MedicalRecordManager();
                StaffManager sm = new StaffManager();
                DoctorUI doctorUI = new DoctorUI(useString, sm, mrm, am, scheduleManager);
            }
            //Admin
            else if (useString.startsWith("a") || useString.startsWith("A")){
                StaffManager sm = new StaffManager();
                AdministratorUI adminUI = new AdministratorUI(useString, sm, am);
            } 
            //Pharmacist
            else if (useString.startsWith("p") || useString.startsWith("P") || userName.startsWith("PH") || userName.startsWith("pH")){
                // StaffManager sm = new StaffManager();
                // PharmacistUI pharmacistUI = new PharmacistUI(userName, sm);
            }

        }else {
                System.out.println("Invalid user ID");
        }
    }
}

