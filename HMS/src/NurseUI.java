import java.util.Scanner;

public class NurseUI implements StudentUI {
    private Student student;
    private final Scanner sc = new Scanner(System.in);
    private StudentManager sm;
    private LessonManager lm;
    public OptionHandling oh = new OptionHandling();

    
    public NurseUI(String nurseID, StudentManager sm, LessonManager lm) {
        this.sm = sm;
        this.student = sm.selectIntern(nurseID);

        if (this.student == null) {
            System.out.println("No student found with the given ID: " + nurseID);
        } else {
            userOption();
        }

        if (sm.isValidNurse(nurseID)){
            System.out.println("Welcome Nurse " + nurseID);
        } else {
            System.out.println("Invalid Nurse ID");
        }
    }

    @Override
    public void userOption() {
        int choice;
        System.out.println("Hello " + student.getName() + ".");
        System.out.println("What would you like to do today?[Nurse]");

        while (true) {
            NurseMenu();
            choice = oh.getOption(1, 3);
            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateInfo();
                    break;
                case 3:
                    System.out.println("Logging out. Have a great day!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void NurseMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Nurse Menu:");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. Other Option");
        System.out.println("5. Logout");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void viewProfile() {
        System.out.println("Nurse Profile Details: ");
        // Display student details
    }

    @Override
    public void updateInfo() {
        // Implement update logic specific to student
    }
}
