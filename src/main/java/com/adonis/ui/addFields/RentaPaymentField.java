package com.adonis.ui.addFields;

import com.adonis.data.renta.RentaHistory;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by oksdud on 07.04.2017.
 */
public class RentaPaymentField extends com.vaadin.v7.ui.CustomField<Boolean> {

    private Boolean value;
    private Button paypal;
    private RentaHistory rentaHistory;
    private FormLayout layout = new FormLayout();
    private com.vaadin.v7.ui.CheckBox field = new com.vaadin.v7.ui.CheckBox("");

    public RentaPaymentField() {
    }

    public RentaPaymentField(Boolean value, RentaHistory rentaHistory) {
        this.value = value;
        this.rentaHistory = rentaHistory;
        if(value!=null) {
            paypal = new Button(null, new ExternalResource("https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif"));//new ExternalResource(value));
            field.setValue(value);

        }

    }
    @Override
    public Object getConvertedValue() {
        return this.value;
    }

    @Override
    protected Component initContent() {
        if(value!=null) {
            paypal = new Button(null, new ExternalResource("https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif"));//new ExternalResource(value));
            field.setValue(value);

        }
        if(paypal!=null) {
            paypal.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
            layout.addComponent(paypal);
        }
        layout.addComponent(field);
        field.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                      RentaPaymentField.super.setInternalValue(field.getValue());
                      setInternalValue(field.getValue());
            }
        });
        return layout;
    }


    @Override
    public Class<? extends Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public void setInternalValue(Boolean paid) {
        super.setInternalValue(paid);
        this.value = paid;
        if( paypal == null ) paypal = new Button(null, new ExternalResource("https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif"));//new ExternalResource(value));
        field.setValue(value);

    }

}
