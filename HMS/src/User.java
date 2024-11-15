public abstract class User {
    protected HospitalRole role;
    protected String name;
    protected Gender gender;
    protected String emailAddress;
    protected int phoneNumber;
    protected String userID;
    protected String password;
    protected int age;
    protected boolean isDefaultPassword; 

    //constructor for User class
    public User(String userID){
        this.userID = userID;
    }

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public User(String userID, String password, boolean isDefaultPassword) {
        this.userID = userID;
        this.password = password;
        this.isDefaultPassword = isDefaultPassword;
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

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public int getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender(){
        return gender;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HospitalRole getrole(){
        return role;
    }

    public void setRole(HospitalRole role) {
        this.role = role;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
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

    public boolean isDefaultPassword() {
        return isDefaultPassword;
    }

    public void setDefaultPassword(boolean isDefaultPassword) {
        this.isDefaultPassword = isDefaultPassword;
    }

    public String[] toArray() {
        return new String[]{
                userID,
                password,
                isDefaultPassword ? "1" : "0"
        };
    }
}
