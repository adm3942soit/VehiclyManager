package com.adonis.ui.renta.calendar;

import com.adonis.data.renta.RentaHistory;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.utils.GeoService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.ui.Calendar;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.v7.ui.components.calendar.event.BasicEvent;
import com.vaadin.v7.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.v7.ui.components.calendar.event.CalendarEvent;
import com.vaadin.v7.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.v7.ui.components.calendar.handler.BasicWeekClickHandler;

import java.text.DateFormatSymbols;
import java.util.*;

public class RentaCalendarView extends GridLayout implements View {
    /*https://github.com/vaadin/valo-demo/blob/master/src/main/java/com/vaadin/tests/themes/valo/CalendarTest.java*/
    public static final String NAME = "RENTA CALENDAR VIEW";
    private enum Mode {
        MONTH, WEEK, DAY;
    }
    private PersonService personService;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;
    private GeoService geoService = GeoService.getInstance();

    private static final String DEFAULT_ITEMID = "DEFAULT";
    /**
     * This Gregorian calendar is used to control dates and time inside of this
     * test application.
     */
    private GregorianCalendar calendar;

    /** Target calendar component that this test application is made for. */
    private Calendar calendarComponent;

    private Date currentMonthsFirstDate;

    private final Label captionLabel = new Label("");

    private Button monthButton;

    private Button weekButton;

    private Button dayButton;

    private Button nextButton;

    private Button prevButton;

    private com.vaadin.v7.ui.ComboBox timeZoneSelect;

    private com.vaadin.v7.ui.ComboBox formatSelect;

    private com.vaadin.v7.ui.ComboBox localeSelect;

    private com.vaadin.v7.ui.CheckBox hideWeekendsButton;

    private com.vaadin.v7.ui.CheckBox readOnlyButton;

    private com.vaadin.v7.ui.TextField captionField;

    private Window scheduleEventPopup;

    private final FormLayout scheduleEventFieldLayout = new FormLayout();
    private FieldGroup scheduleEventFieldGroup = new FieldGroup();

    private Button deleteEventButton;

    private Button applyEventButton;

    private Mode viewMode = Mode.WEEK;

    private BasicEventProvider dataSource;

    private Button addNewEvent;

    /*
     * When testBench is set to true, CalendarTest will have static content that
     * is more suitable for Vaadin TestBench testing. Calendar will use a static
     * date Mon 10 Jan 2000. Enable by starting the application with a
     * "testBench" parameter in the URL.
     */
    private boolean testBench = false;

    private String calendarHeight = null;

    private String calendarWidth = null;

    private com.vaadin.v7.ui.CheckBox disabledButton;

    private Integer firstHour;

    private Integer lastHour;

    private Integer firstDay;

    private Integer lastDay;

    private Locale defaultLocale = Locale.US;

    private boolean showWeeklyView;

    private boolean useSecondResolution;

    private com.vaadin.v7.ui.DateField startDateField;
    private com.vaadin.v7.ui.DateField endDateField;

    public RentaCalendarView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {

        this.personService = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setMargin(true);
        setSpacing(true);

        setSizeFull();
        setPrimaryStyleName(ValoTheme.PANEL_WELL);
        addStyleName("backImage");
        setLocale(Locale.getDefault());

        localeSelect = createLocaleSelect();
        timeZoneSelect = createTimeZoneSelect();
        formatSelect = createCalendarFormatSelect();

        initCalendar();
        initLayoutContent();
        addInitialEvents();
    }

    private Date resolveFirstDateOfWeek(Date today,
                                        java.util.Calendar currentCalendar) {
        int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
        currentCalendar.setTime(today);
        while (firstDayOfWeek != currentCalendar
                .get(java.util.Calendar.DAY_OF_WEEK)) {
            currentCalendar.add(java.util.Calendar.DATE, -1);
        }
        return currentCalendar.getTime();
    }
    private Date resolveLastDateOfWeek(Date today,
                                       java.util.Calendar currentCalendar) {
        currentCalendar.setTime(today);
        currentCalendar.add(java.util.Calendar.DATE, 1);
        int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
        // Roll to weeks last day using firstdayofweek. Roll until FDofW is
        // found and then roll back one day.
        while (firstDayOfWeek != currentCalendar
                .get(java.util.Calendar.DAY_OF_WEEK)) {
            currentCalendar.add(java.util.Calendar.DATE, 1);
        }
        currentCalendar.add(java.util.Calendar.DATE, -1);
        return currentCalendar.getTime();
    }

