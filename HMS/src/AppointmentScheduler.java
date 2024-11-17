import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class AppointmentScheduler {
    private ArrayList<Appointment> appointments;
    private ArrayList<Appointment> appointmentsByStatus;
    
    private AppointmentValidator appointmentValidator;


    public AppointmentScheduler( AppointmentValidator appointmentValidator) {
        this.appointmentValidator = appointmentValidator; ;
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

    
}