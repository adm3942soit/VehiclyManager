package com.adonis.ui.renta;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by oksdud on 12.04.2017.
 */
public class RentaLineChartView extends CustomComponent implements View {

    public static final String NAME =  "RENTA CHART VIEW";
    PersonService service;
    RentaHistoryService rentaHistoryService;
    VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();
    static DataSeries lineVehicles = new DataSeries("Calendar available vehicles");
    static Chart lineChart = new Chart();
    Configuration rentaConfiguration = lineChart.getConfiguration();


    public RentaLineChartView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService){
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

        rentaConfiguration.setTitle("Calendar available vehicles");
        rentaConfiguration.getChart().setType(ChartType.LINE);
//        rentaConfiguration.getChart().setBackgroundColor();

        VerticalLayout verticalLayout = new VerticalLayout();

        lineChart = initChart();

        if(lineChart!=null){
            verticalLayout.addComponent(lineChart);
            verticalLayout.setComponentAlignment(lineChart, Alignment.MIDDLE_CENTER);
        }

        Button refresh = new Button("Refresh data");
        refresh.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                verticalLayout.removeComponent(lineChart);
                viewLayout.removeComponent(verticalLayout);
                lineChart = initChart();

                if(lineChart!=null){
                    verticalLayout.addComponent(lineChart);
                    verticalLayout.setComponentAlignment(lineChart, Alignment.MIDDLE_CENTER);
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

    private Chart initChart(){

        Double working = Double.valueOf(rentaHistoryService.findAllWorking().size());
        Double active = Double.valueOf(vehicleService.findAllActive().size());
        Double all = Double.valueOf(vehicleService.findAll().size());
        Double notActive = all - active;
        if(!all.equals(0.0)) {

//            lineVehicles = new DataSeries("Vehicles");

            vehicleService.findAllActiveNumbers().forEach(
                    number->{lineVehicles.add(
                            new DataSeriesItem(rentaHistoryService.getAvailableDate(number), Long.valueOf(number)));
                    });
            rentaConfiguration.addSeries(lineVehicles);
            rentaConfiguration.getxAxis().setTitle("Date");
            rentaConfiguration.getyAxis().setTitle("Number");

        }
       return null;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
