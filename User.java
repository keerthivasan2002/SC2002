import java.util.Scanner;
import java.util.NoSuchElementException;

abstract class User {


    protected String hospitalID;
    protected String password;
    protected String name;
    protected String dateofBirth;
    protected UserRole role;
    protected String gender;
    protected String age;
    protected String email;
    protected String phone;

    
    public User(String hospitalID, String password, String name, String dateOfBirth, UserRole role, String gender, String age, String email, String phone) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.name = name;
        this.dateofBirth = dateOfBirth;
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public User(String hospitalID, String password, UserRole role) {
        this(hospitalID, password, "Unknown", "Unknown", role, "Unknown", 0, "unknown@example.com", "0000000000");
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age){
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    
    public abstract void displayRole();
    public abstract int menu(Scanner sc);
}


enum UserRole {
    ADMINSTRATOR, DOCTOR, PHAMARCIST, PATIENT, INVALID
}

class Patient extends User {
    protected String patientID = hospitalID;
    public Patient(String hospitalID, String password) {
        super(hospitalID, password, UserRole.PATIENT);
    }

    public int menu(Scanner sc){
        int choice = -1;
        while (true) {
            try{ 
                System.out.println("-----------------------------------");
                System.out.println("Patient Menu:");
                System.out.println("1. View Medical Record");
                System.out.println("2. Update Personal Information");
                System.out.println("3. View Available Appointment Slots");
                System.out.println("4. Schedule an Appointment");
                System.out.println("5. Reschedule an Appointment");
                System.out.println("6. Cancel an Appointment");
                System.out.println("7. View Scheduled Prescription");
                System.out.println("8. View Past Appointments Outcome Records");
                System.out.println("9. Logout");
                System.out.println("-----------------------------------");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                if(choice >= 1 && choice <= 9){
                    System.out.println("You entered: "+ choice);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } catch (NoSuchElementException e){
                System.out.println("An error occurred. Please try again.");
                sc.nextLine(); // Consume the invalid input
            }
        }
        return choice;
    }

    @Override
    public void displayRole() {
        System.out.println("Patient");
    }
}


class Pharmacist extends User {
    public Pharmacist(String hospitalID, String password) {
        super(hospitalID, password, UserRole.PHAMARCIST);
    }

    public int menu(Scanner sc){
        int choice = -1;
    
        while (true) {
            try {
                System.out.println("-----------------------------------");
                System.out.println("Pharmacist Menu:");
                System.out.println("1. View Appointment Outcome Records");
                System.out.println("2. Update Prescription Status");
                System.out.println("3. View Medication Inventory");
                System.out.println("4. Submit Replenishment Request");
                System.out.println("9. Logout");
                System.out.println("-----------------------------------");
                System.out.print("Enter your choice: ");
    
                choice = sc.nextInt(); // Read the input
    
                // Validate if the choice is within the menu range
                if (choice > 0 && choice < 6) {
                    break; // Exit the loop if the choice is valid
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (Exception e) {
                // Handle non-integer inputs
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine(); // Clear the invalid input from the scanner
            }
        }
        return choice;
    }
    
    @Override
    public void displayRole() {
        System.out.println("Pharmacist");
    }
}

class Adminstrator extends User {
    public Adminstrator(String hospitalID, String password) {
        super(hospitalID, password, UserRole.ADMINSTRATOR);
    }
    public int menu(Scanner sc){
        int choice = -1;
    
        while (true) {
            try {
                System.out.println("-----------------------------------");
                System.out.println("Adminstrator Menu:");
                System.out.println("1. View and Manage Hospital Staff");
                System.out.println("2. View Appointment Details");
                System.out.println("3. View and Manage Medication Inventory");
                System.out.println("4. Approve Replenishment Request");
                System.out.println("9. Logout");
                System.out.println("-----------------------------------");
                System.out.print("Enter your choice: ");
    
                choice = sc.nextInt(); // Read the input
    
                // Validate if the choice is within the menu range
                if (choice > 0 && choice < 9) {
                    break; // Exit the loop if the choice is valid
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (Exception e) {
                // Handle non-integer inputs
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return choice;
    }

    @Override
    public void displayRole() {
        System.out.println("Adminstrator");
    }
}