import java.util.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppointmentManager {
    //initialising the interface 
    private AppointmentStorage appointmentStorage; //this is to store the data
    private AppointmentScheduler appointmentScheduler;
    private AppointmentValidator appointmentValidator;
    private AppointmentFilter appointmentFilter;
    private AppointmentLookup appointmentLookup;

    //initialising the variables
    private ArrayList<Appointment> appointments;
    private String appointment_File = "Appointment_List.csv";
    private ScheduleManager scheduleManager;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    // //constructor for appointmentManager
    // public AppointmentManager() {
    //     this.appointmentStorage = new AppointmentStorage();
    //     this.appointmentValidator = new AppointmentValidator(appointmentStorage);
    //     this.appointmentScheduler = new AppointmentScheduler(appointmentValidator,appointmentStorage);
    //     this.appointmentFilter = new AppointmentFilter(appointmentStorage, appointmentValidator);
    //     this.appointmentLookup = new AppointmentLookup(appointmentStorage);
    //     // displayAppointment(appointments);
    // }

    public AppointmentManager(AppointmentStorage appointmentStorage, AppointmentValidator appointmentValidator){
       if (appointmentStorage == null) {
            throw new IllegalArgumentException("AppointmentStorage cannot be null");
        }
        if (appointmentValidator == null) {
            throw new IllegalArgumentException("AppointmentValidator cannot be null");
        }
        this.appointmentStorage  = appointmentStorage;
        this.appointmentValidator = appointmentValidator;
        this.appointmentFilter = new AppointmentFilter(appointmentStorage,appointmentValidator);
        this.appointmentLookup = new AppointmentLookup(appointmentStorage);
        this.appointmentScheduler = new AppointmentScheduler(appointmentValidator,appointmentStorage, appointmentLookup);
        // System.out.println("AppointmentManager received AppointmentStorage: [AppointmentManager]" + (appointmentStorage != null));
        // System.out.println("AppointmentManager received AppointmentValidator: [AppointmentManager]" + (appointmentValidator != null));
        // System.out.println("AppointmentFilter initialized with valid dependencies.");
        this.appointments = appointmentStorage.getAppointments();

    }

   



    // //POSSIBLE NEW CONSTRUCTOR
    // public AppointmentManager(AppointmentStorage appointmentStorage, AppointmentScheduler appointmentScheduler, AppointmentValidator appointmentValidator, AppointmentFilter appointmentFilter, AppointmentLookup appointmentLookup){
    //     this.appointmentStorage  = appointmentStorage;
    //     this.appointmentScheduler = appointmentScheduler;
    //     this.appointmentValidator = appointmentValidator;
    //     this.appointmentFilter = appointmentFilter;
    //     this.appointmentLookup = appointmentLookup;
    // }

    public void initializeAppointments() {
        appointmentStorage.loadAppointments();
    }

    //using a schedule manager
    public void setScheduleManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager; // Set scheduleManager after both objects are created
    }

    /* ---------------------------------------- Start Scheduling Function ------------------------------------------ */
    public boolean addAppointment(Patient patient, Staff doctor, Date date, Time startTime, Time endTime) {
        if (appointmentScheduler == null) {
            throw new IllegalStateException("AppointmentScheduler is not initialized[AppointmentManager][addAppointment]");
        }
        return appointmentScheduler.addAppointment(patient, doctor, date, startTime, endTime);
    }

    public boolean rescheduleAppointment(int appointmentID, Date newDate, Time newStartTime, Time newEndTime) {
        if (appointmentScheduler == null) {
            throw new IllegalStateException("AppointmentScheduler is not initialized[AppointmentManager][rescheduleAppointment]");
        }
        return appointmentScheduler.rescheduleAppointment(appointmentID, newDate, newStartTime, newEndTime);
    }

    public boolean cancelAppointment(int appointmentID) {
        if (appointmentScheduler == null) {
            throw new IllegalStateException("AppointmentScheduler is not initialized[AppointmentManager][cancelAppointment]");
        }
        return appointmentScheduler.cancelAppointment(appointmentID);
    }

    public boolean updateAppointmentOutcome(int appointmentID, String outcome) {
        if (appointmentScheduler == null) {
            throw new IllegalStateException("AppointmentScheduler is not initialized[AppointmentManager][updateAppointmentOutcome]");
        }
        return appointmentScheduler.updateAppointmentOutcome(appointmentID, outcome);
    }

    /* ---------------------------------------- End Scheduling Function ------------------------------------------ */

    /* ---------------------------------------- Start Look Up Function ------------------------------------------ */
    public Staff findStaffByID(String staffID) {
        if (appointmentLookup == null) {
            throw new IllegalStateException("AppointmentLookup is not initialized[AppointmentManager][findStaffByID]");
        }
        return appointmentLookup.findStaffByID(staffID);
    }

    public Patient findPatientByID(String patientID) {
        if (appointmentLookup == null) {
            throw new IllegalStateException("AppointmentLookup is not initialized[AppointmentManager][findPatientByID]");
        }
        return appointmentLookup.findPatientByID(patientID);
    }

    public Appointment findAppointmentByID(int id) {
        if (appointmentLookup == null) {
            throw new IllegalStateException("AppointmentLookup is not initialized[AppointmentManager][findAppointmentByID]");
        }
        return appointmentLookup.findAppointmentByID(id);
    }

    public int getFirstAppointmentID(ArrayList<Appointment> filteredAppointments) {
        if (appointmentLookup == null) {
            throw new IllegalStateException("AppointmentLookup is not initialized[AppointmentManager][getFirstAppointmentID]");
        }
        return appointmentLookup.getFirstAppointmentID(filteredAppointments);
    }

    public int getLastAppointmentID(ArrayList<Appointment> appointments) {
        if (appointmentLookup == null) {
            throw new IllegalStateException("AppointmentLookup is not initialized[AppointmentManager][getLastAppointmentID]");
        }
        return appointmentLookup.getLastAppointmentID(appointments);
    }

    public int getValidAppointmentID(ArrayList<Appointment> filteredAppointments) {
        if (appointmentLookup == null) {
            throw new IllegalStateException("AppointmentLookup is not initialized[AppointmentManager][getValidAppointmentID]");
        }
        return appointmentLookup.getValidAppointmentID(filteredAppointments);
    }
    /* ---------------------------------------- End Look Up Function ------------------------------------------ */

    /* ---------------------------------------- Start Filtering Function ------------------------------------------ */
    public ArrayList<Appointment> getPatientAppointments(Patient patient, int status) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null[AppointmentManager][getPatientAppointments]");
        }
        return appointmentFilter.getPatientAppointments(patient, status);
    }
    public ArrayList<Appointment> getDoctorAppointments(Staff doctor, int status) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null[AppointmentManager][getDoctorAppointments]");
        }
        return appointmentFilter.getDoctorAppointments(doctor, status);
    }

    public ArrayList<Appointment> getAppointmentsByStatus(ArrayList<Appointment> appointments, AppointmentStatus status) {
        if (appointments == null) {
            throw new IllegalArgumentException("Appointments cannot be null[AppointmentManager][getAppointmentsByStatus]");
        }
        return appointmentFilter.getAppointmentsByStatus(appointments, status);
    }

    public ArrayList<Appointment> getAppointmentByDate(ArrayList<Appointment> appointments, Calendar cal) {
        if (appointments == null) {
            throw new IllegalArgumentException("Appointments cannot be null[AppointmentManager][getAppointmentByDate]");
        }
        return appointmentFilter.getAppointmentByDate(appointments, cal);
    }

    /* ---------------------------------------- End Filtering Function ------------------------------------------ */

    /* ---------------------------------------- Start Validator Function ------------------------------------------ */
    public boolean checkAppointmentConflict(Patient patient, Staff doctor, Date date, Time startTime, Time endTime) {
        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient and Doctor cannot be null[AppointmentManager][checkAppointmentConflict]");
        }
        return appointmentValidator.checkAppointmentConflict(patient, doctor, date, startTime, endTime);
    }

    public boolean appointmentAlreadyCompletedOrCancelled(int appointmentID) {
        if (appointmentID <= 0) {
            throw new IllegalArgumentException("Invalid appointment ID[AppointmentManager][appointmentAlreadyCompletedOrCancelled]");
        }
        return appointmentValidator.appointmentAlreadyCompletedOrCancelled(appointmentID);
    }

    public boolean appointmentAlreadyHasOutcome(int appointmentID) {
        if (appointmentID <= 0) {
            throw new IllegalArgumentException("Invalid appointment ID[AppointmentManager][appointmentAlreadyHasOutcome]");
        }
        return appointmentValidator.appointmentAlreadyHasOutcome(appointmentID);
    }
    /* ---------------------------------------- End Validator Function ------------------------------------------ */

    /* ---------------------------------------- Start Storage Function ------------------------------------------ */
    public void saveAppointments() {
        if (appointmentStorage == null) {
            throw new IllegalStateException("AppointmentStorage is not initialized[AppointmentManager][saveAppointments]");
        }
        appointmentStorage.saveAppointments();
    }

    public void addAppointmentToCSV(String[] appointment) {
        if (appointmentStorage == null) {
            throw new IllegalStateException("AppointmentStorage is not initialized[AppointmentManager][addAppointmentToCSV]");
        }
        appointmentStorage.addAppointmentToCSV(appointment);
    }

    /* ---------------------------------------- Start Appointment Request Function ------------------------------------------ */

    public void appointmentRequest(Staff doctor){
        ArrayList<Appointment> appointmentByDoctorID = appointmentFilter.getDoctorAppointments(doctor, 1);
        appointmentByDoctorID = appointmentFilter.getAppointmentsByStatus(appointmentByDoctorID, AppointmentStatus.PENDING);
        // for (Appointment appointment : appointments){
        //     if (appointment.getDoctor().getUserID().equals(doctor.getUserID())){
        //         if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING){
        //             appointmentByDoctorID.add(appointment);
        //         }
        //     }
        // }
        if (appointmentByDoctorID.isEmpty()){
            System.out.println("No pending appointment request found.");
            return;
        }
        displayAppointment(appointmentByDoctorID);

        while(true){
            System.out.println("Enter the appointment ID you want to approve or reject: ");
            System.out.println("Enter 0 to go back to the main menu.");
            int selectedAppID = getValidAppointmentID(appointmentByDoctorID);
            if (selectedAppID == -1) {
                break; // Exit the loop if user wants to go back to the main menu
            }
            Appointment selectedAppointment = appointmentLookup.findAppointmentByID(selectedAppID);
            if (selectedAppointment == null) {
                System.out.println("Appointment not found. Please enter a valid appointment ID.");
                continue; // Restart the loop for a valid appointment ID
            }
            System.out.println("Do you want to approve or reject the appointment request?");
            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            OptionHandling oh = new OptionHandling();
            int choice = oh.getOption(1, 3);

            if (choice == 3) {
                break; // Exit to the main menu
            } else if (choice == 1) {
                approveAppointmentRequest(selectedAppointment, true);
                System.out.println("Appointment request approved.");
            } else if (choice == 2) {
                approveAppointmentRequest(selectedAppointment, false);
                System.out.println("Appointment request rejected.");
            }
            displayAppointment(appointmentByDoctorID);
        }
    }

    // Approve or reject an appointment request
    public void approveAppointmentRequest(Appointment appointment, boolean isApproved) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null[AppointmentManager][approveAppointmentRequest]");
        }
        if (isApproved) {
            appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
        } else {
            appointment.setAppointmentStatus(AppointmentStatus.REJECTED);
        }
        appointmentStorage.setAppointment(appointments);
        appointmentStorage.saveAppointments();
    }
    /* ---------------------------------------- End Appointment Request Function ------------------------------------------ */

    /* ---------------------------------------- Start Saving Upcoming Appointments Function ------------------------------------------ */
    //save all the upcoming appointments for a doctor
    public void getUpcomingAppointmentsForDoctor(Staff doctor) {
        //get the current time
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null[AppointmentManager][getUpcomingAppointmentsForDoctor]");
        }
        Calendar cal = Calendar.getInstance();
        String currentTimeString = timeFormat.format(cal.getTime());
        String currentDateString = dateFormat.format(cal.getTime());

        //debug purpose
        System.out.println("Current Date: " + currentDateString);
        System.out.println("Current Time: " + currentTimeString);
        
        ArrayList<Appointment> upcomingAppointments = new ArrayList<>();
        upcomingAppointments = appointmentFilter.getDoctorAppointments(doctor, 1);
        upcomingAppointments = appointmentFilter.getAppointmentsByStatus(upcomingAppointments, AppointmentStatus.CONFIRMED);
        displayAppointment(upcomingAppointments);
    }
    /* ---------------------------------------- End Saving Upcoming Appointments Function ------------------------------------------ */


    /* ---------------------------------------- Start Display Function ------------------------------------------ */

    // Display a list of appointments in a tabular format
    public void displayAppointment(ArrayList<Appointment> filteredAppointments) {
        // System.out.println("-----------------------------------");
        // System.out.println("Appointments: ");
        // System.out.println("-----------------------------------");

        if (filteredAppointments.isEmpty()) {
            System.out.println("No appointments found.[AppointmentManager][displayAppointment]");
            return;
        } else {
            // Print table headers
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-15s %-15s %-15s %-15s %-10s %-10s %-15s %-15s%n",
                    "Appointment ID", "Patient ID", "Doctor ID", "Date", "Time", "End time","Status", "Outcome");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");

            // Print each appointment's details in a formatted manner
            for (Appointment appointment : filteredAppointments) {
                System.out.printf("%-15s %-15s %-15s %-15s %-10s %-10s %-15s %-15s%n",
                        appointment.getAppointmentID(),
                        appointment.getPatient().getPatientID(),
                        appointment.getDoctor().getUserID(),
                        appointment.getStringDate(),
                        appointment.getStringStartTime(),
                        appointment.getStringEndTime(),
                        appointment.getAppointmentStatus(),
                        appointment.getOutcome() != null ? appointment.getOutcome() : "N/A");
            }
        }
    }

    // In AppointmentManager
    public List<Appointment> getAppointments(){
        return appointments;
    }


    //tester function
    public void printAllAppointmentsFromCSV() {
        FileManager appointmentFileManager = new FileManager(appointment_File);
        String[][] appointmentArray = appointmentFileManager.readFile();

        if (appointmentArray == null || appointmentArray.length == 0) {
            System.out.println("No appointments found in the CSV file."); //check if there are appointments
            return;
        }

        // Print the header row (assuming the first row contains headers)
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-40s%n",
                "Appointment ID", "Patient ID", "Doctor ID", "Date", "Start Time", "End Time", "Status", "Outcome");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        // Loop through each row and print appointment details
        for (int i = 1; i < appointmentArray.length; i++) {  // Start from 1 if the first row is a header
            String[] row = appointmentArray[i];
            if (row.length >= 8) {  // Adjust based on expected columns
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-40s%n",
                        row[0], // Appointment ID
                        row[1], // Patient ID
                        row[2], // Doctor ID
                        row[3], // Date
                        row[4], // Start Time
                        row[5], // End Time
                        row[6], // Status
                        row[7] != null ? row[7] : "N/A" // Outcome, handle null values
                );
            }
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }



    public void displayAppointmentsDateAndTime(ArrayList<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        } else {
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Doctor not available at the following date and time: ");
            System.out.printf("%-20s %-20s %-20s %-20s%n",
                    "Doctor ID", "Date", "Start Time", "End Time");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            for (Appointment appointment : appointments) {
                System.out.printf("%-20s %-20s %-20s %-20s%n",
                appointment.getDoctor().getUserID(),
                appointment.getStringDate(),
                appointment.getStringStartTime(),
                appointment.getStringEndTime());
            }
        }
    }

    /* ---------------------------------------- End Display Function ------------------------------------------ */

}