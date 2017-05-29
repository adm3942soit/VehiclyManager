package com.adonis.ui.xls;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.Form;

import java.io.*;

/**
 * Created by oksdud on 13.04.2017.
 */
public class XlsView extends CustomComponent implements View {

    PersonService personService;
    RentaHistoryService rentaHistoryService;
    VehicleService vehicleService;
    VerticalLayout viewLayout = new VerticalLayout();

    public XlsView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        this.personService = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;
        setSizeFull();
        addStyleName(ValoTheme.LAYOUT_WELL);

        Form f = new Form();
        f.setPrimaryStyleName(ValoTheme.TABLE_COMPACT);
        com.vaadin.v7.ui.TextField fieldName = new com.vaadin.v7.ui.TextField("Name file with persons");
        fieldName.setPrimaryStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        fieldName.setEnabled(false);
        com.vaadin.v7.ui.TextArea fieldResult = new com.vaadin.v7.ui.TextArea("Result");
        fieldResult.setPrimaryStyleName(ValoTheme.TEXTAREA_SMALL);
        fieldResult.setEnabled(false);
        f.addField("name", fieldName);
        f.getLayout().addComponent(new Upload("CSV with persons", new Upload.Receiver() {

            public OutputStream receiveUpload(String filename, String MIMEType) {
                fieldName.setValue(filename);
                personService.loadPersons(filename);
                Notification.show("Successfully load persons into db!");
                fieldResult.setValue("Successfully load persons into db!");
                fieldResult.setVisible(true);
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return outputStream;

            }
        }));
//        f.addField("result", fieldResult);

        com.vaadin.v7.ui.TextField fieldName1 = new com.vaadin.v7.ui.TextField("Name file with models");
        fieldName1.setPrimaryStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        fieldName1.setEnabled(false);
//        com.vaadin.v7.ui.TextArea fieldResult1 = new com.vaadin.v7.ui.TextArea("Result");
        f.addField("name1", fieldName1);
//        fieldResult1.setEnabled(false);
//        fieldResult1.setPrimaryStyleName(ValoTheme.TEXTAREA_SMALL);
        f.getLayout().addComponent(new Upload("CSV with models of vehicles", new Upload.Receiver() {

            public OutputStream receiveUpload(String filename, String MIMEType) {
                fieldName1.setValue(filename);
                vehicleService.loadVechicleModels(filename);
                Notification.show("Successfully load models into db!");
                fieldResult.setValue("Successfully load models into db!");
                fieldResult.setVisible(true);
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return outputStream;

            }
        }));
//        f.addField("result1", fieldResult1);


        com.vaadin.v7.ui.TextField fieldName2 = new com.vaadin.v7.ui.TextField("Name file with types");
        fieldName2.setPrimaryStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        fieldName2.setEnabled(false);
//        com.vaadin.v7.ui.TextArea fieldResult2 = new com.vaadin.v7.ui.TextArea("Result");
//        fieldResult2.setPrimaryStyleName(ValoTheme.TEXTAREA_SMALL);
//        fieldResult2.setEnabled(false);
        f.addField("name2", fieldName2);
        f.getLayout().addComponent(new Upload("CSV with types of vehicles", new Upload.Receiver() {

            public OutputStream receiveUpload(String filename, String MIMEType) {
                fieldName2.setValue(filename);
                vehicleService.loadVechicleTypes(filename);
                Notification.show("Successfully load types into db!");
                fieldResult.setValue("Successfully load types into db!");
                fieldResult.setVisible(true);
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return outputStream;

            }
        }));
        f.addField("result", fieldResult);

        viewLayout.addComponent(f);

        setCompositionRoot(viewLayout);
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
