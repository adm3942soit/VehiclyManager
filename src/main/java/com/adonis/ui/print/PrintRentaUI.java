package com.adonis.ui.print;

import com.adonis.ui.MainUI;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Table;

/**
 * Created by oksdud on 13.04.2017.
 */
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class PrintRentaUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        Table table = new Table();
        table.setContainerDataSource(MainUI.rentaHistoryCrudView.container);
        table.setVisibleColumns("person", "vehicle", "fromDate", "toDate", "price", "summa", "paid");
        // Have some content to print
        setContent(table);
        // Print automatically when the window opens
        JavaScript.getCurrent().execute(
                "setTimeout(function() {" +
                        "  print(); self.close();}, 0);");
    }
}
