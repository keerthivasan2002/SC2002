import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentFilter {
    private ArrayList<Appointment> appointments;

    private AppointmentStorage as;
    private AppointmentValidator av = new AppointmentValidator();

    Calendar cal = Calendar.getInstance();


    public AppointmentFilter(AppointmentStorage as) {
        this.as = as;
        this.appointments = as.getAppointments();
        if (this.appointments == null) {
            this.appointments = new ArrayList<>();
        }

    }

    /* ---------------------------------------- Filter by PatientID ------------------------------------------ */
    public ArrayList<Appointment> getPatientAppointments(Patient patient, int  showTypeOfAppointments) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();
        
         // Ensure that appointments is not null
         if (appointments == null) {
            System.out.println("Appointments list is null. Initializing to an empty list.");
            appointments = new ArrayList<>();
        }
        
        for (Appointment appointment : appointments) {
            if (patient.getPatientID().equals(appointment.getPatient().getPatientID())) {
                switch (showTypeOfAppointments) {
                    case 0:
                        patientAppointments.add(appointment);
                        break;
                    case 1:
                        if (appointment.getDate().after(cal.getTime())) {
                            patientAppointments.add(appointment);
                        }
                        break;
                    case -1:
                        if (appointment.getDate().before(cal.getTime())) {
                            patientAppointments.add(appointment);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return patientAppointments;
    }



    /* ---------------------------------------- Filter by DoctorID ------------------------------------------ */
    public ArrayList<Appointment> getDoctorAppointments(Staff doctor, int showPastAppointments) {
        ArrayList<Appointment> doctorAppointments = new ArrayList<>();
        
        // Ensure that appointments is not null
        if (appointments == null) {
            System.out.println("Appointments list is null. Initializing to an empty list.");
            appointments = new ArrayList<>();
        }
        
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