package com.adonis.ui.vehicles;

import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.VehicleModel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import org.vaadin.crudui.crud.AddOperationListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.List;

/**
 * Created by oksdud on 06.04.2017.
 */
public class VehicleModelsCrudView extends VerticalLayout implements View {

    public static final String NAME = "VEHICLE-MODELS VIEW";

    public final GridBasedCrudComponent<VehicleModel> vehiclesCrud = new GridBasedCrudComponent<>(VehicleModel.class, new HorizontalSplitCrudLayout());

    public VehicleModelsCrudView(VehicleService vehicleService) {
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setVehiclesCrudProperties(vehicleService);
        addComponent(vehiclesCrud);
        setComponentAlignment(vehiclesCrud, Alignment.MIDDLE_CENTER);

    }

    public void setVehiclesCrudProperties(VehicleService vehicleService) {
        GridLayoutCrudFormFactory<VehicleModel> formFactory = new GridLayoutCrudFormFactory<>(VehicleModel.class, 1, 5);
        vehiclesCrud.setCrudFormFactory(formFactory);
        vehiclesCrud.setAddOperation(new AddOperationListener<VehicleModel>() {
            @Override
            public VehicleModel perform(VehicleModel vehicleModel) {
//                picture = VehicleModel.getPicture();
                return vehicleService.insertModel(vehicleModel);
            }
        });

        final BeanFieldGroup<VehicleModel> binder = new BeanFieldGroup<VehicleModel>(VehicleModel.class);
        binder.setFieldFactory(new DefaultFieldGroupFieldFactory() {

            @Override
            public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {

                if (type.isAssignableFrom(String.class) && fieldType.isAssignableFrom(ComboBox.class)) {
                    return (T) new ComboBox();
                }

                return super.createField(type, fieldType);
            }

        });
        vehiclesCrud.setUpdateOperation(vehicle -> vehicleService.save(vehicle));
        vehiclesCrud.setDeleteOperation(vehicle -> vehicleService.delete(vehicle));
        vehiclesCrud.setFindAllOperation(() -> vehicleService.findAllModels());
        vehiclesCrud.getCrudFormFactory().setVisiblePropertyIds("vehicleType", "model", "comment");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setFieldType("vehicleType", ComboBox.class);
        vehiclesCrud.getCrudFormFactory().setFieldProvider("vehicleType", () -> new ComboBox("vehicleType", vehicleService.findAllTypesNames()));
        vehiclesCrud.getCrudFormFactory().setFieldCreationListener("vehicleType", field -> {
            com.vaadin.v7.ui.ComboBox comboBox = (com.vaadin.v7.ui.ComboBox) field;
            List<String> items = vehicleService.findAllTypesNames();
            items.forEach(item -> {
                comboBox.addItem(item);
            });
            comboBox.setContainerDataSource(new BeanItemContainer<>(String.class, vehicleService.findAllTypesNames()));
        });

        vehiclesCrud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
        vehiclesCrud.getGrid().setColumns("vehicleType", "model", "comment");

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
