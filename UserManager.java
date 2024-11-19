import java.util.ArrayList;
import java.util.Scanner;

public class UserManager {
    private ArrayList<User> staffList = new ArrayList<>();
    private ArrayList<User> patientList = new ArrayList<>();

    private ReadFile reader = new ReadFile();
    // Method to add people to the list (for testing)
    public void adduser(boolean patientOrStaff) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Add Staff");
        sc.nextLine();
        System.out.print("Enter Hospital ID: ");
        String hospitalID = sc.nextLine();
        System.out.print("Enter passwrd: ");
        String password = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.println("Enter Date of Birth: ");
        String dob = sc.nextLine();
        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();
        System.out.print("Enter Age: ");
        string age = sc.nextInt();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Phone number: ");
        String phone = sc.nextLine();

        String role = getUserRole(hospitalID);
        if (!patientOrStaff) { 
            User addStaff = new User (hospitalID, password, name, role, gender, age, email, phone);
            staffList.add(addStaff);
        }
        else { 
            User addPatient = new User (hospitalID, password, name, dob, gender, age, email, phone);
            patientList.add(addPatient);
        }
        
    }

    // Method to look up a user by ID
    public User lookupuserById(String hospitalID) {
        if ( hospitalID.length() > 4 ){
            for (User user : patientList) {
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        }
        else 
        {   
            for (User user : staffList) {
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        }
        
        return null; // Return null if user not found
    }

    // Method to update user information
    public void updateuserInfo(String id) {
        User user = lookupuserById(id);
        if (user == null) {
            System.out.println("User ID not found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Current information: " + user);

        System.out.print("Enter new name (or press Enter to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            user.setName(newName);
        }


        System.out.print("Enter new role (or press Enter to keep current): ");
        String newRole = scanner.nextLine();
        if (!newRole.isEmpty()) {
            user.setRole(newRole);
        }

        System.out.print("Enter new gender (or press Enter to keep current): ");
        String newGender = scanner.nextLine();
        if (!newGender.isEmpty()) {
            user.setGender(newGender);
        }

        System.out.print("Enter new age (or press Enter to keep current): ");
        String newAge = scanner.nextLine();
        if (!newAge.isEmpty()) {
            user.setAge(newAge);
        }

        System.out.print("Enter new email (or press Enter to keep current): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }

        System.out.print("Enter new phone number (or press Enter to keep current): ");
        String newPhone = scanner.nextLine();
        if (!newPhone.isEmpty()) {
            user.setPhone(newPhone);
        }

        System.out.println("Updated information: " + user);
        reader.writeStaffListCSV(staffList);
        reader.writeStaffListCSV(patientList);
        reader.printTable(staffList);
    }

    public UserRole getUserRole(String hospitalID) {
        if ( hospitalID.length() > 4 ){
            return UserRole.PATIENT;
        }
        else if ( hospitalID.startsWith("D")){
            return UserRole.DOCTOR;
        }
        else if ( hospitalID.startsWith("P")){
            return UserRole.PHAMARCIST;
        }
        else if ( hospitalID.startsWith("A")){
            return UserRole.ADMINSTRATOR;
        }
        else {
            return UserRole.INVALID;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserManager manager = new UserManager();
        ReadFile reader = new ReadFile();
        ArrayList staffList = reader.readStaffListCSV();
        ArrayList patientList = reader.readPatientListCSV();

        // Adding test data
        System.out.println("Adding for patient or staff? (true for patient, false for staff): ");
        boolean patientOrStaff = sc.nextBoolean(); 
        System.out.println(patientOrStaff);
        manager.adduser(patientOrStaff);

        // Look up and update
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the user to update: ");
        String id = scanner.nextLine();

        manager.updateuserInfo(id);

    }
}
