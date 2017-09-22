package com.adonis.ui.renta;

import com.adonis.data.persons.Person;
import com.adonis.data.renta.RentaHistory;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.ui.addFields.RentaPaymentField;
import com.adonis.ui.converters.DatesConverter;
import com.adonis.ui.converters.StringOfInstantToSqlTimestampConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.BeanItem;
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
import java.text.NumberFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by oksdud on 06.04.2017.
 */
public class RentaHistoryCrudView extends VerticalLayout implements View {

    public static final String NAME = "RENTA HISTORY VIEW";
    public static final BeanItemContainer<RentaHistory> container = new BeanItemContainer<RentaHistory>(RentaHistory.class);
    public static List<RentaHistory> objects;
    public static RentaHistory selectedRentaHistoryRecord = null;
    RentaPaymentField rentaPaymentField;
    Double price, priceDay, priceWeek, priceMonth, summa;// = 0.0;
    Timestamp parsedValueFrom, parsedValueTo;
    BeanItem<RentaHistory> beanItem = new BeanItem<RentaHistory>(new RentaHistory());
    final Property<Double> priceProperty = (Property<Double>) beanItem
            .getItemProperty("price");

    com.vaadin.v7.ui.TextField priceTextField = new TextField("price", priceProperty) {
        @Override
        public Object getConvertedValue() {
            return price;
        }
    };
    final Property<Double> priceDayProperty = (Property<Double>) beanItem
            .getItemProperty("priceDay");

    com.vaadin.v7.ui.TextField priceDayTextField = new TextField("priceDay", priceDayProperty) {
        @Override
        public Object getConvertedValue() {
            return priceDay;
        }
    };

    final Property<Double> priceWeekProperty = (Property<Double>) beanItem
            .getItemProperty("priceWeek");

    com.vaadin.v7.ui.TextField priceWeekTextField = new TextField("priceWeek", priceWeekProperty) {
        @Override
        public Object getConvertedValue() {
            return priceWeek;
        }
    };
    final Property<Double> priceMonthProperty = (Property<Double>) beanItem
            .getItemProperty("priceMonth");

    com.vaadin.v7.ui.TextField priceMonthTextField = new TextField("priceMonth", priceMonthProperty) {
        @Override
        public Object getConvertedValue() {
            return priceMonth;
        }
    };

