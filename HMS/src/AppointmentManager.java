

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
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {
    private ArrayList<Appointment> appointments;
    private Schedule schedule;
    private String appointment_File = "Appointment_List.csv";


    public AppointmentManager(Schedule schedule) {
        this.appointments = new ArrayList<>();
        this.schedule = schedule;
        initializeAppointments();
    }

    public void initializeAppointments() {
        FileManager appointmentFileManager = new FileManager(appointment_File);
        String[][] appointmentArray = appointmentFileManager.readFile();

        if (appointmentArray == null || appointmentArray.length == 0) {
            System.out.println("Failed to load appointment data.");
            return;
        }

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
                    date = dateFormat.parse(row[3]); // Assuming date is in the third column
                    Date parsedTime = timeFormat.parse(row[4]);
                    time = new Time(parsedTime.getTime()); // Convert Date to Time
                } catch (ParseException e) {
                    System.out.println("Error parsing time: " + e.getMessage());
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
        
        if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELED) {
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

    // Remove an existing appointment
    public boolean cancelAppointment(int appointmentID) {
        Appointment appointment = findAppointmentByID(appointmentID);
        if (appointment != null && appointment.getAppointmentStatus() != AppointmentStatus.CANCELED) {
            appointment.setAppointmentStatus(AppointmentStatus.CANCELED);
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

    // Helper method to find an appointment by ID
    public Appointment findAppointmentByID(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == id) {
                return appointment;
            }
        }
        return null;
    }

    // Check available time slots
    public List<Time> getAvailableTimeSlots(Date date) {
        // Logic to check available time slots for a given date
        return null;
    }

    // Approve or reject an appointment request
    public void approveAppointmentRequest(Appointment appointment, boolean isApproved) {
        if (isApproved) {
            appointment.setAppointmentStatus(AppointmentStatus.APPROVED);
        } else {
            appointment.setAppointmentStatus(AppointmentStatus.REJECTED);
        }
    }

    // View a list of appointments for a specific date
    public List<Appointment> getAppointmentsForDate(Date date) {
        List<Appointment> appointmentsForDate = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date)) {
                appointmentsForDate.add(appointment);
            }
        }
        return appointmentsForDate;
    }

    // View a list of appointments for a specific doctor
    public List<Appointment> getAppointmentsForDoctor(Staff doctor) {
        List<Appointment> appointmentsForDoctor = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctor)) {
                appointmentsForDoctor.add(appointment);
            }
        }
        return appointmentsForDoctor;
    }

    // View a list of appointments for a specific patient
    public List<Appointment> getAppointmentsForPatient(Patient patient) {
        List<Appointment> appointmentsForPatient = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patient)) {
                appointmentsForPatient.add(appointment);
            }
        }
        return appointmentsForPatient;
    }

    // Record the outcome of an appointment
    public void recordAppointmentOutcome(Appointment appointment, MedicalRecord record) {
        appointment.setAppointmentRecord(record);
    }

    // View a list of upcoming appointments for a doctor
    public List<Appointment> getUpcomingAppointmentsForDoctor(Staff doctor) {
        List<Appointment> upcomingAppointments = new ArrayList<>();
        Date currentDate = new Date();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctor) && appointment.getDate().after(currentDate)) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }

    // View a list of upcoming appointments for a patient
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
}