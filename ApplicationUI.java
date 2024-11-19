import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationUI {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        LoginManager loginManager = new LoginManager();
        loginManager.loadUserData(); //import all the information from the CSV file

        while(true){
            int login = 0;
            //login user
            User user =  loginManager.loginMenu();

            //user login failed
            if (user == null) {
                System.out.println("Login failed. Invalid hospital ID or password.");
                System.out.println("Please try again.");
                user = loginManager.loginMenu();
            }
            //user login successful
            else {
                do{
                    System.out.println( " Login successful.");
                    UserRole role = user.getRole(); // Display the role of the user

                    switch(role){
                        case ADMINSTRATOR:
                            System.out.println("Welcome Adminstrator");

                            break;
                        case DOCTOR:
                            System.out.println("Welcome Doctor");
                            DoctorUI doctor = new DoctorUI();
                            login = doctor.doctorOption();
                            break;
                        case PHAMARCIST:
                            System.out.println("Welcome Pharmacist");
                            break;
                        case PATIENT:
                            System.out.println("Welcome Patient");
                            break;
                        default:
                            break;
                    }
                } while (login != 9);
            }
            System.out.println("Thank you for using the system.");
            break;
        }
    }


    //advance feature - user able to register their account
    public static int userOption(){
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        return choice;
    }
}

