package com.adonis.ui.renta;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
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
import com.adonis.utils.DateUtils;
import com.adonis.utils.PaymentsUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.*;

import java.text.SimpleDateFormat;
import java.util.*;

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

        Date lastData = rentaHistoryService.getAvailableDate(numbers.get(0));
        Date monthAgo = DateUtils.anyDaysAgo(lastData, 30);

        List<Date> dates = new ArrayList<>();
        List<String> datesString = new ArrayList<>();
        dates.add(monthAgo);
        numbers.forEach(number -> {
            dates.add(rentaHistoryService.getHistory(number).getFromDate());
            dates.add(rentaHistoryService.getHistory(number).getToDate());
        });
        dates.add(new Date());
        Collections.sort(dates, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return Long.compare(o1.getTime(), o2.getTime());
            }
        });
        dates.forEach(date -> {
            datesString.add(sdf.format(date));
        });
        List<String> labels = new ArrayList<>();
        List<Double> doubleDates = new ArrayList<>();
        Double hour = Double.valueOf((60 * 60 * 1000));
        for (String number : numbers) {
            List<ColumnRangeChartData> dataVehiclesNumbers = new ArrayList<>();
            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            Date toDate = rentaHistoryService.getHistory(number).getToDate();

            Date nullDate = DateUtils.convertToDate("01/01/1970");
            Date now = new Date();
            List<Double> currentDates = new ArrayList<>();
            currentDates.add(PaymentsUtils.round2(Double.valueOf((now.getTime() - nullDate.getTime()) / hour)));//(fromDate.getTime() - nullDate.getTime() + middle)));//1000));
            currentDates.add(PaymentsUtils.round2(Double.valueOf((fromDate.getTime() - nullDate.getTime())/hour)));
            currentDates.add(PaymentsUtils.round2(Double.valueOf((toDate.getTime() - nullDate.getTime())/hour)));
            ColumnRangeChartData data = new ColumnRangeChartData(
                    currentDates.get(0),
                    currentDates.get(1),
                    currentDates.get(2));
            dataVehiclesNumbers.add(data);
            labels.add(data.getHighChartValue());

            ColumnRangeChartSeries numbersBar = new ColumnRangeChartSeries(
                    vehicleService.findByVehicleNumber(number).getModel() + " " + number + " from :" + sdf.format(fromDate) + " to:" + sdf.format(toDate),
                    dataVehiclesNumbers);
            barChartSeriesList.add(numbersBar);

            lists.add(dataVehiclesNumbers);
            doubleDates.addAll(currentDates);
        }

        barChartSeriesList.forEach(barChartSeries -> {
                    rentaConfiguration.getSeriesList().add(barChartSeries);
                }

        );
        rentaConfiguration.setTooltipEnabled(true);
        rentaConfiguration.setCreditsEnabled(false);

        /*dates*/
        rentaConfiguration.getyAxis().setTitle("Vehicles anavailable dates");
        rentaConfiguration.getyAxis().setAllowDecimals(false);
        Collections.sort(doubleDates, new Comparator<Double>() {
            public int compare(Double o1, Double o2) {
                return Double.compare(o1, o2);
            }
        });

        List<String> doubleString = new ArrayList<>();
//        doubleString.add(String.valueOf(0));
        doubleDates.forEach(aDouble -> {
            doubleString.add(String.valueOf(aDouble));
        });
//        doubleString.add(String.valueOf(new Date().getTime() - nullD.getTime()));
        rentaConfiguration.getyAxis().setCategories(doubleString);//datesString doubleString);//numbers
        rentaConfiguration.getyAxis().setLabelsEnabled(true);
        rentaConfiguration.getyAxis().setAxisValueType(Axis.AxisValueType.DATETIME);
        rentaConfiguration.getyAxis().setLineColor(Color.GREEN);
        rentaConfiguration.getyAxis().setLineWidth(5);


        List<String> counters = new ArrayList<>();
        counters.add(String.valueOf(0));
        for (int i = 1; i <= numbers.size()*10; i=i+10) {
            counters.add(String.valueOf(i));
        }
        counters.add(String.valueOf(numbers.size() + 1));
        Axis axis = new Axis();
        axis.setAxisType(Axis.AxisType.xAxis);
        axis.setTickLength(numbers.size() + 1);
        axis.setAllowDecimals(false);
        axis.setCategories(counters);
        axis.setGridLineWidth(1);
        axis.setAxisValueType(Axis.AxisValueType.DATETIME);
        rentaConfiguration.setxAxis(axis);
        rentaConfiguration.getxAxis().setCategories(counters);
        rentaConfiguration.getxAxis().setLabelsEnabled(false);
        rentaConfiguration.getxAxis().setLineColor(Color.GREEN);
        rentaConfiguration.getxAxis().setLineWidth(5);


        rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);
        rentaConfiguration.setChartMargin(new Margin(110, 10, 10, 100));
        rentaConfiguration.setLegendEnabled(true);

        ColumnRangeChartPlotOptions barChartPlotOptions = new ColumnRangeChartPlotOptions();
        barChartPlotOptions.setDataLabelsFontColor(Colors.LIGHTGRAY);
        barChartPlotOptions.setDataLabelsEnabled(true);
        barChartPlotOptions.setAllowPointSelect(true);
        barChartPlotOptions.setShowCheckBox(false);
        barChartPlotOptions.setAnimated(true);
        barChartPlotOptions.setSteps(HighChartsPlotOptionsImpl.Steps.FALSE);

        rentaConfiguration.setPlotOptions(barChartPlotOptions);
        rentaConfiguration.setLegendEnabled(true);
        rentaConfiguration.setTooltipEnabled(true);

        try {
            chart = HighChartFactory.renderChart(rentaConfiguration);
            chart.setSizeFull();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return chart;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
