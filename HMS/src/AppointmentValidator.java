import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentValidator {
    private ArrayList<Appointment> appointments;

    //constructor class for the appointment validator
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


    //checking all possible conflicts that might arise in the appointmentvalidator
    public boolean checkAppointmentConflict(String patientID, String doctorID, Date date, Time startTime, Time endTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String dateStr = dateFormat.format(date);
        String startTimeStr = timeFormat.format(startTime);
        String endTimeStr = timeFormat.format(endTime);


        //in the case there is no appointments to check against
        if(appointments == null || appointments.isEmpty()){
            return true;
        }

        //interate through the appointments to get the
        for(Appointment appointment: appointments){
            String appointmentDateStr = appointment.getStringDate(); //getting the string date
            String appointmentStartTime = appointment.getStringStartTime(); //getting the string start time
            String appointmentEndTime = appointment.getStringEndTime(); //getting the string end time

            //check if there are any conflicts in patientID
            if (appointment.getPatient().getUserID().equals(patientID) && appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
                return true;
           }

            //check if there are any conflicts doctor side
            if (appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
                return true;
            }

        }

        //ensure it checks the csv also for duplicates
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
        Date appointmentDate = appointment.getDate();
        return appointmentDate.after(cal.getTime()); //this is to get the date of it
    }

    /* ---------------------------------------- Check Time (After Current ) ------------------------------------------ */
    //check if the appointment time is after the current time
    public boolean appointmentTimeIsAfterCurrentTime(Appointment appointment, Calendar cal) {
        Date appointmentTime = appointment.getStartTime();
        return appointmentTime.after(cal.getTime());
    }

//Extra function for debug purposes

    //    /* ---------------------------------------- Check Patient don't double schedule ------------------------------------------ */
//    //ensure patient don't double book
//    public boolean patientIsMatch(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
//        for (Appointment appointment : appointments) {
//            if (appointment.getPatient().getUserID().equals(patientID) && appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /* ---------------------------------------- Check Doctor Availability ------------------------------------------ */
//    //check doctor availability
//    public boolean doctorAndTimeMatch(String doctorID, Date date, Time startTime, Time endTime) {
//        for (Appointment appointment : appointments) {
//            if (appointment.getDoctor().getUserID().equals(doctorID) && appointment.getDate().equals(date) && appointment.getStartTime().equals(startTime) && appointment.getEndTime().equals(endTime)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /* ---------------------------------------- Check Duplicate Appointment ------------------------------------------ */
//    //this is to check if the appointmentValidator has any duplicates
//    //maybe can remove
//    public boolean isDuplicateInCSV(String patientID, String doctorID, Date date, String startTime, Time endTime) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//
//
//        String dateStr = dateFormat.format(date);
//        String startTimeStr = timeFormat.format(startTime);
//        String endTimeStr = timeFormat.format(endTime);
//
//        if (appointments == null || appointments.size() == 0) {
//            return false; // No records in the CSV
//        }
//
//        for (int i = 1; i < appointments.size(); i++) {
//            String[] row = appointments.get(i).toArray();
//
//            // Check if all relevant fields match
//            if (row[1].equals(patientID) && row[2].equals(doctorID) &&
//                    row[3].equals(dateStr) && row[4].equals(startTimeStr) && row[5].equals(endTimeStr)) {
//                return true; // Duplicate found
//            }
//        }
//        return false; // No duplicate found
//    }
}