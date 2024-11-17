import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentValidator {
    private ArrayList<Appointment> appointments;

    // initialize thedate and time format
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public AppointmentValidator(AppointmentStorage appointmentStorage) {
        this.appointments = appointmentStorage.getAppointments(); // Get all appointments from the CSV file
    }

    /* ---------------------------------------- Check DoctorID ------------------------------------------ */
    public boolean doctorIDExists(Staff doctor) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getUserID().equalsIgnoreCase(doctor.getUserID())) {
                return true;
            }
        }
        return false;
    }

    /* ---------------------------------------- Check PatientID ------------------------------------------ */
    public boolean patientIDExists(String patientID) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID)) {
                return true;
            }
        }
        return false;
    }


    /* ---------------------------------------- Check Patient dont double schedule ------------------------------------------ */
    //ensure patient don't double book
    public boolean patientIsMatch(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getUserID().equals(patientID) && appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
                return true;
            }
        }
        return false;
    }

    /* ---------------------------------------- Check Doctor Availability ------------------------------------------ */
    //check doctor availability
    public boolean doctorAndTimeMatch(String doctorID, Date date, Time startTime, Time endTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
                return true;
            }
        }
        return false;
    }

    /* ---------------------------------------- Check Duplicate Appointment ------------------------------------------ */
    // Check if a similar appointment already exists in the CSV file
    public boolean isDuplicateInCSV(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
        

        String dateStr = dateFormat.format(date);
        String startTimeStr = timeFormat.format(startTime);
        String endTimeStr = timeFormat.format(endTime);

        if (appointments == null || appointments.size() == 0) {
            return false; // No records in the CSV
        }

        for (int i = 1; i < appointments.size(); i++) {
            String[] row = appointments.get(i).toArray();

            // Check if all relevant fields match
            if (row[1].equals(patientID) && row[2].equals(doctorID) &&
                    row[3].equals(dateStr) && row[4].equals(startTimeStr) && row[5].equals(endTimeStr)) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicate found
    }

    /* ---------------------------------------- Check AppointmentID ------------------------------------------ */
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
    /* ---------------------------------------- Check Date (After Current Date) ------------------------------------------ */
    public boolean appointmentDateIsAfterCurrentDate(Appointment appointment, Calendar cal) {
        return (appointment.getDate().after(currentDate) || appointment.getDate().equals(currentDate)) ? true : false;
    }

    /* ---------------------------------------- Check Time (After Current ) ------------------------------------------ */
    //check if the appointment time is after the current time
    public boolean appointmentTimeIsAfterCurrentTime(Appointment appointment, Calendar cal) {
        try {
            Date appointmentTime = timeFormat.parse(appointment.getStringStartTime());
            return appointmentTime.after(cal.getTime());
        } catch (ParseException e) {
            System.out.println("[appointmentTimeIsAfterCurrentTime] Error parsing time: " + e.getMessage());
            return false;
        }
    }
}