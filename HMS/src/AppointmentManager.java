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

    //constructor for appointmentManager
    public AppointmentManager() {
        this.appointmentStorage = new AppointmentStorage();
        this.appointmentValidator = new AppointmentValidator();
        this.appointmentScheduler = new AppointmentScheduler(appointmentValidator);
        this.appointmentFilter = new AppointmentFilter(appointmentStorage);
        this.appointmentLookup = new AppointmentLookup(appointmentStorage);
        // displayAppointment(appointments);
    }

    public AppointmentManager(AppointmentStorage appointmentStorage){
        this.appointmentStorage  = appointmentStorage;
        this.appointmentScheduler = new AppointmentScheduler(appointmentValidator);
        this.appointmentValidator = new AppointmentValidator();
        this.appointmentFilter = new AppointmentFilter(appointmentStorage);
        this.appointmentLookup = new AppointmentLookup(appointmentStorage);
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
        return appointmentScheduler.addAppointment(patient, doctor, date, startTime, endTime);
    }

    public boolean rescheduleAppointment(int appointmentID, Date newDate, Time newStartTime, Time newEndTime) {
        return appointmentScheduler.rescheduleAppointment(appointmentID, newDate, newStartTime, newEndTime);
    }

    public boolean cancelAppointment(int appointmentID) {
        return appointmentScheduler.cancelAppointment(appointmentID);
    }

    public boolean updateAppointmentOutcome(int appointmentID, String outcome) {
        return appointmentScheduler.updateAppointmentOutcome(appointmentID, outcome);
    }

    /* ---------------------------------------- End Scheduling Function ------------------------------------------ */

    /* ---------------------------------------- Start Look Up Function ------------------------------------------ */
    public Staff findStaffByID(String staffID) {
        return appointmentLookup.findStaffByID(staffID);
    }

    public Patient findPatientByID(String patientID) {
        return appointmentLookup.findPatientByID(patientID);
    }

    public Appointment findAppointmentByID(int id) {
        return appointmentLookup.findAppointmentByID(id);
    }

    public int getFirstAppointmentID(ArrayList<Appointment> filteredAppointments) {
        return appointmentLookup.getFirstAppointmentID(filteredAppointments);
    }

    public int getLastAppointmentID(ArrayList<Appointment> appointments) {
        return appointmentLookup.getLastAppointmentID(appointments);
    }

    public int getValidAppointmentID(ArrayList<Appointment> filteredAppointments) {
        return appointmentLookup.getValidAppointmentID(filteredAppointments);
    }
    /* ---------------------------------------- End Look Up Function ------------------------------------------ */

    /* ---------------------------------------- Start Filtering Function ------------------------------------------ */
    public ArrayList<Appointment> getPatientAppointments(Patient patient, int status) {
        return appointmentFilter.getPatientAppointments(patient, status);
    }
    public ArrayList<Appointment> getDoctorAppointments(Staff doctor, int status) {
        return appointmentFilter.getDoctorAppointments(doctor, status);
    }

    public ArrayList<Appointment> getAppointmentsByStatus(ArrayList<Appointment> appointments, AppointmentStatus status) {
        return appointmentFilter.getAppointmentsByStatus(appointments, status);
    }

    public ArrayList<Appointment> getAppointmentByDate(ArrayList<Appointment> appointments, Calendar cal) {
        return appointmentFilter.getAppointmentByDate(appointments, cal);
    }

    /* ---------------------------------------- End Filtering Function ------------------------------------------ */

    /* ---------------------------------------- Start Validator Function ------------------------------------------ */
    public boolean checkAppointmentConflict(Patient patient, Staff doctor, Date date, Time startTime, Time endTime) {
        return appointmentValidator.checkAppointmentConflict(patient, doctor, date, startTime, endTime);
    }

    public boolean appointmentAlreadyCompletedOrCancelled(int appointmentID) {
        return appointmentValidator.appointmentAlreadyCompletedOrCancelled(appointmentID);
    }

    public boolean appointmentAlreadyHasOutcome(int appointmentID) {
        return appointmentValidator.appointmentAlreadyHasOutcome(appointmentID);
    }
    /* ---------------------------------------- End Validator Function ------------------------------------------ */

    /* ---------------------------------------- Start Storage Function ------------------------------------------ */
    public void saveAppointments() {
        appointmentStorage.saveAppointments();
    }

    public void addAppointmentToCSV(String[] appointment) {
        appointmentStorage.addAppointmentToCSV(appointment);
    }

    /* ---------------------------------------- Start Appointment Request Function ------------------------------------------ */

    public void appointmentRequest(Staff doctor){
        ArrayList<Appointment> appointmentByDoctorID = appointmentFilter.getDoctorAppointments(doctor, 1);
        for (Appointment appointment : appointments){
            if (appointmentValidator.doctorIDExists(doctor)){
                if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING){
                    appointmentByDoctorID.add(appointment);
                }
            }
        }
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
        if (isApproved) {
            appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
        } else {
            appointment.setAppointmentStatus(AppointmentStatus.REJECTED);
        }
        appointmentStorage.saveAppointments();
    }
    /* ---------------------------------------- End Appointment Request Function ------------------------------------------ */

    /* ---------------------------------------- Start Saving Upcoming Appointments Function ------------------------------------------ */
    //save all the upcoming appointments for a doctor
    public void getUpcomingAppointmentsForDoctor(Staff doctor) {
        //get the current time
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
            System.out.println("No appointments found.");
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



    //Function to see the available appointment
    public void ViewAvailableTime(Date date){
        // boolean unavailableTimeSlot = false;
        for(Schedule schedule: scheduleManager.getSchedules()){
            if(schedule.getDate().equals(date)){

            }
        }

        for(Appointment appointment: appointments){
            if(appointment.getDate().equals(date)){
                System.out.println("");
            }
        }
    }

    /* ---------------------------------------- End Display Function ------------------------------------------ */

}