package com.adonis.ui.addFields;

import com.adonis.data.vehicles.VehicleType;
import com.google.common.base.Strings;
import com.vaadin.annotations.PropertyId;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.ui.Form;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by oksdud on 07.04.2017.
 */
public class ImageField extends Form {

    private String value;
    private Image  image;
    private FieldGroup fieldGroup;


    @PropertyId("picture")
    private com.vaadin.v7.ui.TextField textField = new com.vaadin.v7.ui.TextField("Picture");

    public ImageField() {
        super(new FormLayout());
        initContent();
    }

    public ImageField(Layout formLayout, String value) {
        super(formLayout);
        this.value = value;
        initContent();
    }
    public ImageField( String value) {
        super(new FormLayout());
        this.value = value;
        initContent();
    }

    protected Component initContent() {
        FormLayout layout = new FormLayout();
        if(image!=null) {
            layout.addComponent(image);
        }
        if(value!=null)textField.setValue(value);
        layout.addComponent(textField);
        fieldGroup = new BeanFieldGroup<VehicleType>(VehicleType.class);
        fieldGroup.bind(textField, "picture");
        return layout;
    }

    public void doSetValue(String value) {
        this.value = value;
        if(value!=null) {
            image = new Image(null, new ExternalResource(value));
        }
    }

    @Override
    public String getValue() {
        return this.value;
    }


    @Override
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(this.value);
    }

    @Override
    public void clear() {
        this.value = "";
    }

    @Override
    public void forEach(Consumer<? super Component> action) {

    }

    @Override
    public Spliterator<Component> spliterator() {
        return null;
    }

    @Override
    public void addValidator(Validator validator) {
        return;
    }

}
