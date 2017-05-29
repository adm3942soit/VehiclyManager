package com.adonis.ui.print;

import com.adonis.data.persons.Person;
import com.adonis.ui.MainUI;
import com.adonis.ui.converters.DateUtils;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Table;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by oksdud on 13.04.2017.
 */
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class PrintPersonsUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        Table table = new Table();
        table.setContainerDataSource(MainUI.personsCrudView.container);
        table.setVisibleColumns("firstName", "lastName", "email", "birthDate", "phoneNumber");
        setContent(table);
        // Print automatically when the window opens
        JavaScript.getCurrent().execute(
                "setTimeout(function() {" +
                        "  print(); self.close();}, 0);");
    }

    public static int index = 1;

    public static File createXLS(String fileName, List<Person> people) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Excel Sheet");
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("firstName");
            rowhead.createCell((short) 1).setCellValue("lastName");
            rowhead.createCell((short) 2).setCellValue("email");
            rowhead.createCell((short) 3).setCellValue("birthDate");
            rowhead.createCell((short) 4).setCellValue("login");
            rowhead.createCell((short) 5).setCellValue("phoneNumber");


            people.forEach(person -> {
                        HSSFRow row = sheet.createRow((short) index);
                        row.createCell((short) 0).setCellValue(person.getFirstName());
                        row.createCell((short) 1).setCellValue(person.getLastName());
                        row.createCell((short) 2).setCellValue(person.getEmail());
                        row.createCell((short) 3).setCellValue(DateUtils.utilDateToString(person.getBirthDate()));
                        row.createCell((short) 4).setCellValue(person.getLogin());
                        row.createCell((short) 5).setCellValue(person.getPhoneNumber());
                        index++;
                    }
            );
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
            System.out.println("Data is saved in excel file.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    return new File(fileName);
    }
}
