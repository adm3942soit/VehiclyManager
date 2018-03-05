package com.adonis.ui.addFields;

import com.adonis.data.service.FotoAlbumService;
import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.Vehicle;
import com.adonis.data.vehicles.VehicleModel;
import com.adonis.data.vehicles.img.FotoAlbum;
import com.adonis.ui.img.ImgViewer;
import com.adonis.ui.img.ImgViewerUI;
import com.adonis.ui.persons.CardPopup;
import com.adonis.utils.FileReader;
import com.adonis.utils.FilenameUtils;
import com.adonis.utils.VaadinUtils;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import org.vaadin.easyuploads.UploadField;

import java.io.*;

import static com.adonis.utils.VaadinUtils.getInitialPath;

/**
 * Created by oksdud on 07.04.2017.
 */
public class VehicleAlbumField extends com.vaadin.v7.ui.CustomField<FotoAlbum> {

    private FieldGroup fieldGroup = new BeanFieldGroup<FotoAlbum>(FotoAlbum.class);
    public static  FotoAlbum currentFotoAlbum;
    public static Vehicle vehicle;
    public static VehicleService vehicleService;
    public static FotoAlbumService fotoAlbumService;
    private FormLayout layout = new FormLayout();
    static final Action esc = new ShortcutAction("Close window",
            ShortcutAction.KeyCode.ESCAPE, null);
    static final Action[] actions = new Action[] { esc };
    final ClosableDialog window = new ClosableDialog("View foto album", layout);
    HorizontalLayout horizontalLayout = new HorizontalLayout();

    public VehicleAlbumField(VehicleService vehicleService, FotoAlbumService fotoAlbumService) {
        this.fotoAlbumService = fotoAlbumService;
        this.vehicleService = vehicleService;
    }

    public VehicleAlbumField(FotoAlbum value, Vehicle vehicle, VehicleService vehicleService, FotoAlbumService fotoAlbumService) {
        this.fotoAlbumService = fotoAlbumService;
        this.vehicleService = vehicleService;
        this.currentFotoAlbum = value;
        this.vehicle = vehicle;
    }

    @Override
    public Object getConvertedValue() {
        return this.currentFotoAlbum;
    }

    @Override
    protected Component initContent() {
       window.setClosable(true);
       Image image = new Image();
        image.setWidth(90, Unit.PIXELS);
        image.setHeight(90, Unit.PIXELS);

       Button button = new Button("View fotos", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {

//                getUI().addWindow(window);

            }
        });

        BrowserWindowOpener browserWindowOpener = new BrowserWindowOpener(ImgViewerUI.class);
        browserWindowOpener.setFeatures("height=400,width=400,resizable");
        browserWindowOpener.extend(button);

        window.addCloseListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                try {
                    fieldGroup.commit();
                } catch (FieldGroup.CommitException ex) {
                    ex.printStackTrace();
                }
            }
        });

        window.setWidth(null);
        layout.setWidth(null);
        layout.setMargin(true);
        return button;

    }


    @Override
    public Class<? extends FotoAlbum> getType() {
        return FotoAlbum.class;
    }

    @Override
    public void setInternalValue(FotoAlbum picture) {
        FotoAlbum curPicture = picture != null ? picture : null;
        super.setInternalValue(curPicture);
        this.currentFotoAlbum = curPicture;

    }
    class ClosableDialog extends Window implements Action.Handler {

        ClosableDialog(String caption, Component component) {
            setModal(true);
            setCaption(caption);
            addActionHandler(this);
//            Button ok = new Button("Ok");
//            addComponent(ok);
//            ok.focus();
            setContent(component);
        }

        public void handleAction(Action action, Object sender, Object target) {
            if (action == esc) {
//                ((Window) getParent()).removeWindow(this);
                getUI().removeWindow(window);
            }
        }

        public Action[] getActions(Object target, Object sender) {
            return actions;
        }
        @Override
        public void close()
        {
            getUI().removeWindow(window);
        }
    }
    private void showUploadedImage(UploadField upload, Image image, String fileName) throws IOException {
        File value = (File) upload.getValue();
        String newNameFile = FilenameUtils.renameExtension(FilenameUtils.getName(value.getAbsolutePath().toString()));
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

}
