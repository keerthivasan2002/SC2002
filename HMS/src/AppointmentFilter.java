import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentFilter {
    private ArrayList<Appointment> appointments;

    private AppointmentStorage as = new AppointmentStorage();
    private AppointmentValidator av = new AppointmentValidator();

    Calendar cal = Calendar.getInstance();


    public AppointmentFilter() {
        this.appointments = as.getAppointments();
    }

    /* ---------------------------------------- Filter by PatientID ------------------------------------------ */
    public ArrayList<Appointment> getPatientAppointments(Patient patient, int  showTypeOfAppointments) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();

        // System.out.println("\nAppointments for Patient ID: " + patientID);
        for (Appointment appointment : appointments) {
            if (av.patientIDExists(patient)) {
                if (showTypeOfAppointments == 1 && av.afterDate(appointment, cal)) {
                    patientAppointments.add(appointment);
                }else if (showTypeOfAppointments == -1 && appointment.getDate().before(cal.getTime())){
                    patientAppointments.add(appointment);
                }else if (showTypeOfAppointments == 0){
                    patientAppointments.add(appointment);
                }
            }
        }
        return patientAppointments;
    }



    /* ---------------------------------------- Filter by DoctorID ------------------------------------------ */
    public ArrayList<Appointment> getDoctorAppointments(Staff doctor, int showPastAppointments) {
        ArrayList<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (av.doctorIDExists(doctor)) {
                if (showPastAppointments == 1 && av.afterDate(appointment, cal)) {
                    doctorAppointments.add(appointment);
                }else if (showPastAppointments == -1 && appointment.getDate().before(cal.getTime())){
                    doctorAppointments.add(appointment);
                }else if (showPastAppointments == 0){
                    doctorAppointments.add(appointment);
                }
            }
        }
        return doctorAppointments;
    }

    /* ---------------------------------------- Filter by Status ------------------------------------------ */
    public ArrayList<Appointment> getAppointmentsByStatus(ArrayList<Appointment> appointments, AppointmentStatus status) {
        ArrayList<Appointment> appointmentByStatus = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentStatus() == status) {
                appointmentByStatus.add(appointment);
            }
        }
        return appointmentByStatus;
    }
        

    public ArrayList<Appointment> getAppointmentByDate(ArrayList<Appointment> appointments, Calendar cal) {
        ArrayList<Appointment> appointmentByDate = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(cal.getTime()) || appointment.getDate().after(cal.getTime())) {
                appointmentByDate.add(appointment);
            }
        }
        return appointmentByDate;
    }
}