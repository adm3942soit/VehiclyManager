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
import com.adonis.utils.DateUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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
        for (String number : numbers) {
            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            Date toDate = rentaHistoryService.getHistory(number).getToDate();
            Double middle = Double.valueOf(toDate.getTime()-fromDate.getTime()/2);

            List<ColumnRangeChartData> dataVehiclesNumbers = new ArrayList<>();
            List<Double> currentDates = new ArrayList<>();
            currentDates.add(Double.valueOf((fromDate.getTime() - monthAgo.getTime()+ middle)));//1000));
            currentDates.add(Double.valueOf((fromDate.getTime() - monthAgo.getTime())));
            currentDates.add(Double.valueOf((toDate.getTime() - monthAgo.getTime())));
//            Collections.sort(currentDates, new Comparator<Double>() {
//                public int compare(Double o1, Double o2) {
//                    return Double.compare(o1, o2);
//                }
//            });
            ColumnRangeChartData data = new ColumnRangeChartData(
                    currentDates.get(0),
                    currentDates.get(1),
                    currentDates.get(2));
            dataVehiclesNumbers.add(data);
            labels.add(data.getHighChartValue());

            ColumnRangeChartSeries numbersBar = new ColumnRangeChartSeries(
                    vehicleService.findByVehicleNumber(number).getModel() + " " + number + " from :" + sdf.format(fromDate) + " to:" + sdf.format(toDate),
                    dataVehiclesNumbers);

            lists.add(dataVehiclesNumbers);
            barChartSeriesList.add(numbersBar);
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

        List<String> doubleString = new ArrayList<>();
        doubleString.add(String.valueOf(0));
        doubleDates.forEach(aDouble -> {doubleString.add(String.valueOf(aDouble));});
        doubleString.add(String.valueOf(new Date().getTime()-monthAgo.getTime()));
        rentaConfiguration.getyAxis().setCategories(datesString);//doubleString);//numbers
        rentaConfiguration.getyAxis().setLabelsEnabled(true);
//        List<String> counters  = new ArrayList<>();
//        for(int i=1; i<=numbers.size();i++){
//            counters.add(String.valueOf(i));
//        }
//
//        rentaConfiguration.getxAxis().setCategories(counters);
        rentaConfiguration.getxAxis().setLabelsEnabled(false);

        rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);
        rentaConfiguration.setChartMargin(new Margin(110, 10, 10, 100));
        rentaConfiguration.setLegendEnabled(true);

        ColumnRangeChartPlotOptions barChartPlotOptions = new ColumnRangeChartPlotOptions();
        barChartPlotOptions.setDataLabelsFontColor(Colors.LIGHTGRAY);
        barChartPlotOptions.setDataLabelsEnabled(false);
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
