import java.util.Scanner;


public class ApplicationUI {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Scanner sc = new Scanner(System.in);
        String exit = "0";

        while(!exit.equals("1")){
            System.out.println("Welcome to the login page");
            System.out.println("Please enter your Hospital ID and Password");
            System.out.print("Username: ");
            String userName = sc.next();

            System.out.print("Password: ");
            String password = sc.next();

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
        if((userName.charAt(0) == 'p' || userName.charAt(0) == 'P') && userName.length() == 5){ //Patient UI
            PatientManager pm = new PatientManager();
            MedicalRecordManager mrm = new MedicalRecordManager();
            PatientUI patientUI = new PatientUI(userName, pm, mrm);
        }else if((userName.charAt(0) == 'p' || userName.charAt(0) == 'P') && userName.length() == 4){
            //pharmacist UI
        }else if(userName.charAt(0) == 'd' || userName.charAt(0) == 'D'){//DoctorUI
            StaffManager sm = new StaffManager();
            DoctorUI doctorUI = new DoctorUI(userName, sm);
        }else{
            //adminUI
        }


    }
}

