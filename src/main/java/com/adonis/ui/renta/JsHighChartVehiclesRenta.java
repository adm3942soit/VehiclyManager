package com.adonis.ui.renta;

import com.vaadin.ui.AbstractJavaScriptComponent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.util.Calendar.LONG_FORMAT;
import static java.util.Calendar.MONTH;

@com.vaadin.annotations.JavaScript({"at/downdrown/vaadinaddons/highchartsapi/jquery-2.1.3.min.js",
        "at/downdrown/vaadinaddons/highchartsapi/highcharts.js", "js_highChartVehiclesRenta.js", "org/vaadin/highcharts/highcharts-connector.js"})
public class JsHighChartVehiclesRenta extends AbstractJavaScriptComponent {


    public JsHighChartVehiclesRenta(String data, String labels, String categories, String id) {
        this.setId(id);
        getState().data = data;
        getState().titleChart = "Calendar vehicles usage for the last month";
        Date now = new Date();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(now);
        getState().titleY = "Dates for " +
                gregorianCalendar.getDisplayName(MONTH, LONG_FORMAT, Locale.getDefault()) + " " +
                gregorianCalendar.get(Calendar.YEAR);
        getState().labels = labels;
        getState().categories = categories;
        getState().idComponent = id;
    }

    @Override
    public JsHighChartState2 getState() {
        return (JsHighChartState2) super.getState();
    }


}
