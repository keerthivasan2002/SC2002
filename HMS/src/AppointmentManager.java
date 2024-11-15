

/*This class manage multiple appointment instances. it acts as a centralized controller
 * that handles the scheduling, approval, cancellation, and tracking of appointments.
 * This class typically includes:
 * 1. Adding new appointments
 * 2. Removing existing appointments
 * 3. Check available time slots
 * 4. Allowing doctor to review and approve or reject appointment requests
 * 5. Allowing patients to view, schedule, reschedule, or cancel appointments
 * 6. Providing a list of appointments for a specific date, doctor, or patient
 * 7. Record the outcome of an appointment
 * 8. view a list of upcoming appointments for doctors and patients
 * 9.
 *
 */

import java.io.File;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class AppointmentManager {
    private ArrayList<Appointment> appointments;
    private Schedule schedule;
    private String appointment_File = "Appointment_List.csv";
    private static ArrayList <Patient> patients = new ArrayList<>();
    private static ArrayList <Staff> staffList = new ArrayList<>();

    public AppointmentManager() {
        this.appointments = new ArrayList<>();
        initializeAppointments();
    }


    public AppointmentManager(Schedule schedule) {
        this.appointments = new ArrayList<>();
        this.schedule = schedule;
        initializeAppointments();
    }

    /* ------------------------------------------------- Start Initialization Function ------------------------------------------ */
    public void initializeAppointments() {
        FileManager appointmentFileManager = new FileManager(appointment_File);
        String[][] appointmentArray = appointmentFileManager.readFile();
        patients = PatientManager.getPatients();
        staffList = StaffManager.getStaffList();


        //debug purpose
        // if (patients == null) {
        //     System.out.println("Failed to load patient data.[AppointmentManager]");
        // }else {
        //     // PatientManager patientManager = new PatientManager();
        //     // patientManager.displayPatient();
        //     System.out.println("Patient data loaded successfully.[AppointmentManager]");
        // }

        // if (staffList == null) {
        //     System.out.println("Failed to load staff data. [AppointmentManager]");
        // }else{
        //     // StaffManager staffManager = new StaffManager();
        //     // staffManager.displayStaffMembers();
        //     System.out.println("Staff data loaded successfully. [AppointmentManager]");
        // }

        if (appointmentArray == null || appointmentArray.length == 0) {
            System.out.println("Failed to load appointment data.[AppointmentManager]");
            return;
        }

        SimpleDateFormat originalFormat = new SimpleDateFormat("d/M/yyyy");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the pattern as needed
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for (int i = 1; i < appointmentArray.length; i++) {
            String[] row = appointmentArray[i];
            if (row.length >= 7) {
                String appointmentID = row[0];
                String patientID = row[1];
                // System.out.println("I am here: " + patientID);
                String doctorID = row[2];

                Date date = null;
                Time time = null;

                // Parse date and time from the CSV file
                try {
                    // String dateInput = row[3].trim(); // Assuming date is in the third column
                    // date = originalFormat.parse(dateInput); // Assuming date is in the third column
                    // String formattedDate = dateFormat.format(date); // Format the date to desired format
                    // String formattedDate = dateFormat.format(dateInput); // Format the date to desired format
                    // row[3] = formattedDate; // Update the date in the record
                    date = dateFormat.parse(row[3]); // Convert String to Date
                    Date parsedTime = timeFormat.parse(row[4]);
                    time = new Time(parsedTime.getTime()); // Convert Date to Time
                } catch (ParseException e) {
                    System.out.println("Error parsing time: hey hey " + e.getMessage());
                    // Handle the error appropriately
                }
                AppointmentStatus status = AppointmentStatus.valueOf(row[5].toUpperCase()); // Assuming status is in the fifth column
                String outcome = row[6]; // Assuming outcome is in the sixth column

                Appointment appointment = new Appointment(patientID, doctorID, date, time);
                appointment.setAppointmentStatus(status);
                appointment.setOutcome(outcome);
                appointments.add(appointment);
            } else {
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row));
            }
            // For debug purposes [ensure file from appointment is read properly]
            // for (Appointment appointment : appointments) {
            //     System.out.println("Appointment ID: " + appointment.getAppointmentID());
            //     System.out.println("Patient ID: " + appointment.getPatient().getUserID());
            //     System.out.println("Doctor ID: " + appointment.getDoctor().getUserID());
            //     System.out.println("Date: " + appointment.getDate());
            //     System.out.println("Time: " + appointment.getTime());
            //     System.out.println("Status: " + appointment.getAppointmentStatus());
            //     System.out.println("Outcome: " + appointment.getOutcome());
            //     System.out.println("-----------------------------------");
            // }
        }
    }

    /* ------------------------------------------------- Start Scheduling Function ------------------------------------------ */

    public boolean patientIsMatch(String patientID, String doctorID, Date date, Time time) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID) && appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }

    public boolean doctorAndTimeMatch(String doctorID, Date date, Time time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }

    // Add a new appointment
    public boolean addAppointment(String patientID, String doctorID, Date date, Time time) {
        System.out.println("Scheduling appointment for Patient ID: " + patientID + ", Doctor ID: " + doctorID + ", Date: " + date + ", Time: " + time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        int appointmentID = appointments.size() + 1;

        if (patientIsMatch(patientID, doctorID, date, time) == true || doctorAndTimeMatch(doctorID, date, time) == true) {
            System.out.println("Appointment already exists.");
            return false;
        }else {
            Appointment newAppointment = new Appointment(patientID, doctorID, date, time);
            appointments.add(newAppointment);
            System.out.println("Appointment scheduled with ID: " + newAppointment.getAppointmentID());

             // Save the new appointment to the file
             String[] appointment = new String[]{String.valueOf(appointmentID), patientID, doctorID, dateFormat.format(date), timeFormat.format(time), "PENDING", "NULL"};
             FileManager appointmentFM = new FileManager(appointment_File);
             appointmentFM.addNewRow(appointment);
            
            return true;
        }

        // if (schedule.isTimeSlotAvailable(date, time)) {
        //     Appointment newAppointment = new Appointment(patientID, doctorID, date, time);
        //     appointments.add(newAppointment);
        //     schedule.bookTimeSlot(date, time); // Mark the slot as booked
        //     System.out.println("Appointment scheduled with ID: " + newAppointment.getAppointmentID());

        //     // Save the new appointment to the file
        //     String[] appointment = new String[]{String.valueOf(appointmentID), patientID, doctorID, dateFormat.format(date), timeFormat.format(time), "PENDING", "NULL"};
        //     FileManager appointmentFM = new FileManager(appointment_File);
        //     appointmentFM.addNewRow(appointment);

        //     return true;
        // } else {
        //     System.out.println("Selected time slot is not available.");
        //     return false;
        // }

        
    }


    //reschedule an existing appointment
    public boolean rescheduleAppointment(int appointmentID, Date newDate, Time newTime) {
        Appointment appointment = findAppointmentByID(appointmentID);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
            String doctorID = appointment.getDoctor().getUserID();

            try{
                String dateString =  appointment.getStringDate();
                String timeString = appointment.getStringTime();

                Date oldDate = dateFormat.parse(dateString);
                Date praseTime = timeFormat.parse(timeString);
                Time oldTime = new Time(praseTime.getTime());

                
            } catch (ParseException e) {
                System.out.println("Error parsing date: " + e.getMessage());
                return false;
            }

            if (doctorAndTimeMatch(doctorID, newDate, newTime) == true) {
                System.out.println("Appointment already occupied.");
                return false;
            }else {
                appointment.setDate(newDate);
                appointment.setTime(newTime);
                System.out.println("Appointment ID " + appointmentID + " rescheduled.");
                System.out.println("New Date: " + newDate + ", New Time: " + newTime);
                saveAppointments();
                
                return true;
            }

            // // Check availability for the new time slot
            // if (schedule.isTimeSlotAvailable(newDate, newTime)) {
            //     // Update appointment and free the old slot
            //     schedule.freeTimeSlot(oldDate, oldTime);
            //     appointment.setDate(newDate);
            //     appointment.setTime(newTime);
            //     schedule.bookTimeSlot(newDate, newTime);
            //     System.out.println("Appointment ID " + appointmentID + " rescheduled.");
            //     System.out.println("New Date: " + newDate + ", New Time: " + newTime);
            //     saveAppointments();
            //     return true;
            // } else {
            //     System.out.println("New time slot is not available.");
            //     return false;
            // }
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

    public static Patient findPatientByID(String patientID) {
        for (Patient patient : patients) {
            if (patient.getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null; // Return null if patient not found
    }

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
        ArrayList appointmentByDoctorID = new ArrayList();
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
        // Get the current date without time (using LocalDate)
        LocalDate currentDate = LocalDate.now();
        // System.out.println("Current Date: " + currentDate);
        
        //get the current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // String currentTime = sdf.format(cal.getTime());
        String currentTime = "12:00";
        // System.out.println("Current Time: " + currentTime);

        ArrayList upcomingAppointments = new ArrayList<>();
    
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
            // System.out.println("Appointment Doctor ID: " + appointment.getDoctor());
            // System.out.println("compare with : " + doctor);
            if (appointment.getDoctor().getUserID().equalsIgnoreCase(doctor.getUserID())) {
                // Convert appointment date to LocalDate for comparison
                // System.out.println("i am here2");
                LocalDate appointmentDate = appointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                // System.out.println("Appointment Date: " + appointmentDate);
                // System.out.println("Current Date: " + currentDate);
                // Compare if the appointment date is in the future or today
                if (appointmentDate.isAfter(currentDate) || appointmentDate.isEqual(currentDate)) {
                    // System.out.println("Upcoming Appointment Date: " + appointmentDate);

                    // Check if the appointment time is after the current time
                    if (appointmentTimeIsAfterCurrentTime(appointment, currentTime)) {
                        upcomingAppointments.add(appointment);
                    }
                    // upcomingAppointments.add(appointment);
                }
            }
        }

        //debug purpose
        // System.out.println("Display before filter out the status of the appointment");
        //Display the upcoming appointments
        // displayAppointment(upcomingAppointments);

        // Filter out the appointments that are not confirmed
        ArrayList confirmedAppointments = getAppointmentsByStatus(upcomingAppointments, AppointmentStatus.CONFIRMED);
        // System.out.println("Display after filter out the status of the appointment");
        
        
        displayAppointment(confirmedAppointments);

    }

    //check if the appointment time is after the current time
    public boolean appointmentTimeIsAfterCurrentTime(Appointment appointment, String currentTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            Date appointmentTime = timeFormat.parse(appointment.getStringTime());
            Date currentTimeDate = timeFormat.parse(currentTime);
            return appointmentTime.after(currentTimeDate);
        } catch (ParseException e) {
            System.out.println("[appointmentTimeIsAfterCurrentTime] Error parsing time: " + e.getMessage());
            return false;
        }
    }
    /* ---------------------------------------- End Saving Upcoming Appointments Function ------------------------------------------ */

    //save all the upcoming appointments for a patient
    public List<Appointment> getUpcomingAppointmentsForPatient(Patient patient) {
        List<Appointment> upcomingAppointments = new ArrayList<>();
        Date currentDate = new Date();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patient) && appointment.getDate().after(currentDate)) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }

    //display all the appointments based on the selected patientID
    public ArrayList<Appointment> viewPatientAppointments(String patientID) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();

        System.out.println("\nAppointments for Patient ID: " + patientID);
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
            if (appointment.getUserID().equalsIgnoreCase(doctorID)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }
    

    //save the appointment based on patientID
    public ArrayList<Appointment> getAppointmentsByPatientID(String patientID) {
        ArrayList<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientID().equalsIgnoreCase(patientID)) {
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
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-10s %-15s %-15s%n",
                "Appointment ID", "Patient ID", "Doctor ID", "Date", "Time", "Status", "Outcome");
        System.out.println("------------------------------------------------------------------------------------------------------");

        // Print each appointment's details in a formatted manner
        for (Appointment appointment : filteredAppointments) {
            System.out.printf("%-15s %-15s %-15s %-15s %-10s %-15s %-15s%n",
                    appointment.getAppointmentID(),
                    appointment.getPatientID(),
                    appointment.getUserID(),
                    appointment.getStringDate(),
                    appointment.getStringTime(),
                    appointment.getAppointmentStatus(),
                    appointment.getOutcome() != null ? appointment.getOutcome() : "N/A");
        }
    }
}



}