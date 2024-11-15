import java.sql.Time;
import java.util.Date;

public class Appointment extends Events {
    private static int idCounter = 1;
    private int appointmentID;
    private AppointmentStatus appointmentStatus;
    private MedicalRecord appointmentRecord;
    private String outcome;
    private Patient patient;

    public Appointment(String patientID, String userID, Date date, Time startTime, Time endTime, String description) {
        super(userID, date, startTime, endTime, description);
        this.appointmentID = idCounter++;
        description = "Appointment";
        this.patient = new Patient(patientID);
        this.appointmentStatus = AppointmentStatus.SCHEDULED; // Default status
        this.appointmentRecord = new MedicalRecord(patientID, date, patientID, userID, false);
    }

    // Constructor without description (using default "Appointment" description)
    public Appointment(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
        super(doctorID, date, startTime, endTime, "Appointment");
        this.appointmentID = idCounter++;
        this.patient = new Patient(patientID);
        this.appointmentStatus = AppointmentStatus.SCHEDULED; // Default status
        this.appointmentRecord = new MedicalRecord(patientID, date, patientID, doctorID, false);
    }

    // Appointment-specific getters and setters
    public Patient getPatient() {
        return patient;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public MedicalRecord getAppointmentRecord() {
        return appointmentRecord;
    }

    public void setAppointmentRecord(MedicalRecord appointmentRecord) {
        this.appointmentRecord = appointmentRecord;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    // Convert to array for table or CSV representations
    public String[] toArray() {
        return new String[]{
                String.valueOf(appointmentID),
                getPatient().getPatientID(),
                getDoctor().getUserID(),
                getStringDate(),
                getStringStartTime(),
                getStringEndTime(),
                appointmentStatus.toString(),
                outcome != null ? outcome : "" // Check for null outcome
        };
    }
}
