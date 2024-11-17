import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentFilter {
    

    public AppointmentFilter(AppointmentStorage storage) {
        this.storage = appoi;
    }

    /* ---------------------------------------- Filter by PatientID ------------------------------------------ */
    public ArrayList<Appointment> viewPatientAppointments(String patientID, boolean showPastAppointments) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        // System.out.println("\nAppointments for Patient ID: " + patientID);
        for (Appointment appointment : appointments) {
            if (appointmentValidator.patientIDExists(patientID)) {
                if (!showPastAppointments && appointmentValidator.appointmentDateIsAfterCurrentDate(appointment, cal)) {
                    patientAppointments.add(appointment);
                }else if (showPastAppointments && appointment.getDate().before(cal.getTime())){
                    patientAppointments.add(appointment);
                }
            }
        }
        return patientAppointments;
    }


}