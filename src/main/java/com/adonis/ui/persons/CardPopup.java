package com.adonis.ui.persons;

/**
 * Created by oksdud on 10.04.2017.
 */

import com.adonis.data.payments.CreditCard;
import com.adonis.data.persons.Person;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;

public class CardPopup extends com.vaadin.v7.ui.CustomField<CreditCard> {

    private FieldGroup fieldGroup = new BeanFieldGroup<CreditCard>(CreditCard.class);
    public static  CreditCard currentCreditCard;
    public static Person person;
    public CardPopup() {
    }
    static final Action esc = new ShortcutAction("Close window",
            ShortcutAction.KeyCode.ESCAPE, null);
    static final Action[] actions = new Action[] { esc };
    FormLayout layout = new FormLayout();
    final ClosableDialog window = new ClosableDialog("Edit credit card", layout);

    public CardPopup(Person person) {
        this.person = person;
        currentCreditCard = person.getCard();
    }

    @Override
    protected Component initContent() {
        window.setClosable(true);
        com.vaadin.v7.ui.TextArea number = new com.vaadin.v7.ui.TextArea("Card number:");
        com.vaadin.v7.ui.TextField type = new com.vaadin.v7.ui.TextField("Type:");
        com.vaadin.v7.ui.TextField cvv2 = new com.vaadin.v7.ui.TextField("CVV2:");
        com.vaadin.v7.ui.TextField expireYear = new com.vaadin.v7.ui.TextField("Expire year:");
        com.vaadin.v7.ui.TextField expireMonth = new com.vaadin.v7.ui.TextField("Expire month:");
        layout.addComponent(number);
        layout.addComponent(type);
        layout.addComponent(cvv2);
        layout.addComponent(expireYear);
        layout.addComponent(expireMonth);
        fieldGroup.bind(number, "number");
        fieldGroup.bind(cvv2, "cvv2");
        fieldGroup.bind(type, "type");
        fieldGroup.bind(expireYear, "expireYear");
        fieldGroup.bind(expireMonth, "expireMonth");
        Button button = new Button("Open credit card editor", new ClickListener() {

            public void buttonClick(ClickEvent event) {

//                getUI().addWindow(window);

            }
        });
        BrowserWindowOpener browserWindowOpener = new BrowserWindowOpener(CreditCardUI.class);
        browserWindowOpener.setFeatures("height=400,width=400,resizable");
        browserWindowOpener.extend(button);

        window.addCloseListener(new CloseListener() {
            public void windowClose(CloseEvent e) {
                try {

                    CreditCard creditCard = new CreditCard();
                    creditCard.setNumber(number.getValue());
                    creditCard.setCvv2(cvv2.getValue());
                    creditCard.setExpireYear(expireYear.getValue());
                    creditCard.setExpireMonth(expireMonth.getValue());
                    creditCard.setType(type.getValue());

                    fieldGroup.commit();
                } catch (FieldGroup.CommitException ex) {
                    ex.printStackTrace();
                }
            }
        });

//        window.center();
        window.setWidth(null);
        layout.setWidth(null);
        layout.setMargin(true);
        return button;
    }

    @Override
    public Class<CreditCard> getType() {
        return CreditCard.class;
    }

    @Override
    public void setInternalValue(CreditCard creditCard) {
        currentCreditCard = creditCard!=null?creditCard:new CreditCard();
        super.setInternalValue(currentCreditCard);
        fieldGroup.setItemDataSource(new BeanItem<CreditCard>(currentCreditCard));
    }
    class ClosableDialog extends Window implements Action.Handler {

        ClosableDialog(String caption, Component component) {
            setModal(true);
            setCaption(caption);
            addActionHandler(this);
//            Button ok = new Button("Ok");
//            addComponent(ok);
//            ok.focus();
            setContent(component);
        }

        public void handleAction(Action action, Object sender, Object target) {
            if (action == esc) {
//                ((Window) getParent()).removeWindow(this);
                getUI().removeWindow(window);
            }
        }

        public Action[] getActions(Object target, Object sender) {
            return actions;
        }
        @Override
        public void close()
        {
            getUI().removeWindow(window);
        }
    }

    public static void setPerson(Person person) {
        CardPopup.person = person;
        CardPopup.currentCreditCard = person.getCard();
    }
}