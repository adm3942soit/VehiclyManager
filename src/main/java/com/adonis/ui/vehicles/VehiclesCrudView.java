package com.adonis.ui.vehicles;

import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.Vehicle;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.List;

/**
 * Created by oksdud on 06.04.2017.
 */
public class VehiclesCrudView extends VerticalLayout implements View {

    public static final String NAME = "VEHICLES VIEW";

    public final GridBasedCrudComponent<Vehicle> vehiclesCrud = new GridBasedCrudComponent<>(Vehicle.class, new HorizontalSplitCrudLayout());
    public static final BeanItemContainer<Vehicle> container = new BeanItemContainer<Vehicle>(Vehicle.class);
    public static List<Vehicle> objects;

    public VehiclesCrudView(VehicleService vehicleService) {
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setVehiclesCrudProperties(vehicleService);
        addComponent(vehiclesCrud);
        objects = vehicleService.findAll();

        objects.forEach(vehicle -> {
            container.addBean(vehicle);
        });


        setComponentAlignment(vehiclesCrud, Alignment.MIDDLE_CENTER);

    }

    public void setVehiclesCrudProperties(VehicleService vehicleService) {
        GridLayoutCrudFormFactory<Vehicle> formFactory = new GridLayoutCrudFormFactory<>(Vehicle.class, 1, 10);
        vehiclesCrud.setCrudFormFactory(formFactory);

        vehiclesCrud.setAddOperation(vehicle -> vehicleService.insert(vehicle));
        vehiclesCrud.setUpdateOperation(vehicle -> vehicleService.save(vehicle));
        vehiclesCrud.setDeleteOperation(vehicle -> vehicleService.delete(vehicle));
        vehiclesCrud.setFindAllOperation(() -> vehicleService.findAll());
        vehiclesCrud.getCrudFormFactory().setVisiblePropertyIds("vehicleNmbr", "licenseNmbr", "make", "vehicleType","model", "year", "status",  "active", "location", "vinNumber", "price");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
        vehiclesCrud.getGrid().setColumns("vehicleNmbr", "licenseNmbr", "make", "vehicleType", "model", "year", "status",  "active", "location", "vinNumber", "price");

        vehiclesCrud.getCrudFormFactory().setFieldType("vehicleType", ComboBox.class);
        vehiclesCrud.getCrudFormFactory().setFieldProvider("vehicleType", () -> new ComboBox("vehicleType", vehicleService.findAllTypesNames()));
        vehiclesCrud.getCrudFormFactory().setFieldCreationListener("vehicleType", field -> {
            com.vaadin.v7.ui.ComboBox comboBox = (com.vaadin.v7.ui.ComboBox) field;
            List<String> items = vehicleService.findAllTypesNames();
            items.forEach(item -> {
                comboBox.addItem(item);
            });
        });

        vehiclesCrud.getCrudFormFactory().setFieldType("model", ComboBox.class);
        vehiclesCrud.getCrudFormFactory().setFieldProvider("model", () -> new ComboBox("model", vehicleService.findAllModelNames()));
        vehiclesCrud.getCrudFormFactory().setFieldCreationListener("model", field -> {
            com.vaadin.v7.ui.ComboBox comboBox = (com.vaadin.v7.ui.ComboBox) field;
            List<String> items = vehicleService.findAllModelNames();
            items.forEach(item -> {
                comboBox.addItem(item);
            });
        });


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
