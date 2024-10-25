public abstract class User {
    protected HospitalRole role;
    protected String name;
    protected Gender gender;
    protected String emailAddress;
    protected int phoneNumber;
    protected String userID;
    protected String password;

    // Constructor for User class
    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    //getter methods for the subclasses
    public String getUserID(){
        return userID;
    }

    public String getName(){
        return name;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public Gender getGender(){
        return gender;
    }

    public HospitalRole role(){
        return role;
    }

    //setter methods
    protected String updatePassword(String new_Password){
        password = new_Password;
        return password;
    }

    protected int updatePhoneNumber(int new_Phone_Number){
        phoneNumber = new_Phone_Number;
        return phoneNumber;
    }

    protected String updateEmailAddress(String new_Email_Address){
        emailAddress = new_Email_Address;
        return emailAddress;
    }
}
