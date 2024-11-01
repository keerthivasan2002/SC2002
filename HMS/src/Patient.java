public class Patient extends User {
    private String dateOfbirth;
    private BloodType bloodType;

    // Constructor with additional parameters
    public Patient(String userID, String password, String name, Gender gender, String emailAddress, int phoneNumber, String dateOfbirth, BloodType bloodType, HospitalRole role) {
        super(userID, password, name, gender, emailAddress, phoneNumber,role);
        this.dateOfbirth = dateOfbirth;
        this.bloodType = bloodType;

    }

    //tester function
    //to check if the patients are loaded in correctly
    public String toString() {
        return "Patient{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", dateOfbirth='" + dateOfbirth + '\'' +
                ", gender=" + gender +
                ", bloodType=" + bloodType +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    public String getDateOfbirth(){
        return dateOfbirth;
    }

    public BloodType getBloodType() {
        return bloodType;
    }
}
