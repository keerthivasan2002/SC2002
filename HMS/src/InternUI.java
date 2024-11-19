import java.util.Scanner;

public class InternUI implements UserUI {
    private Intern intern;
    private final Scanner sc = new Scanner(System.in);
    private InternManager im;
    OptionHandling oh = new OptionHandling();
    LessonManager lm;

    public InternUI(String internID, InternManager im, LessonManager lm) {
        this.intern = im.selectIntern(internID);
        this.im = im;
        this.lm = lm;
        lm.initializeLessons();


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
            InternMenu();
            choice = getOption(1, 5);
            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateInfo();
                    break;
                case 3:
                    lm.displayWeeklyTimetable();
                    break;
                case 4:
                    lm.displayTodayTimetable();
                    break;
                case 5:
                    System.out.println("Logging out. Have a great day!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void InternMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Intern Menu:");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. View Weekly Timetable");
        System.out.println("4. View Daily Timetable");
        System.out.println("5. Logout");
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

    public void updateInternMenu() {
        System.out.println("----------------------------------------------");
        System.out.println("Which information would you like to change?");
        System.out.println("1. Change Phone Number");
        System.out.println("2. Change Password");
        System.out.println("3. Back to Main Menu.");
        System.out.println("----------------------------------------------");
    }

    private void updateInfo() {
        int changChoice = -1;
        while(changChoice != 3) {
            updateInternMenu();
            changChoice = oh.getOption(1, 3);
            switch (changChoice) {
                case 1:
                    System.out.print("Please enter your new phone number: ");
                    int newPhoneNumber = im.setInternNumber();
                    intern.setPhoneNumber(newPhoneNumber);
                    System.out.println("Updated Phone Number: " + intern.getPhoneNumber());
                    break;
                case 2:
                    System.out.print("Enter your current password for verification: ");
                    String verify = sc.nextLine();
                    if (verify.equals(intern.getPassword())) {
                        String newPassword = im.changePasswordString();
                        im.setPassword(newPassword, intern.userID);
                        System.out.println("Password updated successfully.");
                    } else {
                        System.out.println("Incorrect password. Please try again.");
                    }
                    break;
                case 3:
                    break;
                default:
                    break;

            }
            im.saveInterns();
            System.out.println("Information has been saved successfully!");
        }
    }

}
