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
        String file = System.getProperty("user.dir") + "/HMS/src/dependencies/" + getFile(); //i think should work on everbody computer
        //String file = System.getProperty("user.dir") + "/dependencies/" + getFile(); //i think should work on everbody computer


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
        String filePath = System.getProperty("user.dir") + "/HMS/src/dependencies/" + getFile();
        //String filePath = System.getProperty("user.dir") + "/dependencies/" + getFile();

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
    public void addNewRow(String[] rowData) {
        addNewRow(rowData, true); // Call overloaded version with append by default
    }

    // Overloaded addNewRow with append parameter
    public void addNewRow(String[] rowData, boolean append) {
       //String filePath = System.getProperty("user.dir") + "/dependencies/" + getFile();
       String filePath = System.getProperty("user.dir") + "/HMS/src/dependencies/" + getFile();


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            writer.write(String.join(",", rowData));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}