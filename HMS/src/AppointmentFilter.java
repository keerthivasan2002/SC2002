import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentFilter {
    private ArrayList<Appointment> appointments;

    private AppointmentStorage as;
    private AppointmentValidator av;;

    Calendar cal = Calendar.getInstance();


    public AppointmentFilter(AppointmentStorage as, AppointmentValidator av) {
        if (as == null) {
            throw new IllegalArgumentException("AppointmentStorage cannot be null");
        }
        if (av == null) {
            throw new IllegalArgumentException("AppointmentValidator cannot be null");
        }
    
        this.as = as;
        this.av = av;
        this.appointments = as.getAppointments();
        if (this.appointments == null) {
            System.out.println("AppointmentStorage returned null. Initializing an empty list.[AppointmentFilter]");
            this.appointments = new ArrayList<>();
        }
        // System.out.println("AppointmentFilter initialized with valid AppointmentStorage: [AppointmentFilter]" + (as != null));
        // System.out.println("AppointmentFilter initialized with valid AppointmentValidator:  [AppointmentFilter] " + (av != null));
        // System.out.println("AppointmentFilter initialized with " + this.appointments.size() + " appointments.");

    }
    
    /* ---------------------------------------- Filter by PatientID ------------------------------------------ */
    public ArrayList<Appointment> getPatientAppointments(Patient patient, int  showTypeOfAppointments) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();
        
         // Ensure that appointments is not null
         if (appointments == null) {
            System.out.println("Appointments list is null. Initializing to an empty list.[AppointmentFilter]");
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
    public ArrayList<Appointment> getDoctorAppointments(Staff doctor, int showTypeOfAppointments) {
        ArrayList<Appointment> doctorAppointments = new ArrayList<>();
        
        // Ensure that appointments is not null
        if (appointments == null) {
            System.out.println("Appointments list is null. Initializing to an empty list.[AppointmentFilter]");
            appointments = new ArrayList<>();
        }
        
        for (Appointment appointment : appointments) {
            if (doctor.getUserID().equalsIgnoreCase(appointment.getDoctor().getUserID())) {
                switch (showTypeOfAppointments) {
                    case 0:
                        doctorAppointments.add(appointment);
                        break;
                    case 1:
                        if (appointment.getDate().after(cal.getTime())) {
                            doctorAppointments.add(appointment);
                        }
                        break;
                    case -1:
                        if (appointment.getDate().before(cal.getTime())) {
                            doctorAppointments.add(appointment);
                        }
                        break;
                    default:
                        break;
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