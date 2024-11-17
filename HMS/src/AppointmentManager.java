import java.util.Date;
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
    private static ArrayList <Patient> patients = new ArrayList<>();
    private static ArrayList <Staff> staffList = new ArrayList<>();
    private ScheduleManager scheduleManager;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    //constructor for appointmentManager
    public AppointmentManager() {
        this.appointmentStorage = new AppointmentStorage();
        this.appointmentValidator = new AppointmentValidator();
        this.appointmentScheduler = new AppointmentScheduler(appointmentValidator);
        this.appointmentFilter = new AppointmentFilter();
        this.appointmentLookup = new AppointmentLookup();
        this.appointments = appointmentStorage.getAppointments(); //initialise the appointments
    }

    //using a schedule manager
    public void setScheduleManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager; // Set scheduleManager after both objects are created
    }

    
    /* ---------------------------------------- Start Appointment Request Function ------------------------------------------ */

    public void appointmentRequest(Staff doctor){
        ArrayList<Appointment> appointmentByDoctorID = new ArrayList();
        // System.out.println("i am here first ");
        for (Appointment appointment : appointments){
            if (appointment.getDoctor().getUserID().equalsIgnoreCase(doctor.getUserID())){
                // System.out.println("i am here second ");
                if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING){
                    // System.out.println("i am here third ");
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

    //do we need this??
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

    public boolean appointmentAlreadyHasOutcome(int appointmentID) {
        Appointment appointment = appointmentLookup.findAppointmentByID(appointmentID);
        if (appointment.getOutcome() != "NULL" || appointment.getOutcome() != "N/A") {
            return false;
        } else {
            return true;
        }
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

}