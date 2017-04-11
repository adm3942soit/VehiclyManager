package com.adonis.ui.addFields;

import com.adonis.data.vehicles.VehicleModel;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;

/**
 * Created by oksdud on 07.04.2017.
 */
public class VehicleModelImageField extends com.vaadin.v7.ui.CustomField<String> {

    private String value;
    private Image  image;
    private VehicleModel vehicleModel;
    private FormLayout layout = new FormLayout();
    private com.vaadin.v7.ui.TextField textField = new com.vaadin.v7.ui.TextField("");

    public VehicleModelImageField() {
    }

    public VehicleModelImageField(String value, VehicleModel model) {
        this.value = value;
        this.vehicleModel = model;
        if(value!=null) {
            image = new Image(null, new ExternalResource(value));//new ExternalResource(value));
            textField.setValue(value);

        }

    }
    @Override
    public Object getConvertedValue() {
        return this.value;
    }

    @Override
    protected Component initContent() {
        if(value!=null) {
            image = new Image(null, new ExternalResource(value));//new ExternalResource(value));
            textField.setValue(value);

        }
        if(image!=null) {
            image.setWidth(90, Unit.PIXELS);
            image.setHeight(90, Unit.PIXELS);
            layout.addComponent(image);
        }
        layout.addComponent(textField);
        textField.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                VehicleModelImageField.super.setInternalValue(textField.getValue());
                setInternalValue(textField.getValue());
            }
        });
        return layout;
    }


    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public void setInternalValue(String picture) {
        String curPicture = picture!=null?picture:"";
        super.setInternalValue(curPicture);
        this.value = curPicture;
        if( image == null ) image = new Image(null, new ExternalResource(value));//new ExternalResource(value));
        textField.setValue(value);
        image.setSource(new ExternalResource(curPicture));
        image.setWidth(90, Unit.PIXELS);
        image.setHeight(90, Unit.PIXELS);

    }

}
