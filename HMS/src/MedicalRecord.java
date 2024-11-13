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
        String formattedDate = getDateOfDiagnosis();
        return new String[]{
                patientID,
                formattedDate,
                diagnosis,
                prescription,
                String.valueOf(prescriptionStatus)
        };
    }

    //getter functions
    public String getDateOfDiagnosis() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(dateOfDiagnosis);
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
