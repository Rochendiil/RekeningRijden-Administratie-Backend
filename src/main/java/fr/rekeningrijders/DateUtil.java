package fr.rekeningrijders;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil
{
    private DateUtil()
    {
        // Can't be instanced
    }

    public static class DateRange
    {
        public final Date start;
        public final Date end;

        public DateRange(Date start, Date end)
        {
            this.start = start;
            this.end = end;
        }
    }

    public static DateRange getStartEndDates(short year, Month month)
    {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set((int)year, month.ordinal(), 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date end = calendar.getTime();
        return new DateRange(start, end);
    }

    public static boolean dateBetween(Date date, Date start, Date end)
    {
        int a = date.compareTo(start);
        if (a < 0) return false;
        int b = date.compareTo(end);
        return b <= 0;
    }
}