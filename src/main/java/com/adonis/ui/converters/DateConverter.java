package com.adonis.ui.converters;


import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by oksdud on 31.03.2017.
 */
@Slf4j
public class DateConverter implements Converter<LocalDate, Date> {
    @Override
    public Result<Date> convertToModel(LocalDate value, ValueContext context) {
        try {
            return Result.ok(getDate(value));
        } catch (Exception e) {
            return Result.error("Please enter valid date");
        }
    }

    @Override
    public LocalDate convertToPresentation(Date value, ValueContext context) {
        try {
            return getLocalDate(value);
        } catch (Exception e) {
            String er = "Exception "+e.toString();
            log.error(er);
            return null;
        }
    }

    /*
        class MyConverter  {
            @Override
            public Result<Integer> convertToModel(String fieldValue, ValueContext context) {
                // Produces a converted value or an error
                try {
                    // ok is a static helper method that creates a Result
                    return Result.ok(Integer.valueOf(fieldValue));
                } catch (NumberFormatException e) {
                    // error is a static helper method that creates a Result
                    return Result.error("Please enter a number");
                }
            }

            @Override
            public String convertToPresentation(Integer integer, ValueContext context) {
                // Converting to the field type should always succeed,
                // so there is no support for returning an error Result.
                return String.valueOf(integer);
            }
        }
    */
    public static LocalDate getLocalDate(Date date) {
        return date == null ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date getDate(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
