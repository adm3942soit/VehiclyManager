package com.adonis.ui.print;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by oksdud on 13.04.2017.
 */
public class PrintView extends CustomComponent implements View {

    PersonService personService;
    RentaHistoryService rentaHistoryService;
    // The view root layout
    VerticalLayout viewLayout = new VerticalLayout();

    public PrintView(PersonService personService, RentaHistoryService rentaHistoryService) {
        this.personService = personService;
        this.rentaHistoryService = rentaHistoryService;
        setSizeFull();
        addStyleName(ValoTheme.LAYOUT_WELL);


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

//        composition(layout);
        viewLayout.addComponent(printPersons);
        viewLayout.addComponent(printRenta);
        setCompositionRoot(viewLayout);
    }

//    void composition(VerticalLayout layout) {
//        // BEGIN-EXAMPLE: layout.popupview.composition
//        com.vaadin.v7.ui.TextField tf = new com.vaadin.v7.ui.TextField();
//        layout.addComponent(tf);
//
//        Table table = new Table(null, TableExample.generateContent());
//        table.setSelectable(true);
//
//        final PopupView popup = new PopupView("Small", table);
//        layout.addComponent(popup);
//
//        tf.addListener(new Property.ValueChangeListener() {
//            private static final long serialVersionUID = -7331971790077682727L;
//
//            public void valueChange(Property.ValueChangeEvent event) {
//                popup.setPopupVisible(true);
//            }
//        });
//        tf.setImmediate(true);
//        // END-EXAMPLE: layout.popupview.composition
//    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
