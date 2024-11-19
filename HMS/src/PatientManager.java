import java.util.ArrayList;
import java.util.Scanner;

public class PatientManager{
    private static ArrayList<Patient> patients = new ArrayList<>(); // List of all patients
    private String patient_File = "Patient_List.csv";
    
    private Scanner sc = new Scanner(System.in);
    // Constructor for PatientManager
    public PatientManager() {
        System.out.println("Initializing PatientManager...");
        initializePatients(); // Load patient data from file
        System.out.println("PatientManager initialized with " + patients.size() + " patients.");

    }

    //initialise the array based on the csv that is loaded in
    private void initializePatients() {
        FileManager patientFileManager = new FileManager(patient_File);
        String[][] patientArray = patientFileManager.readFile();

        if (patientArray == null || patientArray.length == 0) {
            System.out.println("Failed to load patient data.[PatientManager]"); //exception statement to show the data is loaded properly
            return;
        }

        for (int i = 1; i < patientArray.length; i++) { // Start from 1 to skip the header row
            String[] row = patientArray[i];
            if (row.length >= 8) {  // Ensure there are enough fields in the row
                String userID = row[0];
                String password = row[1];
                String name = row[2];
                String dateOfBirth = row[3];
                Gender gender = Gender.valueOf(row[4].toUpperCase());  // Assuming row[4] is the gender
                BloodType bloodType = BloodType.valueOf(row[5].toUpperCase().replace("+", "_POSITIVE").replace("-", "_NEGATIVE")); // Adjust blood type parsing accordingly
                String emailAddress = row[6];
                int phoneNumber = row[7].isEmpty() ? 0 : Integer.parseInt(row[7]);
                HospitalRole role = HospitalRole.valueOf(row[8].toUpperCase());

                // Create a new Patient object and add it to the list
                Patient patient = new Patient(userID, password, name, gender, emailAddress, phoneNumber, dateOfBirth, bloodType,role);
                patients.add(patient); //adding new patient data
            } else {
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row)); //exception statement to show the data is loaded properly
            }
        }

    }

    

    //selecting the specific patient that we want to look at
    public Patient selectPatient(String patientID){
        for(Patient patient : patients){
            if(patient.userID.equals(patientID)){
                return patient;
            }
        }

        System.out.println("An error has occurred: Patient with ID " + patientID + "not found!");
        return null;
    }


    // Save all patients to the CSV file
    public void savePatients() {
        FileManager patientFileManager = new FileManager(patient_File);

        // Convert each patient to a string array
        String[][] patientData = new String[patients.size() + 1][];
        patientData[0] = new String[]{"UserID", "Password", "Name", "DateOfBirth", "Gender", "BloodType", "EmailAddress", "PhoneNumber", "Role"}; // Header row

        for (int i = 0; i < patients.size(); i++) {
            patientData[i + 1] = patients.get(i).toArray();
        }

        // Write to file
        patientFileManager.writeFile(patientData, false);
    }

    
    
    //tester function
    public void printPatients() {
        for (Patient patient : patients) {
            System.out.println(patient); // Print each patient's details
        }
    }

    public static ArrayList<Patient> getPatients(){
        return patients;
    }

    public void displayPatient(){
        System.out.println("Displaying all Patient");
        if (patients.isEmpty()) {
            System.out.println("No patient to display.");
        } else {
            for (Patient patient : patients) {
                System.out.println("ID: " + patient.getPatientID());
                System.out.println("Password: " + patient.getPassword());
                System.out.println("Name: " + patient.getName());
                System.out.println("Date of Birth: " + patient.getDateOfBirth());
                System.out.println("Gender: " + patient.getGender());
                System.out.println("Blood Type: " + patient.getBloodType());
                System.out.println("Email: " + patient.getEmailAddress());
                System.out.println("Phone: " + patient.getPhoneNumber());
                System.out.println("-----------------------------");
            }
        }
    }

    public String setPatientEmail(){
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

    public int setPatientPhoneNumber(){
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
    
    public String changePasswordString(){
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
        return newPassword;
    }

    public void changePasswordMenu(){
        System.out.println("Change Password");
        System.out.println("Your password must meet the following criteria:");
        System.out.println("- At least 8 characters long");
        System.out.println("- Contain at least one uppercase letter");
        System.out.println("- Contain at least one lowercase letter");
        System.out.println("- Contain at least one number");
        System.out.println("- Contain at least one special character (!@#$%^&*()-+=)");
        System.out.println("-------------------------------------------------------------------");
    }
    public void setPassword(String password, String userID) {
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
                patient.setPassword(password);
                System.out.println("Password updated successfully.[patientmanager]" + patient.getPassword());
                savePatients(); // Save the updated password to the CSV file
                return;
            }
        }
    }

    public boolean isValidPassword(String password) {
        // Enforce password requirements
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.matches(".*[!@#$%^&*()-+=].*"); // Check for at least one special character
        boolean isLengthValid = password.length() >= 8; // Minimum length 8 characters
    
        return hasUppercase && hasLowercase && hasDigit && hasSpecial && isLengthValid;
    }

}
