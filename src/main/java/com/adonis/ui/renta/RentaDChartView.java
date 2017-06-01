package com.adonis.ui.renta;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.utils.DateUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
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

import java.util.Date;

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
        Boolean showWorkingTodayOrPerWeek = false;
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
                Date date = new Date();
                Date date1 = DateUtils.sub(date, 1, DateUtils.DAY);
                Date date2 = DateUtils.sub(date1, 1, DateUtils.DAY);
                Date date3 = DateUtils.sub(date2, 1, DateUtils.DAY);
                Date date4 = DateUtils.sub(date3, 1, DateUtils.DAY);
                Date date5 = DateUtils.sub(date4, 1, DateUtils.DAY);
                Date date6 = DateUtils.sub(date5, 1, DateUtils.DAY);
                DataSeries dataSeriesRenta = new DataSeries().add(
                        rentaHistoryService.findCountTripsByDate(date6),
                        rentaHistoryService.findCountTripsByDate(date5),
                        rentaHistoryService.findCountTripsByDate(date4),
                        rentaHistoryService.findCountTripsByDate(date3),
                        rentaHistoryService.findCountTripsByDate(date2),
                        rentaHistoryService.findCountTripsByDate(date1),
                        rentaHistoryService.findCountTripsByDate(date)
                );
                Axes axesRenta = new Axes().addAxis(
                        new XYaxis()
                                .setRenderer(AxisRenderers.CATEGORY)
                                .setTicks(new Ticks().add(
                                        DateUtils.convertToString(date6),
                                        DateUtils.convertToString(date5),
                                        DateUtils.convertToString(date4),
                                        DateUtils.convertToString(date3),
                                        DateUtils.convertToString(date2),
                                        DateUtils.convertToString(date1),
                                        DateUtils.convertToString(date)))
                );

                Highlighter highlighter = new Highlighter().setShow(false);
                Options options = new Options()
                        .setSeriesDefaults(seriesDefaults)
                        .setAxes(axes)
                        .setHighlighter(highlighter);
                Options optionsRenta = new Options()
                        .setSeriesDefaults(seriesDefaults)
                        .setAxes(axesRenta)
                        .setHighlighter(highlighter);

                DCharts chart = new DCharts()
                        .setDataSeries(showWorkingTodayOrPerWeek?dataSeriesRenta:dataSeries)
                        .setOptions(showWorkingTodayOrPerWeek?optionsRenta:options)
                        .show();

                viewLayout.addComponent(chart);

                CheckBox choose = new CheckBox("Show working per week", showWorkingTodayOrPerWeek);
                viewLayout.addComponent(choose);
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
