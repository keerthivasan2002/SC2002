
public class Staff extends User {

    public Staff(String userID, String password, boolean isDefaultPassword) {
        super(userID, password, isDefaultPassword);
    }
    //constructor class
    public Staff(String userID, String password, String name, HospitalRole role, Gender gender, int age, String emailAddress, int phoneNumber) {
        super(userID, password, name, gender,emailAddress, phoneNumber, role);  // Corrected order
        this.age = age;
    }
    

    //creating another constructor
    public Staff(String userID){
        super(userID);
    }

    
    // Method to convert the Staff's attributes to a String array for CSV storage
    public String[] toArray() {
        return new String[]{
                userID,
                password,
                name,
                role.toString(),
                gender.toString(),
                String.valueOf(age),
                emailAddress,
                String.valueOf(phoneNumber)
        };
    }

    public String toString(){
        return userID;
    }

    public String[] toDisplayArray() {
        return new String[]{
                userID,
                name
        };
    }
  
}
