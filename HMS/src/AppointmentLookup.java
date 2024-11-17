import java.util.ArrayList;

public class AppointmentLookup {
    private ArrayList<Appointment> appointments;

    private AppointmentStorage as;

    private ArrayList <Patient> patients = new ArrayList<>();
    private ArrayList <Staff> staffList = new ArrayList<>();

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
    public Patient findPatientByID(String patientID) {
        for (Patient patient : patients) {
            if (patient.getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null; // Return null if patient not found
    }

    // find staff by staff ID
    public Staff findStaffByID(String staffID) {
        for (Staff staff : staffList) {
            if (staff.getUserID().equals(staffID)) {
                return staff;
            }
        }
        return null; // Return null if staff not found
    }

    // find minimum appointment ID
    public int getFirstAppointmentID(ArrayList<Appointment> filteredappointments){
        int firstAppointmentID = getLastAppointmentID(filteredappointments);
        for (Appointment appointment : filteredappointments){
            if (appointment.getAppointmentID() < firstAppointmentID){
                firstAppointmentID = appointment.getAppointmentID();
            }
        }
        // System.out.println("First Appointment ID: " + firstAppointmentID);
        return firstAppointmentID;
    }

    // find maximum appointment ID
    public int getLastAppointmentID(ArrayList<Appointment> appointments) {
        int lastAppointmentID = 0;
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() > lastAppointmentID) {
                lastAppointmentID = appointment.getAppointmentID();
            }
        }
        // System.out.println("Last Appointment ID: " + lastAppointmentID);
        return lastAppointmentID;
    }

    // ensure user key in the correct appointment ID
    public int getValidAppointmentID(ArrayList<Appointment> filteredAppointments) {
        OptionHandling oh = new OptionHandling();
        int selectedAppointmentID = -1;
        boolean valid = false;

        while (!valid) {
            selectedAppointmentID = oh.getOption(0, Integer.MAX_VALUE);

            if (selectedAppointmentID == 0) {
                return -1; // Return -1 to indicate user wants to go back to the main menu
            }
            // Check if the entered appointment ID is in the filtered list
            for (Appointment appointment : filteredAppointments) {
                if (appointment.getAppointmentID() == selectedAppointmentID) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                System.out.println("Invalid appointment ID. Please enter a valid appointment ID from the list.");
            }
        }

        return selectedAppointmentID;
    }

}