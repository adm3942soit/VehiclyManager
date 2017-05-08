package com.adonis.ui.persons;

import com.adonis.data.payments.CreditCard;
import com.adonis.data.service.PersonService;
import com.adonis.ui.MainUI;
import com.adonis.ui.validators.CreditCardValidator;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;

/**
 * Created by oksdud on 18.04.2017.
 */
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class CreditCardUI extends UI {

    public FieldGroup fieldGroup = new BeanFieldGroup<CreditCard>(CreditCard.class);
    public FormLayout layout = new FormLayout();
    com.vaadin.v7.ui.TextArea number = new com.vaadin.v7.ui.TextArea("Card number:");
    com.vaadin.v7.ui.TextField type = new com.vaadin.v7.ui.TextField("Type:");
    com.vaadin.v7.ui.TextField cvv2 = new com.vaadin.v7.ui.TextField("CVV2:");
    com.vaadin.v7.ui.TextField expireYear = new com.vaadin.v7.ui.TextField("Expire year:");
    com.vaadin.v7.ui.TextField expireMonth = new com.vaadin.v7.ui.TextField("Expire month:");

    PersonService personService;
    @Override
    protected void init(VaadinRequest request) {
        personService = MainUI.personsCrudView.personService;
        fieldGroup.setItemDataSource(new BeanItem<CreditCard>(CardPopup.currentCreditCard, CreditCard.class));

        number.addValidator( new Validator() {
            @Override
            public void validate(Object value) throws InvalidValueException {
                if(!CreditCardValidator.isValidString(String.valueOf(value))){
                    Notification.show("Wrong Card Number");
                    return;
                }
            }
        });
        number.setImmediate(true);

        fieldGroup.bind(number, "number");
        fieldGroup.bind(cvv2, "cvv2");
        fieldGroup.bind(expireYear, "expireYear");
        fieldGroup.bind(expireMonth, "expireMonth");
        fieldGroup.bind(type, "type");

        HorizontalLayout inLayout = new HorizontalLayout();
        Button close = new Button("Ok");
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                CreditCard creditCard = new CreditCard();
                creditCard.setNumber(number.getValue());
                creditCard.setCvv2(cvv2.getValue());
                creditCard.setExpireYear(expireYear.getValue());
                creditCard.setExpireMonth(expireMonth.getValue());
                creditCard.setType(type.getValue());
//                creditCard.setPersonId(CardPopup.person.getId());
                try {
                    fieldGroup.commit();
                    JavaScript.eval("close()");
                    getUI().close();

                } catch (Exception e) {
                    e.printStackTrace();
                    JavaScript.eval("close()");
                    getUI().close();

                }
            }
        });
//        Button cancel = new Button("Cancel");
        inLayout.addComponents( close);

        layout.addComponent(number);
        layout.addComponent(type);
        layout.addComponent(cvv2);
        layout.addComponent(expireYear);
        layout.addComponent(expireMonth);
        layout.addComponent(inLayout);
        setContent(layout);
    }
}
