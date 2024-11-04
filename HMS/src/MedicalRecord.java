import java.util.Date;

public class MedicalRecord {
    private String patientID;
    private Date dateOfDiagnosis;
    private String diagnosis;
    private String prescription;
    private boolean prescriptionStatus;


    public MedicalRecord(String patientID, Date dateOfDiagnosis, String diagnosis, String prescription, boolean prescriptionStatus){
        this.patientID = patientID;
        this.dateOfDiagnosis = dateOfDiagnosis;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.prescriptionStatus = prescriptionStatus;
    }

    //getter functions
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
}
