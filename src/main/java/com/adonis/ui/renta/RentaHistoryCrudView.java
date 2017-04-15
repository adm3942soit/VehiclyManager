package com.adonis.ui.renta;

import com.adonis.data.renta.RentaHistory;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.ui.converters.DateUtils;
import com.adonis.ui.converters.StringOfInstantToSqlTimestampConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.FieldProvider;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
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

    Double price, summa;// = 0.0;
    Timestamp parsedValueFrom, parsedValueTo;
    com.vaadin.v7.ui.TextField priceTextField = new TextField("price"){
        @Override
        public Object getConvertedValue() {
            return price;
        }
    };
    com.vaadin.v7.ui.TextField summaTextField = new TextField("summa"){
        @Override
        public Object getConvertedValue() {
            return summa;
        }

    };
    com.vaadin.v7.ui.DateField fromDateDateField = new com.vaadin.v7.ui.DateField("fromDate") {

        @Override
        protected Timestamp handleUnparsableDateString(
                String dateString) {
            try {
                parsedValueFrom = DateUtils.convertValue(dateString);
                return parsedValueFrom;
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        @Override
        public Object getConvertedValue() {
            return parsedValueFrom;
        }

    };
    com.vaadin.v7.ui.DateField toDateDateField = new com.vaadin.v7.ui.DateField("toDate") {
        @Override
        protected Timestamp handleUnparsableDateString(
                String dateString) {

            try {
                // try to parse with alternative format
                parsedValueTo = DateUtils.convertValue(dateString);
                return parsedValueTo;
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        @Override
        public Object getConvertedValue() {
            return parsedValueTo;
        }

    };

    public final GridBasedCrudComponent<RentaHistory> crud = new GridBasedCrudComponent<>(RentaHistory.class, new HorizontalSplitCrudLayout());
    private PersonService personService;
    private VehicleService vehicleService;
    @PostConstruct
    private void init(){
     summaTextField.setConverter(StringToDoubleConverter.class);
        priceTextField.setConverter(StringToDoubleConverter.class);
    }

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
        formFactory.setFieldProvider("price", new FieldProvider() {
            @Override
            public Field buildField() {
                priceTextField.setConverter(StringToDoubleConverter.class);
               // priceTextField.setConvertedValue(String.valueOf(price));
                return priceTextField;
            }
        });
//        crud.getAddButton().addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                price = priceTextField.getValue()==null?null:Double.parseDouble(priceTextField.getValue());
//               summa = summaTextField.getValue()==null?null: Double.parseDouble(summaTextField.getValue());
//            }
//        });
        formFactory.setFieldProvider("summa", new FieldProvider() {
            @Override
            public Field buildField() {
                summaTextField.setConverter(StringToDoubleConverter.class);
                return summaTextField;
            }
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
                        priceTextField.setConverter(StringToDoubleConverter.class);
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
                        parsedValueFrom = DateUtils.getTimeStamp(dateFrom);
                        parsedValueTo = DateUtils.getTimeStamp(dateTo);
                        if(dateFrom!=null && dateTo!=null && priceTextField!=null && priceTextField.getValue()!=null){
                            long countMinutes = (dateTo.getTime()-dateFrom.getTime())/1000/60/60;
                            if(summaTextField!=null && price!=null) {
                                summaTextField.setConverter(StringToDoubleConverter.class);
                                summa = price * countMinutes;
                                summaTextField.setValue(String.valueOf(summa));
                                summaTextField.setEnabled(false);
                            }
                        }
                    }
                });
                return toDateDateField;
            }
        });
        crud.setCrudFormFactory(formFactory);
        crud.getGrid().setColumns("person", "vehicle", "fromDate", "toDate", "price", "summa", "paid");


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
