package com.adonis.ui.renta;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

/**
 * Created by oksdud on 12.04.2017.
 */
public class RentaDChartView extends CustomComponent implements View {

    public static final String NAME = "VEHICLES CHART VIEW";
    PersonService service;
    RentaHistoryService rentaHistoryService;
    VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();

    public RentaDChartView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;
        setSizeFull();

        Double working = Double.valueOf(rentaHistoryService.findAllWorking().size());
        Double active = Double.valueOf(vehicleService.findAllActive().size());
        Double all = Double.valueOf(vehicleService.findAll().size());
        Double notActive = all - active;

        if (!all.equals(0.0)) {
            try {
                DataSeries dataSeries = new DataSeries().add(
                        Double.valueOf(working), Double.valueOf(active - working), Double.valueOf(notActive));
                SeriesDefaults seriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.BAR);
                Axes axes = new Axes().addAxis(
                        new XYaxis()
                                .setRenderer(AxisRenderers.CATEGORY)
                                .setTicks(new Ticks().add("working", "not working", "not active"))
                );
                Highlighter highlighter = new Highlighter().setShow(false);
                Options options = new Options()
                        .setSeriesDefaults(seriesDefaults)
                        .setAxes(axes)
                        .setHighlighter(highlighter);

                DCharts chart = new DCharts()
                        .setDataSeries(dataSeries)
                        .setOptions(options)
                        .show();
                viewLayout.addComponent(chart);
                viewLayout.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        viewLayout.setSizeFull();
        setCompositionRoot(viewLayout);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
