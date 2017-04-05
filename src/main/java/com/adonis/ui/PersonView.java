package com.adonis.ui;

import com.adonis.data.persons.Person;
import com.adonis.ui.converters.DateConverter;
import com.google.common.base.Strings;
import com.vaadin.data.Binder;
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
		binder.bindInstanceFields(this );

		if(view){
			picture.setVisible(false);
		}else {
			if(Strings.isNullOrEmpty(picture.getValue())) {
				picture.setVisible(true);
			}else
				picture.setVisible(false);
		}

		save.addClickListener(evt -> {
			try {
				Person person = binder.getBean();
				if(person!=null && person.getId()!=null) {
					saveEvt.savePerson(person);
				}else{
					addListener.addPerson(binder.getBean());
					pictureImage.setSource(new ExternalResource(picture.getValue()));
				}
				binder.writeBean(binder.getBean());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        add.addClickListener(evt->{
			try {
				this.view = false;
				picture.setVisible(true);
				this.setPerson(new Person());
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		cancel.addClickListener(evt -> {

		});

		delete.addClickListener(evt -> {
			delEvt.deletePerson(binder.getBean());
		});
	}

	public void setPerson(Person selectedRow) {
		binder.setBean(selectedRow);
		if(!Strings.isNullOrEmpty(selectedRow.getPicture())) {
			pictureImage.setSource(new ExternalResource(selectedRow.getPicture()));
			picture.setValue(selectedRow.getPicture());
			picture.setVisible(!view);
		}
		else {
			pictureImage.setSource(new ExternalResource(""));
			picture.setValue("");
			picture.setVisible(true);
		}
	}

}
