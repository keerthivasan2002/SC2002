import java.util.ArrayList;
import java.util.Scanner;

class LoginManager{
    private ArrayList<String[]> patientList;
    private ArrayList<String[]> staffList;

    public LoginManager(){
        patientList = new ArrayList<String[]>();
        staffList = new ArrayList<String[]>();
    }

    public void loadUserData(){
        ReadFile reader = new ReadFile();
        try{
            patientList = reader.readPatientListCSV();
            staffList = reader.readStaffListCSV();
            
            reader.printTable(patientList);
            reader.printTable(staffList);
        }catch(Exception e){
            System.out.println("Error reading CSV file" + e.getMessage());
        }
    }

    public User authenticate(String hospitalID, String password){
        UserRole role = getUserRole(hospitalID);
        // System.out.println("I am here");
        System.out.println(role);
        switch (role) {
            case PATIENT:
                if (verifyCredentials(patientList, hospitalID, password)){
                    return new Patient(hospitalID, password);
                }
                break;
            case DOCTOR:
                if (verifyCredentials(staffList, hospitalID, password)){
                    return new Doctor(hospitalID, password);
                }
                break;
            case PHAMARCIST:
                if (verifyCredentials(staffList, hospitalID, password)){
                    return new Pharmacist(hospitalID, password);
                }
                break;
            case ADMINSTRATOR:
                if (verifyCredentials(staffList, hospitalID, password)){
                    return new Adminstrator(hospitalID, password);
                }
                break;
            default:
                System.out.println("Invalid Hospital ID or Password");
                break;
            }
        return null;    
    }

    private boolean verifyCredentials(ArrayList<String[]> userList, String hospitalID, String password){
        for (String[] user : userList){
            if (user[0].equals(hospitalID) && user[1].equals(password)){
                // System.out.println("I am in verifyCredentials");
                return true;
            }
        }
        return false;
    }   

    private UserRole getUserRole(String hospitalID){
        if ( hospitalID.length() > 4 ){
            if (hospitalID.startsWith("P")){
                return UserRole.PATIENT;
            }
        }
        else 
        {   if (hospitalID.startsWith("D")){
                return UserRole.DOCTOR;
            }else if (hospitalID.startsWith("P")){
                return UserRole.PHAMARCIST;
            }else if (hospitalID.startsWith("A")){
                return UserRole.ADMINSTRATOR;
            }
        }
        return null;
    }

    public User loginMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the login page");
        System.out.println("Please enter your Hospital ID and Password");
        System.out.println("Hospital ID: ");
        String hospitalID = sc.nextLine();
        System.out.println("Password: ");
        String password = sc.nextLine();

        return authenticate(hospitalID, password);
    }
}