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
                    String userIDString = sc.next().toUpperCase();
                    //String userIDString = "P1001"; // speed up testing purpose

                    System.out.print("Password: ");
                    String password = sc.next();
                    //String password = "password1001";

                    LogInManager login = new LogInManager(userIDString, password);
                    boolean accept = login.authoriseLogin();
                    if (accept) {
                        System.out.println("\nWelcome");
                        userOption(userIDString); // does this work??
                    } else {
                        System.out.println("Login failed.");
                        System.out.println("If you would like to exit. Press 1.");
                        System.out.println("If you would like to try again. press any other key.");
                        exit = sc.next();
                    }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
            }finally{
                sc.nextLine();
            }
        }
        sc.close();
    }

    public static void userOption(String userIDString) {
        //initialise outside to avoid initialising multiple times
        ScheduleManager scheduleManager = new ScheduleManager();

        //all the separate appointment classes
        AppointmentValidator amv = new AppointmentValidator();
        AppointmentStorage ams = new AppointmentStorage();
        AppointmentFilter amf = new AppointmentFilter();
        AppointmentScheduler amschedule = new AppointmentScheduler(amv);
        AppointmentLookup aml = new AppointmentLookup();

        AppointmentManager am = new AppointmentManager(ams, amschedule, amv, amf,aml);

        // Step 2: Set the references in each other
        scheduleManager.setAppointmentManager(am);
        am.setScheduleManager(scheduleManager);

        // Step 3: Now initialize appointments and schedules as needed
        //am.initializeAppointments();        // Initialize appointments
        scheduleManager.initialiseSchedule(); // Initialize schedule


        String useString = userIDString.toUpperCase();

        if (useString.length() == 5){

            //Patient 
            if (useString.startsWith("p") || useString.startsWith("P")){ // [To Remove] Can remove
                MedicalRecordManager mrm = new MedicalRecordManager();
                PatientManager pm = new PatientManager();
                new PatientUI(useString, pm, mrm, scheduleManager, am);
            }
        } else if(useString.length() == 4){
            //Doctor
            if (useString.startsWith("d") || useString.startsWith("D")){
                MedicalRecordManager mrm = new MedicalRecordManager();
                StaffManager sm = new StaffManager();
                new DoctorUI(useString, sm, mrm, am, scheduleManager);
            }
            //Admin
            else if (useString.startsWith("a") || useString.startsWith("A")){
                StaffManager sm = new StaffManager();
                new AdministratorUI(useString, sm, am);
            } 

            //Pharmacist
            else if (useString.startsWith("p") || useString.startsWith("P")){
                 StaffManager sm = new StaffManager();
                 new PharmacistUI(useString, sm);
            } 

        }else {
                System.out.println("Invalid user ID");
        }
    }
}

