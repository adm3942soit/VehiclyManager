package com.adonis.ui.renta;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Margin;
import at.downdrown.vaadinaddons.highchartsapi.model.data.ColumnRangeChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.ColumnRangeChartPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.HighChartsPlotOptionsImpl;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnRangeChartSeries;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oksdud on 12.04.2017.
 */

public class RentaAnavailableCalendarView extends CustomComponent implements View {
    /*https://github.com/downdrown/VaadinHighChartsAPI-Demo/blob/master/src/main/java/at/downdrown/vaadinaddons/demoui/views/BarChartExamples.java*/
    public static final String NAME = "VEHICLES USAGE CALENDAR";
    private PersonService service;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();

    private HighChart chart;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RentaAnavailableCalendarView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

        chart = initChart();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        if (chart != null) {
            verticalLayout.addComponent(chart);
            verticalLayout.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
            verticalLayout.setExpandRatio(chart, 1.0f);
        }

        Button refresh = new Button("Refresh data");
        refresh.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (chart != null) verticalLayout.removeComponent(chart);
                viewLayout.removeComponent(verticalLayout);
                chart = initChart();

                if (chart != null) {
                    verticalLayout.addComponent(chart);
                    verticalLayout.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
                    verticalLayout.setExpandRatio(chart, 1.0f);
                    viewLayout.addComponent(verticalLayout);
                    viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
                    viewLayout.setSizeFull();
                    setCompositionRoot(viewLayout);

                }

            }
        });

//        verticalLayout.addComponent(refresh);
//        verticalLayout.setComponentAlignment(refresh, Alignment.BOTTOM_CENTER);

        viewLayout.addComponent(verticalLayout);
        viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        viewLayout.setSizeFull();
        setCompositionRoot(viewLayout);

    }

    private HighChart initChart() {

        Double all = Double.valueOf(vehicleService.findAll().size());
        if (all.equals(0.0)) {
            return chart;
        }
        HighChart chart = new HighChart();
        ChartConfiguration rentaConfiguration = new ChartConfiguration();
        rentaConfiguration.setTitle("Calendar anavailable dates for vehicles");
        rentaConfiguration.setChartType(ChartType.COLUMNRANGE);
        rentaConfiguration.setBackgroundColor(Colors.WHITE);

        List<List<ColumnRangeChartData>> lists = new ArrayList<>();
        List<String> numbers = vehicleService.findAllActiveNumbers();
        List<ColumnRangeChartSeries> barChartSeriesList = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        numbers.forEach(number -> {
            dates.add(sdf.format(rentaHistoryService.getAvailableDate(number)));
        });

        List<String> labels = new ArrayList<>();
        Date now = new Date();
        for (String number : numbers) {
            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            Date toDate = rentaHistoryService.getHistory(number).getToDate();
            List<ColumnRangeChartData> dataVehiclesNumbers = new ArrayList<>();
            if (now.getTime() <= toDate.getTime()) {
                if (fromDate.getTime() >= now.getTime()) {
                    ColumnRangeChartData data = new ColumnRangeChartData(
                            fromDate.getTime(),
                            now.getTime(),
                            toDate.getTime());
                    dataVehiclesNumbers.add(data);
                    labels.add(data.getHighChartValue());
                } else {
                    ColumnRangeChartData data = new ColumnRangeChartData(
                            now.getTime(),
                            fromDate.getTime(),
                            toDate.getTime());
                    dataVehiclesNumbers.add(data);
                    labels.add(data.getHighChartValue());
                }
            } else {
                ColumnRangeChartData data = new ColumnRangeChartData(
                        fromDate.getTime(),
                        toDate.getTime(),
                        now.getTime());
                dataVehiclesNumbers.add(data);
                labels.add(data.getHighChartValue());

            }

            ColumnRangeChartSeries numbersBar = new ColumnRangeChartSeries(vehicleService.findByVehicleNumber(number).getModel() + " " + number + " from :" + sdf.format(fromDate)+" to:"+sdf.format(toDate), dataVehiclesNumbers);

            lists.add(dataVehiclesNumbers);
            barChartSeriesList.add(numbersBar);
            dates.add(sdf.format(fromDate));
            dates.add(sdf.format(toDate));
        }

        barChartSeriesList.forEach(barChartSeries -> {
                    rentaConfiguration.getSeriesList().add(barChartSeries);
                }

        );
        rentaConfiguration.setTooltipEnabled(true);
        rentaConfiguration.setCreditsEnabled(false);

        /*dates*/
        rentaConfiguration.getyAxis().setTitle("Vehicles anavailable dates");
        rentaConfiguration.getyAxis().setCategories(dates);//numbers
        rentaConfiguration.getyAxis().setLabelsEnabled(true);

        rentaConfiguration.getxAxis().setLabelsEnabled(false);


//        rentaConfiguration.removeBackgroundLines();
        rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);
        rentaConfiguration.setChartMargin(new Margin(150, 10, 50, 90));
        rentaConfiguration.setLegendEnabled(true);

        ColumnRangeChartPlotOptions barChartPlotOptions = new ColumnRangeChartPlotOptions();
        barChartPlotOptions.setDataLabelsFontColor(Colors.LIGHTGRAY);
        barChartPlotOptions.setDataLabelsEnabled(true);
        barChartPlotOptions.setAllowPointSelect(true);
        barChartPlotOptions.setShowCheckBox(false);
        barChartPlotOptions.setSteps(HighChartsPlotOptionsImpl.Steps.CENTER);

        rentaConfiguration.setPlotOptions(barChartPlotOptions);

        try {
            chart = HighChartFactory.renderChart(rentaConfiguration);
            chart.setHeight(100, Unit.PERCENTAGE);
            chart.setWidth(100, Unit.PERCENTAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return chart;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
