package com.adonis.ui.converters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

}
