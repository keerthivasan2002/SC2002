import java.util.ArrayList;

public class Main {
    public static void main(String[] args)
    {
        ReadFile readFile = new ReadFile();
        ArrayList<String[]> patientList = new ArrayList<>();
        ArrayList<String[]> staffList = new ArrayList<>();

        try {
            patientList = readFile.readPatientListCSV();
            System.out.println("Patient List:");
            readFile.printPatientList(patientList);

            // Reading and printing the staff list
            staffList = readFile.readStaffListCSV();
            System.out.println("\nStaff List:");
            readFile.printPatientList(staffList);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}