    final Property<Double> summaProperty = (Property<Double>) beanItem
            .getItemProperty("summa");
    com.vaadin.v7.ui.TextField summaTextField = new TextField("summa", summaProperty) {
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
                parsedValueFrom = DatesConverter.convertValue(dateString);
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
                parsedValueTo = DatesConverter.convertValue(dateString);
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
    private Person person;

    @PostConstruct
    private void init() {
        summaTextField.setConverter(StringToDoubleConverter.class);
        priceTextField.setConverter(StringToDoubleConverter.class);
    }

    public RentaHistoryCrudView(RentaHistoryService service, PersonService personService, VehicleService vehicleService) {
        this.personService = personService;
        this.vehicleService = vehicleService;
        setSizeFull();
        setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addStyleName("backImage");

        objects = service.findAll();

        objects.forEach(rentaHistory -> {
            container.addBean(rentaHistory);
        });

        setCrudProperties(service);
        addComponent(crud);


        setComponentAlignment(crud, Alignment.MIDDLE_CENTER);

    }

    private RentaHistory insert(RentaHistoryService service, RentaHistory history) {
        try {
            price = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceTextField.getValue()).doubleValue();
            priceDay = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceDayTextField.getValue()).doubleValue();
            priceWeek = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceWeekTextField.getValue()).doubleValue();
            priceMonth = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceMonthTextField.getValue()).doubleValue();
            summa = NumberFormat.getNumberInstance(Locale.getDefault()).parse(summaTextField.getValue()).doubleValue();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        history.setPrice(price);
        history.setPriceDay(priceDay);
        history.setPriceWeek(priceWeek);
        history.setPriceMonth(priceMonth);
        history.setSumma(summa);
        return service.insert(history);
    }

    private RentaHistory update(RentaHistoryService service, RentaHistory history) {
        try {
            price = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceTextField.getValue()).doubleValue();
            priceDay = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceDayTextField.getValue()).doubleValue();
            priceWeek = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceWeekTextField.getValue()).doubleValue();
            priceMonth = NumberFormat.getNumberInstance(Locale.getDefault()).parse(priceMonthTextField.getValue()).doubleValue();
            summa = NumberFormat.getNumberInstance(Locale.getDefault()).parse(summaTextField.getValue()).doubleValue();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        history.setPrice(price);
        history.setPriceDay(priceDay);
        history.setPriceWeek(priceWeek);
        history.setPriceMonth(priceMonth);
        history.setSumma(summa);
        return service.save(history);
    }

    public void setCrudProperties(RentaHistoryService service) {
        crud.setAddOperation(history -> insert(service, history));
        crud.setUpdateOperation(history -> update(service, history));
        crud.setDeleteOperation(history -> service.delete(history));
        crud.setFindAllOperation(() -> service.findAll());

        GridLayoutCrudFormFactory<RentaHistory> formFactory = new GridLayoutCrudFormFactory<>(RentaHistory.class, 1, 10);
        formFactory.setVisiblePropertyIds("person", "vehicle", "fromDate", "toDate", "price","priceDay","priceWeek","priceMonth", "summa", "paid");
        formFactory.setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        formFactory.setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");


        formFactory.setFieldType("person", ComboBox.class);
        formFactory.setFieldProvider("person", () -> new ComboBox("person", personService.findAllNames()));
        formFactory.setFieldCreationListener("person", field -> {
            ComboBox comboBox = (ComboBox) field;
            List<String> persons = personService.findAllNames();
            persons.forEach(person -> comboBox.addItem(person));
            comboBox.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {
                    person = personService.findByName((String) comboBox.getValue());
                }
            });
        });
        formFactory.setFieldProvider("price", new FieldProvider() {
            @Override
            public Field buildField() {
                priceTextField.setConverter(StringToDoubleConverter.class);
                // priceTextField.setConvertedValue(String.valueOf(price));
                return priceTextField;
            }
        });
        formFactory.setFieldProvider("priceDay", new FieldProvider() {
            @Override
            public Field buildField() {
                priceDayTextField.setConverter(StringToDoubleConverter.class);
                // priceTextField.setConvertedValue(String.valueOf(price));
                return priceDayTextField;
            }
        });
        formFactory.setFieldProvider("priceWeek", new FieldProvider() {
            @Override
            public Field buildField() {
                priceWeekTextField.setConverter(StringToDoubleConverter.class);
                // priceTextField.setConvertedValue(String.valueOf(price));
                return priceWeekTextField;
            }
        });
        formFactory.setFieldProvider("priceMonth", new FieldProvider() {
            @Override
            public Field buildField() {
                priceMonthTextField.setConverter(StringToDoubleConverter.class);
                // priceTextField.setConvertedValue(String.valueOf(price));
                return priceMonthTextField;
            }
        });

        formFactory.setFieldProvider("paid", new FieldProvider() {
            @Override
            public Field buildField() {
                selectedRentaHistoryRecord = (RentaHistory) crud.getGrid().getSelectedRow();
                rentaPaymentField =
                        selectedRentaHistoryRecord != null ?
                                new RentaPaymentField(
                                        selectedRentaHistoryRecord.getPaid(),
                                        selectedRentaHistoryRecord,
                                        person) :
                                new RentaPaymentField(person, summa);
                if (selectedRentaHistoryRecord != null) {
                    rentaPaymentField.setInternalValue(selectedRentaHistoryRecord.getPaid());
                }
                return rentaPaymentField;
            }
        });
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
                    priceDay = vehicleService.findByVehicleNumber(vehicle_nmbr).getPriceDay();
                    if (priceDayTextField != null && priceDay != 0) {
                        priceDayTextField.setConverter(StringToDoubleConverter.class);
                        priceDayTextField.setValue(String.valueOf(priceDay));
                        priceDayTextField.setEnabled(false);

                    }

                    priceWeek = vehicleService.findByVehicleNumber(vehicle_nmbr).getPriceWeek();
                    if (priceWeekTextField != null && priceWeek != 0) {
                        priceWeekTextField.setConverter(StringToDoubleConverter.class);
                        priceWeekTextField.setValue(String.valueOf(priceWeek));
                        priceWeekTextField.setEnabled(false);

                    }
                    priceMonth = vehicleService.findByVehicleNumber(vehicle_nmbr).getPriceMonth();
                    if (priceMonthTextField != null && priceMonth != 0) {
                        priceMonthTextField.setConverter(StringToDoubleConverter.class);
                        priceMonthTextField.setValue(String.valueOf(priceMonth));
                        priceMonthTextField.setEnabled(false);

                    }

                }
            });
        });
        crud.getGrid().getColumn("fromDate").setRenderer(new com.vaadin.v7.ui.renderers.DateRenderer("%1$te/%1$tm/%1$tY %1$tH:%1$tM:%1$tS"));


        formFactory.setFieldProvider("fromDate", new FieldProvider() {
            @Override
            public Field buildField() {
                fromDateDateField.setResolution(Resolution.SECOND);
                fromDateDateField.setDateFormat("yyyy-MM-dd HH:mm:ss");
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
                        parsedValueFrom = DatesConverter.getTimeStamp(dateFrom);
                        parsedValueTo = DatesConverter.getTimeStamp(dateTo);
                        if (dateFrom != null && dateTo != null
                                && priceTextField != null && priceTextField.getValue() != null
                                && priceDayTextField != null && priceDayTextField.getValue() != null
                                && priceWeekTextField != null && priceWeekTextField.getValue() != null
                                && priceMonthTextField != null && priceMonthTextField.getValue() != null
                                ) {
                            long countHours = (dateTo.getTime() - dateFrom.getTime()) / 1000 / 60 / 60;
                            long countDays = countHours/ (24);
                            long countWeeks = countDays/7;
                            long countMonthes = countDays/28;
                            if (summaTextField != null && price != null && priceDay != null && priceWeek != null && priceMonth != null) {
                                summaTextField.setConverter(StringToDoubleConverter.class);
                                summa = countDays<1?
                                        (price * countHours)
                                        :(countWeeks<1?
                                               (priceDay * countDays):
                                               (countMonthes<1?
                                                       (priceWeek * countWeeks):
                                                       (priceMonth * countMonthes))
                                          );
                                summaTextField.setValue(String.valueOf(summa));
                                summaTextField.setEnabled(false);
                                if (rentaPaymentField != null) {
                                    rentaPaymentField.setPerson(person != null ? personService.findByCustomerId(person.getId()) : null);
                                    rentaPaymentField.setSumma(summa);
                                    if (selectedRentaHistoryRecord != null) {
                                        selectedRentaHistoryRecord.setSumma(summa);
                                        selectedRentaHistoryRecord.setPrice(price);
                                        selectedRentaHistoryRecord.setPriceDay(priceDay);
                                        selectedRentaHistoryRecord.setPriceWeek(priceWeek);
                                        selectedRentaHistoryRecord.setPriceMonth(priceMonth);
                                    }

                                }

                            }
                        }
                    }
                });
                return toDateDateField;
            }
        });
        crud.setCrudFormFactory(formFactory);
        crud.getGrid().setColumns("person", "vehicle", "fromDate", "toDate", "price","priceDay", "priceWeek","priceMonth","summa", "paid");


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public PersonService getPersonService() {
        return personService;
    }
}
