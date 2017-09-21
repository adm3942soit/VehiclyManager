package com.adonis.ui.vehicles;

import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.VehicleType;
import com.adonis.ui.addFields.VehicleTypeImageField;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.Field;
import org.vaadin.crudui.crud.AddOperationListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.FieldProvider;
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
        addStyleName("backImage");
        setVehiclesCrudProperties(vehicleService);
        addComponent(vehiclesCrud);
        setComponentAlignment(vehiclesCrud, Alignment.MIDDLE_CENTER);

    }

    public void setVehiclesCrudProperties(VehicleService vehicleService) {
        GridLayoutCrudFormFactory<VehicleType> formFactory = new GridLayoutCrudFormFactory<>(VehicleType.class, 1, 5);
        vehiclesCrud.setCrudFormFactory(formFactory);
        vehiclesCrud.setAddOperation(new AddOperationListener<VehicleType>() {
            @Override
            public VehicleType perform(VehicleType vehicleType) {
                return vehicleService.insertType(vehicleType);
            }
        });
        vehiclesCrud.setUpdateOperation(vehicle -> vehicleService.save(vehicle));
        vehiclesCrud.setDeleteOperation(vehicle -> vehicleService.delete(vehicle));
        vehiclesCrud.setFindAllOperation(() -> vehicleService.findAllTypes());
        vehiclesCrud.getCrudFormFactory().setVisiblePropertyIds("picture", "type" );
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setFieldProvider("picture",
                new FieldProvider() {
                    @Override
                    public Field buildField() {
                        VehicleTypeImageField imageField =
                                ((VehicleType) vehiclesCrud.getGrid().getSelectedRow()) != null ?
                                        new VehicleTypeImageField(((VehicleType) vehiclesCrud.getGrid().getSelectedRow()).getPicture(), ((VehicleType) vehiclesCrud.getGrid().getSelectedRow())):
                                        new VehicleTypeImageField();
                        if(((VehicleType)vehiclesCrud.getGrid().getSelectedRow())!=null)
                            imageField.setInternalValue((((VehicleType)vehiclesCrud.getGrid().getSelectedRow()).getPicture()));
                        return imageField;
                    }
                }
                );
//        vehiclesCrud.getCrudFormFactory().setFieldType("picture", com.vaadin.v7.ui.TextField.class);
//        vehiclesCrud.getCrudFormFactory().setFieldCreationListener("picture", field -> {
//            com.vaadin.v7.ui.TextField textField = (com.vaadin.v7.ui.TextField) field;
//            textField.addStyleName(ValoTheme.TEXTFIELD_LARGE);
//            textField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//            textField.setIcon(new ThemeResource("img/SUV/2017LI.jpg"));
//
//        });
        vehiclesCrud.getGrid().setColumns("type", "picture");

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