    private void addInitialEvents() {
        Date originalDate = calendar.getTime();
        Date today = getToday();

        // Add a event that last a current week

        Date start = resolveFirstDateOfWeek(today, calendar);
        Date end = resolveLastDateOfWeek(today, calendar);
        CalendarTestEvent event = getNewEvent("Current week", start, end);
        event.setAllDay(true);
        event.setStyleName("colorCurrentWeek");
        event.setDescription("Current week");
        dataSource.addEvent(event);
        int i=1;
        List<String> numbers = vehicleService.findAllActiveNumbers();
        for (String number : numbers) {
            RentaHistory rentaHistory = rentaHistoryService.getHistory(number);
            Date fromDate = rentaHistory.getFromDate();
            Date toDate = rentaHistory.getToDate();
            calendar.set(java.util.Calendar.DATE, fromDate.getDate());
            start = fromDate;
            end = toDate;
            event = getNewEvent("Vehicle "+number+" "+vehicleService.findByVehicleNumber(number).getModel()
                    , start, end);
            int j = Integer.valueOf(String.valueOf(i).substring(0,1));
            event.setStyleName("color"+j);
            dataSource.addEvent(event);
            i++;
        }
        calendar.setTime(originalDate);
    }

    private void initLayoutContent() {

        initNavigationButtons();
        initHideWeekEndButton();
//        initReadOnlyButton();
//        initDisabledButton();
//        initAddNewEventButton();

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
//        hl.setHeight("15%");
        hl.setSpacing(true);
        hl.addComponent(prevButton);
        hl.addComponent(captionLabel);

        CssLayout group = new CssLayout();
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        group.addComponent(dayButton);
        group.addComponent(weekButton);
        group.addComponent(monthButton);
        hl.addComponent(group);

        hl.addComponent(nextButton);
        hl.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        hl.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(group, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

        // monthButton.setVisible(viewMode == Mode.WEEK);
        // weekButton.setVisible(viewMode == Mode.DAY);

        HorizontalLayout controlPanel = new HorizontalLayout();
        controlPanel.setPrimaryStyleName(ValoTheme.PANEL_WELL);
        controlPanel.setSpacing(true);
        controlPanel.setWidth("100%");
//        controlPanel.setHeight("15%");
        controlPanel.setMargin(new MarginInfo(false, false, true, false));
        Label viewCaption = new Label("");//Calendar
        viewCaption.addStyleName(ValoTheme.LABEL_H4);
//        viewCaption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        viewCaption.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        viewCaption.setIcon(new ThemeResource("img/calendar.jpg"));
        controlPanel.addComponent(viewCaption);
//        localeSelect.setPrimaryStyleName(ValoTheme.COMBOBOX_LARGE);
//        timeZoneSelect.setPrimaryStyleName(ValoTheme.COMBOBOX_LARGE);
//        formatSelect.setPrimaryStyleName(ValoTheme.COMBOBOX_LARGE);
        hideWeekendsButton.setPrimaryStyleName(ValoTheme.CHECKBOX_SMALL);
        controlPanel.addComponent(localeSelect);
        controlPanel.addComponent(timeZoneSelect);
        controlPanel.addComponent(formatSelect);
        controlPanel.addComponent(hideWeekendsButton);
//        controlPanel.addComponent(readOnlyButton);
//        controlPanel.addComponent(disabledButton);
//        controlPanel.addComponent(addNewEvent);
//        controlPanel.setExpandRatio(addNewEvent, 1.0f);

        controlPanel.setComponentAlignment(timeZoneSelect,Alignment.MIDDLE_LEFT);
        controlPanel.setComponentAlignment(formatSelect, Alignment.MIDDLE_LEFT);
        controlPanel.setComponentAlignment(localeSelect, Alignment.MIDDLE_LEFT);
        controlPanel.setComponentAlignment(hideWeekendsButton, Alignment.MIDDLE_LEFT);
//        controlPanel.setComponentAlignment(readOnlyButton,
//                Alignment.BOTTOM_LEFT);
//        controlPanel.setComponentAlignment(disabledButton,
//                Alignment.BOTTOM_LEFT);
//        controlPanel.setComponentAlignment(addNewEvent, Alignment.BOTTOM_RIGHT);
//        addComponent(horizontalLayout);
        addComponent(controlPanel);
        setComponentAlignment(controlPanel, Alignment.TOP_LEFT);
        addComponent(hl);
        setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
        addComponent(calendarComponent);
        setRowExpandRatio(getRows() - 1, 1.0f);
        setSizeFull();
        setPrimaryStyleName(ValoTheme.FORMLAYOUT_LIGHT);
    }
    private void initNavigationButtons() {
        monthButton = new Button("", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent  event) {
                switchToMonthView();
            }
        });
        monthButton.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        monthButton.setIcon(new ThemeResource("img/month.png"));
        monthButton.setEnabled(true);

