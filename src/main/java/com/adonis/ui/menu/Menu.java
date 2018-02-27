package com.adonis.ui.menu;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.Vehicle;
import com.adonis.utils.FileReader;
import com.adonis.utils.VaadinUtils;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import lombok.NoArgsConstructor;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;
import org.vaadin.easyuploads.UploadField;
import ua.edu.file.MyFiler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.adonis.utils.VaadinUtils.getInitialPath;

/**
 * Responsive navigation menu presenting a list of available views to the user.
 */
@NoArgsConstructor
public class Menu extends CssLayout {

    public static final String VALO_MENUITEMS = "valo-menuitems";
    private static final String VALO_MENU_TOGGLE = "valo-menu-toggle";
    private static final String VALO_MENU_VISIBLE = "valo-menu-visible";
    private Navigator navigator;
    private Map<String, Button> viewButtons = new HashMap<String, Button>();
    public Button showMenu;
    private CssLayout menuItemsLayout;
    private CssLayout menuPart;
    public final GridBasedCrudComponent<Vehicle> vehiclesCrud = new GridBasedCrudComponent<>(Vehicle.class, new HorizontalSplitCrudLayout());
    public final GridBasedCrudComponent<Person> personsCrud = new GridBasedCrudComponent<>(Person.class, new HorizontalSplitCrudLayout());
    //    public ImageData initialimage;
    Image image = new Image();

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
        showMenu = new Button("Menu", new ClickListener() {
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

    public void setPersonsCrudProperties(PersonService personService) {
        personsCrud.setAddOperation(person -> personService.insert(person));
        personsCrud.setUpdateOperation(person -> personService.save(person));
        personsCrud.setDeleteOperation(person -> personService.delete(person));
        personsCrud.setFindAllOperation(() -> personService.findAll());

        GridLayoutCrudFormFactory<Person> formFactory = new GridLayoutCrudFormFactory<>(Person.class, 1, 10);

        formFactory.setVisiblePropertyIds("firstName", "lastName", "email", "login", "password", "birthDate", "picture", "notes");
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
    public void addView(Class<? extends View> viewClass, final String name,
                        String caption, com.vaadin.server.Resource icon) {
        navigator.addView(name, viewClass);
        createViewButton(name, caption, icon);
    }

    public void addViewWithEditableIcon(View view, final String name, String caption, String nameImage) {
        navigator.addView(name, view);
        createViewButtonWithEditableImage(name, caption, nameImage);
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

    private void createViewButtonWithEditableImage(final String name, String caption, String nameImage) {
        Button button = new Button(caption, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigator.navigateTo(name);
            }

        });
        button.setPrimaryStyleName(ValoTheme.BUTTON_FRIENDLY);
        button.setWidth(50, Unit.PERCENTAGE);
        button.setHeight(90, Unit.PIXELS);
        FileReader.createDirectoriesFromCurrent(getInitialPath());
        final Image image = new Image("", new ThemeResource("img/" + nameImage));
        try {
            FileReader.copyFile(VaadinUtils.getResourcePath(nameImage), VaadinUtils.getInitialPath() + File.separator + nameImage);
            image.setSource(new FileResource(new File(VaadinUtils.getInitialPath() + File.separator + nameImage)));
        } catch (IOException e) {
            e.printStackTrace();
            image.setSource(new ThemeResource("img/" + nameImage));
        }

        image.setWidth(50, Unit.PERCENTAGE);
        image.setHeight(90, Unit.PIXELS);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        horizontalLayout.addComponents(image, button);
        image.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                final UploadField uploadFieldImage = new UploadField();
                uploadFieldImage.setAcceptFilter("image/*");
                horizontalLayout.addComponent(uploadFieldImage, 2);
                uploadFieldImage.getUpload().addListener(new com.vaadin.v7.ui.Upload.SucceededListener() {


                    @Override
                    public void uploadSucceeded(com.vaadin.v7.ui.Upload.SucceededEvent event) {
                        File file = (File) uploadFieldImage.getValue();
                        try {
                            showUploadedImage(uploadFieldImage, image, file.getName(), nameImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        uploadFieldImage.clearDefaulLayout();
                        horizontalLayout.removeComponent(uploadFieldImage);

                    }
                });
                uploadFieldImage.setFieldType(UploadField.FieldType.FILE);
                horizontalLayout.markAsDirty();
                image.setWidth(50, Unit.PERCENTAGE);
                image.setHeight(90, Unit.PIXELS);
                image.setVisible(false);
                image.markAsDirty();
                horizontalLayout.addComponent(image, 0);
            }
        });
        button.setVisible(true);
        image.setVisible(true);
        menuItemsLayout.addComponents(horizontalLayout);
        viewButtons.put(name, button);
    }

    private void showUploadedImage(UploadField upload, Image image, String fileName, String newNameFile) throws IOException {
        File value = (File) upload.getValue();
        //copy to resources
        FileReader.copyFile(value.getAbsolutePath().toString(), VaadinUtils.getResourcePath(newNameFile));
        //copy to server directory
        FileReader.createDirectoriesFromCurrent(getInitialPath());
        FileReader.copyFile(value.getAbsolutePath().toString(), VaadinUtils.getInitialPath() + File.separator + newNameFile);
        FileInputStream fileInputStream = new FileInputStream(value);
        long byteLength = value.length(); //bytecount of the file-content

        byte[] filecontent = new byte[(int) byteLength];
        fileInputStream.read(filecontent, 0, (int) byteLength);
        final byte[] data = filecontent;

        StreamResource resource = new StreamResource(
                new StreamResource.StreamSource() {
                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(data);
                    }
                }, fileName);

        image.setSource(resource);
        image.setVisible(true);
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

    public Button getShowMenu() {
        return showMenu;
    }

}
