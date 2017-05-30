package com.adonis.ui.addFields;

import com.adonis.data.persons.Person;
import com.adonis.data.renta.RentaHistory;
import com.adonis.ui.MainUI;
import com.adonis.ui.print.PrintReginaUI;
import com.adonis.utils.PaymentsUtils;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by oksdud on 07.04.2017.
 */
public class RentaPaymentField extends com.vaadin.v7.ui.CustomField<Boolean> {

    private Boolean value;
    private Button paypal;
    private Button print;
    private RentaHistory rentaHistory;
    private FormLayout layout = new FormLayout();
    private com.vaadin.v7.ui.CheckBox field = new com.vaadin.v7.ui.CheckBox("");
    private PaymentsUtils paymentsUtils = null;// = PaymentsUtils.getInstance();
    com.vaadin.v7.ui.TextArea fieldResult = new com.vaadin.v7.ui.TextArea("");

    private Double summa;
    public RentaPaymentField(Person person, Double summa) {
        this.person = person;
        this.summa = summa;
        this.value = false;
        initCheckBox();
        if (!this.value) {
            initPayPalButton();
        }
        initPrintButton();
        fieldResult.setPrimaryStyleName(ValoTheme.TEXTAREA_SMALL);
        fieldResult.setEnabled(false);


    }

    private Person person;

    public RentaPaymentField(Boolean value, RentaHistory rentaHistory, Person person) {
        this.value = value;
        this.summa = (rentaHistory!=null && rentaHistory.getSumma()!=null)?rentaHistory.getSumma():0.00;
        this.person = person;
        this.rentaHistory = rentaHistory;
        initCheckBox();
        if (!this.value) {
            initPayPalButton();
        }
        initPrintButton();
        fieldResult.setPrimaryStyleName(ValoTheme.TEXTAREA_SMALL);
        fieldResult.setEnabled(false);

    }

    @Override
    public Object getConvertedValue() {
        return this.value;
    }

    private void initPayPalButton() {
        if( paymentsUtils == null )paymentsUtils = PaymentsUtils.getInstance();
        paypal = new Button(null, new ExternalResource("https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif"));//new ExternalResource(value));
        paypal.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        paypal.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {

                    value = paymentsUtils.payWithPaypalAcc(
                            rentaHistory!=null? MainUI.getRentaHistoryCrudView().getPersonService().findByName(rentaHistory.getPerson()):
                            person!=null?person:null, summa.longValue());//, "access_token$sandbox$dkfqgn25cxb7z4t5$29193a5f4e04ed44168c1ccdf45ad5ff");
                    Notification.show("Successfully!");
                    fieldResult.setValue("Successfully!");
                    fieldResult.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    Notification.show(e.getMessage());
                    fieldResult.setValue(e.getMessage());
                    value = false;
                }
                field.setValue(value);
            }
        });
        paypal.setEnabled(false);//!!!!
    }
    private void initPrintButton() {
        print = new Button(null, new ExternalResource("https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif"));//new ExternalResource(value));
        print.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        print.setIcon(new ThemeResource("img/printRegina.jpg"));
        // Create an opener extension
        BrowserWindowOpener opener =
                new BrowserWindowOpener(PrintReginaUI.class);
        opener.setFeatures("height=400,width=400,resizable");
        opener.extend(print);

        print.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        print.setEnabled(this.value);//!!!!
    }

    private void initCheckBox() {
        if (this.value == null) this.value = false;
        field.setValue(this.value);
//        field.setEnabled(false);//!!!!
        field.setEnabled(true);
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
        initPayPalButton();
        initPrintButton();
        if (!this.value) layout.addComponent(paypal);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponentsAndExpand(field, print);
        layout.addComponent(horizontalLayout);
        if (!this.value) layout.addComponent(fieldResult);
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
            if (paypal == null) initPayPalButton();
        }


    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSumma(Double summa) {
        this.summa = summa;
    }
}
