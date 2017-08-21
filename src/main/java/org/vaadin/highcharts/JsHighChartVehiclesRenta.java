package org.vaadin.highcharts;

import com.adonis.ui.renta.JsHighChartState;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.util.Calendar.LONG_FORMAT;
import static java.util.Calendar.MONTH;

@com.vaadin.annotations.JavaScript({"jquery-3.2.1.min.js", "highchartsLocal2.js","js_highChartVehiclesRenta.js", "highcharts-connectorLocal2.js"})
public class JsHighChartVehiclesRenta extends AbstractHighChart {

    public JsHighChartVehiclesRenta(String data, String labels, String categories) {
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
    }

    @Override
    public com.adonis.ui.renta.JsHighChartState getState() {
        return (JsHighChartState) super.getState();
    }
}
