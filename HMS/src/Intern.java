public class Intern extends User {
    private String dateOfBirth;
    private String specialisation;
    private BloodType bloodType;

    //constructor class for students in the hospital
    public Intern(String internID, String password, String name, Gender gender, String emailAddress, int phoneNumber, String dateOfBirth, BloodType bloodType, HospitalRole role, String specialisation){
        super(internID, password, name, gender, emailAddress, phoneNumber,role);
        this.dateOfBirth = dateOfBirth;
        this.specialisation = specialisation;
        this.bloodType = bloodType;
    }

    public String getSpecialisation(){
        return specialisation;
    }


    public String getDateOfBirth(){
        return dateOfBirth;
    }


    @Override
    public String toString() {
        return "StudentID: " + userID +
                ", Name: " + name +
                ", Gender: " + gender +
                ", Date of Birth: " + dateOfBirth +
                ", Blood Type: " + bloodType +
                ", Email Address: " + emailAddress +
                ", Phone Number: " + phoneNumber +
                ", Role: " + role +
                ", Specialisation: " + specialisation;
    }
}
