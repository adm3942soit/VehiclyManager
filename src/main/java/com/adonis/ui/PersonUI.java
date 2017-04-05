package com.adonis.ui;

import com.adonis.data.persons.Person;
import com.adonis.data.persons.service.PersonService;
import com.google.common.collect.Lists;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;

import java.util.List;

/**
 * Created by oksdud on 29.03.2017.
 */
public class PersonUI extends CustomComponent implements View {

   // @Autowired
    PersonService service;

    public static final String NAME = "PersonUI";

    Person customer;
    com.adonis.ui.PersonView editor = new PersonView(this::savePerson, this::deletePerson, this::addPerson, true);
    List customers= Lists.newArrayList();
    Grid<Person> grid = new Grid();
    HorizontalSplitPanel splitter = new HorizontalSplitPanel(grid, editor);
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();



//    @PostConstruct
//    protected void init(VaadinRequest request) {
    public PersonUI (PersonService personService){
        this.service = personService;
        setSizeFull();
        updateGrid();
        grid.addColumn(Person::getFirstName).setCaption("First name");
        grid.addColumn(Person::getLastName).setCaption("Last name");
        grid.addColumn(Person::getEmail).setCaption("Email");
        grid.addColumn(Person::getBirthDate).setCaption("BirthDay");
        grid.addColumn(Person::getPicture).setCaption("Picture");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(e -> updateForm());
        grid.getEditor().addSaveListener(new EditorSaveListener() {
            @Override
            public void onEditorSave(EditorSaveEvent event) {
                List<Person> listPersons = (List<Person>)event.getGrid().getSelectedItems();
                listPersons.forEach(person -> service.update(person));
            }
        });
        grid.setSizeFull();
        editor.setSizeFull();
        splitter.setFirstComponent(grid);
        splitter.setSecondComponent(editor);
        splitter.setSizeFull();

//        setContent(splitter);

        viewLayout.setSizeFull();
        viewLayout.addComponentsAndExpand(grid, editor, splitter);
        viewLayout.setComponentAlignment(splitter, Alignment.MIDDLE_CENTER);
//        viewLayout.addComponent(editor);
//        viewLayout.addComponent(splitter);
        setCompositionRoot(viewLayout);
        selectDefault();
    }

    private void selectDefault() {
        if (!customers.isEmpty()) {
            grid.getSelectionModel().select((Person)this.customers.get(0));
        }
    }


    private void updateForm() {
        if (!grid.getSelectedItems().isEmpty()) {
            customer = (Person) grid.getSelectionModel().getFirstSelectedItem().get();
            customer = service.findByFirstLastNameEmail(customer.getFirstName(), customer.getLastName(), customer.getEmail());
            editor.setPerson(customer);
            grid.getEditor().save();
        }
    }
    private void updateGrid() {
        customers = service.findAll();
        grid.setItems(customers);
    }

    public void savePerson(Person person) {
        service.update(person);
        updateGrid();
    }

    public void deletePerson(Person person) {
        service.delete(person);
        updateGrid();
    }
    public void addPerson(Person person){
        service.insert(person);
        updateGrid();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
