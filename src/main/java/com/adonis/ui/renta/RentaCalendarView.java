package com.adonis.ui.renta;

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
import com.vaadin.ui.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisValueType.DATETIME;

/**
 * Created by oksdud on 12.04.2017.
 */

public class RentaCalendarView extends CustomComponent implements View {
    /*https://github.com/downdrown/VaadinHighChartsAPI-Demo/blob/master/src/main/java/at/downdrown/vaadinaddons/demoui/views/BarChartExamples.java*/
    public static final String NAME = "RENTA CALENDAR";
    private PersonService service;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();

    private HighChart chart;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RentaCalendarView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
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
        rentaConfiguration.setTitle("Calendar available dates for vehicles");
        rentaConfiguration.setChartType(ChartType.COLUMN);
        rentaConfiguration.setBackgroundColor(Colors.WHITE);

        List<List<HighChartsData>> lists = new ArrayList<>();
        List<String> numbers = vehicleService.findAllActiveNumbers();
        List<ColumnChartSeries> barChartSeriesList = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Date lastData = rentaHistoryService.getAvailableDate(numbers.get(0));
        Date monthAgo = DateUtils.anyDaysAgo(lastData, 30);
        for (String number : numbers) {
            Date lastAvailableData = rentaHistoryService.getAvailableDate(number);

            List<HighChartsData> dataVehiclesNumbers = new ArrayList<>();
            StringDoubleData stringDoubleData = new StringDoubleData(
                    number+ " last available date : "+sdf.format(lastAvailableData),
                    rentaHistoryService.getAvailableDate(number).getTime() - monthAgo.getTime());
            dataVehiclesNumbers.add(stringDoubleData);

            labels.add(stringDoubleData.getHighChartValue());

            ColumnChartSeries numbersBar = new ColumnChartSeries(
       vehicleService.findByVehicleNumber(number).getModel()+" "+number+" available date :"+sdf.format(lastAvailableData),
                    dataVehiclesNumbers);
            lists.add(dataVehiclesNumbers);
            barChartSeriesList.add(numbersBar);
            dates.add(sdf.format(lastAvailableData));
        }

        barChartSeriesList.forEach(barChartSeries -> {
            rentaConfiguration.getSeriesList().add(barChartSeries);}

            );
        rentaConfiguration.setTooltipEnabled(true);
        rentaConfiguration.setCreditsEnabled(true);


        rentaConfiguration.getxAxis().setLabelsEnabled(false);

        rentaConfiguration.getxAxis().setTitle("Available dates");

        /*dates*/
        rentaConfiguration.getyAxis().setCategories(dates);
        rentaConfiguration.getyAxis().setAxisValueType(DATETIME);
        rentaConfiguration.getyAxis().setLabelsEnabled(false);


//        rentaConfiguration.removeBackgroundLines();
        rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);
        rentaConfiguration.setChartMargin(new Margin(50, 20, 10, 120));
        rentaConfiguration.setLegendEnabled(true);

        ColumnChartPlotOptions barChartPlotOptions = new ColumnChartPlotOptions();
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

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return chart;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
