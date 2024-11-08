import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.sql.Time;

public class Schedule {
    private Map<String, String[]> weekSchedule;

    // Constructor of the Schedule class
    public Schedule() {
        weekSchedule = new HashMap<>();
        initializeWeekSchedule();
    }

    //This is to initialise the calendar
    private void initializeWeekSchedule() {
        Calendar calendar = Calendar.getInstance();

        //A while loop to ensure that the start of the week is starting from Monday
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        //the for loop is to initialise the slots available for 24 hours in the Hospital
        for (int i = 0; i < 7; i++) {
            String dayName = getDayName(calendar);

            //The hospital is open for 24 hours
            String[] timeSlots = new String[24];

            for (int j = 0; j < timeSlots.length; j++) {
                if (j == 7) {
                    timeSlots[j] = "Breakfast"; // 7th hour for lunch break
                } else if (j == 11) {
                    timeSlots[j] = "Lunch"; // 18th hour for dinner break
                } else if (j == 18){
                    timeSlots[j] = "Dinner"; // All other slots are free
                }else{
                    timeSlots[j] = "Free";
                }
            }

            weekSchedule.put(dayName, timeSlots);

            // Move to the next day
            calendar.add(Calendar.DATE, 1);
        }
    }

    private String getDayName(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            case Calendar.SUNDAY: return "Sunday";
            default: return "";
        }
    }

    //this is to see the weekly schedule of a patient
    public void viewWeeklySchedule() {
        Calendar calendar = Calendar.getInstance();

        // Making the calendar start on the Monday of the week
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        // Iterate over each day (Monday to Sunday)
        for (int i = 0; i < 7; i++) {
            String dayName = getDayName(calendar);
            String date = dateFormat.format(calendar.getTime()); // Format the date

            // Display the schedule for the day
            System.out.println("\n" + dayName + " (" + date + ") Schedule:");
            String[] timeSlots = weekSchedule.get(dayName);
            for (int j = 0; j < timeSlots.length; j++) {
                String postfix;
                int time = j % 12;

                //logic to show the different times
                if (time == 0) {
                    time = 12; // Handle the 12 AM/PM case
                    postfix = (j < 12) ? "AM" : "PM";
                } else if (j < 12) {
                    postfix = "AM";
                } else {
                    postfix = "PM";
                    time = (time == 12) ? 12 : time; // Adjust time for PM
                }

                int nextTime = (time == 12) ? 1 : time + 1;
                System.out.println(time + postfix + "-" + nextTime + postfix + ": " + timeSlots[j]);
            }

            // Move to the next day
            calendar.add(Calendar.DATE, 1);
        }
    }


    //This is to see the schedule custom to that certain date
    public void viewTodaySchedule() {
        // Get today's date
        Calendar calendar = Calendar.getInstance();

        // Get the day name (Monday-Sunday)
        String dayName = getDayName(calendar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());

        // Display the schedule for today
        System.out.println("\n" + dayName + " (" + date + ") Schedule:");
        String[] timeSlots = weekSchedule.get(dayName);
        for (int j = 0; j < timeSlots.length; j++) {
            String postfix;
            int time = j % 12; // Hours in 12-hour format (0-11)
            if (time == 0) {
                time = 12; // Handle the 12 AM/PM case
                postfix = (j < 12) ? "AM" : "PM";
            } else if (j < 12) {
                postfix = "AM";
            } else {
                postfix = "PM";
                time = (time == 12) ? 12 : time; // Adjust time for PM
            }

            int nextTime = (time == 12) ? 1 : time + 1;
            System.out.println(time + postfix + "-" + nextTime + postfix + ": " + timeSlots[j]);
        }
    }

    //This is to book an appointment
    public void bookTimeSlot(Date date, Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayName = getDayName(calendar);
        String[] timeSlots = weekSchedule.get(dayName);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
        if (timeSlots != null && "Free".equals(timeSlots[hour])) {
            timeSlots[hour] = "Booked";
        }
    }

    //This is to check if the slot is available
    public boolean isTimeSlotAvailable(Date date, Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayName = getDayName(calendar);
        String[] timeSlots = weekSchedule.get(dayName); 
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
        return timeSlots != null && "Free".equals(timeSlots[hour]);
    }

    //This is to cancel an appointment
    public void freeTimeSlot(Date date, Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayName = getDayName(calendar);
        String[] timeSlots = weekSchedule.get(dayName);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
        if (timeSlots != null && "Booked".equals(timeSlots[hour])) {
            timeSlots[hour] = "Free";
        }    
    }
}
