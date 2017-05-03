package com.adonis.ui.print;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.ui.MainUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;

import static com.adonis.ui.print.PrintPersonsUI.createXLS;
import static com.adonis.ui.print.PrintRentaUI.createXLSRenta;
import static com.adonis.ui.print.PrintVehiclesUI.createXLSVehicles;

/**
 * Created by oksdud on 13.04.2017.
 */
public class PrintView extends CustomComponent implements View {

    PersonService personService;
    RentaHistoryService rentaHistoryService;
    VerticalLayout viewLayout = new VerticalLayout();

    public PrintView(PersonService personService, RentaHistoryService rentaHistoryService) {
        this.personService = personService;
        this.rentaHistoryService = rentaHistoryService;
        setSizeFull();
        addStyleName(ValoTheme.LAYOUT_WELL);

        com.vaadin.v7.ui.TextArea fieldResult = new com.vaadin.v7.ui.TextArea("Result");
        fieldResult.setPrimaryStyleName(ValoTheme.TEXTAREA_SMALL);
        fieldResult.setEnabled(false);

        final com.vaadin.ui.Button printPersons = new Button("Print customers");
        printPersons.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        printPersons.setIcon(new ThemeResource("img/grupo.jpg"));
        // Create an opener extension
        BrowserWindowOpener opener =
                new BrowserWindowOpener(PrintPersonsUI.class);
        opener.setFeatures("height=400,width=400,resizable");
        opener.extend(printPersons);

        final com.vaadin.ui.Button printRenta = new Button("Print renta history");
        printRenta.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        printRenta.setIcon(new ThemeResource("img/carRental.jpeg"));
        // Create an opener extension
        BrowserWindowOpener openerRenta =
                new BrowserWindowOpener(PrintRentaUI.class);
        openerRenta.setFeatures("height=400,width=400,resizable");
        openerRenta.extend(printRenta);

        final com.vaadin.ui.Button printVehicles = new Button("Print vehicles");
        printVehicles.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        printVehicles.setIcon(new ThemeResource("img/veh.jpg"));
        // Create an opener extension
        BrowserWindowOpener openerVehicles =
                new BrowserWindowOpener(PrintVehiclesUI.class);
        openerRenta.setFeatures("height=400,width=400,resizable");
        openerVehicles.extend(printVehicles);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        Button xlsPerson = new Button("db persons into xls");
        xlsPerson.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsPerson.setIcon(new ThemeResource("img/xls2.jpg"));
        xlsPerson.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                File file = createXLS("persons.xls", MainUI.personsCrudView.objects);
                Notification.show("Successfully!");
                fieldResult.setValue("Created file "+file.getAbsolutePath()+" successfully!");
            }
        });
        horizontalLayout1.addComponentsAndExpand(printPersons, xlsPerson);
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        Button xlsRenta = new Button("db history_renta into xls");
        xlsRenta.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsRenta.setIcon(new ThemeResource("img/xls2.jpg"));
        xlsRenta.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                File file = createXLSRenta("renta.xls", MainUI.rentaHistoryCrudView.objects);
                Notification.show("Successfully!");
                fieldResult.setValue("Created file "+file.getAbsolutePath()+" successfully!");
            }
        });
        horizontalLayout2.addComponentsAndExpand(printRenta, xlsRenta);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        Button xlsVehicles = new Button("db vehicles into xls");
        xlsVehicles.setIcon(new ThemeResource("img/xls2.jpg"));
        xlsVehicles.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsVehicles.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                File file = createXLSVehicles("vehicles.xls", MainUI.getVehiclesCrudView().objects);
                Notification.show("Successfully!");
                fieldResult.setValue("Created file "+file.getAbsolutePath()+" successfully!");
            }
        });
        horizontalLayout3.addComponentsAndExpand(printVehicles, xlsVehicles);
        viewLayout.addComponent(horizontalLayout1);
        viewLayout.addComponent(horizontalLayout2);
        viewLayout.addComponent(horizontalLayout3);

        viewLayout.addComponent(fieldResult);

        setCompositionRoot(viewLayout);
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
