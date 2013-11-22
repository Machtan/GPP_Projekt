package classes;

/**
 * The Date class represents a specific time on a date, e.g: 17:00 Sat-27-Nov-13
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public class Date {
    
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private String[] months = new String[] {
        "ERROR!",
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "Maj",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Okt",
        "Nov",
        "Dec"
    };


    /**
     * Constructor for the Date class.
     * The index for month and day starts at 1!
     */
    public Date(int year, int month, int day, int hour, int minutes) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
    }
    
    /**
     * Returns a string with the name of the given month number
     * @param index The month number
     * @return The name of the month
     */
    private String getMonthString(int index) {
        if ((index < 0)||(12 < index )) {
            System.err.println("BAD MONTH INDEX: "+index);
            return "ERROR!";
        }
        else {
            return months[index];
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s:%s %s-%s-%s", (hour < 10)? "0"+hour: hour, 
        (minutes < 10)? "0"+hour: hour, day, getMonthString(month), year);
    }
}
