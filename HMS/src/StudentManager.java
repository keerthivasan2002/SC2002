
import java.util.ArrayList;
import java.util.Scanner;

public class StudentManager {
    private static ArrayList<Student> students = new ArrayList<>();
    private String student_file = "Interns.csv";

    private Scanner sc = new Scanner(System.in);

    // Constructor for PatientManager
    public StudentManager() {
            if(students.isEmpty()|| students == null){
                // System.out.println("Failed to load student data. [Intern Manager]"); //exception statement to show the data is loaded properly
            }
            
            intializeStudents();
        
    }

    public void intializeStudents() {
        FileManager internFileManager = new FileManager(student_file);
        String[][] internArray = internFileManager.readFile();

        if(internArray == null || internArray.length == 0){
            System.out.println("Failed to load student data. [Intern Manager]"); //exception statement to show the data is loaded properly
            return;
        }

        for(int i = 1; i < internArray.length; i++){
            String[] row = internArray[i];

            if(row.length >= 5){
                String studentID = row[0];
                String password = row[1];
                String name = row[2];
                String dateOfBirth = row[3];
                Gender gender = Gender.valueOf(row[4].toUpperCase());  // Assuming row[4] is the gender
                BloodType bloodType = BloodType.valueOf(row[5].toUpperCase().replace("+", "_POSITIVE").replace("-", "_NEGATIVE")); // Adjust blood type parsing accordingly
                String emailAddress = row[6];
                int phoneNumber = row[7].isEmpty() ? 0 : Integer.parseInt(row[7]);
                HospitalRole role = HospitalRole.valueOf(row[8].toUpperCase());
                String specialisation = row[9];

                Student student = new Student(studentID, password,name,gender,emailAddress,phoneNumber, dateOfBirth, bloodType, role,specialisation);
                students.add(student);
            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row) + "[Student Manager]");
            }
        }
    }

    private void intializeNurse() {
        FileManager internFileManager = new FileManager(student_file);
        String[][] internArray = internFileManager.readFile();

        if(internArray == null || internArray.length == 0){
            System.out.println("Failed to load student data. [Intern Manager]"); //exception statement to show the data is loaded properly
            return;
        }

        for(int i = 1; i < internArray.length; i++){
            String[] row = internArray[i];

            if(row.length >= 5){
                String studentID = row[0];
                String password = row[1];
                String name = row[2];
                String dateOfBirth = row[3];
                Gender gender = Gender.valueOf(row[4].toUpperCase());  // Assuming row[4] is the gender
                BloodType bloodType = BloodType.valueOf(row[5].toUpperCase().replace("+", "_POSITIVE").replace("-", "_NEGATIVE")); // Adjust blood type parsing accordingly
                String emailAddress = row[6];
                int phoneNumber = row[7].isEmpty() ? 0 : Integer.parseInt(row[7]);
                HospitalRole role = HospitalRole.valueOf(row[8].toUpperCase());
                String specialisation = row[9];

                if(specialisation.equals("Nurse")){
                    Student student = new Student(studentID, password,name,gender,emailAddress,phoneNumber, dateOfBirth, bloodType, role,specialisation);
                    students.add(student);
                }
            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row) + "[Student Manager]");
            }
        }
    }

    private void intializeMedStudents() {
        FileManager internFileManager = new FileManager(student_file);
        String[][] internArray = internFileManager.readFile();

        if(internArray == null || internArray.length == 0){
            System.out.println("Failed to load student data. [Intern Manager]"); //exception statement to show the data is loaded properly
            return;
        }

        for(int i = 1; i < internArray.length; i++){
            String[] row = internArray[i];

            if(row.length >= 5){
                String studentID = row[0];
                String password = row[1];
                String name = row[2];
                String dateOfBirth = row[3];
                Gender gender = Gender.valueOf(row[4].toUpperCase());  // Assuming row[4] is the gender
                BloodType bloodType = BloodType.valueOf(row[5].toUpperCase().replace("+", "_POSITIVE").replace("-", "_NEGATIVE")); // Adjust blood type parsing accordingly
                String emailAddress = row[6];
                int phoneNumber = row[7].isEmpty() ? 0 : Integer.parseInt(row[7]);
                HospitalRole role = HospitalRole.valueOf(row[8].toUpperCase());
                String specialisation = row[9];

                if(!specialisation.equals("Nurse")){
                    Student student = new Student(studentID, password,name,gender,emailAddress,phoneNumber, dateOfBirth, bloodType, role,specialisation);
                    students.add(student);
                }
            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row) + "[Student Manager]");
            }
        }
    }

    public Student selectIntern(String internID){
        for(Student student: students){
            if(student.getUserID().equals(internID)){
                return student;
            }
        }

        System.out.println("An error has occurred: Intern with ID" + internID + "not found!");
        return null;
    }


    //fucntion to set number
    public int setInternNumber(){
        String phone = "";
        while (true) {
            System.out.println("Enter the Intern's phone number (must be 8 digits and start with 8 or 9): ");
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
    //changing password to string
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

    //password menu
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
        for (Student student : students) {
            if (student.getUserID().equals(userID)) {
                student.setPassword(password);
                System.out.println("Password updated successfully.[patientmanager]" + student.getPassword());
                saveInterns(); // Save the updated password to the CSV file
                return;
            }
        }
    }

    //logic for nurse 
    public boolean isValidNurse(String nurseID) {
        for (Student student : students) {
            if (student.getSpecialisation().equals("Nurse")) {
                return true;
            }
        }
        return false;
    }

    //logic for valid password
    public boolean isValidPassword(String password) {
        // Enforce password requirements
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.matches(".*[!@#$%^&*()-+=].*"); // Check for at least one special character
        boolean isLengthValid = password.length() >= 8; // Minimum length 8 characters

        return hasUppercase && hasLowercase && hasDigit && hasSpecial && isLengthValid;
    }


    public void saveInterns() {
        FileManager internFileManager = new FileManager(student_file);

        // Convert each intern to a string array
        String[][] internData = new String[students.size() + 1][];
        internData[0] = new String[]{"UserID","Password","Name","DateOfBirth","Gender","BloodType","EmailAddress","PhoneNumber","Role","Specialisation"}; // Header row

        for (int i = 0; i < students.size(); i++) {
            internData[i + 1] = students.get(i).toArray();
        }

        // Write to file without appending
        internFileManager.writeFile(internData, false); // `false` to overwrite the file entirely
    }

    public boolean isValidMedStudent(String medStudentID) {
        for (Student student : students) {
            if (student.getSpecialisation() != "Nurse") {
                return true;
            }
        }
        return false;
    }

}
