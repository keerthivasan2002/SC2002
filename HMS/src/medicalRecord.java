import java.util.Date;

public class medicalRecord {
    private Date dateOfDiagnosis;
    private String diagnosis;
    private String prescription;
    private boolean prescriptionStatus;


    public medicalRecord(Date dateOfDiagnosis, String diagnosis, String prescription, boolean prescriptionStatus){
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
}
