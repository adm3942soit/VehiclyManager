package com.adonis.ui;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.ui.login.AccessControl;
import com.adonis.ui.login.BasicAccessControl;
import com.adonis.ui.login.LoginView;
import com.adonis.ui.main.MainScreen;
import com.adonis.ui.persons.PersonUI;
import com.adonis.ui.persons.PersonsCrudView;
import com.adonis.ui.persons.RegistrationUI;
import com.adonis.ui.renta.RentaHistoryCrudView;
import com.adonis.ui.vehicles.VehiclesCrudView;
import com.adonis.utils.DatabaseUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Spliterator;
import java.util.function.Consumer;

import static com.adonis.install.VehicleManagerInstaller.createShortcat;

/**
 * Created by oksdud on 03.04.2017.
 */
@SpringUI
@Theme("mytheme")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class MainUI extends UI {

    @Autowired
    public PersonService service;
    @Autowired
    public VehicleService vehicleService;
    @Autowired
    public RentaHistoryService rentaHistoryService;

    private AccessControl accessControl = new BasicAccessControl();

    PersonUI personView;
    public LoginView loginView;
    RegistrationUI registrationUI;
    public static MainScreen mainScreen;
    public static PersonsCrudView personsCrudView;
    public static RentaHistoryCrudView rentaHistoryCrudView;
    public static VehiclesCrudView vehiclesCrudView;
    public static Person loginPerson;

    @PostConstruct
    void load() {
        createShortcat();
        try {
            DatabaseUtils.createDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(service.findTotalCustomer()==0) {
            service.loadData();
        }
        if(vehicleService.findTotalVehicle()==0){
            vehicleService.loadData();
        }
        if(vehicleService.findLastType()==null){
            vehicleService.loadVechicleTypes("");
        }
        if(vehicleService.findLastModel()==null){
            vehicleService.loadVechicleModels("");
        }

        personView = new PersonUI(service, false, null);
        loginView = new LoginView(service, new LoginView.LoginListener() {
            @Override
            public void loginSuccessful() {
                showMainView();
            }
        });
        registrationUI = new RegistrationUI(service);

        personsCrudView = new PersonsCrudView(service);
        rentaHistoryCrudView = new RentaHistoryCrudView(rentaHistoryService,service, vehicleService);
        vehiclesCrudView = new VehiclesCrudView(vehicleService);

        mainScreen = new MainScreen(MainUI.this);
    }


    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);

        setLocale(request.getLocale());
        getPage().setTitle("Car Manager");
        new Navigator(this, this);
        getNavigator().addView(LoginView.NAME, loginView);
        getNavigator().addView(RegistrationUI.NAME, registrationUI);
        getNavigator().addView(PersonUI.NAME, personView);
        getNavigator().addView(MainScreen.NAME, mainScreen);
        getNavigator().addView(PersonsCrudView.NAME, personsCrudView);
        if (!accessControl.isUserSignedIn()) {
            getNavigator().navigateTo(LoginView.NAME);
        } else {
            showMainView();
        }


    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        getNavigator().navigateTo(MainScreen.NAME);
    }

    @Override
    public void forEach(Consumer<? super Component> action) {

    }

    @Override
    public Spliterator<Component> spliterator() {
        return null;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public PersonsCrudView getPersonsCrudView() {
        return personsCrudView;
    }

    public static RentaHistoryCrudView getRentaHistoryCrudView() {
        return rentaHistoryCrudView;
    }

    public static VehiclesCrudView getVehiclesCrudView() {
        return vehiclesCrudView;
    }

    @Override
    public void markAsDirty() {

    }
}
