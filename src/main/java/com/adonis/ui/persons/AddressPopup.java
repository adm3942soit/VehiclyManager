package com.adonis.ui.persons;

/**
 * Created by oksdud on 10.04.2017.
 */

import com.adonis.data.persons.Address;
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

public class AddressPopup extends com.vaadin.v7.ui.CustomField<Address> {

    private FieldGroup fieldGroup = new BeanFieldGroup<Address>(Address.class);
    public static  Address currentAddress;
    public AddressPopup() {
    }
    static final Action esc = new ShortcutAction("Close window",
            ShortcutAction.KeyCode.ESCAPE, null);
    static final Action[] actions = new Action[] { esc };
    FormLayout layout = new FormLayout();
    final ClosableDialog window = new ClosableDialog("Edit address", layout);

    @Override
    protected Component initContent() {
        window.setClosable(true);
        com.vaadin.v7.ui.TextArea street = new com.vaadin.v7.ui.TextArea("Street address:");
        com.vaadin.v7.ui.TextField zip = new com.vaadin.v7.ui.TextField("Zip code:");
        com.vaadin.v7.ui.TextField city = new com.vaadin.v7.ui.TextField("City:");
        com.vaadin.v7.ui.TextField country = new com.vaadin.v7.ui.TextField("Country:");
        layout.addComponent(street);
        layout.addComponent(zip);
        layout.addComponent(city);
        layout.addComponent(country);
        fieldGroup.bind(street, "street");
        fieldGroup.bind(zip, "zip");
        fieldGroup.bind(city, "city");
        fieldGroup.bind(country, "country");
        Button button = new Button("Open address editor", new ClickListener() {

            public void buttonClick(ClickEvent event) {

//                getUI().addWindow(window);

            }
        });
        BrowserWindowOpener browserWindowOpener = new BrowserWindowOpener(AddressUI.class);
        browserWindowOpener.setFeatures("height=400,width=400,resizable");
        browserWindowOpener.extend(button);

        window.addCloseListener(new CloseListener() {
            public void windowClose(CloseEvent e) {
                try {

                    Address address = new Address();
                    address.setCity(city.getValue());
                    address.setCountry(country.getValue());
                    address.setStreet(street.getValue());
                    address.setZip(zip.getValue());
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
    public Class<Address> getType() {
        return Address.class;
    }

    @Override
    public void setInternalValue(Address address) {
        currentAddress = address!=null?address:new Address();
        super.setInternalValue(currentAddress);
        fieldGroup.setItemDataSource(new BeanItem<Address>(currentAddress));
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
}