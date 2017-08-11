package com.adonis.ui.renta;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Margin;
import at.downdrown.vaadinaddons.highchartsapi.model.data.HighChartsData;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.StringDoubleData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.BarChartPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.BarChartSeries;
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

        verticalLayout.addComponent(refresh);
        verticalLayout.setComponentAlignment(refresh, Alignment.BOTTOM_CENTER);

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
        rentaConfiguration.setChartType(ChartType.BAR);
        rentaConfiguration.setBackgroundColor(Colors.WHITE);

        List<List<HighChartsData>> lists = new ArrayList<>();
        List<String> numbers = vehicleService.findAllActiveNumbers();
        List<BarChartSeries> barChartSeriesList = new ArrayList<>();
        for (String number : numbers) {
            Date lastAvailableData = rentaHistoryService.getAvailableDate(number);
            List<HighChartsData> dataVehiclesNumbers = new ArrayList<>();
            dataVehiclesNumbers.add(new StringDoubleData(
                    number+ " last date : "+sdf.format(lastAvailableData), rentaHistoryService.getAvailableDate(number).getTime()));
            lists.add(dataVehiclesNumbers);
            BarChartSeries numbersBar = new BarChartSeries("Vehicle "+number, dataVehiclesNumbers);
            barChartSeriesList.add(numbersBar);

        }


        List<String> dates = new ArrayList<>();
        for (String number : numbers) {
            dates.add(sdf.format(rentaHistoryService.getAvailableDate(number)));
        }
        for (BarChartSeries barChartSeries : barChartSeriesList) {
            rentaConfiguration.getSeriesList().add(barChartSeries);
        }

        rentaConfiguration.getxAxis().setLabelsEnabled(true);
        rentaConfiguration.getyAxis().setLabelsEnabled(true);
        rentaConfiguration.getyAxis().setCategories(dates);
        rentaConfiguration.getxAxis().setCategories(numbers);
        rentaConfiguration.getxAxis().setShowFirstLabel(true);
        rentaConfiguration.getxAxis().setShowLastLabel(true);
        rentaConfiguration.getxAxis().setAllowDecimals(false);

//        rentaConfiguration.removeBackgroundLines();
        rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);
        rentaConfiguration.setChartMargin(new Margin(100, 50, 100, 120));
        rentaConfiguration.setLegendEnabled(true);

        BarChartPlotOptions barChartPlotOptions = new BarChartPlotOptions();
        barChartPlotOptions.setDataLabelsFontColor(Colors.LIGHTGRAY);
        barChartPlotOptions.setShowCheckBox(false);

        rentaConfiguration.setPlotOptions(barChartPlotOptions);

        try {
        chart = HighChartFactory.renderChart(rentaConfiguration);
        chart.setHeight(60, Unit.PERCENTAGE);
        chart.setWidth(90, Unit.PERCENTAGE);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return chart;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
