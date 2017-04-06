package com.adonis.ui.vehicles;

import com.adonis.data.service.VehicleService;
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
public class VehiclesCrudView extends VerticalLayout implements View {

    public static final String NAME = "VEHICLES VIEW";

    public final GridBasedCrudComponent<Vehicle> vehiclesCrud = new GridBasedCrudComponent<>(Vehicle.class, new HorizontalSplitCrudLayout());

    public VehiclesCrudView(VehicleService vehicleService) {
//        CustomLayout content = new CustomLayout("persons");
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setVehiclesCrudProperties(vehicleService);
        addComponent(vehiclesCrud);


        setComponentAlignment(vehiclesCrud, Alignment.MIDDLE_CENTER);

    }

    public void setVehiclesCrudProperties(VehicleService vehicleService) {
        GridLayoutCrudFormFactory<Vehicle> formFactory = new GridLayoutCrudFormFactory<>(Vehicle.class, 1, 10);
        vehiclesCrud.setCrudFormFactory(formFactory);

        vehiclesCrud.setAddOperation(vehicle -> vehicleService.insert(vehicle));
        vehiclesCrud.setUpdateOperation(vehicle -> vehicleService.save(vehicle));
        vehiclesCrud.setDeleteOperation(vehicle -> vehicleService.delete(vehicle));
        vehiclesCrud.setFindAllOperation(() -> vehicleService.findAll());
        vehiclesCrud.getCrudFormFactory().setVisiblePropertyIds("vehicleNmbr", "licenseNmbr", "make", "model", "year", "status", "vehicleType", "active", "location", "vinNumber");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
        vehiclesCrud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
        vehiclesCrud.getGrid().setColumns("vehicleNmbr", "licenseNmbr", "make", "model", "year", "status", "vehicleType", "active", "location", "vinNumber");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
