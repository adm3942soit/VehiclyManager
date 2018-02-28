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
public class DatesConverter {
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
   public static java.util.Date convertSqlDate(java.sql.Date sqlDate){
       return new java.util.Date(sqlDate.getTime());
   }
   public static String utilDateToString(java.util.Date utilDate){
        if(utilDate==null){
            return "";
        }
       return new java.util.Date(utilDate.getTime()).toInstant()  // Convert `java.util.Date` to `Instant`.
               .atOffset( ZoneOffset.UTC )  // Transform `Instant` to `OffsetDateTime`.
               .format( DateTimeFormatter.ISO_LOCAL_DATE )  // Generate a String.
               .replace( "T" , " " );  // Put a SPACE in the middle.
   }
    public static String timestampToString(java.sql.Timestamp timestamp){
        return getUtilDate(timestamp).toInstant()  // Convert `java.util.Date` to `Instant`.
                .atOffset( ZoneOffset.UTC )  // Transform `Instant` to `OffsetDateTime`.
                .format( DateTimeFormatter.ISO_LOCAL_DATE_TIME )  // Generate a String.
                .replace( "T" , " " );  // Put a SPACE in the middle.
    }
    /*Timestamp->unix time*/
    public static Integer getUnixTime(Timestamp timestamp) {
        int now = Long.valueOf(getSqlTime(getUtilDate(timestamp)).getTime() / 1000).intValue();
//        int midnight = Long.valueOf(getSqlTime(DateUtils.beginningOfTheDay(getUtilDate(timestamp))).getTime() / 1000).intValue();
        return now;
    }

}
