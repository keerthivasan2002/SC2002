public class Student extends User {
    private String dateOfBirth;
    private String specialisation;
    private BloodType bloodType;

    //constructor class for students in the hospital
    public Student(String internID, String password, String name, Gender gender, String emailAddress, int phoneNumber, String dateOfBirth, BloodType bloodType, HospitalRole role, String specialisation){
        super(internID, password, name, gender, emailAddress, phoneNumber,role);
        this.dateOfBirth = dateOfBirth;
        this.specialisation = specialisation;
        this.bloodType = bloodType;
    }

    public Student(String userID) {
        super(userID);
    }

    public String getSpecialisation(){
        return specialisation;
    }


    public String getDateOfBirth(){
        return dateOfBirth;
    }


    @Override
    public String[] toArray() {
        return new String[] {
                userID,
                password,
                name,
                dateOfBirth,
                gender.toString(),
                bloodType.toString(),
                emailAddress,
                String.valueOf(phoneNumber),
                role.toString(),
                specialisation
        };
    }

}
