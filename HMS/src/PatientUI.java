import java.sql.Time;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class PatientUI {
    private String userID;
    private Patient patient;
    private PatientManager pm; //keep reference to a PatientManager
    private MedicalRecordManager mrm;
    private ScheduleManager scheduleManager;
    private AppointmentManager appointmentManager;
    private ArrayList<Appointment> Appointment_Record;

    private Scanner sc = new Scanner(System.in);

    public PatientUI(String userID, PatientManager pm, MedicalRecordManager mrm, ScheduleManager scheduleManager, AppointmentManager am){
        this.userID = userID;
        this.pm = pm;
        this.mrm = mrm;
        this.patient = pm.selectPatient(userID);
        this.scheduleManager = scheduleManager;
        
        this.appointmentManager = am;

        //Handling errors with the code
        if(this.patient == null){
            System.out.println("No patient found with the given ID:" + userID);
        }else{
            patientOption();
        }
    }

    // Function to get the option from the user
    public int getOption(){
        int option = 0;
        boolean valid = false;
        Scanner sc = new Scanner(System.in);
        while (!valid){
            try{
                System.out.println("Enter your option from 1 to 9: ");
                option = sc.nextInt();
                System.out.println("You entered: " + option);
                if (option < 0){
                    throw new IntNonNegativeException();
                }else if (option == 0 || option > 9){
                    throw new InvalidPositiveOptionException();
                }else {
                    valid = true;
                }
            }catch (IntNonNegativeException e){
                //Handle negative numbers
                System.out.println(e.getMessage());
                option = getOption();
            }catch (InvalidPositiveOptionException e){
                //Handle invalid positive numbers
                System.out.println(e.getMessage());
                option = getOption();
            }catch (Exception e){
                //handle non integer numebr
                System.out.println("Invalid input. Please enter a valid number.");
                // option = getOption();
            }
        }
        return option;
        
    }

    public int getOptionAgain(){
        int option = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your option: ");
        option = sc.nextInt();

        if (option < 0){
            System.out.println("Error: your option MUST NOT be negative number!");
            System.out.println("You are out! ");
            System.out.println("Program Terminating! ");
            System.exit(0);
        }else if (option == 0 || option > 9){
            System.out.println("Error: your option MUST be between 1 to 9!");
            System.out.println("You are out! ");
            System.out.println("Program Terminating! ");
            System.exit(0);
        }else{
            System.out.println("Error: your are idiot!");
            System.out.println("You are out! ");
            System.out.println("Program Terminating! ");
            System.exit(0);
        }
        return option;
    }

    public void patientOption(){
        int choice = -1;

        System.out.println("Hello " + patient.getName() + ".");
        System.out.println("What would you like to do today?");
        while (true){
            patientMenu();
            choice = getOption();
            switch (choice){
                case 1: //view medical record
                    System.out.println("===============================");
                    System.out.println("Viewing Medical Records");
                    System.out.println("===============================");
                    viewMedicalRecords();
                    break;
                case 2: //update Personal information
                    patientInfo();
                    updatePatientInfo();
                    break;
                case 3: //View Available appointment Slots
                    // viewAvailableAppointmentSlots();
                    break;
                case 4: //Schedule an Appointment
                    requestNewAppointment();
                    break;
                case 5: //Reschedule an Appointment
                    rescheduleAppointment();
                    break;
                case 6: //Cancel an Appointment
                    cancelAppointment();
                    break;
                case 7: //View Scheduled Appointment
                    viewAppointments();
                    break;
                case 8: //View Past Appointment Records
                    viewAppointmentOutcome();
                    break;
                case 9://logout
                    System.out.println("Thank you! Hope to see you soon :) \n");
                    System.exit(0);
                    return;
                default: 
                    break;
            }

        }
    }

    // Function to view medical records
    private void viewMedicalRecords() {
        ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(userID);
        if (records.isEmpty()) {
            System.out.println("No medical records found for the patient.");
        } else {
            for (MedicalRecord record : records) {
                System.out.println("Diagnosis Date: " + record.getStringDateOfDiagnosis());
                System.out.println("Diagnosis: " + record.getDiagnosis());
                System.out.println("Prescription: " + record.getPrescription());
                System.out.println("Prescription Status: " + (record.isPrescriptionStatus() ? "Approved" : "Not Approved"));
                System.out.println("-----------------------------------");
            }
        }
    }

    //create a function for the patient menu
    private void patientMenu(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Patient Menu:");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Prescription");
        System.out.println("8. View Past Appointments Outcome Records");
        System.out.println("9. Logout");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.print("Enter your choice: ");
    }

    //method for testing purposes
    private void patientInfo() {
        System.out.println("Patient Information: ");
        System.out.println("Account type: " + patient.getrole());
        System.out.println("User ID: " + patient.getUserID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Blood Type: " + patient.getBloodType());
        System.out.println("Email Address: " + patient.getEmailAddress());
        System.out.println("Phone Number: " + patient.getPhoneNumber());
    }

    private void updatePatientInfo(){
        int changeChoice;
        System.out.println("Which information would you like to change?");
        System.out.println("1. Change Email Address");
        System.out.println("2. Change Phone Number");
        System.out.println("3. Change Password");
        System.out.println("Press any other number to exit.");

        changeChoice = sc.nextInt();
        sc.nextLine(); // Clear the buffer

        while (changeChoice > 0 && changeChoice < 4) {
            switch (changeChoice) {
                case 1:
                    System.out.print("Please enter your new email: ");
                    String newEmail = sc.nextLine();
                    patient.updateEmailAddress(newEmail);
                    System.out.println("Updated Email: " + patient.getEmailAddress());
                    break;

                case 2:
                    System.out.print("Please enter your new phone number: ");
                    int newPhoneNumber = sc.nextInt();
                    sc.nextLine();
                    patient.updatePhoneNumber(newPhoneNumber);
                    System.out.println("Updated Phone Number: " + patient.getPhoneNumber());
                    break;

                case 3:
                    System.out.print("Enter your current password for verification: ");
                    String verify = sc.nextLine();
                    if (verify.equals(patient.getPassword())) {
                        System.out.print("Enter new password: ");
                        String newPassword = sc.nextLine();
                        patient.updatePassword(newPassword);
                        System.out.println("Password updated successfully.");
                    } else {
                        System.out.println("Incorrect password. Please try again.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Returning to the main menu.");
                    break;
            }

            // Save the updated patient list to the file
            pm.savePatients();
            System.out.println("Information updated and saved successfully.");

            // Ask if they want to continue updating
            System.out.println("\nWould you like to update more information?");
            System.out.println("1. Change Email Address");
            System.out.println("2. Change Phone Number");
            System.out.println("3. Change Password");
            System.out.println("Press any other number to exit.");
            changeChoice = sc.nextInt();
            sc.nextLine(); // Clear the buffer
        }
    }

    //request new appointment
    private void requestNewAppointment() {
        
        System.out.println("Add Appointment");
        System.out.println("Enter the following details to schedule an appointment:");
        System.out.print("Doctor ID: ");
        String doctorID = sc.nextLine();

        Date date = null;
        Time time = null;

        System.out.print("Date (yyyy-MM-dd): ");
        String dateInput = sc.next().trim();
        System.out.print("Time (HH:mm): ");
        String timeInput = sc.next().trim();

        try{            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(dateInput);
            
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date parsedTime = timeFormat.parse(timeInput);
            time = new Time(parsedTime.getTime());

            boolean success = appointmentManager.addAppointment(userID, doctorID, date, time);
            
            if (success) {
                System.out.println("Appointment scheduled successfully.");
            } else {
                System.out.println("Failed to schedule appointment. Please try another time slot.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date or time format. Please try again. \n" + e.getMessage());
            sc.nextLine(); // Clear the buffer
            requestNewAppointment();
        }
        sc.nextLine(); // Clear the buffer
    }

    //reschedule appointment
    private void rescheduleAppointment() {
        System.out.print("Enter Appointment ID to Reschedule: ");
        int appointmentID = sc.nextInt();
        sc.nextLine(); // Consume newline

        Date newDate = null;
        Time newTime = null;

        System.out.print("Enter New Appointment Date (yyyy-MM-dd): ");
        String dateInput = sc.next();
        System.out.print("Enter New Appointment Time (HH:mm): ");
        String timeInput = sc.next();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            newDate = dateFormat.parse(dateInput);
            
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date parsedTime = timeFormat.parse(timeInput);
            newTime = new Time(parsedTime.getTime());

            boolean success = appointmentManager.rescheduleAppointment(appointmentID, newDate, newTime);
            if (success) {
                System.out.println("Appointment rescheduled successfully.");
            } else {
                System.out.println("Failed to reschedule appointment. New time slot may not be available.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date or time format. Please try again.");
            sc.nextLine(); // Clear the buffer
            rescheduleAppointment();
        }
        System.out.println("End of appointment rescheduled .");
        sc.nextLine(); // Clear the buffer
    }

    //cancel appointment
    private void cancelAppointment() {
        System.out.print("Enter Appointment ID to Cancel: ");
        int appointmentID = sc.nextInt();
        sc.nextLine(); // Consume newline

        boolean success = appointmentManager.cancelAppointment(appointmentID);
        if (success) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Failed to cancel appointment. Appointment may not exist or is already canceled.");
        }
        sc.nextLine(); // Clear the buffer
    }

    protected void viewAppointments() {
        System.out.println("Viewing Appointments");
        
        ArrayList<Appointment> Appointment_Record =  appointmentManager.viewPatientAppointments(userID);
        if (Appointment_Record.size() == 0) {
            System.out.println("No appointments found for patient. " + userID);
            return;
        }
        for (Appointment appointment : Appointment_Record) {
            System.out.println("Appointment ID: " + appointment.getAppointmentID());
            System.out.println("Doctor: " + appointment.getDoctor());
            System.out.println("Date: " + appointment.getStringDate());
            System.out.println("Time: " + appointment.getStringTime());
            System.out.println("Status: " + appointment.getAppointmentStatus());
            System.out.println("Outcome: " + appointment.getOutcome());
            System.out.println("-----------------------------------");
        }
        System.out.println("End of Appointments");
    }

    protected void viewAppointmentOutcome() {
        System.out.print("Enter Appointment ID to view outcome: ");
        int appointmentID = sc.nextInt();
        sc.nextLine(); // Consume newline
    
        Appointment appointment = appointmentManager.findAppointmentByID(appointmentID);
        if (appointment != null && appointment.getPatient().getPatientID().equals(userID)) {
            String outcome = appointment.getOutcome();
            System.out.println("Appointment Outcome: " + (outcome.isEmpty() ? "No outcome recorded" : outcome));
        } else {
            System.out.println("Appointment not found or not authorized to view.");
        }

        sc.nextLine(); // Clear the buffer
    }
}
