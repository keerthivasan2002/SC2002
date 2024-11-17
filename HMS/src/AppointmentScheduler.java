import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentScheduler {
    private ArrayList<Appointment> appointments;
    
    private AppointmentValidator av;
    private AppointmentStorage as;
    // private AppointmentFilter af;
    private AppointmentLookup al;
    // private ScheduleManager s;

    //initialize the date and time format
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public AppointmentScheduler( AppointmentValidator appointmentValidator) {
        this.av = appointmentValidator; ;
        this.as = new AppointmentStorage();
        // this.af = new AppointmentFilter();
        this.al = new AppointmentLookup();
        // this.s = new ScheduleManager();

        this.appointments = as.getAppointments();
    }


    
    // Add a new appointment
    public boolean addAppointment(Patient patient, Staff doctor, Date date, Time startTime, Time endTime) {
        System.out.println("Scheduling appointment for Patient ID: " + patient.getPatientID() + ", Doctor ID: " + doctor.getUserID() + ", Date: " + date + ", Start Time: " + startTime + ", End Time:" + endTime);

        // if (av.patientIsMatch(patientID, doctorID, date, startTime, endTime) ||
        //         av.doctorAndTimeMatch(doctorID, date, startTime, endTime) ||
        //         av.isDuplicateInCSV(patientID, doctorID, date, startTime, endTime)) { // Check CSV for duplicates
        //     System.out.println("Appointment already exists.");
        //     return false;
        // } 
        
        if (av.checkAppointmentConflict(patient, doctor, date, startTime, endTime)) {
            System.out.println("Appointment slot is already occupied.");
            return false;
        }        
        else {
            int appointmentID = appointments.size() + 1;
            Appointment newAppointment = new Appointment(patient.getPatientID(), doctor.getUserID(), date, startTime, endTime);
            appointments.add(newAppointment);
            System.out.println("Appointment scheduled with ID: " + newAppointment.getAppointmentID());

            // Save the new appointment to the file
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String[] appointment = new String[]{
                    String.valueOf(appointmentID),
                    patient.getPatientID(), 
                    doctor.getUserID(),   
                    dateFormat.format(date),
                    timeFormat.format(startTime),
                    timeFormat.format(endTime),
                    "PENDING", 
                    "NULL"
            };

            as.addAppointmentToCSV(appointment);
            return true;
        }
    }



public boolean rescheduleAppointment(int appointmentID, Date newDate, Time newStartTime, Time newEndTime) {
    Appointment appointment = al.findAppointmentByID(appointmentID);
    if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
        Staff doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        if (av.checkAppointmentConflict(patient, doctor, newDate, newEndTime, newEndTime)){
            System.out.println("Appointment slot is already occupied.");
            return false;
        }

        // // Check if the doctor or patient has an existing appointment at the new time slot
        // if (av.doctorAndTimeMatch(doctorID, newDate, newStartTime, newEndTime) ||
        //         av.patientIsMatch(appointment.getPatient().getUserID(), doctorID, newDate, newStartTime, newEndTime)) {
        //     System.out.println("Appointment slot is already occupied.");
        //     return false;
        // }


        // // Check for any existing conflicting appointment for the doctor or patient at the new time slot
        // if (av.doctorAndTimeMatch(doctorID, newDate, newStartTime, newEndTime) ||
        //         av.patientIsMatch(appointment.getPatient().getUserID(), doctorID, newDate, newStartTime, newEndTime) ||
        //         av.isDuplicateInCSV(appointment.getPatient().getUserID(), doctorID, newDate, newStartTime, newEndTime)) {
        //     System.out.println("Appointment slot is already occupied by you.");
        //     return false;
        // }

        // Update appointment details
        appointment.setDate(newDate);
        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);

        // displayAppointment(appointments);

        // System.out.println("Appointment ID " + appointmentID + " rescheduled.");
        // System.out.println("New Date: " + newDate + ", New Start Time: " + newStartTime + ", New End Time: " + newEndTime);
        as.saveAppointments();

        return true;
    } else {
        System.out.println("Appointment not found or already canceled.");
        return false;
    }
}



    //change the existing appointemnt's status to cancelled
    public boolean cancelAppointment(int appointmentID) {
        Appointment appointment = al.findAppointmentByID(appointmentID);
        if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
            appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
            // schedule.freeTimeSlot(appointment.getDate(),appointment.getTime());
            System.out.println("Appointment ID " + appointmentID + " has been canceled.");
            as.saveAppointments();
            return true;
        } else {
            System.out.println("Appointment not found or already canceled.");
            return false;
        }
    }

    //update the outcome of an appointment
    public boolean updateAppointmentOutcome(int appointmentID, String outcome) {
        Appointment appointment = al.findAppointmentByID(appointmentID);
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