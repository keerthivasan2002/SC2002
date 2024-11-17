import java.util.ArrayList;

public class AppointmentLookup {
    private ArrayList<Appointment> appointments;

    private AppointmentStorage as;

    private static ArrayList <Patient> patients = new ArrayList<>();
    private static ArrayList <Staff> staffList = new ArrayList<>();

    public AppointmentLookup() {
        this.as = new AppointmentStorage();
        this.appointments = as.getAppointments();

    }    

    // find appointment by ID
    public Appointment findAppointmentByID(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == id) {
                return appointment;
            }
        }
        return null;
    }

    // find patient by patient ID
    public static Patient findPatientByID(String patientID) {
        for (Patient patient : patients) {
            if (patient.getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null; // Return null if patient not found
    }

    // find staff by staff ID
    public static Staff findStaffByID(String staffID) {
        for (Staff staff : staffList) {
            if (staff.getUserID().equals(staffID)) {
                return staff;
            }
        }
        return null; // Return null if staff not found
    }
}