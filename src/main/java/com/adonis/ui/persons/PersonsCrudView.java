package com.adonis.ui.persons;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.ui.addFields.PersonImageField;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.FieldProvider;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by oksdud on 06.04.2017.
 */

public class PersonsCrudView extends VerticalLayout implements View {

    public static final String NAME = "CUSTOMER VIEW";

    public static final GridBasedCrudComponent<Person> personsCrud = new GridBasedCrudComponent<>(Person.class, new HorizontalSplitCrudLayout());
    public static final BeanItemContainer<Person> container = new BeanItemContainer<Person>(Person.class);
    public static List<Person> objects;
    public PersonService personService;

    public PersonsCrudView(PersonService personService) {
        this.personService = personService;
        setSizeFull();
        addStyleName("backImage");
//        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setPersonsCrudProperties(personService);
        addComponent(personsCrud);

        objects = personService.findAll();

        objects.forEach(person -> {
            container.addBean(person);
        });
        setComponentAlignment(personsCrud, Alignment.MIDDLE_CENTER);

    }

    public void setPersonsCrudProperties(PersonService personService) {
        personsCrud.setAddOperation(person -> personService.insert(person));
        personsCrud.setUpdateOperation(person -> personService.save(person));
        personsCrud.setDeleteOperation(person -> personService.delete(person));
        personsCrud.setFindAllOperation(() -> personService.findAll());

        GridLayoutCrudFormFactory<Person> formFactory = new GridLayoutCrudFormFactory<>(Person.class, 1, 10);
        formFactory.setVisiblePropertyIds("picture", "firstName", "lastName", "email", "phoneNumber", "gender", "login", "password", "birthDate", "notes", "address", "card");
        formFactory.setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        formFactory.setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");


        formFactory.setFieldType("password", com.vaadin.v7.ui.PasswordField.class);
        formFactory.setFieldProvider("picture", new FieldProvider() {
            @Override
            public Field buildField() {
                PersonImageField imageField =
                        ((Person) personsCrud.getGrid().getSelectedRow()) != null ?
                                new PersonImageField(((Person) personsCrud.getGrid().getSelectedRow()).getPicture(), ((Person) personsCrud.getGrid().getSelectedRow())) :
                                new PersonImageField();
                if (((Person) personsCrud.getGrid().getSelectedRow()) != null)
                    imageField.setInternalValue((((Person) personsCrud.getGrid().getSelectedRow()).getPicture()));
                return imageField;
            }
        });

        formFactory.setFieldType("address", AddressPopup.class);
        formFactory.setFieldProvider("address", () -> new AddressPopup());
        formFactory.setFieldCreationListener("address", field -> {
            AddressPopup address = (AddressPopup) field;
            if (((Person) personsCrud.getGrid().getSelectedRow()) != null)
                address.setInternalValue(((Person) personsCrud.getGrid().getSelectedRow()).getAddress());
            address.setValidationVisible(true);
        });
        formFactory.setFieldType("card", CardPopup.class);
        formFactory.setFieldProvider("card", () -> new CardPopup(((Person) personsCrud.getGrid().getSelectedRow()) != null?
                personService.findByCustomerId(((Person) personsCrud.getGrid().getSelectedRow()).getId()):new Person()));
        formFactory.setFieldCreationListener("card", field -> {
            CardPopup card = (CardPopup) field;

            if (((Person) personsCrud.getGrid().getSelectedRow()) != null) {
                Person person = personService.findByCustomerId(((Person) personsCrud.getGrid().getSelectedRow()).getId());
                card.setPerson(person);
                card.setInternalValue(person.getCard());
            }else {
                card.setPerson(new Person());
            }
            card.setValidationVisible(true);
        });

        formFactory.setFieldType("gender", com.vaadin.v7.ui.ComboBox.class);
        String[] gender = {"mail", "femail"};
        formFactory.setFieldProvider("gender", () -> new ComboBox("gender", Arrays.asList(gender)));
        formFactory.setFieldCreationListener("gender", field -> {
            com.vaadin.v7.ui.ComboBox comboBox = (com.vaadin.v7.ui.ComboBox) field;
            comboBox.addItem(gender[0]);
            comboBox.addItem(gender[1]);
        });

        personsCrud.setCrudFormFactory(formFactory);
        personsCrud.getGrid().setColumns("firstName", "lastName", "email", "login", "birthDate", "notes", "picture");
        personsCrud.getGrid().getColumn("birthDate").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY"));
        personsCrud.getCrudFormFactory().setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("dd/MM/yyyy"));


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public static GridBasedCrudComponent<Person> getPersonsCrud() {
        return personsCrud;
    }
}
