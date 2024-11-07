public class Patient extends User {
    private String dateOfbirth;
    private BloodType bloodType;

    // Constructor with additional parameters
    public Patient(String userID, String password, String name, Gender gender, String emailAddress, int phoneNumber, String dateOfbirth, BloodType bloodType, HospitalRole role) {
        super(userID, password, name, gender, emailAddress, phoneNumber,role);
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
}
