package com.adonis.ui.renta;

import com.vaadin.ui.AbstractJavaScriptComponent;

@com.vaadin.annotations.JavaScript({"jquery.min.js", "highcharts.js", "js_highChartRenta.js"})
public class JsHighChartRenta extends AbstractJavaScriptComponent {

    private static final long serialVersionUID = 1913474773889181118L;

    public JsHighChartRenta(String data) {
        getState().data = data;
        getState().title = "Renta calendar";
        getState().units = "Dates";
    }

    @Override
    public JsHighChartState getState() {
        return (JsHighChartState) super.getState();
    }
}
