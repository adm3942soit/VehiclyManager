package com.adonis.ui.persons;

import com.adonis.data.persons.Person;
import com.adonis.ui.converters.DateConverter;
import com.adonis.ui.login.LoginView;
import com.adonis.ui.main.MainScreen;
import com.google.common.base.Strings;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.server.ExternalResource;

public class PersonView extends PersonDesign {

    Boolean view = true;

    public interface PersonSaveListener {
        void savePerson(Person person);
    }

    public interface PersonDeleteListener {
        void deletePerson(Person person);
    }

    public interface PersonAddListener {
        void addPerson(Person person);
    }

    Binder<Person> binder = new Binder<>(Person.class);

    public PersonView(PersonSaveListener saveEvt, PersonDeleteListener delEvt, PersonAddListener addListener, Boolean view) {
        this.view = view;
        binder.forField(picture).bind("picture");
        binder.forField(dayOfBirth).withConverter(new DateConverter()).bind("birthDate");
        binder.bindInstanceFields(this);

        if (view) {
            picture.setVisible(false);

        } else {
            if (Strings.isNullOrEmpty(picture.getValue())) {
                picture.setVisible(true);
            } else
                picture.setVisible(false);
            add.setVisible(false);
        }

        picture.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                if (!event.getValue().isEmpty()) {
                    pictureImage.setSource(new ExternalResource(picture.getValue()));
                }
            }
        });
        save.addClickListener(evt -> {
            try {
                Person person = binder.getBean() != null ? binder.getBean() : new Person();
                if (Strings.isNullOrEmpty(person.getFirstName())) {
                    person.setFirstName(firstName.getValue());
                }
                if (Strings.isNullOrEmpty(person.getLastName())) {
                    person.setLastName(lastName.getValue());
                }
                if (Strings.isNullOrEmpty(person.getEmail())) {
                    person.setEmail(email.getValue());
                }
                if (person.getBirthDate() == null) {
                    person.setBirthDate(DateConverter.getDate(dayOfBirth.getValue()));
                }
                if (Strings.isNullOrEmpty(person.getLogin())) {
                    person.setLogin(login.getValue());
                }
                if (Strings.isNullOrEmpty(person.getPassword())) {
                    person.setPassword(password.getValue());
                }
                if (Strings.isNullOrEmpty(person.getPicture())) {
                    person.setPicture(picture.getValue());
                }
                if (Strings.isNullOrEmpty(person.getNotes())) {
                    person.setNotes(notes.getValue());
                }

                if (person != null && person.getId() != null) {
                    saveEvt.savePerson(person);
                } else {
                    addListener.addPerson(person);
                    pictureImage.setSource(new ExternalResource(picture.getValue()));
                }
                binder.writeBean(person);
                if (!view) getUI().getNavigator().navigateTo(LoginView.NAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        add.addClickListener(evt -> {
            try {
                this.view = false;
                picture.setVisible(true);
                this.setPerson(new Person());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        cancel.addClickListener(evt -> {
            getUI().getNavigator().navigateTo(MainScreen.NAME);
        });

        delete.addClickListener(evt -> {
            delEvt.deletePerson(binder.getBean());
        });
    }

    public void setPerson(Person selectedRow) {
        binder.setBean(selectedRow);
        if (!Strings.isNullOrEmpty(selectedRow.getPicture())) {
            pictureImage.setSource(new ExternalResource(selectedRow.getPicture()));
            picture.setValue(selectedRow.getPicture());
            picture.setVisible(!view);
        } else {
            pictureImage.setSource(new ExternalResource(""));
            picture.setValue("");
            picture.setVisible(true);
        }
    }

}
