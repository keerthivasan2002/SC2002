import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MedicalRecordManager {
    private ArrayList<MedicalRecord> MedicalRecords;
    private String Mr_file = "Medical_Records.csv";

    public MedicalRecordManager(){
        MedicalRecords = new ArrayList<>();
        initializeMedicalRecords();
    }

    public void initializeMedicalRecords(){
        FileManager medicalRecordFM = new FileManager(Mr_file);
        String[][] medicalRecordArray = medicalRecordFM.readFile();

        if(medicalRecordArray == null || medicalRecordArray.length == 0){
            System.out.println("Failed to load Medical Record data.");
            return;
        }

        //Date format to match the date in the CSV file
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for(int i = 1 ; i < medicalRecordArray.length; i++){
            String[] row = medicalRecordArray[i];

            if(row.length >= 2){ //Ensure that there are enough fields in the row
                String patientID = row[0];
                Date dateOfDiagnosis = null;
                try {
                    dateOfDiagnosis = dateFormat.parse(row[1]); // Assuming the date is in the second column
                } catch (ParseException e) {
                    System.out.println("Error parsing date for patient ID " + patientID + ": " + e.getMessage());
                    continue; // Skip to the next row if date parsing fails
                }
                String diagnosis = row[2];
                String prescription = row[3];
                Boolean prescriptionStatus = Boolean.parseBoolean(row[4]);

                MedicalRecord medicalRecord = new MedicalRecord(patientID, dateOfDiagnosis,diagnosis,prescription,prescriptionStatus);
                MedicalRecords.add(medicalRecord);
            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row));
            }
        }
    }


    // Method to get all medical records for a specific patient by userID
    public ArrayList<MedicalRecord> getAllRecordsForPatient(String userID) {
        ArrayList<MedicalRecord> patientRecords = new ArrayList<>();
        for (MedicalRecord record : MedicalRecords) {
            if (record.getPatientID().equals(userID)) {
                patientRecords.add(record);
            }
        }
        return patientRecords;
    }

}
