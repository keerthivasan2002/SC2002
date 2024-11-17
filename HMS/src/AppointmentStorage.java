import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AppointmentStorage{
    private ArrayList<Appointment> appointments = new ArrayList<>();
    private String appointment_File = "Appointment_List.csv";
    
    FileManager appointmentFileManager = new FileManager(appointment_File);
    
    // initialize thedate and time format
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public AppointmentStorage() {
        appointments = new ArrayList<>();
    }   

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    // Read from CSV file to memory
    public ArrayList<Appointment> loadAppointments() {
        appointments.clear(); // Clear existing appointments before loading new ones
        String[][] appointmentArray = appointmentFileManager.readFile();
    
        if (appointmentArray == null || appointmentArray.length == 0) {
            System.out.println("Failed to load appointment data.[AppointmentStorage]");
            // return appointments; // Return an empty list instead of null
        }
    
        for (int i = 1; i < appointmentArray.length; i++) {
            String[] row = appointmentArray[i];
            if (row.length > 7) {
                int appointmentID = Integer.parseInt(row[0]);
                // System.out.println("I am here: " + appointmentID);
                String patientID = row[1];
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
                    endTime = new Time(parsedEndTime.getTime());
                } catch (ParseException e) {
                    System.out.println("Error parsing time: [Appointment Strorage]" + e.getMessage());
                    // Skip this row if there's an error in parsing
                    continue;
                }
    
                AppointmentStatus status = AppointmentStatus.valueOf(row[6].toUpperCase()); 
                String outcome = row[7];
    
                Appointment appointment = new Appointment(patientID, doctorID, date, startTime, endTime);
                appointment.setAppointmentStatus(status);
                appointment.setOutcome(outcome);
                appointments.add(appointment);
            } else {
                System.out.println("Incomplete data in row, skipping: [AppointmentStorage] " + String.join(",", row));
            }
        }        
        return appointments;
    }
    

    //Write from meomory to CSV file
    public void saveAppointments() {
        // Header for CSV
        String[][] appointmentData = new String[appointments.size() + 1][7];
        appointmentData[0] = new String[]{"AppointmentID", "PatientID", "DoctorID", "Date", "Time", "Appointment Status", "Appointment Outcome"};

        for (int i = 0 ; i < appointments.size() ; i ++) {
            appointmentData[i + 1] = appointments.get(i).toArray();
        }

        //Write to file
        appointmentFileManager.writeFile(appointmentData, false);
    }


    // Add a new appointment to the CSV file
    public void addAppointmentToCSV(String[] appointment) {
        appointmentFileManager.addNewRow(appointment);
    }

    // Display a list of appointments in a tabular format
    public void displayAppointment(ArrayList<Appointment> filteredAppointments) {
        if (filteredAppointments.isEmpty()) {
            System.out.println("No appointments found. [AppointmentStorage]");
            return;
        } else {
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

    
}