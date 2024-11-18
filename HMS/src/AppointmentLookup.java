import java.util.ArrayList;

public class AppointmentLookup {
    private ArrayList<Appointment> appointments;
    private AppointmentStorage as;

    private static ArrayList <Patient> patients = new ArrayList<>();
    private static ArrayList <Staff> staffList = new ArrayList<>();
    private static boolean isInitialized = false; 

    public AppointmentLookup(AppointmentStorage as) {
        if (as == null) {
            throw new IllegalArgumentException("AppointmentStorage cannot be null[ AppointmentLookup ]");
        }
        this.as = as;
        this.appointments = as.getAppointments();
        
    }    

    private void initializePatientsAndStaff() {
        System.out.println("Initializing patients and staff lists... [ AppointmentLookup ]");

        // Use PatientManager to load patient data
        PatientManager patientManager = new PatientManager();
        ArrayList<Patient> loadedPatients = patientManager.getPatients();
        if (loadedPatients != null) {
            patients.addAll(loadedPatients);
            System.out.println("Loaded " + loadedPatients.size() + " patients.");
        } else {
            System.out.println("Failed to load patient data.[ AppointmentLookup ]");
        }

        // Use StaffManager to load staff data
        StaffManager staffManager = new StaffManager();
        ArrayList<Staff> loadedStaff = staffManager.getStaffList();
        if (loadedStaff != null) {
            staffList.addAll(loadedStaff);
            System.out.println("Loaded " + loadedStaff.size() + " staff members.");
        } else {
            System.out.println("Failed to load staff data.[ AppointmentLookup ]");
        }
    }

    // find appointment by ID
    public Appointment findAppointmentByID(int id) {
        if (appointments == null) {
            appointments = as.getAppointments(); // Ensure appointments are initialized
        }
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
                System.out.println("Patient found: " + patient.getPatientID());
                return patient;
            }
        }
        System.out.println("Patient not found.");
        return null; // Return null if patient not found
    }

    // find staff by staff ID
    public Staff findStaffByID(String staffID) {
        System.out.println("Staff ID: " + staffID);
    //     System.out.println("I am here okaaaaaayyy ");
    //     System.out.println("Size of staffList: " + staffList.size());

        for (Staff staff : as.getDoctors()) {
            // System.out.println("I am here okaaaaaayyy too");
            System.out.println("compare to Staff ID: " + staff.getUserID());
            if (staff.getUserID().equalsIgnoreCase(staffID)) {
                System.out.println("found staff " + staff.getUserID());
                return staff;
            }
        }
        // System.out.println("Staff not found");

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
                System.out.println("Invalid appointment ID. Please enter a valid appointment ID from the list.[AppointmentLookup]");
            }
        }

        return selectedAppointmentID;
    }

}