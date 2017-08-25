package com.adonis.ui.renta;

import com.vaadin.ui.AbstractJavaScriptComponent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.util.Calendar.LONG_FORMAT;
import static java.util.Calendar.MONTH;

@com.vaadin.annotations.JavaScript({"at/downdrown/vaadinaddons/highchartsapi/jquery-2.1.3.min.js",
        "at/downdrown/vaadinaddons/highchartsapi/highcharts.js", "js_highChartRenta.js", "org/vaadin/highcharts/highcharts-connector.js"})
public class JsHighChartRenta extends AbstractJavaScriptComponent {


    public JsHighChartRenta(String data, String id) {
        this.setId(id);
        getState().data = data;
        getState().title = "Calendar of cars employment for the last month";
        Date now = new Date();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(now);
        getState().units = "Dates for "+
                gregorianCalendar.getDisplayName(MONTH,LONG_FORMAT, Locale.getDefault() )+" "+
                gregorianCalendar.get(Calendar.YEAR);
        getState().idComponent = id;
    }

    @Override
    public JsHighChartState getState() {
        return (JsHighChartState) super.getState();
    }

}
