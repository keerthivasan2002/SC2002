import java.util.ArrayList;

public class StaffManager {
    private ArrayList<Staff> staffs;
    private  String staff_file = "Staff_List.csv";

    public StaffManager(){
        staffs = new ArrayList<>();
        initializeStaffs(); //load the staff data from the file
    }

    //intialise the array based on the csv that is loaded in
    private void initializeStaffs(){
        FileManager staffFileManager = new FileManager(staff_file);
        String[][] staffArray = staffFileManager.readFile();

        //checking for potential errors
        if(staffArray == null || staffArray.length == 0){
            System.out.println("Failed to load patient data.");
            return;
        }

        for(int i = 1; i < staffArray.length; i++){
            String[] row = staffArray[i];

            if(row.length >= 6){
                String userID = row[0];
                String password = row[1];
                String name = row[2];
                HospitalRole role = HospitalRole.valueOf(row[3].toUpperCase());
                Gender gender = Gender.valueOf(row[4].toUpperCase());
                int age = Integer.parseInt(row[5]);
                String emailAddress = row[6];
                int phoneNumber = row[7].isEmpty() ? 0 : Integer.parseInt(row[7]);

                Staff staff = new Staff(userID, password, name, role, gender, age, emailAddress, phoneNumber);
                staffs.add(staff); //adding new staff data
            }else{
                System.out.println("Incomplete data in row, skipping: " + String.join(",", row)); //exception statement to show the data is loaded properly
            }
        }
    }

    public Staff selectStaff(String staffID){
        for(Staff staff : staffs){
            if(staff.userID.equals(staffID)){
                return staff;
            }
        }

        System.out.println("An error has occurred: User with ID " + staffID + "not found!");
        return null;
    }

    

}
