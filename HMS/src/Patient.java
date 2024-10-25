public class Patient extends User{
    private String dateOfbirth;
    private BloodType bloodType;

    //initialising the constructor class
    public Patient(String userID, String password){
        super(userID, password);

    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public String getDateOfbirth(){
        return dateOfbirth;
    }
}
