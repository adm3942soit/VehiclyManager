package com.adonis.ui.renta;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.PieChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.PieChartSeries;
import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

/**
 * Created by oksdud on 12.04.2017.
 */
public class RentaChartView extends CustomComponent implements View {

    public static final String NAME =  "RENTA CHART VIEW";
    PersonService service;
    RentaHistoryService rentaHistoryService;
    VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();
    ChartConfiguration rentaConfiguration = new ChartConfiguration();

    public RentaChartView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService){
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;
        setSizeFull();
        rentaConfiguration.setTitle("TestRenta");
        rentaConfiguration.setChartType(ChartType.PIE);
        rentaConfiguration.setBackgroundColor(Colors.WHITE);
        Double working = Double.valueOf(rentaHistoryService.findAllWorking().size());
        Double active = Double.valueOf(vehicleService.findAllActive().size());
        Double all = Double.valueOf(vehicleService.findAll().size());
        Double notActive = all - active;
        if(!all.equals(0.0)) {
            PieChartSeries pieVehicles = new PieChartSeries("Vehicles");
            PieChartData workingVehicles = new PieChartData("Working", Double.valueOf((working / all) * 100));
            PieChartData notWorkingVehicles = new PieChartData("Not working", Double.valueOf(((active - working) / all) * 100));
            PieChartData notActiveVehicles = new PieChartData("Not active", Double.valueOf((notActive / all) * 100));
            pieVehicles.getData().add(workingVehicles);
            pieVehicles.getData().add(notActiveVehicles);
            pieVehicles.getData().add(notWorkingVehicles);


            rentaConfiguration.getSeriesList().add(pieVehicles);
            try {
                HighChart pieChart = HighChartFactory.renderChart(rentaConfiguration);
                pieChart.setHeight(80, UNITS_PERCENTAGE);
                pieChart.setWidth(80, UNITS_PERCENTAGE);
                System.out.println("PieChart Script : " + rentaConfiguration.getHighChartValue());
                viewLayout.addComponent(pieChart);
                viewLayout.setComponentAlignment(pieChart, Alignment.MIDDLE_CENTER);
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
