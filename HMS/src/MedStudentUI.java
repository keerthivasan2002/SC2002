import java.util.Scanner;

public class MedStudentUI implements StudentUI {
    private Student student;
    private final Scanner sc = new Scanner(System.in);
    private StudentManager im;
    public OptionHandling oh = new OptionHandling();
    private LessonManager lm;

    public MedStudentUI(String internID, StudentManager im, LessonManager lm) {
        this.student = im.selectIntern(internID);
        this.im = im;
        this.lm = lm;
        lm.initializeLessons();


        if (this.student == null) {
            System.out.println("No student found with the given ID: " + internID);
        } else {
            userOption();
        }
    }

    @Override
    public void userOption() {
        int choice;
        System.out.println("Hello " + student.getName() + ".");
        System.out.println("What would you like to do today?");

        while (true) {
            InternMenu();
            choice = oh.getOption(1, 5);
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

    public void viewProfile() {
        System.out.println("\nProfile Details:");
        System.out.println("Student ID: " + student.getUserID());
        System.out.println("Name: " + student.getName());
        System.out.println("Gender: " + student.getGender());
        System.out.println("Date of Birth: " + student.getDateOfBirth());
        System.out.println("Specialization: " + student.getSpecialisation());
        System.out.println("Email: " + student.getEmailAddress());
        System.out.println("Phone: " + student.getPhoneNumber());
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

    public void updateInfo() {
        int changChoice = -1;
        while(changChoice != 3) {
            updateInternMenu();
            changChoice = oh.getOption(1, 3);
            switch (changChoice) {
                case 1:
                    System.out.print("Please enter your new phone number: ");
                    int newPhoneNumber = im.setInternNumber();
                    student.setPhoneNumber(newPhoneNumber);
                    System.out.println("Updated Phone Number: " + student.getPhoneNumber());
                    break;
                case 2:
                    System.out.print("Enter your current password for verification: ");
                    String verify = sc.nextLine();
                    if (verify.equals(student.getPassword())) {
                        String newPassword = im.changePasswordString();
                        im.setPassword(newPassword, student.userID);
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
