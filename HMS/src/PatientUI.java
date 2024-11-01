import java.util.Scanner;

public class PatientUI {
    private String userID;
    Patient patient;
    PatientManager pm; //keep reference to a PatientManager

    Scanner sc = new Scanner(System.in);

    public PatientUI(String userID, PatientManager pm){
        this.userID = userID;
        this.pm = pm;
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

        choice = sc.nextInt();

        switch (choice){
            case 1: //view medical record
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
                System.out.println("Thank you! Hope to see you soon :)");
                break;

        }
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
        int change_choice;
        System.out.println("Which information would you like to change?");
        System.out.println("1. Change Email Address");
        System.out.println("2. Change Phone Number");
        System.out.println("3. Change Password");
        System.out.println("press any other numbers to exit this!");

        change_choice = sc.nextInt();
        sc.nextLine(); //clears any buffer

        PatientManager pm = new PatientManager(); //creating a new instance of patient manager here to update the csv

        switch (change_choice){
            case 1:
                String new_Email;
                System.out.println("Please key in your new email:");
                new_Email = sc.nextLine();
                patient.updateEmailAddress(new_Email);
                System.out.println(patient.getEmailAddress());
                break;

            case 2:
                int new_PhoneNumber;
                System.out.println("Please key in your new number:");
                new_PhoneNumber = sc.nextInt();
                patient.updatePhoneNumber(new_PhoneNumber);
                System.out.println(patient.getPhoneNumber());
                break;

            case 3:
                String new_password;
                String verify;
                System.out.println("Key in your current password for verification:");
                verify = sc.nextLine();
                if(verify.equals(patient.password)){
                    System.out.println("New password:");
                    new_password = sc.nextLine();
                    patient.updatePassword(new_password);
                    System.out.println(patient.getPassword());
                }else{
                    System.out.println("Wrong Password.");
                    System.out.println("Please try again with the correct password!");
                }

            default:
                break;
        }
    }


}
