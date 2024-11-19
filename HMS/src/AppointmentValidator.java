import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentValidator {
    private ArrayList<Appointment> appointments;
    private AppointmentStorage as;

    // initialize thedate and time format
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public AppointmentValidator(AppointmentStorage as) {
        if (as == null) {
            throw new IllegalArgumentException("AppointmentStorage cannot be null");
        }
        System.out.println("AppointmentValidator received AppointmentStorage: [AppointmentValidator]" + (as != null));
    
        this.as = as;
        System.out.println("AppointmentValidator initialized with a valid AppointmentStorage.");

        this.appointments = as.getAppointments();
        // if (this.appointments == null) {
        //     this.appointments = new ArrayList<>(); // Initialize if null
        // }
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
    public boolean patientIDExists(Patient patient) {
        System.out.println("Patient ID: " + patient.getPatientID());
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getPatientID().equals(patient.getPatientID())) {
                return true;
            }
        }
        return false;
    }
    /* ---------------------------------------- Check Date (After Current Date) ------------------------------------------ */
    public boolean afterDate(Appointment appointment, Calendar cal) {
        return (appointment.getDate().after(cal.getTime()) || appointment.getDate().equals(cal.getTime())) ? true : false;
    }

    /* ---------------------------------------- Check Time (After Current ) ------------------------------------------ */
    //check if the appointment time is after the current time
    public boolean afterTime(Appointment appointment, Calendar cal) {
        return (appointment.getStartTime().after(cal.getTime()) || appointment.getStartTime().equals(cal.getTime())) ? true : false;
    }
    /* ---------------------------------------- Check Date (After Current Date) ------------------------------------------ */
    // Helper method to check if two dates are on the same day
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isSameTime(Time time1, Time time2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(time1);
        cal2.setTime(time2);
        return cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
                cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE);
    }


    /* ---------------------------------------- Check Patient don't double schedule ------------------------------------------ */
    //checking all possible conflicts that might arise in the appointmentvalidator
    public boolean checkAppointmentConflict(Patient patient, Staff doctor, Date date, Time startTime, Time endTime){
        // If there are no appointments, no conflict exists
        if(appointments == null || appointments.isEmpty()){
            return false;
        }
    
        for(Appointment appointment: appointments){
            // Check if the appointment is on the same day
            if(isSameDay(appointment.getDate(), date)){
                // Check for overlapping times
                if(timesOverlap(appointment.getStartTime(), appointment.getEndTime(), startTime, endTime)){
                    // Conflict exists if the doctor is the same or the patient has another appointment
                    if(appointment.getDoctor().getUserID().equalsIgnoreCase(doctor.getUserID()) ||
                       appointment.getPatient().getPatientID().equalsIgnoreCase(patient.getPatientID())){
                        return true;
                    }
                }
            }
        }
    
        return false; // No conflict found
    }
    
    // Helper method to determine if two time periods overlap
    private boolean timesOverlap(Time existingStart, Time existingEnd, Time newStart, Time newEnd){
        return newStart.before(existingEnd) && existingStart.before(newEnd);
    }
    

        // //ensure it checks the csv also for duplicates
        // for (int i = 1; i < appointments.size(); i++) {
        //     String[] row = appointments.get(i).toArray();

        //     // Check if all relevant fields match
        //     if (row[1].equals(patientID) && row[2].equals(doctorID) &&
        //             row[3].equals(dateStr) && row[4].equals(startTimeStr) && row[5].equals(endTimeStr)) {
        //         return true; // Duplicate found
        //     }

        // }


     // No duplicate found


    /* ---------------------------------------- Check AppointmentID ------------------------------------------ */
    

//Extra function for debug purposes

    //    /* ---------------------------------------- Check Patient don't double schedule ------------------------------------------ */
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
   //this is to check if the appointmentValidator has any duplicates
   //maybe can remove
   public boolean isDuplicateInCSV(String patientID, String doctorID, Date date, Time startTime, Time endTime) {
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


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


    public boolean appointmentAlreadyCompletedOrCancelled(int appointmentID) {
        AppointmentLookup al = new AppointmentLookup(as);
        Appointment appointment = al.findAppointmentByID(appointmentID);
        if (appointment != null && (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED || appointment.getAppointmentStatus() == AppointmentStatus.REJECTED || appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean appointmentAlreadyHasOutcome(int appointmentID) {
        AppointmentLookup al = new AppointmentLookup(as);
        Appointment appointment = al.findAppointmentByID(appointmentID);
        if (appointment.getOutcome() != "NULL" || appointment.getOutcome() != "N/A") {
            return false;
        } else {
            return true;
        }
    }
}