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
        // System.out.println("Loading Medical Records..." );
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

            if(row.length >= 5){ //Ensure that there are enough fields in the row
                int medicalRecordID = Integer.parseInt(row[0]);
                String patientID = row[1];
                Date dateOfDiagnosis = null;
                try {
                    dateOfDiagnosis = dateFormat.parse(row[2]); // Assuming the date is in the second column
                } catch (ParseException e) {
                    System.out.println("Error parsing date for patient ID " + patientID + ": " + e.getMessage());
                    continue; // Skip to the next row if date parsing fails
                }
                String diagnosis = row[3];
                String prescription = row[4];
                Boolean prescriptionStatus = Boolean.parseBoolean(row[5]);

                MedicalRecord medicalRecord = new MedicalRecord(patientID, dateOfDiagnosis,diagnosis,prescription,prescriptionStatus);
                MedicalRecords.add(medicalRecord);
            }else{
                System.out.println("Incomplete data in row, skipping: [Medical Record Manager]" + String.join(",", row));
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

    public ArrayList<MedicalRecord> getMedicalRecord(){
        return MedicalRecords;
    }

    public MedicalRecord getRecordByID(int medicalRecordID){
        for(MedicalRecord record : MedicalRecords){
            if(record.getMedicalRecordID() == medicalRecordID){
                return record;
            }
        }
        return null;
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

    public void updateMedicalRecord(int medicalRecordID) {
        for (MedicalRecord record : MedicalRecords) {
            if (record.getMedicalRecordID() == medicalRecordID) {
                record.setPrescriptionStatus(true);
            }
        }
    }

    public int getFirstMedicalRecordID(ArrayList<MedicalRecord> medicalRecords){
        int firstMedicalRecordID = getLastMedicalRecordID(medicalRecords);
        for (MedicalRecord record : medicalRecords){
            if (record.getMedicalRecordID() < firstMedicalRecordID){
                firstMedicalRecordID = record.getMedicalRecordID();
            }
        }
        return firstMedicalRecordID;
    }

    public int getLastMedicalRecordID(ArrayList<MedicalRecord> medicalRecords){
        int lastMedicalRecordID = 0;
        for (MedicalRecord record : medicalRecords){
            if (record.getMedicalRecordID() > lastMedicalRecordID){
                lastMedicalRecordID = record.getMedicalRecordID();
            }
        }
        return lastMedicalRecordID;
    }
    
    public int getValidMedicalRecordID(){
        OptionHandling oh = new OptionHandling();
        int selectedID = -1;
        boolean valid = false;

        while(!valid){
            selectedID = oh.getOption(0, Integer.MAX_VALUE);

            if (selectedID == 0){
                return -1;
            }else if(selectedID < 0){
                return -2;
            }
            for (MedicalRecord record : MedicalRecords){
                if (record.getMedicalRecordID() == selectedID){
                    valid = true;
                    break;
                }
            }
            if (!valid){
                System.out.println("Invalid Medical Record ID. Please try again or enter 0 to cancel.");
            }
        }
        return selectedID;
    }

    public void changeMedicalRecordPrescriptionStatus(String patientID, Date dateOfDiagnosis, Boolean newStatus) {
        for (MedicalRecord record : MedicalRecords) {
            if (record.getPatientID().equals(patientID) && record.getDateOfDiagnosis().equals(dateOfDiagnosis)) {
                record.setPrescriptionStatus(newStatus);
                saveMedicalRecords();
                return;
            }
        }
        System.out.println("No medical record found for patient ID " + patientID + " and diagnosis date " + dateOfDiagnosis);
    }

    public void saveMedicalRecords() {
        FileManager medicalRecordFM = new FileManager(Mr_file);

        // Header for CSV
        String[] header = {"MedicalRecordID","PatientID", "DateOfDiagnosis", "Diagnosis", "Prescription", "PrescriptionStatus"};
        medicalRecordFM.addNewRow(header, false); // Write header
    
        // Loop through each MedicalRecord and write individually
        for (MedicalRecord record : MedicalRecords) {
            String[] recordData = new String[6];
            recordData[0] = String.valueOf(record.getMedicalRecordID());
            recordData[1] = record.getPatientID();
            recordData[2] = record.getStringDateOfDiagnosis();
            recordData[3] = record.getDiagnosis();
            recordData[4] = record.getPrescription();
            recordData[5] = String.valueOf(record.isPrescriptionStatus());
    
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

    public ArrayList<MedicalRecord> setMedicalRecords(String userID){
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

    // Display a list of appointments in a tabular format    
    public void displayMedicalRecords(ArrayList<MedicalRecord> medicalRecords) {
        if (medicalRecords.isEmpty()) {
            System.out.println("No medical records found.");
            return;
        }
    
        // Print table headers
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-30s %-20s %-20s%n", 
                "Medical Record ID", "Patient ID", "Date of Diagnosis", "Diagnosis", "Prescription", "Prescription Status");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
    
    
        // Print each medical record's details in a formatted manner
        for (MedicalRecord medicalRecord : medicalRecords) {
            System.out.printf("%-20s %-30s %-20s %-30s %-20s %-20s%n",
                    medicalRecord.getMedicalRecordID(),
                    medicalRecord.getPatientID(),
                    medicalRecord.getStringDateOfDiagnosis(),
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getPrescription(),
                    medicalRecord.isPrescriptionStatus() ? "Approved" : "Not Approved");
        }
    
        System.out.println("-----------------------------------------------------------------------------------------------");
    }
    
}
