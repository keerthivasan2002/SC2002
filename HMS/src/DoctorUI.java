import java.sql.Time;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DoctorUI {
    protected String userID;
    protected Staff doctor;
    protected StaffManager sm;
    protected Patient patient;
    protected MedicalRecordManager mrm;
    protected ScheduleManager scheduleManager;
    private AppointmentManager am;

    Scanner sc = new Scanner(System.in);


    public DoctorUI(String userID, StaffManager sm, MedicalRecordManager mrm, AppointmentManager am, ScheduleManager scheduleManager){
        this.userID = userID;
        this.sm = sm;
        this.doctor = sm.selectStaff(userID);
        this.mrm = mrm;
        this.am = am;

        
        this.scheduleManager = scheduleManager;
        //Handling errors with the code
        if(this.doctor == null){
            System.out.println("No doctors found with the given ID:" + userID);
        }else{
            doctorOption();
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
                //handle non integer number
                System.out.println("Invalid input. Please enter a valid number.");
                option = getOption();
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
        }else if (option == 0 || option > 8){
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

    public void doctorOption(){
        int choice;
        System.out.println("Hello " + doctor.getName() + ".");
        System.out.println("What would you like to do today?");
        while(true){
            doctorMenu();
            choice = getOption();
            // System.out.println("I am here " );
            switch (choice){
                case 1: //view Patient Medical Record
                    System.out.println("===============================");
                    System.out.println("View Patient Medical Record");
                    System.out.println("===============================");
                    viewPatientMedicalRecord();
                    break;
                case 2: //Update patient Medical Record
                    System.out.println("Update Patient Medical Record");
                    updatePatientMedicalRecord();
                    break;
                case 3: //view Personal Schedule
                    System.out.println("View Personal Schedule");
                    personalSchedule();
                    break;
                case 4: //Set availability for appointment
                    updateSchedule();
                    break;
                case 5: //Accept or Decline Appointment Request
                    System.out.println("Accept or Decline Appointment Request");
                    
                    am.appointmentRequest(doctor);

                    break;
                case 6: //View Upcoming Appointment
                    am.getUpcomingAppointmentsForDoctor(doctor);
                    break;
                case 7: //Record Appointment Outcome
                    recordAppointmentOutcome();
                    break;
                case 8: //Log out
                    System.out.println("Thank you! Hope to see you soon :)\n");
                    System.exit(0);
                    return;
                default:
                    break;
            }
        }
    }


    private void doctorMenu(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Doctor Menu:");
        System.out.println("1. View Patient Medical Record");
        System.out.println("2. Update Patient Medical Record");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Unavailability for Appointment");
        System.out.println("5. Accept or Decline Appointment Request");
        System.out.println("6. View Upcoming Appointment");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.print("Enter your choice: ");
    }

    public void viewPatientMedicalRecord(){
        PatientManager pm = new PatientManager();
        System.out.println("Enter the patient ID: ");
        String patientID = sc.next();
        Patient patient = pm.selectPatient(patientID);
        if(patient == null){
            System.out.println("Patient not found.");
            return;
        }
        ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(patientID);
        if(records.size() == 0){
            System.out.println("No medical records found for patient " + patientID);
            return;
        }
        System.out.println("Medical records for patient " + patientID + ":");
        for(MedicalRecord record : records){
            System.out.println("Diagnosis Date: " + record.getStringDateOfDiagnosis());
            System.out.println("Diagnosis: " + record.getDiagnosis());
            System.out.println("Prescription: " + record.getPrescription());
            System.out.println("Prescription Status: " + (record.isPrescriptionStatus() ? "Approved" : "Not Approved"));
            System.out.println("-----------------------------------");
        }

    }

    public void updatePatientMedicalRecord() {
        PatientManager pm = new PatientManager();

        
        // System.out.println("Enter the patient ID: ");
        // String patientID = sc.next();
        // Patient patient = pm.selectPatient(patientID);
        // if (patient == null) {
        //     System.out.println("Patient not found.");
        //     return;
        // }
    
        // ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(patientID);
        // if (records.isEmpty()) {
        //     System.out.println("No medical records found for patient " + patientID);
        //     return;
        // }

        System.out.println("Enter patientID: ");
        String patientID = sc.next();    
        System.out.println("Enter the date of diagnosis (yyyy-MM-dd): ");
        String dateInput = sc.next();
        Date dateOfDiagnosis;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateOfDiagnosis = dateFormat.parse(dateInput);
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
            return;
        }

        sc.nextLine(); //clear the buffer
    
        System.out.println("Enter the diagnosis: ");
        String diagnosis = sc.nextLine();
        System.out.println("Enter the prescription: ");
        String prescription = sc.nextLine();
        System.out.println("Enter the prescription status (true/false): ");
        boolean prescriptionStatus = sc.nextBoolean();
    
        // Create a new MedicalRecord object
        MedicalRecord newRecord = new MedicalRecord(patientID, dateOfDiagnosis, diagnosis, prescription, prescriptionStatus);
        mrm.addNewRecord(patientID, dateOfDiagnosis, diagnosis, prescription, prescriptionStatus); // Add to MedicalRecordManager
    
        System.out.println("Medical record added successfully.");
    }
    
    public void recordAppointmentOutcome(){
        PatientManager pm = new PatientManager();

        System.out.println("Enter the Patient ID: ");
        String patientID = sc.next().toUpperCase();
        
        Patient patient = pm.selectPatient(patientID);
        if(patient == null){
            System.out.println("Patient not found.");
            return;
        }else{
            ArrayList<Appointment> appointments = am.viewPatientAppointments(patientID);
            if(appointments.size() == 0){
                System.out.println("No appointments found for patient " + patientID);
                return;
            }
            System.out.println("before the loop");
            for (Appointment appointment : appointments){
                System.out.println("Appointment ID: " + appointment.getAppointmentID());
                System.out.println("Date: " + appointment.getStringDate());
                System.out.println("Time: " + appointment.getStringTime());
                System.out.println("Outcome: " + appointment.getOutcome());
                System.out.println("-----------------------------------");
            }

            System.out.println("Enter the appointment ID: ");
            int appointmentID = sc.nextInt();
            Appointment appointment = am.findAppointmentByID(appointmentID);
            sc.nextLine();

            try {
                if(appointment == null){
                    throw new Exception("Appointment not found.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Enter the outcome of the appointment: ");
            String outcome = sc.nextLine();
            if (outcome.isEmpty()){
                outcome = "No outcome recorded.";
                System.out.println("Outcome = " + outcome);
                return;
            }
            System.out.println("Outcome = " + outcome);
            appointment.setOutcome(outcome);
            am.saveAppointments();
            System.out.println("Appointment outcome recorded successfully.");
            // sc.nextLine(); // Clear the buffer
        }

        // System.out.println("Enter the outcome of the appointment: ");
        // String outcome = sc.next();
        // appointment.setOutcome(outcome);
        // System.out.println("Appointment outcome recorded successfully.");


        // System.out.println("Debug purpose");
        // for (Appointment appointment : appointments){
        //     System.out.println("Appointment ID: " + appointment.getAppointmentID());
        //     System.out.println("Date: " + appointment.getDate());
        //     System.out.println("Time: " + appointment.getTime());
        //     System.out.println("Outcome: " + appointment.getOutcome());
        //     System.out.println("-----------------------------------");
        // }
        // sc.nextLine(); // Clear the buffer
    }

    private void personalSchedule() {
        System.out.println("How would you like to view your personal schedule?");
        System.out.println("\t1. Calendar Format");
        System.out.println("\t2. Today's events");
        System.out.println("\t3. List out all upcoming events");

        int choice = sc.nextInt();

        if (choice == 1) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            scheduleManager.printMonthlyCalendar(month, year, userID);
        } else if (choice == 2) {
            scheduleManager.viewTodaysEvents(userID);
        }else{
            scheduleManager.printSchedule(userID);
        }
    }

    private void updateSchedule(){
        System.out.println("Update your schedule:");

        System.out.println("Date (yyyy-MM-dd):");
        String dateInput = sc.next().trim();

        System.out.print("Start Time (HH:mm): ");
        String startTimeInput = sc.next().trim();

        System.out.print("End Time (HH:mm): ");
        String endTimeInput = sc.next().trim();
        sc.nextLine(); // Clear the buffer

        System.out.println("Description: ");
        String comments = sc.nextLine();

        // Set comments to null if the user didn't enter a description
        if (comments.isEmpty()) {
            comments = null;
        }

        try{
            //placing in the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateInput);

            //placing in the start time
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date parsedTime = timeFormat.parse(startTimeInput);
            Time startTime = new Time(parsedTime.getTime());

            //placing in the end time
            timeFormat = new SimpleDateFormat("HH:mm");
            parsedTime = timeFormat.parse(endTimeInput);
            Time endTime = new Time(parsedTime.getTime());

            boolean success = scheduleManager.addSchedule(doctor, date, startTime, endTime, comments);

        }catch (ParseException e){
            System.out.println("Invalid date or time format. Please try again. \n");
            sc.nextLine();
            personalSchedule();
        }
    }

    public void viewUpcomingRecord(){
        am.getUpcomingAppointmentsForDoctor(doctor);
  
    }
}