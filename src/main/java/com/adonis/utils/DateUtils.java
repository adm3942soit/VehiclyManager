package com.adonis.utils;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.getInstance;

/**
 * @author Nikolajs Arhipovs <nikolajs.arhipovs at gmail.com>
 */
@Slf4j
public class DateUtils {

    public static final int MINUTE = 1000 * 60;
    public static final int HOUR = MINUTE * 60;
    public static final int DAY = HOUR * 24;
    public static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
        private static final long serialVersionUID = 1L;

        {
            add(new SimpleDateFormat("M/dd/yyyy"));
//            add(new SimpleDateFormat("dd-MMM-yy"));
            add(new SimpleDateFormat("dd/mmm/yy"));
            add(new SimpleDateFormat("dd.M.yyyy"));
            add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("yyyy/MM/dd hh:mm"));
            add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.MMM.yyyy"));
            add(new SimpleDateFormat("dd-MMM-yyyy"));
            add(new SimpleDateFormat("yyyy-MM-dd"));
            add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
            add(new SimpleDateFormat("yyyy-mmmm-dd hh:mm:ss"));
            add(new SimpleDateFormat("yyyy-mmmm-dd"));

        }
    };


    public static Date add(Date date, int number, int type) {
        return addOrSub(date, number, type);
    }

    public static Date sub(Date date, int number, int type) {
        return addOrSub(date, -number, type);
    }

    private static Date addOrSub(Date date, int number, int type) {
        Calendar calendar = calendarFactory(date);
        switch (type) {
            case MINUTE:
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + number);
                break;
            case HOUR:
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + number);
                break;
            case DAY:
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + number);
                break;
        }
        return calendar.getTime();
    }

    private static Calendar calendarFactory(Date date) {
        Calendar calendar = getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date beginningOfTheDay(Date date) {
        Calendar calendar = calendarFactory(date);
        return beginningOfTheDay(calendar).getTime();
    }

    public static Date endOfTheDay(Date date) {
        Calendar calendar = calendarFactory(date);
        return endOfTheDay(calendar).getTime();
    }

    public static Date beginningOfTheMonth(Date date) {
        Calendar calendar = calendarFactory(date);
        beginningOfTheDay(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date endOfTheMonth(Date date) {
        Calendar calendar = calendarFactory(date);
        endOfTheDay(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }
    public static Date yesterday() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }
    public static Date addDays(Date date, int count){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, count);
        return calendar.getTime();

    }
    private static Calendar beginningOfTheDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar;
    }

    private static Calendar endOfTheDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        return calendar;
    }

    public static Date convertToDate(String input) {
        Date date = null;
        if (null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = (Date)format.parseObject(input);
            } catch (ParseException e) {
                log.error("Try other date formats");
            }
            if (date != null) {
                break;
            }
        }

        return date;
    }

    public static Date convertToDateByFormat(String input, SimpleDateFormat format) {
        String source = input;
        SimpleDateFormat newFormat = null;
        if (format.equals(dateFormats.get(3))) {
            source = source + ":00";
            newFormat = dateFormats.get(8);
        }
        Date date = null;
        if (null == input) {
            return null;
        }
        try {
            format.setLenient(false);
            date = newFormat != null ? newFormat.parse(source) : format.parse(input);
        } catch (ParseException e) {
            log.error("Try other date formats");
        }

        return date;
    }

    public static String convertToString(Date input) {
        String date = null;
        if (null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.format(input);
            } catch (Exception e) {
                log.error("Try other date formats");
            }
            if (date != null) {
                break;
            }
        }

        return date;
    }

    public static String convertToStringByFormat(Date input, SimpleDateFormat format) {
        String date = null;
        if (null == input) {
            return null;
        }
        try {
            format.setLenient(false);
            date = format.format(input);
        } catch (Exception e) {
            log.error("Try other date formats");
        }
        return date;
    }

    public static int getHourFromTime(String time) {//"hh:mm:ss"
        if (time.isEmpty()) return -1;
        int ind = time.indexOf(":");
        try {
            if (ind == -1) return Integer.valueOf(time);
            return Integer.valueOf(time.substring(0, ind));
        } catch (Exception ex) {
            return -1;
        }

    }

    public static int getMinutesFromTime(String time) {//"hh:mm:ss"
        if (time.isEmpty()) return -1;
        int ind = time.indexOf(":");
        try {
            if (ind == -1) return -1;
            String mmss = time.substring(ind + 1);
            ind = mmss.indexOf(":");
            if (ind == -1) {
                return Integer.valueOf(mmss);
            } else {
                return Integer.valueOf(mmss.substring(0, ind));
            }
        } catch (Exception ex) {
            return -1;
        }

    }

    public static int getSecondsFromTime(String time) {//"hh:mm:ss"
        if (time.isEmpty()) return -1;
        int ind = time.indexOf(":");
        try {
            if (ind == -1) return -1;
            String mmss = time.substring(ind + 1);
            ind = mmss.indexOf(":");
            if (ind == -1) {
                return 0;
            } else {
                return Integer.valueOf(mmss.substring(ind + 1));
            }
        } catch (Exception ex) {
            return -1;
        }

    }

    public static int getYearFromTime(String time) {//"yyyy/MM/dd"
        if (time.isEmpty()) return -1;
        int ind = time.indexOf("/");
        try {
            if (ind == -1) return Integer.valueOf(time);
            return Integer.valueOf(time.substring(0, ind));
        } catch (Exception ex) {
            return -1;
        }

    }

    public static int getMonthFromTime(String time) {//"yyyy/MM/dd"
        if (time.isEmpty()) return -1;
        int ind = time.indexOf("/");
        try {
            if (ind == -1) return -1;
            String mmdd = time.substring(ind + 1);
            ind = mmdd.indexOf("/");
            if (ind == -1) {
                return Integer.valueOf(mmdd);
            } else {
                return Integer.valueOf(mmdd.substring(0, ind));
            }
        } catch (Exception ex) {
            return -1;
        }

    }

    public static int getDayFromTime(String time) {//"yyyy/MM/dd"
        if (time.isEmpty()) return -1;
        int ind = time.indexOf("/");
        try {
            if (ind == -1) return -1;
            String mmdd = time.substring(ind + 1);
            ind = mmdd.indexOf("/");
            if (ind == -1) {
                return Integer.valueOf(mmdd);
            } else {
                return Integer.valueOf(mmdd.substring(ind + 1));
            }
        } catch (Exception ex) {
            return -1;
        }

    }

    private static String getYMDfromString(String dateString) {
        return dateString.split(" ")[0];
    }

    private static String getHMSfromString(String dateString) {
        return dateString.split(" ")[1];
    }

    public static Date transformStringToDate(String input, SimpleDateFormat format) {
        if(Strings.isNullOrEmpty(input)  || format == null) return null;
        String source = input;

        if (format.equals(dateFormats.get(3))) {
            source = source + ":00";
        } else {
            return null;
        }

        int year = getYearFromTime(getYMDfromString(source));
        int month = getMonthFromTime(getYMDfromString(source));
        int day = getDayFromTime(getYMDfromString(source));
        int hh = getHourFromTime(getHMSfromString(source));
        int mm = getMinutesFromTime(getHMSfromString(source));
        int ss = getSecondsFromTime(getHMSfromString(source));

        Calendar calendar = GregorianCalendar.getInstance();
        TimeZone helsinkiTz = TimeZone.getTimeZone("Europe/Helsinki");
        calendar.setTimeZone(helsinkiTz);
        calendar.set(year, month - 1, day, hh, mm, ss);

        Date newDate = calendar.getTime();

        return newDate;
    }
}
