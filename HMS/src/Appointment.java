import javax.print.Doc;
import java.sql.Time;
import java.util.Date;

/*This class represents a single appointment. 
 * It stores about an individual appointment.
 * This class typically includes fields for appointment's data and possibly some methods 
 * for manipulating that data at the inidividual appointment level.
 */

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

    //constructor class
    public Appointment(String patientID, String userID, Date date, Time time) {
        this.appointmentID = idCounter++;
        this.patient = new Patient(patientID); // Assuming Patient class has this constructor
        this.doctor = new Staff(userID); // Assuming Staff class has this constructor
        this.date = date;
        this.time = time;
        this.appointmentStatus = AppointmentStatus.SCHEDULED; // Set the default status to 'SCHEDULED'
        this.appointmentRecord = new MedicalRecord(patientID, date, patientID, userID, false);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
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

    public String getOutcome(){
        return outcome;
    }

    public void setOutcome(String outcome){
        this.outcome = outcome;
    }       
    
    public String[] toArray() {
        return new String[]{
            String.valueOf(appointmentID),
            patient.getUserID(),
            doctor.getUserID(),
            date.toString(),
            time.toString(),
            appointmentStatus.toString(),
            outcome != null ? outcome : "", // Check for null outcome
        };
    }
    
}
