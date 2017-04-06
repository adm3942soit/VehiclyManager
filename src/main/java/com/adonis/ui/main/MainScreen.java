package com.adonis.ui.main;


import com.adonis.ui.MainUI;
import com.adonis.ui.login.LoginView;
import com.adonis.ui.menu.Menu;
import com.adonis.ui.persons.PersonUI;
import com.adonis.ui.persons.PersonsCrudView;
import com.adonis.ui.persons.RegistrationUI;
import com.adonis.ui.vehicles.VehiclesCrudView;
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

    public static final String NAME = "MainScreen";
    LoginView loginView;

    public MainScreen(MainUI ui) {

        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(ui.service, ui.vehicleService, navigator);
        menu.setStyleName(ValoTheme.MENU_ROOT);
        menu.addView(new VehiclesCrudView(ui.vehicleService), VehiclesCrudView.NAME, VehiclesCrudView.NAME, new ThemeResource("img/vehicles1.jpg"));
        menu.addView(new PersonsCrudView(ui.service), PersonsCrudView.NAME, PersonsCrudView.NAME, new ThemeResource("img/customers.jpg"));
        menu.addView(new RegistrationUI(ui.service), "CUSTOMER REGISTRATION" , "CUSTOMER REGISTRATION", new ThemeResource("img/Register-Today.jpg"));
        menu.addView(new PersonUI(ui.service), "PROFILE" , "PROFILE", new ThemeResource("img/user-icon.jpg"));
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
