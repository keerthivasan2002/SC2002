import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
}
