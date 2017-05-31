package com.adonis.ui.print;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.ui.MainUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
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
    File personsFile = null;
    File historyFile = null;
    File vehiclesFile = null;

    FileDownloader personsFileDownloader = null;
    Resource resourcePersonsFile = null;
    FileDownloader historyFileDownloader = null;
    Resource resourceHistoryFile = null;
    FileDownloader vehiclesFileDownloader = null;
    Resource resourceVehiclesFile = null;

    public PrintView(PersonService personService, RentaHistoryService rentaHistoryService) {
//        addExtension(downloader);
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
        Button xlsPersonDownload = new Button("persons.xls download");
        xlsPersonDownload.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsPersonDownload.setIcon(new ThemeResource("img/cloud-download.png"));
        xlsPersonDownload.setEnabled(false);
        xlsPerson.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsPerson.setIcon(new ThemeResource("img/xls2.jpg"));
        xlsPerson.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                personsFile = createXLS("persons.xls", MainUI.personsCrudView.objects);
                resourcePersonsFile = new FileResource(new File(personsFile.getAbsolutePath()));
                personsFileDownloader = new FileDownloader(resourcePersonsFile);
                xlsPersonDownload.setEnabled(true);
                personsFileDownloader.extend(xlsPersonDownload);
                Notification.show("Successfully!");
                fieldResult.setValue("Created file " + personsFile.getAbsolutePath() + " successfully!");
            }
        });



        horizontalLayout1.addComponentsAndExpand(printPersons, xlsPerson, xlsPersonDownload);
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        Button xlsRentaDownload = new Button("history_renta.xls download");
        xlsRentaDownload.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsRentaDownload.setIcon(new ThemeResource("img/cloud-download.png"));
        xlsRentaDownload.setEnabled(false);

        Button xlsRenta = new Button("db history_renta into xls");
        xlsRenta.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsRenta.setIcon(new ThemeResource("img/xls2.jpg"));
        xlsRenta.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                historyFile = createXLSRenta("renta.xls", MainUI.rentaHistoryCrudView.objects);
                resourceHistoryFile = new FileResource(new File(historyFile.getAbsolutePath()));
                historyFileDownloader = new FileDownloader(resourceHistoryFile);
                xlsRentaDownload.setEnabled(true);
                historyFileDownloader.extend(xlsRentaDownload);
                Notification.show("Successfully!");
                fieldResult.setValue("Created file " + historyFile.getAbsolutePath() + " successfully!");
            }
        });
        horizontalLayout2.addComponentsAndExpand(printRenta, xlsRenta, xlsRentaDownload);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();

        Button xlsVehiclesDownload = new Button("vehicles.xls download");
        xlsVehiclesDownload.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsVehiclesDownload.setIcon(new ThemeResource("img/cloud-download.png"));

        Button xlsVehicles = new Button("db vehicles into xls");
        xlsVehicles.setIcon(new ThemeResource("img/xls2.jpg"));
        xlsVehicles.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        xlsVehicles.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                vehiclesFile = createXLSVehicles("vehicles.xls", MainUI.getVehiclesCrudView().objects);
                resourceVehiclesFile = new FileResource(new File(vehiclesFile.getAbsolutePath()));
                vehiclesFileDownloader = new FileDownloader(resourceVehiclesFile);
                xlsVehiclesDownload.setEnabled(true);
                vehiclesFileDownloader.extend(xlsVehiclesDownload);
                Notification.show("Successfully!");
                fieldResult.setValue("Created file " + vehiclesFile.getAbsolutePath() + " successfully!");
            }
        });

        horizontalLayout3.addComponentsAndExpand(printVehicles, xlsVehicles, xlsVehiclesDownload);
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
