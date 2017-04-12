package com.adonis.ui.converters;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by oksdud on 12.04.2017.
 */
public class StringOfInstantToSqlTimestampConverter implements Converter<String, Timestamp> {

    // Member vars.
    final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    protected DateFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        f.setLenient(false);
        return f;
    }

    public Class<Timestamp> getModelType() {
        return Timestamp.class;
    }


    @Override
    public Result<Timestamp> convertToModel(String value, ValueContext context) {

        if (value == null) {
            return null;
        }

        // Remove leading and trailing white space
        String trimmed = value.trim();
//        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        ParsePosition parsePosition = new ParsePosition( 0 );
//        Date parsedValue = this.getFormat( locale ).parse( trimmed , parsePosition );
//        if ( parsePosition.getIndex() != trimmed.length() ) {
//            throw new Converter.ConversionException( "Could not convert '" + trimmed + "' to " + getModelType().getName() );
//        }
//        Instant instant = null;
//        try {
//            instant = Instant.parse(trimmed); // Uses DateTimeFormatter.ISO_INSTANT.
//        } catch (DateTimeParseException e) {
//            logger.error("Could not convert '" + trimmed + "' to java.time.Instant on the way to get " + getModelType().getName());
//        }
//        if (instant == null) {
//            logger.error("The instant is null after parsing. Should not be possible. Message # ACE6DA4E-44C8-434C-A2AD-F946E5CFAEFD.");
//            logger.error("The Instant is null after parsing while attempting to convert '" + trimmed + "' to java.time.Instant on the way to get " + getModelType().getName() + "Message # 77A767AB-7D42-490F-9C2F-2775F4443A8D.");
//        }
//        Timestamp parsedValue = Timestamp.from(instant);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
        Timestamp parsedValue = new Timestamp(dateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)));
        return Result.ok(parsedValue);
    }

    @Override
    public String convertToPresentation(Timestamp value, ValueContext context) {
        if (value == null) {
            return null;
        }

        Instant instant = value.toInstant();
        String dateTimeStringInIsoFormat = instant.toString();   // Uses DateTimeFormatter.ISO_INSTANT.
        return dateTimeStringInIsoFormat;
    }
}

