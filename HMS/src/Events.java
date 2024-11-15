import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Events {
    private Staff doctor;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String description;

    // Shared date and time formatters
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public Events(String doctorID, Date date, Time startTime, Time endTime, String description) {
        this.doctor = new Staff(doctorID);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    // Getter methods
    public Staff getDoctor() {
        return doctor;
    }

    public Date getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    // Formatted getters for date and time
    public String getStringStartTime() {
        return TIME_FORMAT.format(getStartTime());
    }

    public String getStringEndTime() {
        return TIME_FORMAT.format(getEndTime());
    }

    public String getStringDate() {
        return DATE_FORMAT.format(getDate());
    }

    // Setter methods
    public void setDoctor(String doctorID) {
        this.doctor = new Staff(doctorID);
    }

    public void setDate(Date newDate) {
        this.date = newDate;
    }

    public void setStartTime(Time newStartTime) {
        this.startTime = newStartTime;
    }

    public void setEndTime(Time newEndTime) {
        this.endTime = newEndTime;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
