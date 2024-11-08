public class Patient extends User {
    private String dateOfbirth;
    private BloodType bloodType;
    private String patientID;
    
    // Constructor with additional parameters
    public Patient(String patientID, String password, String name, Gender gender, String emailAddress, int phoneNumber, String dateOfbirth, BloodType bloodType, HospitalRole role) {
        super(patientID, password, name, gender, emailAddress, phoneNumber,role);
        this.dateOfbirth = dateOfbirth;
        this.bloodType = bloodType;

    }

    //constructor to use in the
    public Patient(String userID){
        super(userID); //call it from the user class
    }

    //function used to add to the csv
    public String[] toArray() {
        return new String[]{
                userID,
                password,
                name,
                dateOfbirth,
                gender.toString(),
                bloodType.toString(),
                emailAddress,
                String.valueOf(phoneNumber),
                role.toString()
        };
    }

    public String getDateOfbirth(){
        return dateOfbirth;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public String getPatientID() {
        return patientID;
    }

    public String toString(){
        return patientID;
    }
}
