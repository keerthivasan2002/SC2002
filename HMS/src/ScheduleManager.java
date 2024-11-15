import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleManager {
    private ArrayList<Schedule> schedules;
    private String scheduleFile = "Schedule.csv";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public ScheduleManager() {
        this.schedules = new ArrayList<>();
        initialiseSchedule();
    }

    public void initialiseSchedule() {
        // Load schedules from the file
        FileManager scheduleFileManager = new FileManager(scheduleFile);
        String[][] scheduleArray = scheduleFileManager.readFile();

        for (int i = 1; i < scheduleArray.length; i++) {
            String[] row = scheduleArray[i];
            if (row.length >= 5) {
                try {
                    String doctorID = row[0].trim();
                    Date date = dateFormat.parse(row[1].trim());
                    Time timeStart = new Time(timeFormat.parse(row[2].trim()).getTime());
                    Time timeEnd = new Time(timeFormat.parse(row[3].trim()).getTime());
                    String description = row[4].trim();

                    Schedule schedule = new Schedule(doctorID, date, timeStart, timeEnd, description);
                    schedules.add(schedule);

                } catch (ParseException e) {
                    System.out.println("Error parsing date/time for row: " + (i + 1) + " - " + e.getMessage());
                }
            }
        }
    }

    public void printMonthlyCalendar(int month, int year,String id) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1); // Set to the first day of the specified month

        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // Get the starting day of the week (Sunday = 0)

        System.out.println("Monthly Schedule:");
        System.out.println("=================");
        System.out.println("Sun   Mon   Tue   Wed   Thu   Fri   Sat");

        // Print leading spaces for the first row
        for (int i = 0; i < firstDayOfWeek; i++) {
            System.out.print("      ");
        }

        // Loop through each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            Date date = cal.getTime();

            // Check if the day has an appointment for the specific doctor
            boolean hasAppointment = schedules.stream()
                    .anyMatch(s -> isSameDay(s.getDate(), date) && s.getDoctor().getUserID().equals(id));

            // Print day with consistent width (4 characters for day, 2 for spacing)
            System.out.printf("%2d%s   ", day, hasAppointment ? "*" : " ");

            // Move to the next line at the end of the week
            if ((day + firstDayOfWeek) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // Helper method to check if two dates are on the same day
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    //overview of all the upcoming events
    public void printSchedule(String id) {
        Date now = new Date(); // Current date and time

        // Filter schedules for the specified doctor and exclude finished events
        List<Schedule> upcomingSchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getDoctor().getUserID().equals(id)) {
                // Combine date and end time to create a full Date object for end time
                Date endTimeWithDate = combineDateAndTime(schedule.getDate(), schedule.getEndTime());

                // Only add the schedule if it is upcoming or in progress
                if (endTimeWithDate.after(now)) {
                    upcomingSchedules.add(schedule);
                }
            }
        }

        // Sort the filtered list by date and start time
        upcomingSchedules.sort(Comparator.comparing(Schedule::getDate)
                .thenComparing(Schedule::getStartTime));

        // Print the sorted upcoming schedule
        System.out.println("Upcoming Schedule for Doctor ID: " + id);
        System.out.println("=====================================");
        if (upcomingSchedules.isEmpty()) {
            System.out.println("No upcoming events scheduled.");
        } else {
            for (Schedule schedule : upcomingSchedules) {
                System.out.println(schedule.getDoctor().getUserID() + " - "
                        + schedule.getStringDate() + " "
                        + schedule.getStringStartTime() + "-"
                        + schedule.getStringEndTime() + " "
                        + schedule.getDescription());
            }
        }
    }

    //function to view today event
    public void viewTodaysEvents(String id) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime(); // Current date and time

        ///System.out.println("Today's Date and Time: " + now); // Tester output for the current date and time

        System.out.println("Today's Events:");
        System.out.println("================");

        boolean hasEvents = false;
        for (Schedule schedule : schedules) {
            // Check if the event is today and belongs to the correct doctor
            if (isSameDay(schedule.getDate(), now) && schedule.getDoctor().getUserID().equals(id)) {
                // Combine the event date with the end time for accurate comparison
                Date endTimeWithDate = combineDateAndTime(schedule.getDate(), schedule.getEndTime());

                // Compare the combined date-time end time to the current time
                String status = endTimeWithDate.before(now) ? "Done" : "Upcoming";
                displaySchedule(schedule);
                System.out.println("Status: " + status);
                System.out.println("-----------------------------------");
                hasEvents = true;
            }
        }

        if (!hasEvents) {
            System.out.println("No events scheduled for today.");
        }
    }

    // Helper method to combine a date with a time
    private Date combineDateAndTime(Date date, Time time) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        // Set hours, minutes, seconds from the time to the date
        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        dateCal.set(Calendar.MILLISECOND, 0); // Clear milliseconds

        return dateCal.getTime(); // Return combined Date object
    }


    //method to display schedule
    private void displaySchedule(Schedule schedule) {
        System.out.println("Doctor ID: " + schedule.getDoctor().getUserID());
        System.out.println("Date: " + schedule.getStringDate());
        System.out.println("Start Time: " + schedule.getStringStartTime());
        System.out.println("End Time: " + schedule.getStringEndTime());
        System.out.println("Description: " + schedule.getDescription());
    }




    public boolean addSchedule(Staff doctor, Date date, Time startTime, Time endTime, String description) {
        System.out.println("Adding period of unavailability for " + doctor.getName());
        System.out.println("User ID: " + doctor.getUserID());

        // Combine date with start and end times to form full Date objects for comparison
        Date startDateTime = combineDateAndTime(date, startTime);
        Date endDateTime = combineDateAndTime(date, endTime);

        // Check for conflicts with existing schedules
        for (Schedule schedule : schedules) {
            if (schedule.getDoctor().getUserID().equals(doctor.getUserID()) && isSameDay(schedule.getDate(), date)) {
                Date existingStart = combineDateAndTime(schedule.getDate(), schedule.getStartTime());
                Date existingEnd = combineDateAndTime(schedule.getDate(), schedule.getEndTime());

                // Check if there is any overlap which will check for duplicates as well
                if ((startDateTime.before(existingEnd) && endDateTime.after(existingStart)) ||
                        startDateTime.equals(existingStart) || endDateTime.equals(existingEnd)) {
                    System.out.println("Conflict detected with existing schedule: "
                            + schedule.getStringStartTime() + "-" + schedule.getStringEndTime());
                    return false;
                }
            }
        }

        // If no conflict, add the schedule
        Schedule newSchedule = new Schedule(doctor.getUserID(), date, startTime, endTime, description);
        schedules.add(newSchedule);
        System.out.println("Schedule added successfully.");

        String[] schedule = new String[]{doctor.userID, dateFormat.format(date), timeFormat.format(startTime), timeFormat.format(endTime), description};
        FileManager appointmentFM = new FileManager(scheduleFile);
        appointmentFM.addNewRow(schedule);
        return true;
    }

}
