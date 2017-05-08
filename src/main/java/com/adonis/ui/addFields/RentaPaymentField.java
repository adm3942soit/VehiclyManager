package com.adonis.ui.addFields;

import com.adonis.data.persons.Person;
import com.adonis.data.renta.RentaHistory;
import com.adonis.ui.MainUI;
import com.adonis.utils.PaymentsUtils;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
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
    private PaymentsUtils paymentsUtils = PaymentsUtils.getInstance();
    private Double summa;
    public RentaPaymentField(Person person, Double summa) {
        this.person = person;
        this.summa = summa;
        this.value = false;
        initCheckBox();
        if (!this.value) {
            initButton();
        }

    }

    private Person person;

    public RentaPaymentField(Boolean value, RentaHistory rentaHistory, Person person) {
        this.value = value;
        this.summa = rentaHistory!=null?rentaHistory.getSumma():0.00;
        this.person = person;
        this.rentaHistory = rentaHistory;
        initCheckBox();
        if (!this.value) {
            initButton();
        }

    }

    @Override
    public Object getConvertedValue() {
        return this.value;
    }

    private void initButton() {
        paypal = new Button(null, new ExternalResource("https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif"));//new ExternalResource(value));
        paypal.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        paypal.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    value = paymentsUtils.payWithPaypalAcc(
                            rentaHistory!=null? MainUI.getRentaHistoryCrudView().getPersonService().findByName(rentaHistory.getPerson()):
                            person!=null?person:null, summa.longValue(), "access_token$sandbox$dkfqgn25cxb7z4t5$29193a5f4e04ed44168c1ccdf45ad5ff");
                    Notification.show("Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    Notification.show(e.getMessage());
                    value = false;
                }
                field.setValue(value);
            }
        });

    }

    private void initCheckBox() {
        if (this.value == null) this.value = false;
        field.setValue(this.value);
        field.setEnabled(false);
        field.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                RentaPaymentField.super.setInternalValue(field.getValue());
                setInternalValue(field.getValue());
            }
        });

    }

    @Override
    protected Component initContent() {
        initCheckBox();
        initButton();
        if (!this.value) layout.addComponent(paypal);
        layout.addComponent(field);
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
        field.setEnabled(false);
        if (this.value == null) this.value = false;
        field.setValue(this.value);
        if (!this.value) {
            if (paypal == null) initButton();
        }


    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSumma(Double summa) {
        this.summa = summa;
    }
}
