import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleManager {
    //private Map<String, String[]> weekSchedule;// the mapping for the weekly schedule

    //initialising the array list for schedules
    private ArrayList<Schedule> schedules;
    private String scheduleFile = "Schedule.csv";



    public ScheduleManager() {
        //weekSchedule = new HashMap<>();
        this.schedules = new ArrayList<>();
        initialiseSchedule();
    }

    //initialising the schedule
    public void initialiseSchedule(){
        FileManager scheduleFileManager = new FileManager(scheduleFile);
        String[][] scheduleArray = scheduleFileManager.readFile();

        //terminate if the file not loaded properly
        if(scheduleArray == null || scheduleArray.length == 0){
            System.out.println("Failed to load appointment data.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the pattern as needed
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for (int i = 1; i < scheduleArray.length; i++) {  // Start at 1 to skip header
            String[] row = scheduleArray[i];

            if (row.length >= 5) {  // Ensure sufficient data in each row
                try {
                    String doctorID = row[0].trim();
                    Date date = dateFormat.parse(row[1].trim());
                    Time timeStart = new Time(timeFormat.parse(row[2].trim()).getTime());
                    Time timeEnd = new Time(timeFormat.parse(row[3].trim()).getTime());
                    String description = row[4].trim();

                    // Create a new Schedule object and add it to the list
                    Schedule schedule = new Schedule(doctorID, date, timeStart, timeEnd, description);
                    schedules.add(schedule);

                } catch (ParseException e) {
                    System.out.println("Error parsing date/time for row: " + (i + 1) + " - " + e.getMessage());
                }
            } else {
                System.out.println("Incomplete data in row, skipping: " + (i + 1));
            }
        }

    }
}


