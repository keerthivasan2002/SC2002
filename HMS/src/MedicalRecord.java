import java.text.SimpleDateFormat;
import java.util.Date;

public class MedicalRecord {
    private static int idCounter = 1;
    private int medicalRecordID;
    private String patientID;
    private Date dateOfDiagnosis;
    private String diagnosis;
    private String prescription;
    private boolean prescriptionStatus;


    public MedicalRecord(String patientID, Date dateOfDiagnosis, String diagnosis, String prescription, boolean prescriptionStatus){
        this.medicalRecordID = idCounter++;
        this.patientID = patientID;
        this.dateOfDiagnosis = dateOfDiagnosis;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.prescriptionStatus = prescriptionStatus;
    }

    public String[] toArray(){
        return new String[]{
                String.valueOf(medicalRecordID),
                patientID,
                getStringDateOfDiagnosis(),
                diagnosis,
                prescription,
                String.valueOf(prescriptionStatus)
        };
    }

    //getter functions
    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public String getStringDateOfDiagnosis() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateOfDiagnosis);
    }

    public Date getDateOfDiagnosis() {
        return dateOfDiagnosis;
    }

    public String getDiagnosis(){
        return diagnosis;
    }

    public String getPrescription(){
        return prescription;
    }

    public boolean isPrescriptionStatus() {
        return prescriptionStatus;
    }

    public String getPatientID() {
        return patientID;
    }

    //setter functions
    public int setMedicalRecordID(int medicalRecordID) {
        return this.medicalRecordID = medicalRecordID;
    }
    public String setDiagnosis(String diagnosis) {
        return this.diagnosis = diagnosis;
    }

    public String setPrescription(String prescription) {
        return this.prescription = prescription;
    }

    public boolean setPrescriptionStatus(boolean prescriptionStatus) {
        return this.prescriptionStatus = prescriptionStatus;
    }

    public Date setDateOfDiagnosis(Date dateOfDiagnosis) {
        return this.dateOfDiagnosis = dateOfDiagnosis;
    }

    
}
