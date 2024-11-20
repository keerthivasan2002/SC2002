import java.lang.reflect.Array;
import java.sql.Time;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DoctorUI implements UserUI{
    protected String userID;
    protected Staff doctor;
    protected StaffManager sm;
    protected Patient patient;
    protected MedicalRecordManager mrm;
    private ArrayList<MedicalRecord> medicalRecord;
    protected ScheduleManager scheduleManager;
    private AppointmentManager am;
    Scanner sc = new Scanner(System.in);
    OptionHandling oh = new OptionHandling();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DoctorUI(String userID, StaffManager sm, MedicalRecordManager mrm, AppointmentManager am, ScheduleManager scheduleManager){
        this.userID = userID;
        this.sm = sm;
        this.doctor = sm.selectStaff(userID);
        this.mrm = mrm;
        this.am = am;
        this.medicalRecord = mrm.getMedicalRecord();
        
        this.scheduleManager = scheduleManager;
        //Handling errors with the code
        if(this.doctor == null){
            System.out.println("No doctors found with the given ID:" + userID);
        }else{
             userOption();
        }
    }

    public void userOption(){
        int choice;
        System.out.println("Hello " + doctor.getName() + ".");
        System.out.println("What would you like to do today?");
        while(true){
            doctorMenu();
            choice = oh.getOption(1, 8);
            // System.out.println("I am here " );
            switch (choice){
                case 1: //view Patient Medical Record
                    System.out.println("\n");
                    System.out.println("===============================");
                    System.out.println("View Patient Medical Record");
                    System.out.println("===============================");
                    viewPatientMedicalRecord(); //this works
                    System.out.println("\n");
                    break;
                case 2: //Update patient Medical Record
                    System.out.println("Update Patient Medical Record");
                    //am.printAllAppointmentsFromCSV(); -> the data in the csv is correct
                    updatePatientMedicalRecordV2();
                    break;
                case 3: //view Personal Schedule
                    System.out.println("View Personal Schedule");
                    personalSchedule();
                    break;
                case 4: //Set availability for appointment
                    updateSchedule(); //This is done
                    break;
                case 5: //Accept or Decline Appointment Request
                    System.out.println("Accept or Decline Appointment Request");
                    am.appointmentRequest(doctor);

                    break;
                case 6: //View Upcoming Appointment
                    viewUpcomingRecord();
                    break;
                case 7: //Record Appointment Outcome
                    recordAppointmentOutcome();
                break;
                case 8: //Log out
                    System.out.println("Thank you! Hope to see you soon :)\n"); //This works
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
    }

    public void viewPatientMedicalRecord(){
        PatientManager pm = new PatientManager();
        System.out.println("Enter the patient ID: ");
        String patientID = sc.nextLine().toUpperCase();
        Patient patient = pm.selectPatient(patientID);
        if(patient == null){
            System.out.println("Patient not found.");
            return;
        }
        ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(patientID);
        mrm.displayMedicalRecords(records);

    }

    public void updatePatientMedicalRecord() {
        PatientManager pm = new PatientManager();

        System.out.println("Enter patientID: ");
        String patientID = sc.next().toUpperCase();  
        Patient patient = pm.selectPatient(patientID);
        if(patient == null){
            System.out.println("Patient not found.");
            return;
        }
        sc.nextLine(); // Clear the buffer
        Calendar calendar = Calendar.getInstance();
        Date dateOfDiagnosis;
        while(true){
            try {
                System.out.println("Enter the date of diagnosis (yyyy-MM-dd): ");
                String dateInput = sc.nextLine();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateOfDiagnosis = dateFormat.parse(dateInput);
                Date dateOfBirth = dateFormat.parse(patient.getDateOfBirth());
                if (dateOfDiagnosis.after(calendar.getTime())) {
                    throw new Exception("Date of diagnosis cannot be in the future.");
                }else if (dateOfDiagnosis.before(dateOfBirth)){
                    throw new Exception("Date of diagnosis cannot be before the patient's date of birth.");
                }else {
                    break;
                }
            } catch (ParseException e) {
                System.out.println("Error parsing date: " + e.getMessage());
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    
        System.out.println("Enter the diagnosis: ");
        String diagnosis = sc.nextLine();
        System.out.println("Enter the prescription: ");
        String prescription = sc.nextLine();
        // System.out.println("Enter the prescription status (true/false): ");
        boolean prescriptionStatus = false;
    
        // Create a new MedicalRecord object
        // MedicalRecord newRecord = new MedicalRecord(patientID, dateOfDiagnosis, diagnosis, prescription, prescriptionStatus);
        // mrm.addNewRecord(newRecord); // Add to MedicalRecordManager
        mrm.addNewRecord(patientID, dateOfDiagnosis, diagnosis, prescription, prescriptionStatus); // Add to MedicalRecordManager
    
        // Medical record added successfully.
    }

    public void updatePatientMedicalRecordV2(){
        PatientManager pm = new PatientManager();
        
        // Get the patient
        System.out.println("Enter the patient ID: ");
        String patientID = sc.next().toUpperCase();
        Patient patient = pm.selectPatient(patientID);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        // Get the medical records for the patient
        ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(patientID);
        if (records.isEmpty()) {
            System.out.println("No medical records found for patient " + patientID);
            return;
        }
        // Display the medical records
        mrm.displayMedicalRecords(records);
        if (records.isEmpty()) {
            System.out.println("No medical records found for patient " + patientID);
            return;
        }

        // Update the medical records
        try{
            System.out.println("Enter the record ID to update: ");
            int recordID = sc.nextInt();
            sc.nextLine(); // Clear the buffer
            MedicalRecord record = mrm.getRecordByID(recordID);
            if (record == null) {
                System.out.println("Record not found.");
                return;
            }
            System.out.println("Enter the date of diagnosis (yyyy-MM-dd): ");
            String dateInput = sc.nextLine();
            Date dateOfDiagnosis = dateFormat.parse(dateInput);
            record.setDateOfDiagnosis(dateOfDiagnosis);
            System.out.println("Enter the diagnosis: ");
            String diagnosis = sc.nextLine();
            record.setDiagnosis(diagnosis);
            System.out.println("Enter the prescription: ");
            String prescription = sc.nextLine();
            record.setPrescription(prescription);
            // System.out.println("Enter the prescription status (true/false): ");
            boolean prescriptionStatus = false;
            record.setPrescriptionStatus(prescriptionStatus);
            // MedicalRecord newRecord = new MedicalRecord(patientID, dateOfDiagnosis, diagnosis, prescription, prescriptionStatus);
            // mrm.addNewRecord(newRecord);
            System.out.println("Medical record updated successfully.");
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }       
        mrm.saveMedicalRecords();
        System.out.println("Medical record added successfully.");
    }
    
    public void recordAppointmentOutcome() {
        PatientManager pm = new PatientManager();
    
        System.out.println("Enter the Patient ID: ");
        String patientID = sc.next().toUpperCase();
        Patient patient = pm.selectPatient(patientID);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        ArrayList<Appointment> appointments = am.getPatientAppointments(patient, 0);
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointments found for the patient.");
            return;
        }
        am.displayAppointment(appointments);

        //am.displayAppointment(appointments);
        try{
            System.out.println("Enter the appointment ID: ");
            System.out.println("Enter 0 to go back.");
            int appointmentID = sc.nextInt();
            if (appointmentID == 0) {
                return;
            }
            Appointment selectedAppointment = am.findAppointmentByID(appointmentID);
            System.out.println(selectedAppointment.getAppointmentID());
            System.out.println(selectedAppointment.getOutcome());
            sc.nextLine(); // Clear the buffer
            if (selectedAppointment.getOutcome() != null && !selectedAppointment.getOutcome().equals("NULL")) {
                System.out.println("Outcome already recorded for this appointment.");
            } else {
                System.out.println("Enter the outcome of the appointment: ");
                String outcome = sc.nextLine();
                if (outcome.isEmpty()) {
                    outcome = "No outcome recorded.";
                }
            
                selectedAppointment.setOutcome(outcome);
                am.saveAppointments();
            }
        }catch (Exception e){
            System.out.println("Invalid appointment ID. Please try again.");
            sc.nextLine();
            recordAppointmentOutcome();
        }  
       
        System.out.println("Appointment outcome recorded successfully.");
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
            //scheduleManager.viewTodaysSchedule(userID);
            scheduleManager.viewTodaysEvent(userID);
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

            if (success) {
                System.out.println("Schedule added successfully.");
            } else {
                System.out.println("Failed to add schedule.");
            }
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