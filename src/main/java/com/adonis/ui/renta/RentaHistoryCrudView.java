package com.adonis.ui.renta;

import com.adonis.data.renta.RentaHistory;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.ui.converters.StringOfInstantToSqlTimestampConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.FieldProvider;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by oksdud on 06.04.2017.
 */
public class RentaHistoryCrudView extends VerticalLayout implements View {

    public static final String NAME = "RENTA HISTORY VIEW";
    public static final BeanItemContainer<RentaHistory> container = new BeanItemContainer<RentaHistory>(RentaHistory.class);
    public static List<RentaHistory> objects;

    Double price;// = 0.0;
    com.vaadin.v7.ui.TextField priceTextField;
    com.vaadin.v7.ui.TextField summaTextField;
    com.vaadin.v7.ui.DateField fromDateDateField = new com.vaadin.v7.ui.DateField("fromDate") {
        Timestamp parsedValue;
        @Override
        protected Timestamp handleUnparsableDateString(
                String dateString) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                parsedValue = new Timestamp(dateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)));
                return parsedValue;
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        @Override
        public Object getConvertedValue() {
            return this.parsedValue;
        }

    };
    com.vaadin.v7.ui.DateField toDateDateField = new com.vaadin.v7.ui.DateField("toDate") {
        Timestamp parsedValue;
        @Override
        protected Timestamp handleUnparsableDateString(
                String dateString) {

            try {
                // try to parse with alternative format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                parsedValue = new Timestamp(dateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0)));
                return parsedValue;
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        @Override
        public Object getConvertedValue() {
            return this.parsedValue;
        }

    };

    public final GridBasedCrudComponent<RentaHistory> crud = new GridBasedCrudComponent<>(RentaHistory.class, new HorizontalSplitCrudLayout());
    private PersonService personService;
    private VehicleService vehicleService;

    public RentaHistoryCrudView(RentaHistoryService service, PersonService personService, VehicleService vehicleService) {
        this.personService = personService;
        this.vehicleService = vehicleService;
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        objects = service.findAll();

        objects.forEach(rentaHistory -> {
            container.addBean(rentaHistory);
        });

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
        crud.getGrid().getColumn("fromDate").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY %1$tH:%1$tM:%1$tS"));
        formFactory.setFieldProvider("fromDate", new FieldProvider() {
            @Override
            public Field buildField() {
                fromDateDateField.setResolution(Resolution.SECOND);
                fromDateDateField.setDateFormat( "yyyy-MM-dd HH:mm:ss");
                fromDateDateField.setConverter(StringOfInstantToSqlTimestampConverter.class);
                return fromDateDateField;
            }
        });
        crud.getGrid().getColumn("toDate").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY %1$tH:%1$tM:%1$tS"));
        formFactory.setFieldProvider("toDate", new FieldProvider() {
            @Override
            public Field buildField() {
                toDateDateField.setResolution(Resolution.SECOND);
                toDateDateField.setDateFormat("yyyy-MM-dd HH:mm:ss");
                toDateDateField.setConverter(StringOfInstantToSqlTimestampConverter.class);
                toDateDateField.addListener(new Listener() {
                    @Override
                    public void componentEvent(Event event) {
                        Date dateFrom = fromDateDateField.getValue();
                        Date dateTo = toDateDateField.getValue();
                        if(dateFrom!=null && dateTo!=null && priceTextField!=null && priceTextField.getValue()!=null){
                            long countMinutes = (dateTo.getTime()-dateFrom.getTime())/1000/60/60;
                           // price = Double.parseDouble(priceTextField.getValue());
                            if(summaTextField!=null && price!=null) {
                                summaTextField.setValue(String.valueOf(price * countMinutes));
                                summaTextField.setEnabled(false);
                            }
                        }
                    }
                });
                return toDateDateField;
            }
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
