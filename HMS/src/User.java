public abstract class User {
    protected HospitalRole role;
    protected String name;
    protected Gender gender;
    protected String emailAddress;
    protected int phoneNumber;
    protected String userID;
    protected String password;
    protected int age;

    //constructor for User class
    public User(String userID){
        this.userID = userID;
    }

    // Constructor for User class
    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    // Additional constructor to initialise Patients
    public User(String userID, String password, String name, Gender gender, String emailAddress, int phoneNumber, HospitalRole role) {
        this(userID, password);  // Call the first constructor
        this.name = name;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    //Constructor to initialize Doctors
    public User(String userID, String password, String name, Gender gender, int age, HospitalRole role, String emailAddress, int phoneNumber) {
        this(userID, password, name, gender, emailAddress, phoneNumber, role);
        this.age = age;
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

    public int getPhoneNumber(){
        return phoneNumber;
    }

    public Gender getGender(){
        return gender;
    }

    public String getPassword(){
        return password;
    }

    public HospitalRole getrole(){
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
