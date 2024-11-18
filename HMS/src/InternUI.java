import java.util.Scanner;

public class InternUI implements UserUI {
    private String internID;
    private Intern intern;
    private final Scanner sc = new Scanner(System.in);

    public InternUI(String internID, InternManager sm) {
        internID =internID;
        this.intern = sm.selectIntern(internID);

        if (this.intern == null) {
            System.out.println("No intern found with the given ID: " + internID);
        } else {
            userOption();
        }
    }

    @Override
    public void userOption() {
        int choice;
        System.out.println("Hello " + intern.getName() + ".");
        System.out.println("What would you like to do today?");

        while (true) {
            studentMenu();
            choice = getOption(1, 4);
            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateProfile();
                    break;
                case 3:
                    //viewSchedule();
                    break;
                case 4:
                    System.out.println("Logging out. Have a great day!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void studentMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Intern Menu:");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. View Schedule");
        System.out.println("4. Logout");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private int getOption(int min, int max) {
        int option;
        while (true) {
            System.out.print("Please enter your choice: ");
            try {
                option = Integer.parseInt(sc.nextLine());
                if (option >= min && option <= max) {
                    break;
                } else {
                    System.out.println("Option out of range. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return option;
    }

    private void viewProfile() {
        System.out.println("\nProfile Details:");
        System.out.println("Student ID: " + intern.getUserID());
        System.out.println("Name: " + intern.getName());
        System.out.println("Gender: " + intern.getGender());
        System.out.println("Date of Birth: " + intern.getDateOfBirth());
        System.out.println("Specialization: " + intern.getSpecialisation());
        System.out.println("Email: " + intern.getEmailAddress());
        System.out.println("Phone: " + intern.getPhoneNumber());
        System.out.println();
    }

    private void updateProfile() {
        System.out.println("\nUpdate Profile:");
        System.out.print("Enter new email address (current: " + intern.getEmailAddress() + "): ");
        String email = sc.nextLine();
        System.out.print("Enter new phone number (current: " + intern.getPhoneNumber() + "): ");
        String phoneInput = sc.nextLine();
        int phone = Integer.parseInt(phoneInput);

        intern.setEmailAddress(email);
        intern.setPhoneNumber(phone);
        System.out.println("Profile updated successfully.\n");
    }

}
