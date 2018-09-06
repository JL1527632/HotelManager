package com.leon.example.orangehotel.SupportUtilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtilities {

    final static long MILLIS_TO_DAYS = 86400000;
    final long MILLIS_TO_MINUTES = 60000;
    final long MILLIS_TO_HOURS = 3600000;
    final static String TAG = "DU";

    /**
     * Returns output format as EEE. MMM dd, YYYY based on input values
     * for Year, Month and Day in integer format.
     */
    public static String getDateFormat(int y, int m, int d) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setLenient(true);

        /* set(int year, int month, int date)
         * Sets the values for the calendar fields YEAR, MONTH, and DAY_OF_MONTH.
         */
        SimpleDateFormat dFormatter = new SimpleDateFormat("EEE. MMM dd, YYYY");
        myCalendar.set(y, m - 1, d);
        Date date = myCalendar.getTime();

        return dFormatter.format(date);
    }

    public static String getTodayAsString()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dFormatter = new SimpleDateFormat("EEE. MMM dd, YYYY");
        return dFormatter.format(date);
    }

    public static String getDateFormat(Date date) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("EEE. MMM dd, YYYY");
        return dFormatter.format(date);
    }

    public static String getSimpleDateFormat(Date date) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("MM/dd/YYYY");
        return dFormatter.format(date);
    }

    //Assumes an input of MM/dd/YYYY
    public static Date getDateObject(String simpleDateFormatString) {
        String formattedString = simpleDateFormatString;

        //Break points should be fixed at 2 and 5
        int m = Integer.parseInt(formattedString.substring(0, 2));
        int d = Integer.parseInt(formattedString.substring(3, 5));
        int y = Integer.parseInt(formattedString.substring(6));

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setLenient(true);
        myCalendar.set(y, m - 1, d);
        return myCalendar.getTime();
    }

    /* Creates a Date object from parameters entered as
    year, month, and day. Where month is the valued as interpreted by a user.
    i.e. January has a value of 1 and December a value of 12.
     */
    public static Date getDateObject(int y, int m, int d) {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setLenient(true);
        myCalendar.set(y, m - 1, d);
        return myCalendar.getTime();
    }

    /* Returns a Date object corresponding to n days of difference
    Negative values return previous dates, zero returns the same date.
    Positive values return future dates*/
    public static Date getAdjustedDate(Date date, int n) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("YYYYMMdd");
        String formattedString = dFormatter.format(date);

        int y = Integer.parseInt(formattedString.substring(0, 4));
        int m = Integer.parseInt(formattedString.substring(4, 6));
        int d = Integer.parseInt(formattedString.substring(6));

        d += n;

        return getDateObject(y, m, d);
    }

    /* Creates a Date object from Calendar object
     */
    public static Date getDateObject(Calendar calendar) {
        return calendar.getTime();
    }

    /*
     * Generates a String to be used as table name in format
     * YYYYMMDD. example: 20070829 representing August 29, 2007
     */
    public static String generateTableName(int year, int month, int day) {
        int formattedYear = year;

        //Checks year to be present as four digits.
        //Calculations default to the century in which the application is  run.
        if (year < 999) {
            //Calculates the numerical value for the current century.
            Calendar myCalendar = Calendar.getInstance();
            int century = myCalendar.get(Calendar.YEAR);

            century /= 100;

            formattedYear = (year % 100) + (100 * century);
        }
        //Program assumes dates will only need to be saved until the year 9,999.
        //After which, the numbering system loops.
        else if (year > 10000) {
            formattedYear = year % 10000;
        }

        String formattedMonth = "";
        if (month < 10)
            formattedMonth = "0" + month;
        else
            formattedMonth = "" + month;

        String formattedDay = "";
        if (day < 10)
            formattedDay = "0" + day;
        else
            formattedDay = "" + day;

        //Else, the year is assumed to be in the correct format.
        String formattedTableName = "" + formattedYear + formattedMonth + formattedDay;

        return formattedTableName;
    }

    public static int calculateDifferenceInDays(Date in, Date out) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(in);

        Calendar calendarOut = Calendar.getInstance();
        calendarOut.setTime(out);

        long inTime = calendarIn.getTimeInMillis();
        long outTime = calendarOut.getTimeInMillis();

        long days = outTime - inTime;
        days = days / MILLIS_TO_DAYS;

        return (int) days;
    }

    public static String generateTableHeader(Date date) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("YYYYMMdd");
        return dFormatter.format(date);
    }

    public static int getYear(Date date) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("YYYY");
        String formattedString = dFormatter.format(date);

        return Integer.parseInt(formattedString);
    }

    public static int getMonth(Date date) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("MM");
        String formattedString = dFormatter.format(date);

        return Integer.parseInt(formattedString);
    }

    public static int getDay(Date date) {
        SimpleDateFormat dFormatter = new SimpleDateFormat("dd");
        String formattedString = dFormatter.format(date);

        return Integer.parseInt(formattedString);
    }
}
