import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

public class StaffManager {
    private static ArrayList<Staff> staffs;
    private static ArrayList<Staff> users = new ArrayList<>();
    private String staff_file = "Staff_List.csv";
    private String user_file = "UserID.csv";

    Scanner sc = new Scanner(System.in);


    public StaffManager(){
        staffs = new ArrayList<>();
        initializeStaffs(); //load the staff data from the file
    }

    //intialise the array based on the csv that is loaded in
    private void initializeStaffs(){
        FileManager staffFileManager = new FileManager(staff_file);
        
        String[][] staffArray = staffFileManager.readFile();

        //checking for potential errors
        if(staffArray == null || staffArray.length == 0){
            System.out.println("Failed to load patient data.[StaffManager]");
            return;
        }
        

        for(int i = 1; i < staffArray.length; i++){
            String[] row = staffArray[i];

            if(row.length >= 6){
                String userID = row[0];
                String password = row[1];
                String name = row[2];
                HospitalRole role = HospitalRole.valueOf(row[3].toUpperCase());
                Gender gender = Gender.valueOf(row[4].toUpperCase());
                int age = Integer.parseInt(row[5]);
                String emailAddress = row[6];
                int phoneNumber = row[7].isEmpty() ? 0 : Integer.parseInt(row[7]);

                Staff staff = new Staff(userID, password, name, role, gender, age, emailAddress, phoneNumber);
                staffs.add(staff); //adding new staff data
            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row)); //exception statement to show the data is loaded properly
            }
        }

        //display initialisation file
        //System.out.println("Staff data loaded successfully.[StaffManager]");
        // displayStaffMembers();
    }

    public Staff selectStaff(String staffID){
        for(Staff staff : staffs){
            if(staff.userID.equals(staffID)){
                return staff;
            }
        }

        System.out.println("An error has occurred: User with ID " + staffID + "not found!");
        return null;
    }

    public void setPassword(String newPassword, String staffID) {
        for (Staff staff : staffs) {
            if (staff.getUserID().equalsIgnoreCase(staffID)) {
                staff.setPassword(newPassword);
                System.out.println("Password updated successfully.");
                saveStaff();
                return;
            }
        }
        System.out.println("Staff member with ID " + staffID + " was not found.");
    }

    public void addStaffMember(String id, String password) {
        boolean isDefaultPassword = true; // By default, the password is set to default
        Staff newUser = new Staff(id, password, true); // Assuming default password is true
        users.add(newUser);

        FileManager staffFileManager = new FileManager(staff_file);
        String[] row = {id, password, String.valueOf(isDefaultPassword)};
        staffFileManager.addNewRow(row);
    }

    public void addStaffMember(){
        String id = "", password = "password", name = "", role = "", gender = "", email = "";
        int age = 0, phone = 0;
        boolean isDefaultPassword = true;
        try{
            System.out.println("Adding a new staff member");
            System.out.println("Enter staff's name: ");
            name = sc.nextLine();
            role = setStaffRole();
            gender = setStaffGender();
            age = setStaffAge();
            email = setStaffEmail();
            phone = setStaffPhoneNumber();
            id = setStaffID(role);
            password = "password"; //default password
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        HospitalRole roles = HospitalRole.valueOf(role.toUpperCase());
        Gender genders = Gender.valueOf(gender.toUpperCase());

        //create a new staff object 
        String[] newStaffRecord = new String[] {id, password, name, role, gender, String.valueOf(age), email, String.valueOf(phone)};
        Staff newStaff = new Staff (id, password, name, roles, genders, age, email, phone);
        staffs.add(newStaff);

        
        String[] newUserRecord = new String[] {id, password, String.valueOf(isDefaultPassword)};
        Staff newUser = new Staff(id, password, isDefaultPassword);
        users.add(newUser);


        //add new staff record to the staff.csv and user.csv file
        FileManager staffFileManager = new FileManager(staff_file);
        staffFileManager.addNewRow(newStaffRecord);

        FileManager staffFileManager2 = new FileManager(user_file);
        staffFileManager2.addNewRow(newUserRecord);

        sc.nextLine(); //clear buffer
    }

    public static String setStaffID(String role){
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        }

        char prefix = role.charAt(0);
        System.out.println("Prefix : " + prefix);
        int maxId = 0;


        for (Staff staff : staffs) {
            String staffIDString = staff.getUserID();
            if (staffIDString.startsWith(String.valueOf(prefix))) {
                String numericPart = staff.getUserID().substring(1); // Extract the numeric part
                int idNumber = Integer.parseInt(numericPart);
                if (idNumber > maxId) {
                    maxId = idNumber;
                }
            }
        }

        int newIdNumber = maxId + 1;

        return String.format("%s%03d", prefix, newIdNumber); 
    }

    public String setStaffRole(){
        int choice = -1;
        while (true) {
            System.out.println("Enter the staff member's role : ");
            System.out.println("1. Doctor");
            System.out.println("2. Pharmacist");
            System.out.println("3. Administrator");
            System.out.println("Enter your choise");
            choice = sc.nextInt();

            if (choice == 1){
                return "Doctor";
            }else if (choice == 2){
                return "Pharmacist";
            }else if (choice == 3){
                return "Administrator";
            }else {
                System.out.println("Invalid role. The role must start with an uppercase letter.");

            }
        }
    }

    public String setStaffGender(){
        int choice = -1;
        while (true) {
            System.out.println("Enter the staff member's gender : ");   
            System.out.println("1. Male");
            System.out.println("2. Female");
            choice = sc.nextInt();
            if (choice == 1){
                return "Male";
            }else if (choice == 2){
                return "Female";
            }else {
                System.out.println("Invalid choice, please select either 1 or 2. ");
            }
        }
    }


    public int setStaffAge(){
        int age = 0;
        while (true) {
            try {
                System.out.println("Enter the staff member's age (must be greater than 18): ");
                age = sc.nextInt();
                sc.nextLine(); // Consume the newline character after nextInt()
                if (age > 18) {
                    return age; // Valid age
                } else if (age < 0){
                    throw new IntNonNegativeException();
                }else {
                    System.out.println("Invalid age. Age must be greater than 18.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for age.");
            } catch (IntNonNegativeException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public String setStaffEmail(){
        String email = "";
        while (true) {
            System.out.println("Enter the staff member's email (must end with @hotmail.com, @gmail.com, @outlook.com, or @yahoo.com): ");
            email = sc.nextLine();
            if (email.matches(".+@(hotmail|gmail|outlook|yahoo)\\.com")) {
                return email;
            }else{
                System.out.println("Invalid email. Please enter a valid email address ending with @hotmail.com, @gmail.com, @outlook.com, or @yahoo.com.");
            }
        }
    }

    public int setStaffPhoneNumber(){
        String phone = "";
        while (true) {
            System.out.println("Enter the staff member's phone number (must be 8 digits and start with 8 or 9): ");
            phone = sc.nextLine();
            if (phone.matches("[89]\\d{7}")) {
                try {
                    // Convert phone number from String to Integer
                    return Integer.valueOf(phone);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Phone number is not valid for integer conversion.");
                    return 0; // Return null if the conversion fails
                }
            }
        }
    }

    public void saveStaff(){
        FileManager staffFileManager = new FileManager(staff_file);

        String[][] staffData = new String[staffs.size() + 1][8]; // Create a 2D array to store the data
        staffData[0] = new String[] {"UserID, Password, Name, Role, Gender, Age, Email, Phone"}; // Header row
        
        for (int i = 0; i < staffs.size(); i++) {
            staffData[i + 1] = staffs.get(i).toArray();
        }

        staffFileManager.writeFile(staffData, false);
    }

    public void updateMenu(){
        System.out.println("Which of the following would you like to change? ");
        System.out.println("1. Reset Password");
        System.out.println("2. Update Name");
        System.out.println("3. Update Gender");
        System.out.println("4. Update Role");
        System.out.println("5. Update Age");
        System.out.println("6. Update Email Address");
        System.out.println("7. Update Phone Number");
        System.out.println("8. Back to Main Menu");
        System.out.println("-------------------------------------------------------");
    }

    public void updateStaffMember(String updateStaffIDString){
        Staff staffToUpdate = null;

        for (Staff staff : staffs) {
            if (staff.getUserID().equalsIgnoreCase(updateStaffIDString)) {
                staffToUpdate = staff;
                break;
            }
        }
        int changeChoice = -1;
        
        while (changeChoice != 8 ){
            updateMenu();
            changeChoice = sc.nextInt();
            sc.nextLine();
            switch (changeChoice){
                case 1:
                    System.out.println("Reset staff password to default.");
                    String password = "password";
                    staffToUpdate.setPassword(password);
                    // staff.updatePassword(password);
                    System.out.println("Passward reset successfully.");
                    break;
                case 2:
                    System.out.println("Please enter new staff's name: ");
                    String newName = sc.nextLine();
                    staffToUpdate.setName(newName);
                    System.out.println("Name updated successfully.");
                    break;
                case 3:
                    System.out.print("Enter new gender: ");
                    String newGender = setStaffGender();   
                    System.out.println("new Gender: " + newGender);
                    try {
                        Gender gender = Gender.valueOf(newGender.toUpperCase());
                        staffToUpdate.setGender(gender);
                        System.out.println("Gender updated successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid gender. ");
                    }
                    System.out.println("Gender updated successfully.");
                    break;
                case 4:
                    System.out.print("Enter new role: ");
                    String newRole = setStaffRole().toUpperCase();

                    try {
                        HospitalRole role = HospitalRole.valueOf(newRole);
                        staffToUpdate.setRole(role);
                        // System.out.println("Role = "    + role);
                        String newUserID = setStaffID(newRole);
                        staffToUpdate.setUserID(newUserID);
                        System.out.println("Role updated successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid role. ");
                    }
                    // System.out.println("Role updated successfully.");
                    break;
                case 5:
                    // System.out.print("Enter new age: ");
                    int newAge = setStaffAge();
                    staffToUpdate.setAge(newAge);
                    System.out.println("Age updated successfully.");
                    break;
                case 6:
                    // System.out.print("Enter new email address: ");
                    String newEmail = setStaffEmail();
                    staffToUpdate.setEmailAddress(newEmail);
                    System.out.println("Email updated successfully.");
                    break;
                case 7:
                    System.out.print("Enter new phone number: ");
                    int newPhone = setStaffPhoneNumber();
                    staffToUpdate.setPhoneNumber(newPhone);
                    System.out.println("Phone number updated successfully.");
                    break;
                case 8:
                    break;
                default:
                    break;

            }
        }
        saveStaff();
    }   

    public void displayStaffMembers() {
        System.out.println("Displaying all staff members");
        if (staffs.isEmpty()) {
            System.out.println("No staff members to display.");
        } else {
            // Print table headers
            System.out.printf("%-10s %-10s %-15s %-15s %-10s %-5s %-25s %-15s%n",
                    "ID", "Password", "Name", "Role", "Gender", "Age", "Email", "Phone");
            System.out.println("--------------------------------------------------------------------------------------------------------");
    
            // Print each staff member's details in a formatted manner
            for (Staff staff : staffs) {
                System.out.printf("%-10s %-10s %-15s %-15s %-10s %-5d %-25s %-15s%n",
                        staff.getUserID(),
                        staff.getPassword(),
                        staff.getName(),
                        staff.getrole(),
                        staff.getGender(),
                        staff.getAge(),
                        staff.getEmailAddress(),
                        staff.getPhoneNumber());
            }
        }
    }
    public void displayAllDoctors() {
        System.out.println("Displaying all doctors");
        if (staffs.isEmpty()) {
            System.out.println("No staff members to display.");
        } else {
            // Print table headers for just ID and Name
            System.out.printf("%-10s %-15s%n", "ID", "Name");
            System.out.println("-------------------------------------");
    
            // Print each doctor's details in a formatted manner
            boolean hasDoctors = false; // To check if there are any doctors
            for (Staff staff : staffs) {
                if (staff.getrole().equals(HospitalRole.DOCTOR)) {
                    hasDoctors = true; // Found at least one doctor
                    System.out.printf("%-10s %-15s%n",
                            staff.getUserID(),
                            staff.getName());
                }
            }
    
            // If no doctors were found
            if (!hasDoctors) {
                System.out.println("No doctors found.");
            }
        }
    }
    

    public static void removeStaffMember(String staffIDRecord) {
        boolean removed = false;
        Iterator<Staff> iterator = staffs.iterator();

        System.out.println("Remove Staff Member: ");
        while (iterator.hasNext()) {
            Staff staff = iterator.next();
            if (staff.getUserID().trim().equals(staffIDRecord)) {
                iterator.remove();
                removed = true;
                System.out.println("Staff member with ID " + staffIDRecord + " has been removed successfully.");
                break;
            }
        }

        if (!removed) {
            System.out.println("Staff member with ID " + staffIDRecord + " was not found.");
        }
    }

    public static ArrayList<Staff> getStaffList(){
        // if (staffs == null || staffs.isEmpty()) {
        //     System.out.println("No staff members found.");
        // }else{
        //     System.out.println("Staff data loaded successfully.[StaffManager]");
        // }
        return staffs;
    }

    
}
