import java.util.Scanner;


public class ApplicationUI{
    public static void main(String[] args) {
        //System.out.println(System.getProperty("user.dir"));
        Scanner sc = new Scanner(System.in);
        String exit = "0";

        while(!exit.equals("1")){
            try{
                
                    System.out.println("======Welcome to the HMS login page======");
                    System.out.println("Please enter your Hospital ID and Password");

                    System.out.println();
                    System.out.print("UserID: ");
                    // String userIDString = sc.next().toUpperCase();
                    String userIDString = "D001"; // speed up testing purpose

                    System.out.print("Password: ");
                    // String password = sc.next();
                    String password = "Password@001";

                    LogInManager login = new LogInManager(userIDString, password);
                    System.out.println("\nLogInManager initialized");

                    boolean accept = login.authoriseLogin();
                    System.out.println("Login authorised: " + accept);

                    if (accept) {
                        System.out.println("\nWelcome");
                        userOption(userIDString); // does this work??
                        System.out.println("If you would like to exit. Press 1.");
                    } else {
                        System.out.println("Login failed.");
                        System.out.println("If you would like to exit. Press 1.");
                        System.out.println("If you would like to try again. press any other key.");
                        exit = sc.next();
                    }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again. [Application UI]");
            }finally{
                sc.nextLine();
            }
        }
        sc.close();
    }

    public static void userOption(String userIDString) {
        try {
            AppointmentStorage as = new AppointmentStorage();
            System.out.println("AppointmentStorage initialized: [ApplicationUI]" + (as != null));

            AppointmentValidator av = new AppointmentValidator(as);
            System.out.println("AppointmentValidator received AppointmentStorage: [ApplicationUI]" + (av != null));

            // AppointmentFilter af = new AppointmentFilter(as, av);
            // System.out.println("AppointmentFilter initialized with AppointmentStorage: [ApplicationUI]" + (as != null));

            AppointmentManager am = new AppointmentManager(as,av);
            System.out.println("AppointmentManager initialized with valid dependencies.[ApplicationUI]" + (am != null));

            ScheduleManager scheduleManager = new ScheduleManager();
            scheduleManager.setAppointmentManager(am);
            am.setScheduleManager(scheduleManager);

            scheduleManager.initialiseSchedule(); // Initialize schedule
            am.initializeAppointments(); // Initialize appointments
            
            String useString = userIDString.toUpperCase();
    
            if (useString.length() == 5) {
                if (useString.startsWith("P")) {
                    MedicalRecordManager mrm = new MedicalRecordManager();
                    PatientManager pm = new PatientManager();
                    new PatientUI(useString, pm, mrm, scheduleManager, am);
                }
            } else if (useString.length() == 4) {
                if (useString.startsWith("D")) {
                    MedicalRecordManager mrm = new MedicalRecordManager();
                    StaffManager sm = new StaffManager();
                    new DoctorUI(useString, sm, mrm, am, scheduleManager);
                } else if (useString.startsWith("A")) {
                    StaffManager sm = new StaffManager();
                    new AdministratorUI(useString, sm, am);
                } else if (useString.startsWith("P")) {
                    StaffManager sm = new StaffManager();
                    new PharmacistUI(useString, sm);
                }
            } else {
                System.out.println("Invalid user ID");
            }
        } catch (Exception e) {
            System.out.println("An error occurred in userOption(). [Application UI]");
            e.printStackTrace(); // Print stack trace for more details
        }
    }
    
}

