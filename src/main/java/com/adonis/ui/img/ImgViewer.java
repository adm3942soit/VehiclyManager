package com.adonis.ui.img;


import com.adonis.data.service.FotoAlbumService;
import com.adonis.data.service.VehicleService;
import com.adonis.data.vehicles.img.Foto;
import com.adonis.ui.MainUI;
import com.adonis.ui.addFields.VehicleAlbumField;
import com.adonis.utils.VaadinUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Slider;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import org.tepi.imageviewer.ImageViewer;
import org.tepi.imageviewer.ImageViewer.ImageSelectedEvent;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class ImgViewer extends CustomComponent implements View {

    private ImageViewer imageViewer;
    private VerticalLayout mainLayout;
    private TextField selectedImage = new TextField();
    private VehicleService vehicleService;
    private FotoAlbumService fotoAlbumService;

    public ImgViewer(VehicleService vehicleService, FotoAlbumService fotoAlbumService) {
        this.vehicleService= vehicleService;
        this.fotoAlbumService=fotoAlbumService;
        mainLayout = new com.vaadin.v7.ui.VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.addStyleName(Reindeer.LAYOUT_BLACK);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        setSizeFull();
        com.vaadin.v7.ui.Label info = new com.vaadin.v7.ui.Label(
                "<b>ImageViewer Demo Application</b>&nbsp;&nbsp;&nbsp;"
                        + "<i>Try the arrow keys, space/enter and home/end."
                        + " You can also click on the pictures or use the "
                        + "mouse wheel.&nbsp;&nbsp;", com.vaadin.v7.ui.Label.CONTENT_XHTML);
        Button style = new Button("Click here to toggle style.");
        style.setStyleName(Reindeer.BUTTON_LINK);
        style.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                if (getStyleName().contains("black")) {
                    removeStyleName(Reindeer.LAYOUT_BLACK);
                } else {
                    addStyleName(Reindeer.LAYOUT_BLACK);
                }
            }
        });
        imageViewer = new ImageViewer();
        imageViewer.setSizeFull();
        imageViewer.setImages(createImageList());
        imageViewer.setAnimationEnabled(false);
        imageViewer.setSideImageRelativeWidth(0.7f);

        imageViewer.addListener(new ImageViewer.ImageSelectionListener() {
            public void imageSelected(ImageSelectedEvent e) {
                if (e.getSelectedImageIndex() >= 0) {
                    selectedImage.setValue(String.valueOf(e.getSelectedImageIndex()));
                } else {
                    selectedImage.setValue("-");
                }
            }
        });
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSizeUndefined();
        hl.setMargin(false);
        hl.setSpacing(true);
        hl.addComponent(info);
        hl.addComponent(style);

        mainLayout.addComponent(hl);
        mainLayout.addComponent(imageViewer);
        mainLayout.setExpandRatio(imageViewer, 1);

        Layout ctrls = createControls();
        mainLayout.addComponent(ctrls);
        mainLayout.setComponentAlignment(ctrls, Alignment.BOTTOM_CENTER);

        Label images = new Label(
                "Sample Photos: Bruno Monginoux / www.Landscape-Photo.net (cc-by-nc-nd)");
        images.setSizeUndefined();
        images.setStyleName("licence");
        mainLayout.addComponent(images);
        mainLayout.setComponentAlignment(images, Alignment.BOTTOM_RIGHT);
//        addComponent(viewerLayout);
        imageViewer.setCenterImageIndex(0);
