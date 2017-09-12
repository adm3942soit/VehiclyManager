package com.adonis.ui.renta.highchartsapi;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Margin;
import at.downdrown.vaadinaddons.highchartsapi.model.data.HighChartsData;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.StringDoubleData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.ColumnChartPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.HighChartsPlotOptionsImpl;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnChartSeries;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.utils.DateUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisValueType.DATETIME;
import static com.adonis.utils.NumberUtils.round;

/**
 * Created by oksdud on 12.04.2017.
 */

public class RentaCalendarView extends CustomComponent implements View {
    /*https://github.com/downdrown/VaadinHighChartsAPI-Demo/blob/master/src/main/java/at/downdrown/vaadinaddons/demoui/views/BarChartExamples.java*/
    public static final String NAME = "CALENDAR AVAILABLE DATES FOR VIHICLES";
    private PersonService service;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RentaCalendarView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        HorizontalLayout viewLayout = new HorizontalLayout();
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

        HighChart chart = initChart();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        if (chart != null) {
            verticalLayout.addComponent(chart);
            verticalLayout.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
            verticalLayout.setExpandRatio(chart, 1.0f);
        }

        viewLayout.addComponent(verticalLayout);
        viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        viewLayout.setSizeFull();
        setCompositionRoot(viewLayout);

    }

    private HighChart initChart() {

        Double all = Double.valueOf(vehicleService.findAll().size());
        if (all.equals(0.0)) {
            return new HighChart();
        }
        HighChart chart = new HighChart();
        ChartConfiguration rentaConfiguration = new ChartConfiguration();
        rentaConfiguration.setTitle("Calendar available dates for vehicles");
        rentaConfiguration.setChartType(ChartType.COLUMN);
        rentaConfiguration.setBackgroundColor(Colors.WHITE);

        Double hour = Double.valueOf((60 * 60 * 1000));

        List<List<HighChartsData>> lists = new ArrayList<>();
        List<String> numbers = vehicleService.findAllActiveNumbers();
        List<ColumnChartSeries> barChartSeriesList = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Date lastData = rentaHistoryService.getAvailableDate(numbers.get(0));
        Date monthAgo = DateUtils.anyDaysAgo(lastData, 30);
        Date nullDate = DateUtils.convertToDate("01/01/1970");
        org.joda.time.DateTime nullDateTime = new DateTime(nullDate);
        org.joda.time.DateTime monthAgoDateTime = new DateTime(monthAgo);
        for (String number : numbers) {
            DateTime lastAvailableData = new DateTime(rentaHistoryService.getAvailableDate(number));
            String dateString = lastAvailableData.dayOfMonth().getAsShortText() + " " + lastAvailableData.monthOfYear().getAsShortText() + " " + lastAvailableData.year().getAsString();
            List<HighChartsData> dataVehiclesNumbers = new ArrayList<>();
            StringDoubleData stringDoubleData = new StringDoubleData(
                    number + " last available date : " + dateString,//sdf.format(lastAvailableData),
                    round(Double.valueOf(lastAvailableData.getMillis() - monthAgoDateTime.getMillis()) / hour));
            dataVehiclesNumbers.add(stringDoubleData);

            labels.add(stringDoubleData.getHighChartValue());

            ColumnChartSeries numbersBar = new ColumnChartSeries(
                    vehicleService.findByVehicleNumber(number).getModel() + " " + number + " available date :" + dateString,//sdf.format(lastAvailableData),
                    dataVehiclesNumbers);
            lists.add(dataVehiclesNumbers);
            barChartSeriesList.add(numbersBar);
            dates.add(String.valueOf(round(Double.valueOf(lastAvailableData.getMillis() - nullDateTime.getMillis()) / hour)));//
        }

        barChartSeriesList.forEach(barChartSeries -> {
                    rentaConfiguration.getSeriesList().add(barChartSeries);
                }

        );
        rentaConfiguration.setTooltipEnabled(true);
        rentaConfiguration.setCreditsEnabled(true);
        rentaConfiguration.setLegendEnabled(true);


        rentaConfiguration.getxAxis().setLabelsEnabled(false);
        rentaConfiguration.getxAxis().setCategories(dates);
        rentaConfiguration.getxAxis().setTitle("Available dates");

        /*dates*/
        rentaConfiguration.getyAxis().setCategories(dates);
        rentaConfiguration.getyAxis().setAxisValueType(DATETIME);
        rentaConfiguration.getyAxis().setLabelsEnabled(false);


        rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);
        rentaConfiguration.setChartMargin(new Margin(50, 20, 10, 120));
        rentaConfiguration.setLegendEnabled(true);

        ColumnChartPlotOptions barChartPlotOptions = new ColumnChartPlotOptions();
        barChartPlotOptions.setDataLabelsFontColor(Colors.BROWN);
        barChartPlotOptions.setDataLabelsEnabled(true);
        barChartPlotOptions.setAllowPointSelect(true);
        barChartPlotOptions.setShowCheckBox(false);
        barChartPlotOptions.setSteps(HighChartsPlotOptionsImpl.Steps.RIGHT);
        barChartPlotOptions.setAnimated(true);

        rentaConfiguration.setPlotOptions(barChartPlotOptions);

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
