package de.steinwedel.messagebox;

import com.adonis.data.persons.Person;
import com.adonis.data.service.PersonService;
import com.adonis.utils.VaadinUtils;
import com.vaadin.ui.Notification;

public class MessageForm {
   private static MessageBox mb;
   public static void winDefault(String message){
       mb = MessageBox
               .createInfo()
               .withCaption("Info")
               .withMessage(message)
               .withOkButton(()  -> closeWin());
       mb.asModal(true);
       mb.getButton(ButtonType.OK).setDisableOnClick(false);
       mb.open();

   }
   public static void closeWin(){
       VaadinUtils.getPage().reload();

       mb.close();
       mb.buttonLayout.removeAllComponents();
       mb.asModal(false);
       mb.getWindow().close();

   }
    public static void winSave(PersonService personService, Person person){
        mb = MessageBox
                .createQuestion()
                .withCaption("Info")
                .withMessage("Do you want save this person?")
                .withSaveButton(()  -> {
                    personService.save(person);
                    Notification.show("Person saved successfully!");
                    closeWin();}
                    )
                .withCancelButton(()->{closeWin();})
        ;
        mb.getButton(ButtonType.CANCEL).setDisableOnClick(false);
        mb.getButton(ButtonType.SAVE).setDisableOnClick(false);
        mb.asModal(true);
        mb.open();

    }
    public static void winInsert(PersonService personService, Person person){
        mb = MessageBox
                .createQuestion()
                .withCaption("Info")
                .withMessage("Do you want insert this person?")
                .withSaveButton(()  -> {
                    personService.insert(person);
                    Notification.show("Person inserted successfully!");
                    closeWin();}
                )
                .withCancelButton(()->{closeWin();})
        ;
        mb.getButton(ButtonType.CANCEL).setDisableOnClick(false);
        mb.getButton(ButtonType.SAVE).setDisableOnClick(false);
        mb.asModal(true);
        mb.open();

    }

}
