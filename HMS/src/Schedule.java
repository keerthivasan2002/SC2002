import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule {
    protected Staff doctor;
    protected Date date;
    protected Time startTime;
    protected Time endTime;
    protected String description;


    //constructor class for schedule
    public Schedule(String userID, Date date, Time startTime, Time endTime, String description){
        this.doctor = new Staff(userID);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;

    }

    //getter classes
    public Date getDate() {
        return date;
    }

    public Staff getDoctor() {
        return doctor;
    }

    public String getDescription() {
        return description;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    // Formatted getTime to string method
    public String getStringStartTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(startTime);
    }

    public String getStringEndTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(endTime);
    }

    // Formatted getDate to string method
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }


    //setter classes
    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDoctor(Staff doctor) {
        this.doctor = doctor;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String[] toArray() {
        // Use formatted getDate and getTime methods
        String formattedDate = getStringDate();
        String formattedStartTime = getStringStartTime();
        String formattedEndTime = getStringEndTime();

        return new String[]{
                doctor.getUserID(),
                formattedDate,
                formattedStartTime,
                formattedEndTime,
                description != null ? description : "" // Check if description is null
        };
    }

}

