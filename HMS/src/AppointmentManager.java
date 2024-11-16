import java.sql.Time;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class AppointmentManager {
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
        this.appointments = new ArrayList<>();
    }

    //using a schedule manager
    public void setScheduleManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager; // Set scheduleManager after both objects are created
    }

    /* ------------------------------------------------- Start Initialization Function ------------------------------------------ */
    //Initialisation function to intialise the appointment array
    public void initializeAppointments() {
        FileManager appointmentFileManager = new FileManager(appointment_File);
        String[][] appointmentArray = appointmentFileManager.readFile();

        if (appointmentArray == null || appointmentArray.length == 0) {
            System.out.println("Failed to load appointment data.[AppointmentManager]");
            return;
        }

        for (int i = 1; i < appointmentArray.length; i++) {
            String[] row = appointmentArray[i];
            if (row.length > 7) {
                String appointmentID = row[0];
                String patientID = row[1];
                //System.out.println("I am here: " + patientID);
                String doctorID = row[2];

                Date date = null;
                Time startTime = null;
                Time endTime = null;

                // Parse date and time from the CSV file
                try {
                    date = dateFormat.parse(row[3]); // Convert String to Date
                    Date parsedStartTime = timeFormat.parse(row[4]);
                    Date parsedEndTime = timeFormat.parse(row[5]);

                    startTime = new Time(parsedStartTime.getTime());
                    endTime = new Time (parsedEndTime.getTime());
                } catch (ParseException e) {
                    System.out.println("Error parsing time: " + e.getMessage());
                    // Handle the error appropriately
                }
                AppointmentStatus status = AppointmentStatus.valueOf(row[6].toUpperCase()); // Assuming status is in the fifth column
                String outcome = row[7]; // Assuming outcome is in the sixth column

                Appointment appointment = new Appointment(patientID, doctorID, date, startTime, endTime);
                appointment.setAppointmentStatus(status);
                appointment.setOutcome(outcome);
                appointments.add(appointment);
            } else {
                System.out.println("Incomplete data in row, skipping: lol " + String.join(",", row));
            }

        }

    }

    /* ------------------------------------------------- Start Scheduling Function ------------------------------------------ */

    //ensure patient don't double book
    public boolean patientIsMatch(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID) && appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
                return true;
            }
        }
        return false;
    }

    //check doctor availability
    public boolean doctorAndTimeMatch(String doctorID, Date date, Time startTime, Time endTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
                return true;
            }
        }
        return false;
    }

    // Check if a similar appointment already exists in the CSV file
    public boolean isDuplicateInCSV(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
        FileManager appointmentFileManager = new FileManager(appointment_File);
        String[][] appointmentArray = appointmentFileManager.readFile();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String dateStr = dateFormat.format(date);
        String startTimeStr = timeFormat.format(startTime);
        String endTimeStr = timeFormat.format(endTime);

        if (appointmentArray == null || appointmentArray.length == 0) {
            return false; // No records in the CSV
        }

        for (int i = 1; i < appointmentArray.length; i++) {
            String[] row = appointmentArray[i];

            // Check if all relevant fields match
            if (row[1].equals(patientID) && row[2].equals(doctorID) &&
                    row[3].equals(dateStr) && row[4].equals(startTimeStr) && row[5].equals(endTimeStr)) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicate found
    }


    // Add a new appointment
    public boolean addAppointment(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
        System.out.println("Scheduling appointment for Patient ID: " + patientID + ", Doctor ID: " + doctorID + ", Date: " + date + ", Start Time: " + startTime + ", End Time:" + endTime);

        if (patientIsMatch(patientID, doctorID, date, startTime, endTime) ||
                doctorAndTimeMatch(doctorID, date, startTime, endTime) ||
                isDuplicateInCSV(patientID, doctorID, date, startTime, endTime)) { // Check CSV for duplicates
            System.out.println("Appointment already exists.");
            return false;
        } else {
            int appointmentID = appointments.size() + 1;
            Appointment newAppointment = new Appointment(patientID, doctorID, date, startTime, endTime);
            appointments.add(newAppointment);
            System.out.println("Appointment scheduled with ID: " + newAppointment.getAppointmentID());

            // Save the new appointment to the file
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String[] appointment = new String[]{
                    String.valueOf(appointmentID),
                    patientID, 
                    doctorID,
                    dateFormat.format(date),
                    timeFormat.format(startTime),
                    timeFormat.format(endTime),
                    "PENDING", 
                    "NULL"
            };

            FileManager appointmentFM = new FileManager(appointment_File);
            appointmentFM.addNewRow(appointment);
            return true;
        }
    }

public boolean appointmentAlreadyCompletedOrCancelled(int appointmentID) {
    Appointment appointment = findAppointmentByID(appointmentID);
    if (appointment != null && (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED || appointment.getAppointmentStatus() == AppointmentStatus.REJECTED || appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED)) {
        return true;
    } else {
        return false;
    }
}

public boolean rescheduleAppointment(int appointmentID, Date newDate, Time newStartTime, Time newEndTime) {
    Scanner scanner = new Scanner(System.in);
    Appointment appointment = findAppointmentByID(appointmentID);
    if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
        String doctorID = appointment.getDoctor().getUserID();

        // Check if the doctor or patient has an existing appointment at the new time slot
        if (doctorAndTimeMatch(doctorID, newDate, newStartTime, newEndTime) ||
                patientIsMatch(appointment.getPatient().getUserID(), doctorID, newDate, newStartTime, newEndTime)) {
            System.out.println("Appointment slot is already occupied.");
            return false;
        }

        // Check for any existing conflicting appointment for the doctor or patient at the new time slot
        if (doctorAndTimeMatch(doctorID, newDate, newStartTime, newEndTime) ||
                patientIsMatch(appointment.getPatient().getUserID(), doctorID, newDate, newStartTime, newEndTime) ||
                isDuplicateInCSV(appointment.getPatient().getUserID(), doctorID, newDate, newStartTime, newEndTime)) {
            System.out.println("Appointment slot is already occupied by you.");
            return false;
        }

        // Update appointment details
        appointment.setDate(newDate);
        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);

        // displayAppointment(appointments);

        // System.out.println("Appointment ID " + appointmentID + " rescheduled.");
        // System.out.println("New Date: " + newDate + ", New Start Time: " + newStartTime + ", New End Time: " + newEndTime);
        saveAppointments();

        return true;
    } else {
        System.out.println("Appointment not found or already canceled.");
        return false;
    }
}



    //change the existing appointemnt's status to cancelled
    public boolean cancelAppointment(int appointmentID) {
        Appointment appointment = findAppointmentByID(appointmentID);
        if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
            appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
            // schedule.freeTimeSlot(appointment.getDate(),appointment.getTime());
            System.out.println("Appointment ID " + appointmentID + " has been canceled.");
            saveAppointments();
            return true;
        } else {
            System.out.println("Appointment not found or already canceled.");
            return false;
        }
    }

    //update the outcome of an appointment
    public boolean updateAppointmentOutcome(int appointmentID, String outcome) {
        Appointment appointment = findAppointmentByID(appointmentID);
        if (appointment != null && appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
            appointment.setOutcome(outcome);
            System.out.println("Outcome updated for Appointment ID " + appointmentID);
            return true;
        } else {
            System.out.println("Appointment not found or not confirmed.");
            return false;
        }
    }
    /* ------------------------------------------------- End Scheduling Function ------------------------------------------ */

    /* ------------------------------------------------- Start Look up functions ---------------------------------------------- */

    // Helper method to find an appointment by ID
    public Appointment findAppointmentByID(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == id) {
                return appointment;
            }
        }
        return null;
    }

    //can this be removed???
    public static Patient findPatientByID(String patientID) {
        for (Patient patient : patients) {
            if (patient.getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null; // Return null if patient not found
    }

    //can this be removed??
    public static Staff findStaffByID(String staffID) {
        for (Staff staff : staffList) {
            if (staff.getUserID().equals(staffID)) {
                return staff;
            }
        }
        return null; // Return null if staff not found
    }
    /* ------------------------------------------------- End Look up functions ------------------------------------------ */

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
            Appointment selectedAppointment = findAppointmentByID(selectedAppID);
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
        saveAppointments();
    }
    /* ---------------------------------------- End Appointment Request Function ------------------------------------------ */

    /* ---------------------------------------- Start Saving Upcoming Appointments Function ------------------------------------------ */
    //save all the upcoming appointments for a doctor
    public void getUpcomingAppointmentsForDoctor(Staff doctor) {
        LocalDate currentDate = LocalDate.now();

        //get the current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = "12:00";

        ArrayList<Appointment> upcomingAppointments = new ArrayList<>();

        for (Appointment appointment : appointments) {
            System.out.println("Doctor ID: " + doctor.getUserID());
            if (appointment.getDate() == null){
                System.out.println("Skipping appointment with ID " + appointment.getAppointmentID() + " because the date is null.");
                continue;
            }
            if (appointment.getDoctor() == null){
                System.out.println("Skipping appointment with ID " + appointment.getAppointmentID() + " because the doctor is null.");
                continue;
            }
            if (appointment.getDoctor().getUserID().equalsIgnoreCase(doctor.getUserID())) {
                LocalDate appointmentDate = appointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (appointmentDate.isAfter(currentDate) || appointmentDate.isEqual(currentDate)) {

                    // Check if the appointment time is after the current time
                    if (appointmentTimeIsAfterCurrentTime(appointment, currentTime)) {
                        upcomingAppointments.add(appointment);
                    }
                }
            }
        }
        ArrayList<Appointment> confirmedAppointments = getAppointmentsByStatus(upcomingAppointments, AppointmentStatus.CONFIRMED);
        displayAppointment(confirmedAppointments);
    }

    //check if the appointment time is after the current time
    public boolean appointmentTimeIsAfterCurrentTime(Appointment appointment, String currentTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            Date appointmentTime = timeFormat.parse(appointment.getStringStartTime());
            Date currentTimeDate = timeFormat.parse(currentTime);
            return appointmentTime.after(currentTimeDate);
        } catch (ParseException e) {
            System.out.println("[appointmentTimeIsAfterCurrentTime] Error parsing time: " + e.getMessage());
            return false;
        }
    }
    /* ---------------------------------------- End Saving Upcoming Appointments Function ------------------------------------------ */



    //display all the appointments based on the selected patientID
    public ArrayList<Appointment> viewPatientAppointments(String patientID, boolean showPastAppointments) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        // System.out.println("\nAppointments for Patient ID: " + patientID);
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID)) {
                if (!showPastAppointments && appointment.getDate().after(cal.getTime())) {
                    patientAppointments.add(appointment);
                }else if (showPastAppointments && appointment.getDate().before(cal.getTime())){
                    patientAppointments.add(appointment);
                }
            }
        }
        return patientAppointments;
    }

    //display all the appointments based on the selected patientID
    public ArrayList<Appointment> viewPatientAppointments(String patientID) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        // System.out.println("\nAppointments for Patient ID: " + patientID);
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }

    //Write from meomory to CSV file
    public void saveAppointments() {
        // Save all appointments to a file
        FileManager appointmentFileManager = new FileManager(appointment_File);

        // Header for CSV
        String[][] appointmentData = new String[appointments.size() + 1][7];
        appointmentData[0] = new String[]{"AppointmentID", "PatientID", "DoctorID", "Date", "Time", "Appointment Status", "Appointment Outcome"};

        for (int i = 0 ; i < appointments.size() ; i ++) {
            // Appointment appointment = appointments.get(i);
            // appointmentData[i + 1][0] = String.valueOf(appointment.getAppointmentID());
            // appointmentData[i + 1][1] = appointment.getPatient().getUserID();
            // appointmentData[i + 1][2] = appointment.getDoctor().getUserID();
            // appointmentData[i + 1][3] = new SimpleDateFormat("yyyy-MM-dd").format(appointment.getDate());
            // appointmentData[i + 1][4] = new SimpleDateFormat("HH:mm").format(appointment.getTime());
            // appointmentData[i + 1][5] = appointment.getAppointmentStatus().toString();
            // appointmentData[i + 1][6] = appointment.getOutcome();

            appointmentData[i + 1] = appointments.get(i).toArray();
        }

        //Write to file
        appointmentFileManager.writeFile(appointmentData, false);
    }


    //save the appointment based on doctorID
    public ArrayList<Appointment> getAppointmentsByDoctorID(String doctorID) {
        ArrayList<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if ((appointment.getDoctor().getUserID()).equalsIgnoreCase(doctorID)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }


    //save the appointment based on patientID
    public ArrayList<Appointment> getAppointmentsByPatientID(String patientID) {
        ArrayList<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if ((appointment.getPatient().getPatientID()).equalsIgnoreCase(patientID)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }


    //save the apppointment based on the status
    public ArrayList<Appointment> getAppointmentsByStatus(ArrayList<Appointment> filteredAppointments, AppointmentStatus status) {
        ArrayList<Appointment> filteredStatusAppointments = new ArrayList<>();
        System.out.println("I am here teehee");
        for (Appointment appointment : filteredAppointments) {
            if (appointment.getAppointmentStatus() == status) {
                filteredStatusAppointments.add(appointment);
            }
        }
        return filteredStatusAppointments;
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
        Appointment appointment = findAppointmentByID(appointmentID);
        if (appointment.getOutcome() != "NULL" || appointment.getOutcome() != "N/A") {
            return false;
        } else {
            return true;
        }
    }

}