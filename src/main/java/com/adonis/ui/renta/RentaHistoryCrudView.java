package com.adonis.ui.renta;

import com.adonis.data.renta.RentaHistory;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.List;

/**
 * Created by oksdud on 06.04.2017.
 */
public class RentaHistoryCrudView extends VerticalLayout implements View {

    public static final String NAME = "RENTA HISTORY VIEW";

    public final GridBasedCrudComponent<RentaHistory> crud = new GridBasedCrudComponent<>(RentaHistory.class, new HorizontalSplitCrudLayout());
    private PersonService personService;
    private VehicleService vehicleService;
    public RentaHistoryCrudView(RentaHistoryService service, PersonService personService, VehicleService vehicleService) {
        this.personService = personService;
        this.vehicleService = vehicleService;
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setCrudProperties(service);
        addComponent(crud);


        setComponentAlignment(crud, Alignment.MIDDLE_CENTER);

    }

    public void setCrudProperties(RentaHistoryService service){
        crud.setAddOperation(person -> service.insert(person));
        crud.setUpdateOperation(person -> service.save(person));
        crud.setDeleteOperation(person -> service.delete(person));
        crud.setFindAllOperation(() -> service.findAll());

        GridLayoutCrudFormFactory<RentaHistory> formFactory = new GridLayoutCrudFormFactory<>(RentaHistory.class, 1, 10);
        formFactory.setVisiblePropertyIds("namePerson","nameVehicle","from", "to", "price", "summa", "paid");
        formFactory.setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        formFactory.setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");


        formFactory.setFieldType("namePerson", ComboBox.class);
        formFactory.setFieldProvider("namePerson", () -> new ComboBox("namePerson", personService.findAllNames()));
        formFactory.setFieldCreationListener("namePerson", field -> {
            ComboBox comboBox = (ComboBox) field;
            List<String> persons = personService.findAllNames();
            persons.forEach(person->comboBox.addItem(person));
        });

        formFactory.setFieldType("nameVehicle", ComboBox.class);
        formFactory.setFieldProvider("nameVehicle", () -> new ComboBox("nameVehicle", vehicleService.findAllNames()));
        formFactory.setFieldCreationListener("nameVehicle", field -> {
            ComboBox comboBox = (ComboBox) field;
            List<String> vehicles = vehicleService.findAllNames();
            vehicles.forEach(vehicle->comboBox.addItem(vehicle));
            comboBox.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {

                }
            });
        });

        crud.setCrudFormFactory(formFactory);
        crud.getGrid().setColumns( "namePerson","nameVehicle","from", "to", "price", "summa", "paid");
        crud.getGrid().getColumn("from").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY"));
        crud.getCrudFormFactory().setFieldCreationListener("from", field -> ((DateField) field).setDateFormat("dd/MM/yyyy"));
        crud.getGrid().getColumn("to").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY"));
        crud.getCrudFormFactory().setFieldCreationListener("to", field -> ((DateField) field).setDateFormat("dd/MM/yyyy"));


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
