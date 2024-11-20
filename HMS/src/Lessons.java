import java.sql.Time;

public class Lessons extends Events {
    int courseCode;
    private String moduleDescription;
    private String moduleDifficulty;
    private String day; // New attribute for day

    Staff doctor;
    Student student;

    public Lessons(String userID, Time startTime, Time endTime, int courseCode, String description, String moduleDescription, String moduleDifficulty,String internID, String name, String day) {
        super(userID, startTime, endTime, description); //doctor id, start time, end time, description
        this.courseCode = courseCode;
        this.moduleDescription = moduleDescription;
        this.moduleDifficulty = moduleDifficulty;
        this.day = day; // Initialize the day attribute

        this.doctor = new Staff(userID);
        this.student = new Student(userID);
    }

    // Getter methods
    public String getModuleDescription() {
        return moduleDescription;
    }

    public String getModuleDifficulty() {
        return moduleDifficulty;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public String getDay() { // Getter for day
        return day;
    }

    // Setter methods
    public void setModuleDescription(String newModuleDescription) {
        this.moduleDescription = newModuleDescription;
    }

    public void setModuleDifficulty(String moduleDifficulty) {
        this.moduleDifficulty = moduleDifficulty;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }


    public void setDay(String day) { // Setter for day
        this.day = day;
    }
}
