package com.adonis.ui.menu;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.Vehicle;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import lombok.NoArgsConstructor;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
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

    public Menu(PersonService personService, VehicleService vehicleService, Navigator navigator) {
        this.navigator = navigator;
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        menuPart = new CssLayout();
        menuPart.addStyleName(ValoTheme.MENU_PART);

        // header of the menu
        final HorizontalLayout top = new HorizontalLayout();
//        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        top.setSpacing(true);
        Label title = new Label("My car rental manager");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.setSizeUndefined();

        Image image = new Image(null, new ThemeResource("img/table-logo.png"));
        image.setStyleName(ValoTheme.MENU_LOGO);

        top.addComponent(image);
        top.addComponent(title);
        menuPart.addComponent(top);

        // logout menu item
        MenuBar logoutMenu = new MenuBar();
        logoutMenu.setStyleName(VALO_MENUITEMS);
        logoutMenu.addItem("Logout",  new Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                VaadinSession.getCurrent().getSession().invalidate();
                Page.getCurrent().reload();
            }
        });

        logoutMenu.addStyleName("user-menu");
        menuPart.addComponent(logoutMenu);

        // dataBase menu item
        MenuBar personsMenu = new MenuBar();
        personsMenu.setStyleName(VALO_MENUITEMS);
        personsMenu.addItem("Customers",  new Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                menuPart.setWidth(10F, Unit.PERCENTAGE);
                addComponent(menuPart);
                final VerticalLayout area = new VerticalLayout();
                area.setSizeFull();
                GridBasedCrudComponent<Person> crud = new GridBasedCrudComponent<>(Person.class, new HorizontalSplitCrudLayout());
                crud.setAddOperation(person ->personService.save(person));
                crud.setUpdateOperation(person ->personService.save(person));
                crud.setDeleteOperation(person ->personService.delete(person));
                crud.setFindAllOperation(()->personService.findAll());
                crud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
                crud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
                crud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
                crud.getGrid().setColumns("firstName", "lastName", "email", "login", "birthDate", "picture", "notes");
                area.addComponent(crud);
                area.setWidth(90F, Unit.PERCENTAGE);
                addComponent(area);

            }
        });

        personsMenu.addStyleName("user-menu");
        menuPart.addComponent(personsMenu);

        MenuBar vehiclesMenu = new MenuBar();
        vehiclesMenu.setStyleName(VALO_MENUITEMS);
        vehiclesMenu.addItem("Vehicles",  new Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                menuPart.setWidth(10F, Unit.PERCENTAGE);
                addComponent(menuPart);
                final VerticalLayout area = new VerticalLayout();
                area.setSizeFull();
                GridBasedCrudComponent<Vehicle> crud = new GridBasedCrudComponent<>(Vehicle.class, new HorizontalSplitCrudLayout());
                crud.setAddOperation(vehicle ->vehicleService.save(vehicle));
                crud.setUpdateOperation(vehicle ->vehicleService.save(vehicle));
                crud.setDeleteOperation(vehicle ->vehicleService.delete(vehicle));
                crud.setFindAllOperation(()->vehicleService.findAll());
                crud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.UPDATE, "id", "created", "updated");
                crud.getCrudFormFactory().setDisabledPropertyIds(CrudOperation.ADD, "id", "created", "updated");
                crud.getCrudLayout().setWidth(90F, Unit.PERCENTAGE);
                crud.getGrid().setColumns("vehicleNmbr", "licenseNmbr", "make", "model", "year", "status", "vehicleType", "active", "location", "vinNumber");
                area.addComponent(crud);
                area.setWidth(90F, Unit.PERCENTAGE);
                addComponent(area);

            }
        });

        vehiclesMenu.addStyleName("user-menu");
        menuPart.addComponent(vehiclesMenu);

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
    }

    /**
     * Register a pre-created view instance in the navigation menu and in the
     * {@link Navigator}.
     *
     * @see Navigator#addView(String, View)
     *
     * @param view
     *            view instance to register
     * @param name
     *            view name
     * @param caption
     *            view caption in the menu
     * @param icon
     *            view icon in the menu
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
     * @see Navigator#addView(String, Class)
     *
     * @param viewClass
     *            class of the views to create
     * @param name
     *            view name
     * @param caption
     *            view caption in the menu
     * @param icon
     *            view icon in the menu
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
//        button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        button.setIcon(icon);
        menuItemsLayout.addComponent(button);
        viewButtons.put(name, button);
    }

    /**
     * Highlights a view navigation button as the currently active view in the
     * menu. This method does not perform the actual navigation.
     *
     * @param viewName
     *            the name of the view to show as active
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
