import java.util.Locale;

public class LogInManager {
    private String userName;
    private String password;
    private String fileName = "UserID.csv";

    // Constructor to handle file operations
    public LogInManager(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean authoriseLogin(){
        String id = userName.toLowerCase();
        String pass = password.toLowerCase();
        FileManager openFile = new FileManager(fileName);
        String[][] data = openFile.readFile();//placing the data into a 2D array

        boolean isAuthenticated = false;
        for (int i = 0; i < data.length; i++) {
                if (id.equals(data[i][0].toLowerCase()) && pass.equals(data[i][1].toLowerCase())){
                    System.out.println("Welcome");
                    isAuthenticated = true;
                    break;
                }
        }

        if(!isAuthenticated) {
            System.out.println("Please try again! Those are the wrong particulars.");
        }

        return isAuthenticated;
    }

}
