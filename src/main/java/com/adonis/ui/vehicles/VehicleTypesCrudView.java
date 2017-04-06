package com.adonis.ui.vehicles;

import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.VehicleType;
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
public class VehicleTypesCrudView extends VerticalLayout implements View {

    public static final String NAME = "VEHICLE-TYPES VIEW";

    public final GridBasedCrudComponent<VehicleType> vehiclesCrud = new GridBasedCrudComponent<>(VehicleType.class, new HorizontalSplitCrudLayout());

    public VehicleTypesCrudView(VehicleService vehicleService) {
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setVehiclesCrudProperties(vehicleService);
        addComponent(vehiclesCrud);
        setComponentAlignment(vehiclesCrud, Alignment.MIDDLE_CENTER);

    }

    public void setVehiclesCrudProperties(VehicleService vehicleService) {
        GridLayoutCrudFormFactory<VehicleType> formFactory = new GridLayoutCrudFormFactory<>(VehicleType.class, 1, 5);
        vehiclesCrud.setCrudFormFactory(formFactory);

        vehiclesCrud.setAddOperation(vehicle -> vehicleService.insertType(vehicle));
        vehiclesCrud.setUpdateOperation(vehicle -> vehicleService.save(vehicle));
        vehiclesCrud.setDeleteOperation(vehicle -> vehicleService.delete(vehicle));
        vehiclesCrud.setFindAllOperation(() -> vehicleService.findAllTypes());
        vehiclesCrud.getCrudFormFactory().setVisiblePropertyIds("type", "picture");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
        vehiclesCrud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
        vehiclesCrud.getGrid().setColumns("type", "picture");

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
