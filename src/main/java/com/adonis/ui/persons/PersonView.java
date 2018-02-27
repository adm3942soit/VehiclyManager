package com.adonis.ui.persons;

import com.adonis.data.persons.Person;
import com.adonis.ui.converters.DateConverter;
import com.adonis.ui.login.LoginView;
import com.adonis.ui.main.MainScreen;
import com.adonis.utils.FileReader;
import com.adonis.utils.FilenameUtils;
import com.adonis.utils.SaveImageFromUrl;
import com.adonis.utils.VaadinUtils;
import com.google.common.base.Strings;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import org.vaadin.easyuploads.UploadField;

import java.io.*;

import static com.adonis.utils.VaadinUtils.getInitialPath;
import static com.adonis.utils.VaadinUtils.getPage;

public class PersonView extends PersonDesign {

    Boolean view = true;

    public interface PersonSaveListener {
        void savePerson(Person person);
    }

    public interface PersonDeleteListener {
        void deletePerson(Person person);
    }

    public interface PersonAddListener {
        void addPerson(Person person);
    }

    Binder<Person> binder = new Binder<>(Person.class);

    public PersonView(PersonSaveListener saveEvt, PersonDeleteListener delEvt, PersonAddListener addListener, Boolean view) {
        this.view = view;
        binder.forField(picture).bind("picture");
        binder.forField(dayOfBirth).withConverter(new DateConverter()).bind("birthDate");
        binder.bindInstanceFields(this);
        upload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final UploadField uploadFieldImage = new UploadField();
                uploadFieldImage.setFieldType(UploadField.FieldType.FILE);
                uploadFieldImage.setAcceptFilter("image/*");
                addComponent(uploadFieldImage, 2);
                uploadFieldImage.getUpload().addListener(new com.vaadin.v7.ui.Upload.SucceededListener() {

                    @Override
                    public void uploadSucceeded(com.vaadin.v7.ui.Upload.SucceededEvent event) {
                        File file = (File) uploadFieldImage.getValue();
                        String pictureFileName = file.getName();
                        pictureUploadName = "";
                        try {
                            showUploadedImage(uploadFieldImage, pictureImage, pictureFileName, "picture" + login.getValue() + ".jpg");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        uploadFieldImage.clearDefaulLayout();
                        removeComponent(uploadFieldImage);
                        markAsDirty();
                        picture.setValue(VaadinUtils.getInitialPath() + File.separator + pictureUploadName);
                        picture.markAsDirty();
                        pictureImage.markAsDirty();
                        pictureImage.setVisible(true);

                    }
                });

            }
        });

        if (view) {
            picture.setVisible(false);

        } else {
            if (Strings.isNullOrEmpty(picture.getValue())) {
                picture.setVisible(true);
            } else
                picture.setVisible(false);
            add.setVisible(false);
        }

        picture.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                if (!event.getValue().isEmpty()) {
                    pictureImage.setSource(new ExternalResource(picture.getValue()));
                }
            }
        });
        save.addClickListener(evt -> {
            try {
                Person person = binder.getBean() != null ? binder.getBean() : new Person();
                if (Strings.isNullOrEmpty(person.getFirstName())) {
                    person.setFirstName(firstName.getValue());
                }
                if (Strings.isNullOrEmpty(person.getLastName())) {
                    person.setLastName(lastName.getValue());
                }
                if (Strings.isNullOrEmpty(person.getEmail())) {
                    person.setEmail(email.getValue());
                }
                if (person.getBirthDate() == null) {
                    person.setBirthDate(DateConverter.getDate(dayOfBirth.getValue()));
                }
                if (Strings.isNullOrEmpty(person.getLogin())) {
                    person.setLogin(login.getValue());
                }
                if (Strings.isNullOrEmpty(person.getPassword())) {
                    person.setPassword(password.getValue());
                }
                if (Strings.isNullOrEmpty(person.getPicture())) {
                    person.setPicture(picture.getValue());
                }
                if (Strings.isNullOrEmpty(person.getNotes())) {
                    person.setNotes(notes.getValue());
                }

                if (person != null && person.getId() != null) {
                    saveEvt.savePerson(person);
                } else {
                    if (addListener != null) {
                        addListener.addPerson(person);
                    }
                    pictureImage.setSource(
                            picture.getValue().startsWith("http") ? new ExternalResource(picture.getValue()) :
                                    new FileResource(new File(picture.getValue()))
                    );
                }
                binder.writeBean(person);
                if (!view) getUI().getNavigator().navigateTo(LoginView.NAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        add.addClickListener(evt -> {
            try {
                this.view = false;
                picture.setVisible(true);
                this.setPerson(new Person());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        cancel.addClickListener(evt -> {
            getUI().getNavigator().navigateTo(MainScreen.NAME);
        });

        if (delEvt != null) {
            delete.addClickListener(evt -> {
                delEvt.deletePerson(binder.getBean());
            });
        } else {
            delete.setVisible(false);
            cancel.setVisible(false);
        }
    }

    public void setPerson(Person selectedRow) {
        binder.setBean(selectedRow);
        if (!Strings.isNullOrEmpty(selectedRow.getPicture())) {
            pictureImage.setSource(new ExternalResource(selectedRow.getPicture()));
            picture.setValue(selectedRow.getPicture());
            picture.setVisible(!view);
        } else {
            pictureImage.setSource(new ExternalResource(""));
            picture.setValue("");
            picture.setVisible(true);
        }
    }

    private void showUploadedImage(UploadField upload, Image image, String fileName, String newNameFile) throws IOException {
        pictureUploadName = newNameFile;
        File value = (File) upload.getValue();
        //copy to resources
        FileReader.copyFile(value.getAbsolutePath().toString(), VaadinUtils.getResourcePath(newNameFile));
        //copy to server directory
        FileReader.createDirectoriesFromCurrent(getInitialPath());
        FileReader.copyFile(value.getAbsolutePath().toString(), VaadinUtils.getInitialPath() + File.separator + newNameFile);
        FileReader.copyFile(value.getAbsolutePath().toString(), VaadinUtils.getResourcePath(newNameFile));

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
