package com.adonis.ui.persons;

/**
 * Created by oksdud on 10.04.2017.
 */

import com.adonis.data.persons.Address;
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
    public AddressPopup() {
    }

    @Override
    protected Component initContent() {
        FormLayout layout = new FormLayout();
        final Window window = new Window("Edit address", layout);
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
                getUI().addWindow(window);
            }
        });

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

        window.center();
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
        Address currentAddress = address!=null?address:new Address();
        super.setInternalValue(currentAddress);
        fieldGroup.setItemDataSource(new BeanItem<Address>(currentAddress));
    }
}