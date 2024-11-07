import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private String file;

    //constructor class
    public FileManager(String file){
        this.file = file;
    }


    //getter method
    public String getFile(){
        return file;
    }

    //read the file in 2D array
    public String[][] readFile(){
        // String file = System.getProperty("user.dir") + "/HMS/src/dependencies/" + getFile(); //i think should work on everbody computer
        String file = System.getProperty("user.dir") + "/dependencies/" + getFile(); //i think should work on everbody computer

        BufferedReader reader = null;
        String line = "";
        ArrayList<String[]> dataList = new ArrayList<>(); //temp store rows

        try{
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                String[] row = line.split(",");
                dataList.add(row); //adding the data into the 2D array
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        // Convert ArrayList to 2D array for better run time
        String[][] dataArray = new String[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i); // Copy each row to the array
        }

        return dataArray; // Return the 2D array
    }

    //write the function that will dynamically update the csv file
    public void writeFile(String[][] data, boolean append) {
        // String filePath = System.getProperty("user.dir") + "/HMS/src/dependencies/" + getFile();
        String filePath = System.getProperty("user.dir") + "/dependencies/" + getFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //add new row in the csv file
    public void addNewRow(String[] newRow) {
        String filePath = System.getProperty("user.dir") + "/dependencies/" + getFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", newRow));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void updateRow(String uniqueId, String[] updatedRow){
    //     String filePath = System.getProperty("user.dir") + "/dependencies/" + getFile();
    // List<String[]> existingData = new ArrayList<>();

    // // Step 1: Read existing data from the file
    // try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
    //     String line;
    //     while ((line = reader.readLine()) != null) {
    //         existingData.add(line.split(","));
    //     }
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }

    // // Step 2: Update the row if the unique identifier is found
    // boolean isUpdated = false;
    // for (int i = 0; i < existingData.size(); i++) {
    //     String[] row = existingData.get(i);
        
    //     // Assuming the first column is the unique identifier
    //     if (row[0].equals(uniqueId)) {
    //         existingData.set(i, updatedRow); // Update the row
    //         isUpdated = true;
    //         break;
    //     }
    // }

    // // Step 3: If the row was found and updated, write the data back to the file
    // if (isUpdated) {
    //     try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
    //         for (String[] row : existingData) {
    //             writer.write(String.join(",", row));
    //             writer.newLine();
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // } else {
    //     System.out.println("No row found with unique identifier: " + uniqueId);
    // }
    // }

}
