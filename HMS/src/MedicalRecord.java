import java.text.SimpleDateFormat;
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

    public String[] toArray(){
        return new String[]{
                patientID,
                getStringDateOfDiagnosis(),
                diagnosis,
                prescription,
                String.valueOf(prescriptionStatus)
        };
    }

    //getter functions
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
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setPrescriptionStatus(boolean prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public void setDateOfDiagnosis(Date dateOfDiagnosis) {
        this.dateOfDiagnosis = dateOfDiagnosis;
    }

    
}
