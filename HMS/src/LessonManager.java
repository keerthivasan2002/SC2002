import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LessonManager {
    private static LessonManager instance;
    private ArrayList<Lessons> lessons;
    private String lessonFile = "Lessons.csv";

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public LessonManager() {
        this.lessons = new ArrayList<>();
        initializeLessons();
    }

    // Static method to get the instance (Singleton pattern)
    public static synchronized LessonManager getInstance() {
        if (instance == null) {
            instance = new LessonManager();
        }
        return instance;
    }

    public void initializeLessons() {
        // Load lessons from the file
        FileManager lessonFileManager = new FileManager(lessonFile);
        String[][] lessonArray = lessonFileManager.readFile();

        for (int i = 1; i < lessonArray.length; i++) {
            String[] row = lessonArray[i];
            if (row.length >= 7) {
                try {
                    String doctorID = row[0];
                    Time timeStart = new Time(timeFormat.parse(row[1].trim()).getTime());
                    Time timeEnd = new Time(timeFormat.parse(row[2].trim()).getTime());
                    String description = row[3];
                    int courseCode = Integer.parseInt(row[4]);
                    String moduleDescription = row[5];
                    String moduleDifficulty = row[6];
                    String internID = row[7];
                    String internName = row[8];
                    String day= row [9];


                    Lessons lesson = new Lessons(doctorID, timeStart, timeEnd, courseCode, moduleDescription, moduleDescription, moduleDifficulty,internID, internName, day);
                    lessons.add(lesson);

                } catch (Exception e) {
                    System.out.println("Error parsing time for row: " + (i + 1) + " - " + e.getMessage());
                }
            }
        }
    }

    // Display lessons in a timetable format by day of the week
    public void displayWeeklyTimetable() {
        System.out.println("Weekly Timetable:");
        System.out.println("==================");
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (String day : daysOfWeek) {
            System.out.println(day + ":");
            List<Lessons> lessonsForDay = getLessonsForDay(day);

            if (lessonsForDay.isEmpty()) {
                System.out.println("  No lessons scheduled.");
            } else {
                // Sort lessons by start time for each day
                lessonsForDay.sort(Comparator.comparing(Lessons::getStartTime));

                for (Lessons lesson : lessonsForDay) {
                    System.out.printf("  %s - %s: %s (Code: %d, Difficulty: %s)\n",
                            timeFormat.format(lesson.getStartTime()),
                            timeFormat.format(lesson.getEndTime()),
                            lesson.getModuleDescription(),
                            lesson.getCourseCode(),
                            lesson.getModuleDifficulty());
                }
            }
            System.out.println();
        }
    }

    // Helper method to get lessons for a specific day
    private List<Lessons> getLessonsForDay(String day) {
        List<Lessons> lessonsForDay = new ArrayList<>();
        for (Lessons lesson : lessons) {
            if (lesson.getDay().equalsIgnoreCase(day)) {
                lessonsForDay.add(lesson);
            }
        }
        return lessonsForDay;
    }

    // New method to display lessons for today
    public void displayTodayTimetable() {
        // Get today's day of the week
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString = "";

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayString = "Sunday";
                break;
            case Calendar.MONDAY:
                dayString = "Monday";
                break;
            case Calendar.TUESDAY:
                dayString = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayString = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayString = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayString = "Friday";
                break;
            case Calendar.SATURDAY:
                dayString = "Saturday";
                break;
        }

        System.out.println("Today's Timetable (" + dayString + "):");
        System.out.println("==================");

        List<Lessons> lessonsForToday = getLessonsForDay(dayString);

        if (lessonsForToday.isEmpty()) {
            System.out.println("  No lessons scheduled for today.");
        } else {
            // Sort lessons by start time
            lessonsForToday.sort(Comparator.comparing(Lessons::getStartTime));

            for (Lessons lesson : lessonsForToday) {
                System.out.printf("  %s - %s: %s (Code: %d, Difficulty: %s)\n",
                        timeFormat.format(lesson.getStartTime()),
                        timeFormat.format(lesson.getEndTime()),
                        lesson.getModuleDescription(),
                        lesson.getCourseCode(),
                        lesson.getModuleDifficulty());
            }
        }
    }


    public List<Lessons> getLessons() {
        return lessons;
    }
}
