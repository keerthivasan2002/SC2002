import java.io.*;
import java.util.ArrayList;

class ReadFile {

    public static final String delimiter = ",";  // Correct delimiter (comma)
    public static final String patientCSV = "C:\\Users\\tan_g\\OneDrive\\Documents\\GitHub\\SC2002\\CSV File\\Patient_List.csv";
    public static final String staffCSV = "C:\\Users\\tan_g\\OneDrive\\Documents\\GitHub\\SC2002\\CSV File\\Staff_List.csv";
    public ArrayList<String[]> List = new ArrayList<>();

    public ReadFile getInstance() 
    {
        return new ReadFile();
    }

    public ArrayList<String[]> readPatientListCSV() throws Exception
    {
        BufferedReader reader = null;
        String line = "";
        ArrayList<String[]> patientList = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(patientCSV));
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(delimiter);  // Split each line by delimiter and store as an array
                patientList.add(row);  // Add the array to the list
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(reader != null) {
                reader.close();
            }
        }

        return patientList;
    }
    
    public ArrayList<String[]> readStaffListCSV() throws Exception
    {
        BufferedReader reader = null;
        String line = "";
        ArrayList<String[]> staffList = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(staffCSV));
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(delimiter);  // Split each line by delimiter and store as an array
                staffList.add(row);  // Add the array to the list
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(reader != null) {
                reader.close();
            }
        }
        return staffList;
    }

    //debug purpose
    public void printTable(ArrayList<String[]> List)
    {
        if (List.isEmpty()) {
            System.out.println("The file is empty or could not be read.");
            return;
        }

        // Print header and data rows
        boolean isHeader = true;
        
        for (String[] row : List) {
            // Print each column in the row with a fixed width
            for (String column : row) {
                System.out.printf("%-20s", column.trim());  // Format each column with 20-character width
            }

            System.out.println();  // Move to the next line after printing the row

            // If printing the header, print a separator after it
            if (isHeader) {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
                isHeader = false;
            }
        }
    }

    public void writeStaffListCSV(ArrayList<String[]> staffList) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(staffCSV);

            // Write header line first
            writer.append("Staff ID").append(delimiter)
                  .append("Password").append(delimiter)
                  .append("Name").append(delimiter)
                  .append("Role").append(delimiter)
                  .append("Gender").append(delimiter)
                  .append("Age").append(delimiter)
                  .append("Email").append(delimiter)
                  .append("Phone number").append("\n");

            // Write each staff member's data
            for (String[] staff : staffList) {
                writer.append(String.join(delimiter, staff)).append("\n");
            }

            System.out.println("CSV file written successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
