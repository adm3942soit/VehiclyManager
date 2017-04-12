package com.adonis.ui.renta;

import com.adonis.data.renta.RentaHistory;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.ui.converters.DateUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by oksdud on 06.04.2017.
 */
public class RentaHistoryCrudView extends VerticalLayout implements View {

    public static final String NAME = "RENTA HISTORY VIEW";
    Double price = 0.0;
    com.vaadin.v7.ui.TextField priceTextField;
    com.vaadin.v7.ui.TextField summaTextField;
    com.vaadin.v7.ui.DateField fromDateDateField;
    com.vaadin.v7.ui.DateField toDateDateField;
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

    public void setCrudProperties(RentaHistoryService service) {
        crud.setAddOperation(person -> service.insert(person));
        crud.setUpdateOperation(person -> service.save(person));
        crud.setDeleteOperation(person -> service.delete(person));
        crud.setFindAllOperation(() -> service.findAll());

        GridLayoutCrudFormFactory<RentaHistory> formFactory = new GridLayoutCrudFormFactory<>(RentaHistory.class, 1, 10);
        formFactory.setVisiblePropertyIds("person", "vehicle", "fromDate", "toDate", "price", "summa", "paid");
        formFactory.setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        formFactory.setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");


        formFactory.setFieldType("person", ComboBox.class);
        formFactory.setFieldProvider("person", () -> new ComboBox("person", personService.findAllNames()));
        formFactory.setFieldCreationListener("person", field -> {
            ComboBox comboBox = (ComboBox) field;
            List<String> persons = personService.findAllNames();
            persons.forEach(person -> comboBox.addItem(person));
        });

        formFactory.setFieldType("vehicle", ComboBox.class);
        formFactory.setFieldProvider("vehicle", () -> new ComboBox("vehicle", vehicleService.findAllNames()));
        formFactory.setFieldCreationListener("vehicle", field -> {
            ComboBox comboBox = (ComboBox) field;
            List<String> vehicles = vehicleService.findAllNames();
            vehicles.forEach(vehicle -> comboBox.addItem(vehicle));
            comboBox.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {
                    String vehicle_nmbr = (String) comboBox.getValue();
                    price = vehicleService.findByVehicleNumber(vehicle_nmbr).getPrice();
                    if (priceTextField != null && price != 0) {
                        priceTextField.setValue(String.valueOf(price));
                        priceTextField.setEnabled(false);
                    }
                }
            });
        });
        crud.getGrid().getColumn("fromDate").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY"));
        formFactory.setFieldCreationListener("fromDate", field -> {
            fromDateDateField = (com.vaadin.v7.ui.DateField) field;
            fromDateDateField.setDateFormat("dd/MM/yyyy");
        });
        crud.getGrid().getColumn("toDate").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY"));
        formFactory.setFieldCreationListener("toDate", field -> {
            toDateDateField = (com.vaadin.v7.ui.DateField) field;
            toDateDateField.setDateFormat("dd/MM/yyyy");
            toDateDateField.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {
                    Date dateFrom = fromDateDateField.getValue();
                    Date dateTo = toDateDateField.getValue();
                    if(dateFrom!=null && dateTo!=null && price!=null){
                        LocalDateTime oldDate = DateUtils.getLocalDateTime(dateFrom);
                        LocalDateTime newDate = DateUtils.getLocalDateTime(dateTo);
                        //count seconds between dates
                        Duration duration = Duration.between(oldDate, newDate);
                        long countMinutes = duration.getSeconds()/60;
                        summaTextField.setValue(String.valueOf(price*countMinutes) );
                        summaTextField.setEnabled(false);
                    }
                }
            });
        });

        formFactory.setFieldCreationListener("price", field -> {
            priceTextField = (com.vaadin.v7.ui.TextField) field;
        });
        formFactory.setFieldCreationListener("summa", field -> {
            summaTextField = (com.vaadin.v7.ui.TextField) field;
        });

        crud.setCrudFormFactory(formFactory);
        crud.getGrid().setColumns("person", "vehicle", "fromDate", "toDate", "price", "summa", "paid");


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
