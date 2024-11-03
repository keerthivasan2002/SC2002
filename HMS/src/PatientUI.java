import java.util.ArrayList;
import java.util.Scanner;

public class PatientUI {
    private String userID;
    Patient patient;
    PatientManager pm; //keep reference to a PatientManager
    MedicalRecordManager mrm;

    Scanner sc = new Scanner(System.in);

    public PatientUI(String userID, PatientManager pm, MedicalRecordManager mrm){
        this.userID = userID;
        this.pm = pm;
        this.mrm = mrm;
        this.patient = pm.selectPatient(userID);

        //Handling errors with the code
        if(this.patient == null){
            System.out.println("No patient found with the given ID:" + userID);
        }else{
            PatientOption();
        }
    }

    public void PatientOption(){
        int choice;
        System.out.println("Hello " + patient.getName() + ".");
        System.out.println("What would you like to do today?");
        patientMenu();
        choice = sc.nextInt();

        while(choice < 10){
            switch (choice){
                case 1: //view medical record
                    viewMedicalRecords();
                    break;
                case 2: //update Personal information
                    patientInfo();
                    updatePatientInfo();
                    break;
                case 3: //View Available appointment Slots
                    break;
                case 4: //Schedule an Appointment
                    break;
                case 5: //Reschedule an Appointment
                    break;
                case 6: //Cancel an Appointment
                    break;
                case 7: //View Scheduled Appointment
                    break;
                case 8: //View Past Appointment Records
                    break;
                case 9://logout
                    System.out.println("Thank you! Hope to see you soon :) \n");
                    System.exit(0);
                    return;
            }
            System.out.println("What else would you like to do today?");
            patientMenu();
            choice = sc.nextInt();
        }
    }

    // Function to view medical records
    private void viewMedicalRecords() {
        ArrayList<MedicalRecord> records = mrm.getAllRecordsForPatient(userID);
        if (records.isEmpty()) {
            System.out.println("No medical records found for the patient.");
        } else {
            for (MedicalRecord record : records) {
                System.out.println("Diagnosis Date: " + record.getDateOfDiagnosis());
                System.out.println("Diagnosis: " + record.getDiagnosis());
                System.out.println("Prescription: " + record.getPrescription());
                System.out.println("Prescription Status: " + (record.isPrescriptionStatus() ? "Approved" : "Not Approved"));
                System.out.println("-----------------------------------");
            }
        }
    }

    //create a function for the patient menu
    private void patientMenu(){
        System.out.println("-----------------------------------");
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
        System.out.println("-----------------------------------");
        System.out.print("Enter your choice: ");
    }

    //method for testing purposes
    private void patientInfo() {
        System.out.println("Patient Information: ");
        System.out.println("Account type: " + patient.getrole());
        System.out.println("User ID: " + patient.getUserID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfbirth());
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
}
