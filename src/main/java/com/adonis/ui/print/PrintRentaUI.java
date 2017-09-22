package com.adonis.ui.print;

import com.adonis.data.renta.RentaHistory;
import com.adonis.ui.MainUI;
import com.adonis.ui.converters.DatesConverter;
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
public class PrintRentaUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        Table table = new Table();
        table.setContainerDataSource(MainUI.rentaHistoryCrudView.container);
        table.setVisibleColumns("person", "vehicle", "fromDate", "toDate", "price", "priceDay", "priceWeek","priceMonth","summa", "paid");
        // Have some content to print
        setContent(table);
//        createXLS("renta.xls", MainUI.rentaHistoryCrudView.objects);
        // Print automatically when the window opens
        JavaScript.getCurrent().execute(
                "setTimeout(function() {" +
                        "  print(); self.close();}, 0);");
    }

    public static int index = 1;

    public static File createXLSRenta(String fileName, List<RentaHistory> rents) {
        try {
            File file = new File(fileName);
            if(file.exists()) file.delete();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Excel Sheet");
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("person");
            rowhead.createCell((short) 1).setCellValue("vehicle");
            rowhead.createCell((short) 2).setCellValue("fromDate");
            rowhead.createCell((short) 3).setCellValue("toDate");
            rowhead.createCell((short) 4).setCellValue("price");
            rowhead.createCell((short) 5).setCellValue("summa");
//            rowhead.createCell((short) 6).setCellValue("paid");


            rents.forEach(rentaHistory -> {
                        HSSFRow row = sheet.createRow((short) index);
                        row.createCell((short) 0).setCellValue(rentaHistory.getPerson());
                        row.createCell((short) 1).setCellValue(rentaHistory.getVehicle());
                        row.createCell((short) 2).setCellValue(DatesConverter.timestampToString(rentaHistory.getFromDate()));
                        row.createCell((short) 3).setCellValue(DatesConverter.timestampToString(rentaHistory.getToDate()));
                        row.createCell((short) 4).setCellValue(rentaHistory.getPrice());
                        row.createCell((short) 5).setCellValue(rentaHistory.getSumma());
//                        row.createCell((short) 6).setCellValue(rentaHistory.getPaid());
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
