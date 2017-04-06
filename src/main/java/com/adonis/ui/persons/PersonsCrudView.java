package com.adonis.ui.persons;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.data.vehicles.Vehicle;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

/**
 * Created by oksdud on 06.04.2017.
 */
public class PersonsCrudView extends VerticalLayout implements View {

    public static final String NAME = "CUSTOMER VIEW";

    public final VerticalLayout personsArea = new VerticalLayout();
    public final VerticalLayout vehiclesArea = new VerticalLayout();
    public final GridBasedCrudComponent<Vehicle> vehiclesCrud = new GridBasedCrudComponent<>(Vehicle.class, new HorizontalSplitCrudLayout());
    public final GridBasedCrudComponent<Person> personsCrud = new GridBasedCrudComponent<>(Person.class, new HorizontalSplitCrudLayout());

    public PersonsCrudView(PersonService personService) {
//        CustomLayout content = new CustomLayout("persons");
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setPersonsCrudProperties(personService);
        addComponent(personsCrud);


        setComponentAlignment(personsCrud, Alignment.MIDDLE_CENTER);

    }

    public void setPersonsCrudProperties(PersonService personService){
        personsCrud.setAddOperation(person -> personService.insert(person));
        personsCrud.setUpdateOperation(person -> personService.save(person));
        personsCrud.setDeleteOperation(person -> personService.delete(person));
        personsCrud.setFindAllOperation(() -> personService.findAll());

        GridLayoutCrudFormFactory<Person> formFactory = new GridLayoutCrudFormFactory<>(Person.class, 1, 10);

        formFactory.setVisiblePropertyIds("firstName", "lastName", "email", "login", "password","birthDate", "picture", "notes");
        formFactory.setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        formFactory.setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");


        formFactory.setFieldType("password", com.vaadin.v7.ui.PasswordField.class);
        //formFactory.setFieldType("birthDate",com.vaadin.v7.ui.DateField.class);
        // formFactory.setFieldCreationListener("birthDate", field -> ((com.vaadin.v7.ui.DateField) field).setDateFormat("dd/mm/yy"));

        personsCrud.setCrudFormFactory(formFactory);
        personsCrud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
        personsCrud.getGrid().setColumns("firstName", "lastName", "email", "login", "birthDate", "picture", "notes");


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
