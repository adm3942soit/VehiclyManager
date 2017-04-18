package com.adonis.ui.persons;

import com.adonis.data.persons.Address;
import com.adonis.data.service.PersonService;
import com.adonis.ui.MainUI;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;

/**
 * Created by oksdud on 18.04.2017.
 */
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class AddressUI extends UI {

    public FieldGroup fieldGroup = new BeanFieldGroup<Address>(Address.class);
    public FormLayout layout = new FormLayout();
    public com.vaadin.v7.ui.TextArea street = new com.vaadin.v7.ui.TextArea("Street address:");
    public com.vaadin.v7.ui.TextField zip = new com.vaadin.v7.ui.TextField("Zip code:");
    public com.vaadin.v7.ui.TextField city = new com.vaadin.v7.ui.TextField("City:");
    public com.vaadin.v7.ui.TextField country = new com.vaadin.v7.ui.TextField("Country:");

    PersonService personService;
    @Override
    protected void init(VaadinRequest request) {
        personService = MainUI.personsCrudView.personService;
        fieldGroup.setItemDataSource(new BeanItem<Address>(AddressPopup.currentAddress, Address.class));
        fieldGroup.bind(street, "street");
        fieldGroup.bind(zip, "zip");
        fieldGroup.bind(city, "city");
        fieldGroup.bind(country, "country");
        HorizontalLayout inLayout = new HorizontalLayout();
        Button close = new Button("Ok");
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Address address = new Address();
                address.setCity(city.getValue());
                address.setCountry(country.getValue());
                address.setStreet(street.getValue());
                address.setZip(zip.getValue());
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
        Button cancel = new Button("Cancel");
        inLayout.addComponents(cancel, close);

        layout.addComponent(street);
        layout.addComponent(zip);
        layout.addComponent(city);
        layout.addComponent(country);
        layout.addComponent(inLayout);
        fieldGroup.bind(street, "street");
        fieldGroup.bind(zip, "zip");
        fieldGroup.bind(city, "city");
        fieldGroup.bind(country, "country");
        setContent(layout);
    }
}