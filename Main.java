import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        LoginManager loginManager = new LoginManager();
        loginManager.loadUserData();

        // // Sample login details for testing
        // String inputHospitalID = "P1001"; // Example: HospitalID starts with 'P' for patient
        // String inputPassword = "password";

       

        // Attempt to authenticate the user
        User user =  loginManager.loginMenu();
        if (user != null) {
            System.out.println("Login successful.");
            user.menu(sc); // Display menu base on their role
        } else {
            System.out.println("Login failed. Invalid credentials.");

        }
        sc.close();
    }
}

