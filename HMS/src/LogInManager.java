import java.util.ArrayList;
import java.util.Scanner;


public class LogInManager {
    private String userName;
    private String password;

    private PatientManager patientManager;
    private StaffManager staffManager;

    private ArrayList<User> users;;
    private String user_file = "UserID.csv";
    
    Scanner sc = new Scanner(System.in);

    // Constructor to handle file operations
    public LogInManager(String userName, String password) {
        this.users = new ArrayList<>();
        this.userName = userName;
        this.password = password;
        this.patientManager = new PatientManager();
        this.staffManager = new StaffManager();
        initializeUser();
    }

    public void initializeUser() {
        FileManager userFileManager = new FileManager(user_file);
        String[][] userArray = userFileManager.readFile();
        if (userArray == null || userArray.length == 0) {
            System.out.println("Failed to load user data.[LoginManager]");
            return;
        }

        for (int i = 1; i < userArray.length; i++) {
            String[] row = userArray[i];
            if (row.length >= 3) {
                String userID = row[0];
                String pass = row[1];
                boolean isDefaultPassword = Boolean.parseBoolean(row[2]);
                Staff user = new Staff(userID, pass, isDefaultPassword);
                users.add(user);
            } else {
                System.out.println("Incomplete data in row, skipping: [LoginManager]" + String.join(",", row));
            }
        }
    }

    public boolean authoriseLogin() {
        String id = userName.toUpperCase();
        String pass = password;

        // System.out.println("id: " + id);
        // System.out.println("pass: " + pass);

        for (User user : users) {
            // System.out.println("user id: " + user.getUserID());
            // System.out.println("user pass: " + user.getPassword());
            if (user.getUserID().equalsIgnoreCase(id) && user.getPassword().equals(pass)) {
                if (user.isDefaultPassword()) {
                    // If it's the first time login, prompt to change password
                    System.out.println("This is your first login. You must change your default password.");
                    changePassword(user);
                }
                return true;
            }
        }
        System.out.println("Please try again! Those are the wrong particulars.");
        return false;
    }

    private void changePasswordMenu(){
        System.out.println("Change Password");
        System.out.println("Your password must meet the following criteria:");
        System.out.println("- At least 8 characters long");
        System.out.println("- Contain at least one uppercase letter");
        System.out.println("- Contain at least one lowercase letter");
        System.out.println("- Contain at least one number");
        System.out.println("- Contain at least one special character (!@#$%^&*()-+=)");
        System.out.println("-------------------------------------------------------------------");
    }

    private void changePassword(User user) {
        String newPassword = "";
        while (true) {
            changePasswordMenu();
            System.out.print("Enter your new password: ");
            newPassword = sc.nextLine();
            if (isValidPassword(newPassword)) {
                break;
            } else {
                System.out.println("Password does not meet the requirements. Please try again.");
            }
        }

        user.setPassword(newPassword);
        user.setDefaultPassword(false); // User has changed the default password

        
        // Update the CSV file with the new password
        if (!user.getUserID().startsWith("P1")) {
            System.out.println("Updating patient password...");
            staffManager.setPassword(newPassword, user.getUserID());
        } else {
            System.out.println("Updating staff password...");
            patientManager.setPassword(newPassword, user.getUserID());
        }
        saveUsers();
        
        System.out.println("Password changed successfully. Please log in again with your new password.");
    }

    private void saveUsers() {
        FileManager userFileManager = new FileManager(user_file);
        String[][] userArray = new String[users.size() + 1][3];
        userArray[0] = new String[]{"UserID", "Password", "isDefaultPassword"}; // Header row

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userArray[i + 1] = new String[]{
                user.getUserID(),
                user.getPassword(),
                String.valueOf(user.isDefaultPassword())
        };
        }
        userFileManager.writeFile(userArray, false);
    }

    private boolean isValidPassword(String password) {
        // Enforce password requirements
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.matches(".*[!@#$%^&*()-+=].*"); // Check for at least one special character
        boolean isLengthValid = password.length() >= 8; // Minimum length 8 characters
    
        return hasUppercase && hasLowercase && hasDigit && hasSpecial && isLengthValid;
    }
}
