package com.adonis.ui.converters;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.sql.Timestamp;
import java.util.Date;

public class MyConverter implements Converter<Date, Timestamp> {

    private static final long serialVersionUID = 1L;
    public static final MyConverter INSTANCE = new MyConverter();

    @Override
    public Result<Timestamp> convertToModel(Date value, ValueContext context) {
        return value == null ? null : Result.ok(new Timestamp(value.getTime()));
    }

    @Override
    public Date convertToPresentation(Timestamp value, ValueContext context) {
        return new Date(value.getTime());
    }

    public Class<Timestamp> getModelType() {
        return Timestamp.class;
    }

    public Class<Date> getPresentationType() {
        return Date.class;
    }

    private Object readResolve() {
        return INSTANCE; // preserves singleton property
    }
}