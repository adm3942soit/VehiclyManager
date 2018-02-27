package com.adonis.ui.addFields;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.utils.FileReader;
import com.adonis.utils.SaveImageFromUrl;
import com.adonis.utils.VaadinUtils;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import org.vaadin.easyuploads.UploadField;

import java.io.*;

import static com.adonis.utils.VaadinUtils.getInitialPath;

/**
 * Created by oksdud on 07.04.2017.
 */
public class PersonImageField extends com.vaadin.v7.ui.CustomField<String> {

    private String value;
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private String nameImage;
    private Image image;
    private Person person;
    private PersonService personService;
    private FormLayout layout = new FormLayout();
    private com.vaadin.v7.ui.TextField textField = new com.vaadin.v7.ui.TextField("");


    public PersonImageField(PersonService personService) {
        this.personService = personService;
        image.setVisible(true);
    }

    public PersonImageField(String value, Person person, PersonService personService) {
        this.value = value;
        this.person = person;
        this.personService = personService;
        if (value != null) {
            try {
                nameImage = SaveImageFromUrl.downloadImage(value, VaadinUtils.getResourcePath());
                if (nameImage != null) {
                    image = new Image(null, new FileResource(new File(VaadinUtils.getResourcePath() + File.separator + nameImage)));
                    horizontalLayout.addComponent(image, 0);
                    this.value = VaadinUtils.getResourcePath() + File.separator + nameImage;
                    this.person.setPicture(this.value);
                    this.personService.save(this.person);
                } else {
                    image = new Image(null, new ExternalResource(value));
                    horizontalLayout.addComponent(image, 0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            textField.setValue(value);

        }
    }

    @Override
    public Object getConvertedValue() {
        return this.value;
    }

    @Override
    protected Component initContent() {
        if (value != null) {
            String newName = null;
            try {
                newName = SaveImageFromUrl.downloadImage(value, VaadinUtils.getResourcePath());
                if (newName != null) {
                    image = new Image(null, new FileResource(new File(VaadinUtils.getResourcePath() + File.separator + newName)));
                    this.value = VaadinUtils.getResourcePath() + File.separator + newName;
                    this.person.setPicture(this.value);
                    this.personService.save(this.person);
                } else {
                    image = new Image(null, new ExternalResource(value));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            textField.setValue(value);

        }
        if (image != null) {
            image.setWidth(90, Unit.PIXELS);
            image.setHeight(90, Unit.PIXELS);
            layout.addComponent(image);
        }
        layout.addComponent(textField);
        textField.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                PersonImageField.super.setInternalValue(textField.getValue());
                setInternalValue(textField.getValue());
            }
        });
        return layout;
    }


    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public void setInternalValue(String picture) {
        String curPicture = picture != null ? picture : "";
        String newName = null;
        if (image == null) {
            try {
                newName = SaveImageFromUrl.downloadImage(value, VaadinUtils.getResourcePath());
                if (newName != null) {
                    image = new Image(null, new FileResource(new File(VaadinUtils.getResourcePath() + File.separator + newName)));
                    this.value = VaadinUtils.getResourcePath() + File.separator + newName;
                    this.person.setPicture(this.value);
                    this.personService.save(this.person);
                } else {
                    image = new Image(null, new ExternalResource(value));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                newName = SaveImageFromUrl.downloadImage(value, VaadinUtils.getResourcePath());
                if (newName != null) {
                    image = new Image(null, new FileResource(new File(VaadinUtils.getResourcePath() + File.separator + newName)));
                    this.value = VaadinUtils.getResourcePath() + File.separator + newName;
                    this.person.setPicture(this.value);
                    this.personService.save(this.person);
                } else {
                    image = new Image(null, new ExternalResource(value));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.setInternalValue(curPicture);
        this.value = curPicture;

        textField.setValue(value);

        image.setWidth(90, Unit.PIXELS);
        image.setHeight(90, Unit.PIXELS);

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

}
