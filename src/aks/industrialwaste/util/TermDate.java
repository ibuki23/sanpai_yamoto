package aks.industrialwaste.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 所属日を処理します.
 */
public class TermDate {

    private static int startHour_ = 0;

    public static int getStartHour() {
        return startHour_;
    }

    public static Calendar getTerm(Calendar c) {
        if (null == c) {
            throw new NullPointerException("'c' cannot be null.");
        }

        int hour = c.get(Calendar.HOUR_OF_DAY);
        Calendar term = (Calendar) c.clone();
        if (getStartHour() > hour) {
            term.add(Calendar.DATE, -1);
        }

        return term;
    }

    public static Calendar getTerm(Date time) {
        if (null == time) {
            throw new NullPointerException("'time' cannot be null.");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(time);

        return getTerm(c);
    }

    public static void setStartHour(int hour) throws IllegalArgumentException {
        if ((0 > hour) || (24 <= hour)) {
            throw new IllegalArgumentException("");
        }

        startHour_ = hour;
    }
}
