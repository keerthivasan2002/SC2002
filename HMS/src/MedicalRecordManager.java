import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.lang.reflect.Array;
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
            // For debug purposes [ensure file from medical record is read properly]
            // System.out.println("Loaded Medical Records:");
            // for (MedicalRecord record : MedicalRecords) {
            //     System.out.println("Patient ID: " + record.getPatientID());
            //     System.out.println("Diagnosis Date: " + record.getDateOfDiagnosis());
            //     System.out.println("Diagnosis: " + record.getDiagnosis());
            //     System.out.println("Prescription: " + record.getPrescription());
            //     System.out.println("Prescription Status: " + (record.isPrescriptionStatus() ? "Approved" : "Not Approved"));
            //     System.out.println("-----------------------------------");
            // }
        }
    }


    // Method to get all medical records for a specific patient by userID
    public ArrayList<MedicalRecord> getAllRecordsForPatient(String userID) {
        ArrayList<MedicalRecord> patientRecords = new ArrayList<>();
        for (MedicalRecord record : MedicalRecords) {
            if (record.getPatientID().trim().equalsIgnoreCase(userID.trim())) {
                patientRecords.add(record);
            }
        }
        return patientRecords;
    }

    public void saveMedicalRecords() {
        FileManager medicalRecordFM = new FileManager(Mr_file);
    
        // Header for CSV
        String[] header = {"PatientID", "DateOfDiagnosis", "Diagnosis", "Prescription", "PrescriptionStatus"};
        medicalRecordFM.addNewRow(header, false); // Write header
    
        // Loop through each MedicalRecord and write individually
        for (MedicalRecord record : MedicalRecords) {
            String[] recordData = new String[5];
            recordData[0] = record.getPatientID();
            recordData[1] = new SimpleDateFormat("yyyy-MM-dd").format(record.getDateOfDiagnosis());
            recordData[2] = record.getDiagnosis();
            recordData[3] = record.getPrescription();
            recordData[4] = String.valueOf(record.isPrescriptionStatus());
    
            // Append each record to the file
            medicalRecordFM.addNewRow(recordData, true);
        }
    }
    

    // public void addMedicalRecord(MedicalRecord record){
    //     MedicalRecords.add(record);
    //     saveMedicalRecords();
    // }

    public void addNewRecord(String patientID, Date dateOfDiagnosis, String diagnosis, String prescription, Boolean prescriptionStatus) {
        // Create the record as an array of Strings for the CSV file
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(dateOfDiagnosis);
        String[] recordData = new String[]{patientID, date, diagnosis, prescription, String.valueOf(prescriptionStatus)};

        // Add to the in-memory list
        MedicalRecord newRecord = new MedicalRecord(patientID, dateOfDiagnosis, diagnosis, prescription, prescriptionStatus);
        MedicalRecords.add(newRecord);

        FileManager medicalRecordFM = new FileManager(Mr_file);
        medicalRecordFM.addNewRow(recordData);
    }


    public ArrayList<MedicalRecord> getMedicalRecords(String userID){ 
        ArrayList<MedicalRecord> patientRecords = new ArrayList<>();
        for(MedicalRecord record : MedicalRecords){
            if (record.getPatientID().equals(userID)) {
                patientRecords.add(record);
            }
        }
        return patientRecords;
    }

    public ArrayList<MedicalRecord> getMedicalRecords(){
        return MedicalRecords;
    }
}
