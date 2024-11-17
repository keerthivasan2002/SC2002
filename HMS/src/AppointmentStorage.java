import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AppointmentStorage{
    private ArrayList<Appointment> appointments = new ArrayList<>();
    private String appointment_File = "Appointment_List.csv";
    
    FileManager appointmentFileManager = new FileManager(appointment_File);
    
    

    public ArrayList<Appointment> getAppointments() {
        String[][] appointmentArray = appointmentFileManager.readFile();
        if (appointmentArray == null || appointmentArray.length == 0) {
            System.out.println("Failed to load appointment data.[AppointmentStorage]");
            return null;
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
        return appointments;
    }

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
            appointmentData[i + 1] = appointments.get(i).toArray();
        }

        //Write to file
        appointmentFileManager.writeFile(appointmentData, false);
    }

}