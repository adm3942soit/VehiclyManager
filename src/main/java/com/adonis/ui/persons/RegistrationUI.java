package com.adonis.ui.persons;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by oksdud on 04.04.2017.
 */
public class RegistrationUI extends CustomComponent implements com.vaadin.navigator.View {

    private PersonService service;

    public static final String NAME = "RegistrationUI";

    private Person customer;
    PersonView editor = new PersonView(this::savePerson, this::deletePerson, this::addPerson, false);

    public RegistrationUI (PersonService personService){
        this.service = personService;
        VerticalLayout viewLayout = new VerticalLayout();
        viewLayout.setSizeFull();
        viewLayout.addComponent(editor);
        setCompositionRoot(viewLayout);

    }
    private void savePerson(Person person) {
        service.update(person);
    }

    private void deletePerson(Person person) {
        service.delete(person);
    }
    private void addPerson(Person person){
        service.insert(person);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    @Override
    public void forEach(Consumer<? super Component> action) {

    }

    @Override
    public Spliterator<Component> spliterator() {
        return null;
    }
}