//            imageViewer.focus();
//        imageViewer.setTheme("imageviewertheme");
        setCompositionRoot(mainLayout);
    }

    private Layout createControls() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSizeUndefined();
        hl.setMargin(false);
        hl.setSpacing(true);

        CheckBox c = new CheckBox("HiLite");
        c.setImmediate(true);
        c.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                boolean checked = (Boolean) event.getProperty().getValue();
                imageViewer.setHiLiteEnabled(checked);
                imageViewer.focus();
            }
        });
        c.setValue(true);
        hl.addComponent(c);
        hl.setComponentAlignment(c, Alignment.BOTTOM_CENTER);

        c = new CheckBox("Animate");
        c.setImmediate(true);
        c.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                boolean checked = (Boolean) event.getProperty().getValue();
                imageViewer.setAnimationEnabled(checked);
                imageViewer.focus();
            }
        });
        c.setValue(true);
        hl.addComponent(c);
        hl.setComponentAlignment(c, Alignment.BOTTOM_CENTER);

        Slider s = new Slider("Animation duration (ms)");
        s.setMin(200);
        s.setMax(2000);
        s.setImmediate(true);
        s.setWidth("120px");
        s.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    int duration = (int) Math.round((Double) event
                            .getProperty().getValue());
                    imageViewer.setAnimationDuration(duration);
                    imageViewer.focus();
                } catch (Exception ignored) {
                }
            }
        });
        try {
            s.setValue(350.0);
        } catch (ValueOutOfBoundsException e) {
        }
        hl.addComponent(s);
        hl.setComponentAlignment(s, Alignment.BOTTOM_CENTER);

        s = new Slider("Center image width");
        s.setMin(0.1);
        s.setMax(1);
        s.setResolution(2);
        s.setImmediate(true);
        s.setWidth("120px");
        s.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    double d = (Double) event.getProperty().getValue();
                    imageViewer.setCenterImageRelativeWidth((float) d);
                    imageViewer.focus();
                } catch (Exception ignored) {
                }
            }
        });
        try {
            s.setValue(0.55);
        } catch (ValueOutOfBoundsException e) {
        }
        hl.addComponent(s);
        hl.setComponentAlignment(s, Alignment.BOTTOM_CENTER);

        s = new Slider("Side image count");
        s.setMin(1);
        s.setMax(5);
        s.setImmediate(true);
        s.setWidth("120px");
        s.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    int sideImageCount = (int) Math.round((Double) event
                            .getProperty().getValue());
                    imageViewer.setSideImageCount(sideImageCount);
                    imageViewer.focus();
                } catch (Exception ignored) {
                }
            }
        });
        try {
            s.setValue(2.0);
        } catch (ValueOutOfBoundsException e) {
        }
        hl.addComponent(s);
        hl.setComponentAlignment(s, Alignment.BOTTOM_CENTER);

        s = new Slider("Side image width");
        s.setMin(0.5);
        s.setMax(0.8);
        s.setResolution(2);
        s.setImmediate(true);
        s.setWidth("120px");
        s.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    double d = (Double) event.getProperty().getValue();
                    imageViewer.setSideImageRelativeWidth((float) d);
                    imageViewer.focus();
                } catch (Exception ignored) {
                }
            }
        });
        try {
            s.setValue(0.0);
        } catch (ValueOutOfBoundsException e) {
        }
        hl.addComponent(s);
        hl.setComponentAlignment(s, Alignment.BOTTOM_CENTER);

        s = new Slider("Horizontal padding");
        s.setMin(0);
        s.setMax(10);
        s.setImmediate(true);
        s.setWidth("120px");
        s.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    double d = (Double) event.getProperty().getValue();
                    imageViewer.setImageHorizontalPadding((int) Math.round(d));
                    imageViewer.focus();
                } catch (Exception ignored) {
                }
            }
        });
        try {
            s.setValue(1.0);
        } catch (ValueOutOfBoundsException e) {
        }
        hl.addComponent(s);
        hl.setComponentAlignment(s, Alignment.BOTTOM_CENTER);

        s = new Slider("Vertical padding");
        s.setMin(0);
        s.setMax(10);
        s.setImmediate(true);
        s.setWidth("120px");
        s.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    double d = (Double) event.getProperty().getValue();
                    imageViewer.setImageVerticalPadding((int) Math.round(d));
                    imageViewer.focus();
                } catch (Exception ignored) {
                }
            }
        });
        try {
            s.setValue(5.0);
        } catch (ValueOutOfBoundsException e) {
        }
        hl.addComponent(s);
        hl.setComponentAlignment(s, Alignment.BOTTOM_CENTER);

        selectedImage.setWidth("50px");
        selectedImage.setImmediate(true);
        hl.addComponent(selectedImage);
        hl.setComponentAlignment(selectedImage, Alignment.BOTTOM_CENTER);

        return hl;
    }

    /**
     * Creates a list of Resources to be shown in the ImageViewer.
     *
     * @return List of Resource instances
     */
    private List<Resource> createImageList() {
        List<Resource> img = new ArrayList<Resource>();
        List<Foto> fotos = this.fotoAlbumService.findAllFotos();
        fotos.forEach(foto -> {
            img.add(new FileResource(new File(VaadinUtils.getResourcePath()+File.separator+foto.getFotoName())));
        });
        return img;
    }

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
