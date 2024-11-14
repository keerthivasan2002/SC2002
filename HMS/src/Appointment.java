import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {
    private static int idCounter = 1;
    protected int appointmentID;
    private Patient patient;
    private Staff doctor;
    private AppointmentStatus appointmentStatus;
    private Date date;
    private Time time;
    private MedicalRecord appointmentRecord;
    private String outcome;
    private String patientID;
    private String userID;

    public Appointment(String patientID, String userID, Date date, Time time) {
        this.appointmentID = idCounter++;
        // this.patient = new Patient(patientID); // Assuming Patient class has this constructor
        // this.doctor = new Staff(userID); // Assuming Staff class has this constructor
        this.patientID = patientID;
        this.userID = userID;
        this.date = date;
        this.time = time;
        this.appointmentStatus = AppointmentStatus.SCHEDULED; // Default status
        this.appointmentRecord = new MedicalRecord(patientID, date, patientID, userID, false);
    }

    public String getPatientID() {
        return patientID;
    }

    public String getUserID() {
        return userID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public Staff getDoctor() {
        return doctor;
    }

    public AppointmentStatus getAppointmentStatus() {
        return this.appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    // Formatted getDate to string method
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public Date getDate() {
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Date date = dateFormat.format(this.date);
        return date;
    }

    // Formatted getTime to string method
    public String getStringTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        // SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Time time = timeFormat.format(this.time);
        return time;

    }

    public void setTime(Time time) {
        this.time = time;
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

    public String[] toArray() {
        // Use formatted getDate and getTime methods
        String formattedDate = getStringDate();
        String formattedTime = getStringTime();

        return new String[]{
                String.valueOf(appointmentID),
                patient.getUserID(),
                doctor.getUserID(),
                formattedDate,
                formattedTime,
                appointmentStatus.toString(),
                outcome != null ? outcome : "" // Check for null outcome
        };
    }
}