        weekButton = new Button("", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // simulate week click
                CalendarComponentEvents.WeekClickHandler handler = (CalendarComponentEvents.WeekClickHandler) calendarComponent
                        .getHandler(CalendarComponentEvents.WeekClick.EVENT_ID);
                handler.weekClick(new CalendarComponentEvents.WeekClick(calendarComponent, calendar
                        .get(GregorianCalendar.WEEK_OF_YEAR), calendar
                        .get(GregorianCalendar.YEAR)));
            }
        });
        weekButton.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        weekButton.setIcon(new ThemeResource("img/week.png"));

        dayButton = new Button("", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // simulate day click
                BasicDateClickHandler handler = (BasicDateClickHandler) calendarComponent
                        .getHandler(CalendarComponentEvents.DateClickEvent.EVENT_ID);
                handler.dateClick(new CalendarComponentEvents.DateClickEvent(calendarComponent,
                        calendar.getTime()));
            }
        });
        dayButton.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        dayButton.setIcon(new ThemeResource("img/Day.jpg"));

        nextButton = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleNextButtonClick();
            }
        });
        nextButton.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        nextButton.setIcon(new ThemeResource("img/next.jpg"));
        prevButton = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handlePreviousButtonClick();
            }
        });
        prevButton.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        prevButton.setIcon(new ThemeResource("img/prev.jpg"));

    }

    private void initHideWeekEndButton() {
        hideWeekendsButton = new com.vaadin.v7.ui.CheckBox("Hide weekends");
        hideWeekendsButton.addStyleName(ValoTheme.CHECKBOX_SMALL);
        hideWeekendsButton.setImmediate(true);
        hideWeekendsButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        setWeekendsHidden(hideWeekendsButton.getValue());
                    }
                });
    }

    private void setWeekendsHidden(boolean weekendsHidden) {
        if (weekendsHidden) {
            int firstToShow = (GregorianCalendar.MONDAY - calendar
                    .getFirstDayOfWeek()) % 7;
            calendarComponent.setFirstVisibleDayOfWeek(firstToShow + 1);
            calendarComponent.setLastVisibleDayOfWeek(firstToShow + 5);
        } else {
            calendarComponent.setFirstVisibleDayOfWeek(1);
            calendarComponent.setLastVisibleDayOfWeek(7);
        }

    }

    private void initReadOnlyButton() {
        readOnlyButton = new com.vaadin.v7.ui.CheckBox("Read-only mode");
        readOnlyButton.addStyleName(ValoTheme.CHECKBOX_SMALL);
        readOnlyButton.setImmediate(true);
        readOnlyButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        calendarComponent.setReadOnly(readOnlyButton.getValue());
                    }
                });
    }
    private void initDisabledButton() {
        disabledButton = new com.vaadin.v7.ui.CheckBox("Disabled");
        disabledButton.addStyleName(ValoTheme.CHECKBOX_SMALL);
        disabledButton.setImmediate(true);
        disabledButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        calendarComponent.setEnabled(!disabledButton.getValue());
                    }
                });
    }
    public void initAddNewEventButton() {
        addNewEvent = new Button("Add new event");
        addNewEvent.addStyleName("primary");
        addNewEvent.addStyleName(ValoTheme.BUTTON_SMALL);
        addNewEvent.addClickListener(new com.vaadin.ui.Button.ClickListener() {

            private static final long serialVersionUID = -8307244759142541067L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Date start = getToday();
                start.setHours(0);
                start.setMinutes(0);
                start.setSeconds(0);

                Date end = getEndOfDay(calendar, start);

//                showEventPopup(createNewEvent(start, end), true);
            }
        });
    }
    private com.vaadin.v7.ui.CheckBox createCheckBox(String caption) {
        com.vaadin.v7.ui.CheckBox cb = new com.vaadin.v7.ui.CheckBox(caption);
        cb.setImmediate(true);
        return cb;
    }
    private void initFormFields(Layout formLayout,
                                Class<? extends CalendarEvent> eventClass) {

        startDateField = createDateField("Start date");
        endDateField = createDateField("End date");

        final com.vaadin.v7.ui.CheckBox allDayField = createCheckBox("All-day");
        allDayField.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = -7104996493482558021L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                if (value instanceof Boolean && Boolean.TRUE.equals(value)) {
                    setFormDateResolution(Resolution.DAY);

                } else {
                    setFormDateResolution(Resolution.MINUTE);
                }
            }

        });

        captionField = createTextField("Caption");
        captionField.setInputPrompt("Event name");
        captionField.setRequired(true);
        final com.vaadin.v7.ui.TextField whereField = createTextField("Where");
        whereField.setInputPrompt("Address or location");
        final com.vaadin.v7.ui.TextArea descriptionField = createTextArea("Description");
        descriptionField.setInputPrompt("Describe the event");
        descriptionField.setRows(3);
        // descriptionField.setRequired(true);

        final com.vaadin.v7.ui.ComboBox styleNameField = createStyleNameComboBox();
        styleNameField.setInputPrompt("Choose calendar");
        styleNameField.setTextInputAllowed(false);

        formLayout.addComponent(startDateField);
        // startDateField.setRequired(true);
        formLayout.addComponent(endDateField);
        formLayout.addComponent(allDayField);
        formLayout.addComponent(captionField);
        // captionField.setComponentError(new UserError("Testing error"));
        if (eventClass == CalendarTestEvent.class) {
            formLayout.addComponent(whereField);
        }
        formLayout.addComponent(descriptionField);
        formLayout.addComponent(styleNameField);

        scheduleEventFieldGroup.bind(startDateField, "start");
        scheduleEventFieldGroup.bind(endDateField, "end");
        scheduleEventFieldGroup.bind(captionField, "caption");
        scheduleEventFieldGroup.bind(descriptionField, "description");
        if (eventClass == CalendarTestEvent.class) {
            scheduleEventFieldGroup.bind(whereField, "where");
        }
        scheduleEventFieldGroup.bind(styleNameField, "styleName");
        scheduleEventFieldGroup.bind(allDayField, "allDay");
    }
    private com.vaadin.v7.ui.TextField createTextField(String caption) {
        com.vaadin.v7.ui.TextField f = new com.vaadin.v7.ui.TextField(caption);
        f.setNullRepresentation("");
        return f;
    }

    private com.vaadin.v7.ui.TextArea createTextArea(String caption) {
        com.vaadin.v7.ui.TextArea f = new com.vaadin.v7.ui.TextArea(caption);
        f.setNullRepresentation("");
        return f;
    }

    private com.vaadin.v7.ui.DateField createDateField(String caption) {
        com.vaadin.v7.ui.DateField f = new com.vaadin.v7.ui.DateField(caption);
        if (useSecondResolution) {
            f.setResolution(com.vaadin.v7.shared.ui.datefield.Resolution.SECOND);
        } else {
            f.setResolution(Resolution.MINUTE);
        }
        return f;
    }

    private com.vaadin.v7.ui.ComboBox createStyleNameComboBox() {
        com.vaadin.v7.ui.ComboBox s = new com.vaadin.v7.ui.ComboBox("Calendar");
        s.addContainerProperty("c", String.class, "");
        s.setItemCaptionPropertyId("c");
        Item i = s.addItem("color1");
        i.getItemProperty("c").setValue("Work");
        i = s.addItem("color2");
        i.getItemProperty("c").setValue("Personal");
        i = s.addItem("color3");
        i.getItemProperty("c").setValue("Family");
        i = s.addItem("color4");
        i.getItemProperty("c").setValue("Hobbies");
        return s;
    }
    private void initCalendar() {
        dataSource = new BasicEventProvider();

        calendarComponent = new Calendar(dataSource);
        calendarComponent.setLocale(getLocale());
        calendarComponent.setImmediate(true);

        if (calendarWidth != null || calendarHeight != null) {
            if (calendarHeight != null) {
                calendarComponent.setHeight(calendarHeight);
            }
            if (calendarWidth != null) {
                calendarComponent.setWidth(calendarWidth);
            }
        } else {
            calendarComponent.setSizeFull();
        }

        if (firstHour != null && lastHour != null) {
            calendarComponent.setFirstVisibleHourOfDay(firstHour);
            calendarComponent.setLastVisibleHourOfDay(lastHour);
        }

        if (firstDay != null && lastDay != null) {
            calendarComponent.setFirstVisibleDayOfWeek(firstDay);
            calendarComponent.setLastVisibleDayOfWeek(lastDay);
        }

        Date today = getToday();
        calendar = new GregorianCalendar(new Locale("lv","LV"));
        calendar.setTime(today);
        calendarComponent.getInternalCalendar().setTime(today);

        // Calendar getStartDate (and getEndDate) has some strange logic which
        // returns Monday of the current internal time if no start date has been
        // set
        calendarComponent.setStartDate(calendarComponent.getStartDate());
        calendarComponent.setEndDate(calendarComponent.getEndDate());
        int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
        currentMonthsFirstDate = calendar.getTime();

        updateCaptionLabel();

        if (!showWeeklyView) {
            // resetTime(false);
            // currentMonthsFirstDate = calendar.getTime();
            // calendarComponent.setStartDate(currentMonthsFirstDate);
            // calendar.add(GregorianCalendar.MONTH, 1);
            // calendar.add(GregorianCalendar.DATE, -1);
            // calendarComponent.setEndDate(calendar.getTime());
        }

        addCalendarEventListeners();
    }
    private Date getToday() {
        if (testBench) {
            GregorianCalendar testDate = new GregorianCalendar();
            testDate.set(GregorianCalendar.YEAR, 2000);
            testDate.set(GregorianCalendar.MONTH, 0);
            testDate.set(GregorianCalendar.DATE, 10);
            testDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
            testDate.set(GregorianCalendar.MINUTE, 0);
            testDate.set(GregorianCalendar.SECOND, 0);
            testDate.set(GregorianCalendar.MILLISECOND, 0);
            return testDate.getTime();
        }
        return new Date();
    }

    @SuppressWarnings("serial")
    private void addCalendarEventListeners() {
        // Register week clicks by changing the schedules start and end dates.
        calendarComponent.setHandler(new BasicWeekClickHandler() {

            @Override
            public void weekClick(CalendarComponentEvents.WeekClick event) {
                // let BasicWeekClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.weekClick(event);
                updateCaptionLabel();
                switchToWeekView();
            }
        });

        calendarComponent.setHandler(new CalendarComponentEvents.EventClickHandler() {

            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
//                showEventPopup(event.getCalendarEvent(), false);
            }
        });

        calendarComponent.setHandler(new BasicDateClickHandler() {

            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                // let BasicDateClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.dateClick(event);
                switchToDayView();
            }
        });

        calendarComponent.setHandler(new CalendarComponentEvents.RangeSelectHandler() {

            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                handleRangeSelect(event);
            }
        });
    }

    private com.vaadin.v7.ui.ComboBox createTimeZoneSelect() {
        com.vaadin.v7.ui.ComboBox s = new com.vaadin.v7.ui.ComboBox("Timezone");
        s.addStyleName("tiny");
        s.setWidth("10em");
        s.addContainerProperty("caption", String.class, "");
        s.setItemCaptionPropertyId("caption");
        s.setFilteringMode(FilteringMode.CONTAINS);

        Item i = s.addItem(DEFAULT_ITEMID);
        i.getItemProperty("caption").setValue(
                "Default (" + TimeZone.getDefault().getID() + ")");
        for (String id : TimeZone.getAvailableIDs()) {
            if (!s.containsId(id)) {
                i = s.addItem(id);
                i.getItemProperty("caption").setValue(id);
            }
        }

        if (testBench) {
            s.select("America/New_York");
        } else {
            s.select(DEFAULT_ITEMID);
        }
        s.setImmediate(true);
        s.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                updateCalendarTimeZone(event.getProperty().getValue());
            }
        });

        return s;
    }

    private com.vaadin.v7.ui.ComboBox createCalendarFormatSelect() {
        com.vaadin.v7.ui.ComboBox s = new com.vaadin.v7.ui.ComboBox("Calendar format");
        s.addStyleName("tiny");
        s.setWidth("10em");
        s.addContainerProperty("caption", String.class, "");
        s.setItemCaptionPropertyId("caption");

        Item i = s.addItem(DEFAULT_ITEMID);
        i.getItemProperty("caption").setValue("Default by locale");
        i = s.addItem(Calendar.TimeFormat.Format12H);
        i.getItemProperty("caption").setValue("12H");
        i = s.addItem(Calendar.TimeFormat.Format24H);
        i.getItemProperty("caption").setValue("24H");

        s.select(DEFAULT_ITEMID);
        s.setImmediate(true);
        s.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                updateCalendarFormat(event.getProperty().getValue());
            }
        });

        return s;
    }

    private com.vaadin.v7.ui.ComboBox createLocaleSelect() {
        com.vaadin.v7.ui.ComboBox s = new com.vaadin.v7.ui.ComboBox("Locale");
        s.addStyleName("tiny");
        s.setWidth("10em");
        s.addContainerProperty("caption", String.class, "");
        s.setItemCaptionPropertyId("caption");
        s.setFilteringMode(FilteringMode.CONTAINS);

        for (Locale l : geoService.getLocales()){//Locale.getAvailableLocales()) {
            if (!s.containsId(l)) {
                Item i = s.addItem(l);
                i.getItemProperty("caption").setValue(getLocaleItemCaption(l));
            }
        }

        s.select(geoService.getCountryCodes().getLocaleByCountryName("Latvia"));//Locale.getDefault());
        s.setImmediate(true);
        s.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                updateCalendarLocale((Locale) event.getProperty().getValue());
            }
        });

        return s;
    }
    private void updateCalendarTimeZone(Object timezoneId) {
        TimeZone tz = null;
        if (!DEFAULT_ITEMID.equals(timezoneId)) {
            tz = TimeZone.getTimeZone((String) timezoneId);
        }

        // remember the week that was showing, so we can re-set it later
        Date startDate = calendarComponent.getStartDate();
        calendar.setTime(startDate);
        int weekNumber = calendar.get(java.util.Calendar.WEEK_OF_YEAR);
        calendarComponent.setTimeZone(tz);
        calendar.setTimeZone(calendarComponent.getTimeZone());

        if (viewMode == Mode.WEEK) {
            calendar.set(java.util.Calendar.WEEK_OF_YEAR, weekNumber);
            calendar.set(java.util.Calendar.DAY_OF_WEEK,
                    calendar.getFirstDayOfWeek());

            calendarComponent.setStartDate(calendar.getTime());
            calendar.add(java.util.Calendar.DATE, 6);
            calendarComponent.setEndDate(calendar.getTime());
        }
    }

    private void updateCalendarFormat(Object format) {
        Calendar.TimeFormat calFormat = null;
        if (format instanceof Calendar.TimeFormat) {
            calFormat = (Calendar.TimeFormat) format;
        }

        calendarComponent.setTimeFormat(calFormat);
    }

    private String getLocaleItemCaption(Locale l) {
        String country = l.getDisplayCountry(getLocale());
        String language = l.getDisplayLanguage(getLocale());
        StringBuilder caption = new StringBuilder(country);
        if (caption.length() != 0) {
            caption.append(", ");
        }
        caption.append(language);
        return caption.toString();
    }
    private void updateCalendarLocale(Locale l) {
        int oldFirstDayOfWeek = calendar.getFirstDayOfWeek();
        setLocale(l);
        calendarComponent.setLocale(l);
        calendar = new GregorianCalendar(l);
        int newFirstDayOfWeek = calendar.getFirstDayOfWeek();

        // we are showing 1 week, and the first day of the week has changed
        // update start and end dates so that the same week is showing
        if (viewMode == Mode.WEEK && oldFirstDayOfWeek != newFirstDayOfWeek) {
            calendar.setTime(calendarComponent.getStartDate());
            calendar.add(java.util.Calendar.DAY_OF_WEEK, 2);
            // starting at the beginning of the week
            calendar.set(GregorianCalendar.DAY_OF_WEEK, newFirstDayOfWeek);
            Date start = calendar.getTime();

            // ending at the end of the week
            calendar.add(GregorianCalendar.DATE, 6);
            Date end = calendar.getTime();

            calendarComponent.setStartDate(start);
            calendarComponent.setEndDate(end);

            // Week days depend on locale so this must be refreshed
            setWeekendsHidden(hideWeekendsButton.getValue());
        }

    }

    private void handleNextButtonClick() {
        switch (viewMode) {
            case MONTH:
                nextMonth();
                break;
            case WEEK:
                nextWeek();
                break;
            case DAY:
                nextDay();
                break;
        }
    }

    private void handlePreviousButtonClick() {
        switch (viewMode) {
            case MONTH:
                previousMonth();
                break;
            case WEEK:
                previousWeek();
                break;
            case DAY:
                previousDay();
                break;
        }
    }
    private void handleRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
        Date start = event.getStart();
        Date end = event.getEnd();

        /*
         * If a range of dates is selected in monthly mode, we want it to end at
         * the end of the last day.
         */
        if (event.isMonthlyMode()) {
            end = getEndOfDay(calendar, end);
        }

