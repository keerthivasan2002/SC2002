import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PatientManager{
    private ArrayList<Patient> patients;
    private String patient_File = "Patient_List.csv";

    // Constructor for PatientManager
    public PatientManager() {
        patients = new ArrayList<>();
        initializePatients(); // Load patient data from file
    }

    //initialise the array based on the csv that is loaded in
    private void initializePatients() {
        FileManager patientFileManager = new FileManager(patient_File);
        String[][] patientArray = patientFileManager.readFile();

        if (patientArray == null || patientArray.length == 0) {
            System.out.println("Failed to load patient data.");
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

    //tester function
    public void printPatients() {
        for (Patient patient : patients) {
            System.out.println(patient); // Print each patient's details
        }
    }

}
