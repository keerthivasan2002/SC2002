import java.util.ArrayList;

public class PatientManager {
    private ArrayList<Patient> patients;
    private String file = "Patient_List";

    FileManager loadPatient = new FileManager(file);
    String[][] patientArray = loadPatient.readFile();


}
