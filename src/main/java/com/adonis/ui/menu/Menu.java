package com.adonis.ui.menu;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.Vehicle;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import lombok.NoArgsConstructor;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsive navigation menu presenting a list of available views to the user.
 */
@NoArgsConstructor
public class Menu extends CssLayout {

    private static final String VALO_MENUITEMS = "valo-menuitems";
    private static final String VALO_MENU_TOGGLE = "valo-menu-toggle";
    private static final String VALO_MENU_VISIBLE = "valo-menu-visible";
    private Navigator navigator;
    private Map<String, Button> viewButtons = new HashMap<String, Button>();

    private CssLayout menuItemsLayout;
    private CssLayout menuPart;
    public final GridBasedCrudComponent<Vehicle> vehiclesCrud = new GridBasedCrudComponent<>(Vehicle.class, new HorizontalSplitCrudLayout());
    public final GridBasedCrudComponent<Person> personsCrud = new GridBasedCrudComponent<>(Person.class, new HorizontalSplitCrudLayout());


    public Menu(PersonService personService, VehicleService vehicleService, Navigator navigator) {
        this.navigator = navigator;
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        menuPart = new CssLayout();
        menuPart.addStyleName(ValoTheme.MENU_PART);

        setPersonsCrudProperties(personService);
        setVehiclesCrudProperties(vehicleService);

        // header of the menu
        final HorizontalLayout top = new HorizontalLayout();
        top.addStyleName(ValoTheme.MENU_TITLE);
        top.setSpacing(true);

        Label title = new Label("Vehicle manager");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.setSizeUndefined();

        Image image = new Image(null, new ThemeResource("img/car.png"));
        image.setStyleName(ValoTheme.MENU_LOGO);

        top.addComponent(image);
        top.addComponent(title);
        menuPart.addComponent(top);

        // logout menu item
//        HorizontalLayout logoutLayout = new HorizontalLayout();
//        logoutLayout.addStyleName(ValoTheme.MENU_ITEM);
//        logoutLayout.setSpacing(false);
//
//        MenuBar logoutMenu = new MenuBar();
//        logoutMenu.setStyleName(VALO_MENUITEMS);
//        logoutMenu.addItem("Logout", new MenuBar.Command() {
//
//            @Override
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                VaadinSession.getCurrent().getSession().invalidate();
//                Page.getCurrent().reload();
//            }
//        });
//
//        logoutMenu.addStyleName("user-menu");
//        Image logout = new Image(null, new ThemeResource("img/logout.png"));
//        logoutLayout.addComponent(logout, 0);
//        logoutLayout.addComponent(logoutMenu, 1);
//        menuPart.addComponent(logoutLayout);

        // button for toggling the visibility of the menu when on a small screen
        final Button showMenu = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
                    menuPart.removeStyleName(VALO_MENU_VISIBLE);
                } else {
                    menuPart.addStyleName(VALO_MENU_VISIBLE);
                }
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName(VALO_MENU_TOGGLE);
//        showMenu.setIcon(FontAwesome.NAVICON);
        menuPart.addComponent(showMenu);

        // container for the navigation buttons, which are added by addView()
        menuItemsLayout = new CssLayout();
        menuItemsLayout.setPrimaryStyleName(VALO_MENUITEMS);
        menuPart.addComponent(menuItemsLayout);

        addComponent(menuPart);
        addStyleName("backImage");
    }
    public void setVehiclesCrudProperties(VehicleService vehicleService) {
        GridLayoutCrudFormFactory<Vehicle> formFactory = new GridLayoutCrudFormFactory<>(Vehicle.class, 1, 10);
        vehiclesCrud.setCrudFormFactory(formFactory);

        vehiclesCrud.setAddOperation(vehicle -> vehicleService.insert(vehicle));
        vehiclesCrud.setUpdateOperation(vehicle -> vehicleService.save(vehicle));
        vehiclesCrud.setDeleteOperation(vehicle -> vehicleService.delete(vehicle));
        vehiclesCrud.setFindAllOperation(() -> vehicleService.findAll());
        vehiclesCrud.getCrudFormFactory().setVisiblePropertyIds("vehicleNmbr", "licenseNmbr", "make", "model", "year", "status", "vehicleType", "active", "location", "vinNumber");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        vehiclesCrud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
        vehiclesCrud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
        vehiclesCrud.getGrid().setColumns("vehicleNmbr", "licenseNmbr", "make", "model", "year", "status", "vehicleType", "active", "location", "vinNumber");
    }

    public void setPersonsCrudProperties(PersonService personService){
        personsCrud.setAddOperation(person -> personService.insert(person));
        personsCrud.setUpdateOperation(person -> personService.save(person));
        personsCrud.setDeleteOperation(person -> personService.delete(person));
        personsCrud.setFindAllOperation(() -> personService.findAll());

        GridLayoutCrudFormFactory<Person> formFactory = new GridLayoutCrudFormFactory<>(Person.class, 1, 10);

        formFactory.setVisiblePropertyIds("firstName", "lastName", "email", "login", "password","birthDate", "picture", "notes");
        formFactory.setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
        formFactory.setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");


        formFactory.setFieldType("password", com.vaadin.v7.ui.PasswordField.class);
        //formFactory.setFieldType("birthDate",com.vaadin.v7.ui.DateField.class);
       // formFactory.setFieldCreationListener("birthDate", field -> ((com.vaadin.v7.ui.DateField) field).setDateFormat("dd/mm/yy"));

        personsCrud.setCrudFormFactory(formFactory);
        personsCrud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
        personsCrud.getGrid().setColumns("firstName", "lastName", "email", "login", "birthDate", "picture", "notes");


    }
    /**
     * Register a pre-created view instance in the navigation menu and in the
     * {@link Navigator}.
     *
     * @param view    view instance to register
     * @param name    view name
     * @param caption view caption in the menu
     * @param icon    view icon in the menu
     * @see Navigator#addView(String, View)
     */
    public void addView(View view, final String name, String caption,
                        com.vaadin.server.Resource icon) {
        navigator.addView(name, view);
        createViewButton(name, caption, icon);
    }

    /**
     * Register a view in the navigation menu and in the {@link Navigator} based
     * on a view class.
     *
     * @param viewClass class of the views to create
     * @param name      view name
     * @param caption   view caption in the menu
     * @param icon      view icon in the menu
     * @see Navigator#addView(String, Class)
     */
    public void addView(Class<? extends View> viewClass, final String name,
                        String caption, com.vaadin.server.Resource icon) {
        navigator.addView(name, viewClass);
        createViewButton(name, caption, icon);
    }

    private void createViewButton(final String name, String caption,
                                  com.vaadin.server.Resource icon) {
        Button button = new Button(caption, new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                navigator.navigateTo(name);

            }
        });
        button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        button.setIcon(icon);
        menuItemsLayout.addComponent(button);
        viewButtons.put(name, button);
    }

    /**
     * Highlights a view navigation button as the currently active view in the
     * menu. This method does not perform the actual navigation.
     *
     * @param viewName the name of the view to show as active
     */
    public void setActiveView(String viewName) {
        for (Button button : viewButtons.values()) {
            button.removeStyleName("selected");
        }
        Button selected = viewButtons.get(viewName);
        if (selected != null) {
            selected.addStyleName("selected");
        }
        menuPart.removeStyleName(VALO_MENU_VISIBLE);
    }
}
