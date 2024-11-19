import java.sql.Time;
import java.util.Date;

public class Schedule extends Events {

    public Schedule(String userID, Date date, Time startTime, Time endTime, String description) {
        super(userID, date, startTime, endTime, description);
    }

    // Convert to array for table or CSV representations
    public String[] toArray() {
        return new String[]{
                getDoctor().getUserID(),
                getStringDate(),
                getStringStartTime(),
                getStringEndTime(),
                getDescription() != null ? getDescription() : "" // Check if description is null
        };
    }

    // Optional: Override toString for easier debugging and printing
    @Override
    public String toString() {
        return "Schedule [Doctor ID=" + getDoctor().getUserID() +
                ", Date=" + getStringDate() +
                ", Start Time=" + getStringStartTime() +
                ", End Time=" + getStringEndTime() +
                ", Description=" + (getDescription() != null ? getDescription() : "No description") + "]";
    }
}
