
import java.util.ArrayList;

public class InternManager {
    private static ArrayList<Intern> interns = new ArrayList<>();
    private String intern_file = "Interns.csv";

    // Constructor for PatientManager
    public InternManager() {
        intializeStudents(); // Load patient data from file
    }

    private void intializeStudents() {
        FileManager internFileManager = new FileManager(intern_file);
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
                String specialisation = row[8];

                Intern intern = new Intern(studentID, password,name,gender,emailAddress,phoneNumber, dateOfBirth, bloodType, role,specialisation);
                interns.add(intern);

            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row) + "[Student Manager]");
            }
        }
    }

    public Intern selectIntern(String internID){
        for(Intern intern: interns){
            if(intern.getUserID().equals(internID)){
                return intern;
            }
        }

        System.out.println("An error has occurred: Intern with ID" + internID + "not found!");
        return null;
    }
}
