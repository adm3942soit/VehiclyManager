package com.adonis.ui.main;


import at.downdrown.vaadinaddons.highchartsapi.RentaAnavailableCalendarView;
import at.downdrown.vaadinaddons.highchartsapi.RentaCalendarView;
import at.downdrown.vaadinaddons.highchartsapi.RentaPieChartView;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.ui.MainUI;
import com.adonis.ui.login.LoginView;
import com.adonis.ui.menu.Menu;
import com.adonis.ui.persons.PersonUI;
import com.adonis.ui.persons.PersonsCrudView;
import com.adonis.ui.persons.RegistrationUI;
import com.adonis.ui.print.PrintView;
import com.adonis.ui.renta.RentaCalendarForLastMonth;
import com.adonis.ui.renta.RentaHistoryCrudView;
import com.adonis.ui.vehicles.VehicleModelsCrudView;
import com.adonis.ui.vehicles.VehicleTypesCrudView;
import com.adonis.ui.vehicles.VehiclesCrudView;
import com.adonis.ui.xls.XlsView;
import com.adonis.utils.VaadinUtils;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.NoArgsConstructor;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Content of the UI when the user is logged in.
 */

@NoArgsConstructor
public class MainScreen extends HorizontalLayout implements View {
    private Menu menu;
    private MainUI mainUI;
    public static final String NAME = "MainScreen";
    PersonService personService;
    VehicleService vehicleService;
    RentaHistoryService rentaHistoryService;
    View renta1;
    View renta2;
    View renta3;
    View renta4;
    Navigator navigator;

    public MainScreen(MainUI ui) {
        mainUI = ui;
        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(ui.service, ui.vehicleService, navigator);
        menu.setStyleName(ValoTheme.MENU_ROOT);
        personService = ui.service;
        vehicleService = ui.vehicleService;
        rentaHistoryService = ui.rentaHistoryService;
        menu.addView(new VehicleModelsCrudView(ui.vehicleService), VehicleModelsCrudView.NAME, VehicleModelsCrudView.NAME, new ThemeResource("img/vehicle-model.jpg"));
        menu.addView(new VehicleTypesCrudView(ui.vehicleService), VehicleTypesCrudView.NAME, VehicleTypesCrudView.NAME, new ThemeResource("img/vehicle-type.jpg"));
        menu.addView(new VehiclesCrudView(ui.vehicleService), VehiclesCrudView.NAME, VehiclesCrudView.NAME, new ThemeResource("img/car.jpg"));//vehicles1
        menu.addView(ui.getPersonsCrudView(), PersonsCrudView.NAME, PersonsCrudView.NAME, new ThemeResource("img/customers.jpg"));
        menu.addView(new RentaHistoryCrudView(ui.rentaHistoryService, ui.service, ui.vehicleService), RentaHistoryCrudView.NAME, RentaHistoryCrudView.NAME, new ThemeResource("img/for-rent.jpg"));
        menu.addView(new RentaPieChartView(personService, rentaHistoryService, vehicleService), RentaPieChartView.NAME, RentaPieChartView.NAME, new ThemeResource("img/chartPNG.png"));
        menu.addView(new RentaCalendarView(personService, rentaHistoryService, vehicleService), RentaCalendarView.NAME, RentaCalendarView.NAME, new ThemeResource("img/barChartPNG.png"));
        menu.addView(new RentaAnavailableCalendarView(personService, rentaHistoryService, vehicleService), RentaAnavailableCalendarView.NAME, RentaAnavailableCalendarView.NAME, new ThemeResource("img/FusionChart.png"));
        menu.addView(new RentaCalendarForLastMonth(ui.service, ui.rentaHistoryService, ui.vehicleService), RentaCalendarForLastMonth.NAME, RentaCalendarForLastMonth.NAME, new ThemeResource("img/rangechart.jpg"));
//        menu.addView(new RentaCalendarForLastMonthRevert(ui.service, ui.rentaHistoryService, ui.vehicleService), RentaCalendarForLastMonthRevert.NAME, RentaCalendarForLastMonthRevert.NAME, new ThemeResource("img/barChartPNG.png"));
        menu.addView(new RegistrationUI(ui.service), "CUSTOMER REGISTRATION", "CUSTOMER REGISTRATION", new ThemeResource("img/Register-Today.jpg"));
        menu.addView(new PersonUI(ui.service, true, MainUI.loginPerson), "PROFILE", "PROFILE", new ThemeResource("img/user-icon.jpg"));
        menu.addView(new PrintView(ui.service, ui.rentaHistoryService), "PRINT", "PRINT", new ThemeResource("img/print-icon.jpg"));
        menu.addView(new XlsView(ui.service, ui.rentaHistoryService, ui.vehicleService), "LOAD", "LOAD", new ThemeResource("img/xls1.jpg"));
        menu.addView(new AboutView(), AboutView.VIEW_NAME, AboutView.VIEW_NAME, new ThemeResource("img/info.jpg"));
        menu.addView(ui.getLoginView(), "LOGOUT", "LOGOUT", new ThemeResource("img/logout.jpg"));
        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {

            boolean isLoggedIn = MainUI.loginPerson != null;
            boolean isProfileView = event.getNewView() instanceof PersonUI;
            if (isLoggedIn && isProfileView) {
                PersonUI.grid.getSelectionModel().select(MainUI.loginPerson);
                return true;
            }
            boolean exitNeeded = event.getNewView() instanceof LoginView;
            if (isLoggedIn && exitNeeded) {
                VaadinUtils.getSession().invalidate();
                VaadinUtils.getPage().reload();
            }
            return true;

        }


        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    @Override
    public void forEach(Consumer<? super Component> action) {

    }

    @Override
    public Spliterator<Component> spliterator() {
        return null;
    }
}
