import java.util.Scanner;

public class ApplicationUI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the login page");
        System.out.println("Please enter your Hospital ID and Password");
        System.out.print("Username: ");
        String userName = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        LogInManager login = new LogInManager(userName, password);
        boolean accept = login.authoriseLogin();
    }

    public static void userOption(String userName) {
        if((userName.charAt(0) == 'p' || userName.charAt(0) == 'P') && userName.length() == 4){
            PatientUI patient = new PatientUI();
        }else if((userName.charAt(0) == 'p' || userName.charAt(0) == 'P') && userName.length() == 3){
            //pharmacist UI
        }else if(userName.charAt(0) == 'd' || userName.charAt(0) == 'D'){
            //doctorUI
        }else{
            //adminUI
        }

    }
}

