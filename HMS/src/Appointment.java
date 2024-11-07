import javax.print.Doc;
import java.sql.Time;
import java.util.Date;

public class Appointment {
    Patient patient;
    Staff doctor;
    AppointmentStatus appointmentStatus;
    Date date;
    Time time;
    MedicalRecord appointmentRecord;

    //constructor class
    public Appointment(String patientID, String userID, Date date, Time time) {
        this.patient = new Patient(patientID); // Assuming Patient class has this constructor
        this.doctor = new Staff(userID); // Assuming Staff class has this constructor
        this.date = date;
        this.time = time;
        this.appointmentStatus = AppointmentStatus.SCHEDULED; // Set the default status to 'SCHEDULED'
        this.appointmentRecord = null;
    }

}
