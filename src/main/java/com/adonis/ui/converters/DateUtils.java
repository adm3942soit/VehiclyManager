package com.adonis.ui.converters;

import com.google.common.base.Strings;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by oksdud on 04.04.2017.
 */
public class DateUtils {
    public LocalDate getLocalDate(Date date) {
        return date == null ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date getDate(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public  static LocalDateTime getLocalDateTime(Date date){
        Instant timestamp = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp, TimeZone.getDefault().toZoneId());
      return localDateTime;
    }
    /*util.Date->sql.Timestamp*/
    public static Timestamp getTimeStamp(Date date) {
        if (date==null) return null;
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /*sql.Timestamp->util.Date*/
    public static Date getUtilDate(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        return date;
    }

    /*sql.Timestamp->java.sql.Date*/
    public static java.sql.Date getSqlDate(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        return getSqlDate(date);
    }

    /*util.Date->java.sql.Date*/
    public static java.sql.Date getSqlDate(java.util.Date utilDate) {
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    /*util.Date->java.sql.Time*/
    public static java.sql.Time getSqlTime(java.util.Date utilDate) {
        java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());
        return sqlTime;
    }
   public static Timestamp convertValue(String dateString){
        if(Strings.isNullOrEmpty(dateString)) return null;
       // try to parse with alternative format
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
       Timestamp parsedValueTo = new Timestamp(dateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)));
       return parsedValueTo;
   }
}
