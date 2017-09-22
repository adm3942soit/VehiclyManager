package com.adonis.ui.print;

import com.adonis.data.vehicles.Vehicle;
import com.adonis.ui.MainUI;
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
public class PrintVehiclesUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        Table table = new Table();
        table.setContainerDataSource(MainUI.getVehiclesCrudView().container);
        table.setVisibleColumns("vehicleNmbr", "licenseNmbr", "make", "vehicleType", "model", "year", "active", "location","price", "priceDay", "priceWeek","priceMonth","status");
        // Have some content to print
        setContent(table);
        // Print automatically when the window opens
        JavaScript.getCurrent().execute(
                "setTimeout(function() {" +
                        "  print(); self.close();}, 0);");
    }
    public static int index = 1;
    public static File createXLSVehicles(String fileName, List<Vehicle> vehicles) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Excel Sheet");
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("vehicleNmbr");
            rowhead.createCell((short) 1).setCellValue("licenseNmbr");
            rowhead.createCell((short) 2).setCellValue("make");
            rowhead.createCell((short) 3).setCellValue("vehicleType");
            rowhead.createCell((short) 4).setCellValue("model");
            rowhead.createCell((short) 5).setCellValue("price");
            rowhead.createCell((short) 6).setCellValue("location");


            vehicles.forEach(rent -> {
                        HSSFRow row = sheet.createRow((short) index);
                        row.createCell((short) 0).setCellValue(rent.getVehicleNmbr());
                        row.createCell((short) 1).setCellValue(rent.getLicenseNmbr());
                        row.createCell((short) 2).setCellValue(rent.getMake());
                        row.createCell((short) 3).setCellValue(rent.getVehicleType());
                        row.createCell((short) 4).setCellValue(rent.getModel());
                        row.createCell((short) 5).setCellValue(rent.getPrice());
                        row.createCell((short) 6).setCellValue(rent.getLocation());
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
