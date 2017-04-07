package com.adonis.ui.converters;


import com.adonis.ui.addFields.ImageField;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by oksdud on 31.03.2017.
 */
@Slf4j
public class StringToImageConverter implements Converter<String, ImageField> {

    @Override
    public Result<ImageField> convertToModel(String value, ValueContext context) {
        try {
            return Result.ok(new ImageField(value));
        } catch (Exception e) {
            return Result.error("Please enter valid date");
        }
    }

    @Override
    public String convertToPresentation(ImageField value, ValueContext context) {
        try {
            return value.getValue();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