//        showEventPopup(createNewEvent(start, end), true);
    }

    private void showEventPopup(CalendarEvent event, boolean newEvent) {
        if (event == null) {
            return;
        }

        updateCalendarEventPopup(newEvent);
        updateCalendarEventForm(event);
        // TODO this only works the first time
        captionField.focus();

        if (!getUI().getWindows().contains(scheduleEventPopup)) {
            getUI().addWindow(scheduleEventPopup);
        }

    }

    /* Initializes a modal window to edit schedule event. */
    private void createCalendarEventPopup() {
        VerticalLayout layout = new VerticalLayout();
        // layout.setMargin(true);
        layout.setSpacing(true);

        scheduleEventPopup = new Window(null, layout);
        scheduleEventPopup.setWidth("300px");
        scheduleEventPopup.setModal(true);
        scheduleEventPopup.center();

        scheduleEventFieldLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        scheduleEventFieldLayout.setMargin(false);
        layout.addComponent(scheduleEventFieldLayout);

        applyEventButton = new Button("Apply", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    commitCalendarEvent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        applyEventButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        Button cancel = new Button("Cancel", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                discardCalendarEvent();
            }
        });
        deleteEventButton = new Button("Delete", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                deleteCalendarEvent();
            }
        });
        deleteEventButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        scheduleEventPopup.addCloseListener(new Window.CloseListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(Window.CloseEvent e) {
                discardCalendarEvent();
            }
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        buttons.addComponent(deleteEventButton);
        buttons.addComponent(applyEventButton);
        buttons.setExpandRatio(applyEventButton, 1);
        buttons.setComponentAlignment(applyEventButton, Alignment.TOP_RIGHT);
        buttons.addComponent(cancel);
        layout.addComponent(buttons);

    }
    private void updateCalendarEventPopup(boolean newEvent) {
        if (scheduleEventPopup == null) {
            createCalendarEventPopup();
        }

        if (newEvent) {
            scheduleEventPopup.setCaption("New event");
        } else {
            scheduleEventPopup.setCaption("Edit event");
        }

        deleteEventButton.setVisible(!newEvent);
        deleteEventButton.setEnabled(!calendarComponent.isReadOnly());
        applyEventButton.setEnabled(!calendarComponent.isReadOnly());
    }

    private void updateCalendarEventForm(CalendarEvent event) {
        BeanItem<CalendarEvent> item = new BeanItem<CalendarEvent>(event);
        scheduleEventFieldLayout.removeAllComponents();
        scheduleEventFieldGroup = new FieldGroup();
        initFormFields(scheduleEventFieldLayout, event.getClass());
        scheduleEventFieldGroup.setBuffered(true);
        scheduleEventFieldGroup.setItemDataSource(item);
    }

    private void setFormDateResolution(Resolution resolution) {
        if (startDateField != null && endDateField != null) {
            startDateField.setResolution(resolution);
            endDateField.setResolution(resolution);
        }
    }

    private CalendarEvent createNewEvent(Date startDate, Date endDate) {
        BasicEvent event = new BasicEvent();
        event.setCaption("");
        event.setStart(startDate);
        event.setEnd(endDate);
        event.setStyleName("color1");
        return event;
    }

    /* Removes the event from the data source and fires change event. */
    private void deleteCalendarEvent() {
        BasicEvent event = getFormCalendarEvent();
        if (dataSource.containsEvent(event)) {
            dataSource.removeEvent(event);
        }
        getUI().removeWindow(scheduleEventPopup);
    }

    /* Adds/updates the event in the data source and fires change event. */
    private void commitCalendarEvent() throws Exception {
        scheduleEventFieldGroup.commit();
        BasicEvent event = getFormCalendarEvent();
        if (event.getEnd() == null) {
            event.setEnd(event.getStart());
        }
        if (!dataSource.containsEvent(event)) {
            dataSource.addEvent(event);
        }

        getUI().removeWindow(scheduleEventPopup);
    }

    private void discardCalendarEvent() {
        scheduleEventFieldGroup.discard();
        getUI().removeWindow(scheduleEventPopup);
    }

    @SuppressWarnings("unchecked")
    private BasicEvent getFormCalendarEvent() {
        BeanItem<CalendarEvent> item = (BeanItem<CalendarEvent>) scheduleEventFieldGroup
                .getItemDataSource();
        CalendarEvent event = item.getBean();
        return (BasicEvent) event;
    }

    private void nextMonth() {
        rollMonth(1);
    }

    private void previousMonth() {
        rollMonth(-1);
    }

    private void nextWeek() {
        rollWeek(1);
    }

    private void previousWeek() {
        rollWeek(-1);
    }

    private void nextDay() {
        rollDate(1);
    }

    private void previousDay() {
        rollDate(-1);
    }

    private void rollMonth(int direction) {
        calendar.setTime(currentMonthsFirstDate);
        calendar.add(GregorianCalendar.MONTH, direction);
        resetTime(false);
        currentMonthsFirstDate = calendar.getTime();
        calendarComponent.setStartDate(currentMonthsFirstDate);

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    private void rollWeek(int direction) {
        calendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
        calendar.set(GregorianCalendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek());
        resetCalendarTime(false);
        resetTime(true);
        calendar.add(GregorianCalendar.DATE, 6);
        calendarComponent.setEndDate(calendar.getTime());
    }

    private void rollDate(int direction) {
        calendar.add(GregorianCalendar.DATE, direction);
        resetCalendarTime(false);
        resetCalendarTime(true);
    }

    private void updateCaptionLabel() {
        DateFormatSymbols s = new DateFormatSymbols(getLocale());
        String month = s.getShortMonths()[calendar.get(GregorianCalendar.MONTH)];
        captionLabel.setValue(month + " "
                + calendar.get(GregorianCalendar.YEAR));
    }

    private CalendarTestEvent getNewEvent(String caption, Date start, Date end) {
        CalendarTestEvent event = new CalendarTestEvent();
        event.setCaption(caption);
        event.setStart(start);
        event.setEnd(end);

        return event;
    }

    /*
     * Switch the view to week view.
     */
    public void switchToWeekView() {
        viewMode = Mode.WEEK;
        // weekButton.setVisible(false);
        // monthButton.setVisible(true);
    }

    /*
     * Switch the Calendar component's start and end date range to the target
     * month only. (sample range: 01.01.2010 00:00.000 - 31.01.2010 23:59.999)
     */
    public void switchToMonthView() {
        viewMode = Mode.MONTH;
        // monthButton.setVisible(false);
        // weekButton.setVisible(false);

        int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);

        calendarComponent.setStartDate(calendar.getTime());

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);

        calendarComponent.setEndDate(calendar.getTime());

        calendar.setTime(getToday());
        // resetCalendarTime(true);
    }

    /*
     * Switch to day view (week view with a single day visible).
     */
    public void switchToDayView() {
        viewMode = Mode.DAY;
        // monthButton.setVisible(true);
        // weekButton.setVisible(true);
    }

    private void resetCalendarTime(boolean resetEndTime) {
        resetTime(resetEndTime);
        if (resetEndTime) {
            calendarComponent.setEndDate(calendar.getTime());
        } else {
            calendarComponent.setStartDate(calendar.getTime());
            updateCaptionLabel();
        }
    }

    /*
     * Resets the calendar time (hour, minute second and millisecond) either to
     * zero or maximum value.
     */
    private void resetTime(boolean max) {
        if (max) {
            calendar.set(GregorianCalendar.HOUR_OF_DAY,
                    calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
            calendar.set(GregorianCalendar.MINUTE,
                    calendar.getMaximum(GregorianCalendar.MINUTE));
            calendar.set(GregorianCalendar.SECOND,
                    calendar.getMaximum(GregorianCalendar.SECOND));
            calendar.set(GregorianCalendar.MILLISECOND,
                    calendar.getMaximum(GregorianCalendar.MILLISECOND));
        } else {
            calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
            calendar.set(GregorianCalendar.MINUTE, 0);
            calendar.set(GregorianCalendar.SECOND, 0);
            calendar.set(GregorianCalendar.MILLISECOND, 0);
        }
    }

    private static Date getEndOfDay(java.util.Calendar calendar, Date date) {
        java.util.Calendar calendarClone = (java.util.Calendar) calendar
                .clone();

        calendarClone.setTime(date);
        calendarClone.set(java.util.Calendar.MILLISECOND,
                calendarClone.getActualMaximum(java.util.Calendar.MILLISECOND));
        calendarClone.set(java.util.Calendar.SECOND,
                calendarClone.getActualMaximum(java.util.Calendar.SECOND));
        calendarClone.set(java.util.Calendar.MINUTE,
                calendarClone.getActualMaximum(java.util.Calendar.MINUTE));
        calendarClone.set(java.util.Calendar.HOUR,
                calendarClone.getActualMaximum(java.util.Calendar.HOUR));
        calendarClone.set(java.util.Calendar.HOUR_OF_DAY,
                calendarClone.getActualMaximum(java.util.Calendar.HOUR_OF_DAY));

        return calendarClone.getTime();
    }

    private static Date getStartOfDay(java.util.Calendar calendar, Date date) {
        java.util.Calendar calendarClone = (java.util.Calendar) calendar
                .clone();

        calendarClone.setTime(date);
        calendarClone.set(java.util.Calendar.MILLISECOND, 0);
        calendarClone.set(java.util.Calendar.SECOND, 0);
        calendarClone.set(java.util.Calendar.MINUTE, 0);
        calendarClone.set(java.util.Calendar.HOUR, 0);
        calendarClone.set(java.util.Calendar.HOUR_OF_DAY, 0);

        return calendarClone.getTime();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
