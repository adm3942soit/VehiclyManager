package com.adonis.ui.renta;

import com.vaadin.ui.AbstractJavaScriptComponent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.util.Calendar.LONG_FORMAT;
import static java.util.Calendar.MONTH;

@com.vaadin.annotations.JavaScript({"jquery.min.js", "highcharts.js", "js_highChartRenta.js"})
public class JsHighChartRenta extends AbstractJavaScriptComponent {

    private static final long serialVersionUID = 1913474773889181118L;

    public JsHighChartRenta(String data) {
        getState().data = data;
        getState().title = "Renta calendar for the last month";
        Date now = new Date();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(now);
        getState().units = "Dates for "+
                gregorianCalendar.getDisplayName(MONTH,LONG_FORMAT, Locale.getDefault() )+" "+
                gregorianCalendar.get(Calendar.YEAR);
    }

    @Override
    public JsHighChartState getState() {
        return (JsHighChartState) super.getState();
    }
}
