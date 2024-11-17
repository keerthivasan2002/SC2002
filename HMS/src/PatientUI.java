import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.IllegalArgumentException;


public class PatientUI implements UserUI{
    private String userID;
    private Patient patient;
    private PatientManager pm; //keep reference to a PatientManager
    private MedicalRecordManager mrm;
    // private ScheduleManager scheduleManager;
    private AppointmentManager appointmentManager;

    private Scanner sc = new Scanner(System.in);
    OptionHandling oh = new OptionHandling();
    Calendar cal = Calendar.getInstance();


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public PatientUI(String userID, PatientManager pm, MedicalRecordManager mrm, ScheduleManager scheduleManager, AppointmentManager am){
        this.userID = userID;
        this.pm = pm;
        this.mrm = mrm;
        this.patient = pm.selectPatient(userID);
        // this.scheduleManager = scheduleManager;
        
        this.appointmentManager = am;

        //Handling errors with the code
        if(this.patient == null){
            System.out.println("No patient found with the given ID:" + userID);
        }else{
            userOption();
        }
    }

    public void userOption(){
        int choice = -1;

        System.out.println("Hello " + patient.getName() + ".");
        System.out.println("What would you like to do today?");
        while (true){
            patientMenu();
            choice = oh.getOption(1, 9);
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
                    viewAvailableAppointmentSlots();
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

    private void viewAvailableAppointmentSlots() {
        System.out.println("Each appointment booked is only limited to 30 minutes!");
        System.out.println("Enter the date to view available appointment slots");

        Calendar cal = Calendar.getInstance(); // Get today's date
        Date choiceOfDate = null;
        String doctorID = null;

        // Step 1: Get and validate the date input
        while(true){
            try{
                System.out.print("Date (yyyy-MM-dd): ");
                String dateInput = sc.next().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false); // Strict parsing
                choiceOfDate = dateFormat.parse(dateInput);
                Date today = cal.getTime();
                if (choiceOfDate.before(today) && !choiceOfDate.equals(today)){
                    throw new IllegalArgumentException("Date cannot be in the past.");
                }
                break; // Exit loop if date is valid
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        // Step 2: Get and validate the Doctor ID
        while(true){
            System.out.print("Enter Doctor ID to view available slots: ");
            doctorID = sc.next();
            Staff doctor = appointmentManager.findStaffByID(doctorID);
            if(doctor == null){
                System.out.println("Doctor ID not found. Please try again.");
            } else {
                break; // Exit loop if doctor exists
            }
        }

    }

    // Function to view medical records
    private void viewMedicalRecords() {
        ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(userID);
        mrm.displayMedicalRecords(records);
    }

    /* ---------------------------------------- Menu Function ------------------------------------------ */

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
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointments Outcome Records");
        System.out.println("9. Logout");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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

    //update personal information menu
    public void updatePatientMenu(){
        System.out.println("----------------------------------------------");
        System.out.println("Which information would you like to change?");
        System.out.println("1. Change Email Address");
        System.out.println("2. Change Phone Number");
        System.out.println("3. Change Password");
        System.out.println("4. Back to Main Menu.");
        System.out.println("----------------------------------------------");
    }

    /* ---------------------------------------- Update Personal Information Function ------------------------------------------ */
    private void updatePatientInfo(){
        int changeChoice = -1;
        while (changeChoice != 4) {
            updatePatientMenu();
            changeChoice = oh.getOption(1,4);
            switch (changeChoice) {
                case 1:
                    System.out.print("Please enter your new email: ");
                    String newEmail = pm.setPatientEmail();
                    patient.setEmailAddress(newEmail);
                    System.out.println("Updated Email: " + patient.getEmailAddress());
                    break;

                case 2:
                    System.out.print("Please enter your new phone number: ");
                    int newPhoneNumber = pm.setPatientPhoneNumber();
                    patient.setPhoneNumber(newPhoneNumber);
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
                case 4:
                    break;
                default:
                    System.out.println("Invalid choice. Returning to the main menu.");
                    break;
            }

            // Save the updated patient list to the file
            pm.savePatients();
            System.out.println("Information updated and saved successfully.");
            // sc.nextLine(); // Clear the buffer
        }
    }

    /* ---------------------------------------- Schedule Appointment Function ------------------------------------------ */
    private void requestNewAppointment() {
        
        System.out.println("Add Appointment");
        ArrayList<Appointment> appointment = appointmentManager.getPatientAppointments(patient,0);
        appointment = appointmentManager.getAppointmentByDate(appointment, cal);
        appointmentManager.displayAppointment(appointment);
        System.out.println("Enter the following details to schedule an appointment:");
        System.out.print("Doctor ID: ");
        String doctorID = sc.nextLine().toUpperCase();
        Staff doctor = appointmentManager.findStaffByID(doctorID);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        Time startTime = null;
        Time endTime = null;
        while (true){
            try{            
                System.out.print("Date (yyyy-MM-dd): ");
                String dateInput = sc.next().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date = dateFormat.parse(dateInput);
                if (date.before(cal.getTime()) && !date.equals(cal.getTime())) {
                    throw new IllegalArgumentException("Date cannot be in the past.");
                }
                
                System.out.print("Time (HH:mm): ");
                String timeInput = sc.next().trim();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date parsedTime = timeFormat.parse(timeInput);
                startTime = new Time(parsedTime.getTime());
                if (date.equals(cal.getTime()) && !parsedTime.before(cal.getTime())) {
                    throw new IllegalArgumentException("Time cannot be in the past.");
                }

                // Calculate endTime by adding 30 minutes to startTime
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedTime);
                calendar.add(Calendar.MINUTE, 30);
                endTime = new Time(calendar.getTimeInMillis());

                boolean success = appointmentManager.addAppointment(patient, doctor, date, startTime, endTime);
                if (success) {
                    System.out.println("Appointment scheduled successfully.");
                } else {
                    System.out.println("Failed to schedule appointment. Please try another time slot.");
                }
                break;
            }catch (IllegalArgumentException e){
                System.out.println("Invalid date or time format. Please try again. \n" + e.getMessage());
            }catch (ParseException e) {
                System.out.println("Invalid date or time format. Please try again. \n" + e.getMessage());
            }finally{
                System.out.println("End of appointment scheduling.");
                sc.nextLine(); // Clear the buffer
            }
        }
    }

    /* ---------------------------------------- Reschedule Appointment Function ------------------------------------------ */
    private void rescheduleAppointment() {
        ArrayList<Appointment> appointment = appointmentManager.getPatientAppointments(patient,0);
        appointmentManager.displayAppointment(appointment);
        System.out.print("Enter Appointment ID to Reschedule: ");
        int appointmentID = -1;
        while (true) {
            appointmentID = appointmentManager.getValidAppointmentID(appointment);
        
            if (appointmentID == -1) {
                System.out.println("No appointments found for patient. " + userID);
            } else if (appointmentManager.appointmentAlreadyCompletedOrCancelled(appointmentID)) {
                System.out.println("Appointment has already been completed or canceled or rejected. Please select another appointment.");
            } else {
                break;
            }
        }
        
        Calendar cal = Calendar.getInstance();
        Date newDate = null;
        Time newStartTime = null;
        Time newEndTime = null;

        
        while(true){
            try {
                System.out.print("Enter New Appointment Date (yyyy-MM-dd): ");
                String dateInput = sc.next();   
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                newDate = dateFormat.parse(dateInput);
                if (newDate.before(cal.getTime()) && !newDate.equals(cal.getTime())) {
                    throw new IllegalArgumentException("Date cannot be in the past.");
                }

                System.out.print("Enter New Appointment Time (HH:mm): ");
                String timeInput = sc.next();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date parsedTime = timeFormat.parse(timeInput);
                newStartTime = new Time(parsedTime.getTime());
                if (newDate.equals(cal.getTime()) && !parsedTime.before(cal.getTime())) {
                    throw new IllegalArgumentException("Time cannot be in the past.");
                }

                // Calculate newEndTime by adding 30 minutes to newStartTime
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedTime);
                calendar.add(Calendar.MINUTE, 30);
                newEndTime = new Time(calendar.getTimeInMillis());

                boolean success = appointmentManager.rescheduleAppointment(appointmentID, newDate, newStartTime, newEndTime);
                if (success) {
                    System.out.println("Appointment rescheduled successfully.");
                } else {
                    System.out.println("Failed to reschedule appointment. New time slot may not be available.");
                }
                break;
            }catch (IllegalArgumentException e){
                System.out.println("Invalid date or time format. Please try again. \n" + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Invalid date or time format. Please try again.");
            }finally{
                System.out.println("End of appointment reschedule.");
                sc.nextLine(); // Clear the buffer
            }

        }
    }

    /* ---------------------------------------- Cancel Appointment Function ------------------------------------------ */
    private void cancelAppointment() {
        ArrayList<Appointment> appointment = appointmentManager.getPatientAppointments(patient,0);
        appointmentManager.displayAppointment(appointment);
        System.out.print("Enter Appointment ID to Cancel: ");
        int appointmentID = appointmentManager.getValidAppointmentID(appointment);

        boolean success = appointmentManager.cancelAppointment(appointmentID);
        if (success) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Failed to cancel appointment. Appointment may not exist or is already canceled.");
        }
    }

    /* ---------------------------------------- View Scheduled Appointment Function ------------------------------------------ */
    protected void viewAppointments() {
        System.out.println("Viewing Appointments");
        System.out.println("Appointments found for patient. " + patient.getUserID());
        ArrayList<Appointment> appointment_record = appointmentManager.getPatientAppointments(patient, 1);
        if (appointment_record.isEmpty()) {
            System.out.println("No appointments found for patient. " + userID);
            return;
        }else {
            System.out.println("Appointments found for patient. " + userID);
        }
        appointmentManager.displayAppointment(appointment_record);
        System.out.println("End of Appointments");
    }

    /* ---------------------------------------- View Appointment Outcome Function ------------------------------------------ */
    protected void viewAppointmentOutcome() {
        System.out.println("Viewing Past Appointments Outcome Records");
        ArrayList<Appointment> appointment_record = appointmentManager.getPatientAppointments(patient, -1);
        if (appointment_record.isEmpty()) {
            System.out.println("No past appointments found for patient. " + userID);
            return;
        }else {
            System.out.println("Appointments found for patient. " + userID);
        }
        appointmentManager.displayAppointment(appointment_record);
        System.out.println("End of Appointments");
    }


}
