package at.downdrown.vaadinaddons.highchartsapi;

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
import com.vaadin.ui.VerticalLayout;

/**
 * Created by oksdud on 12.04.2017.
 */
public class RentaPieChartView extends CustomComponent implements View {

    public static final String NAME = "RENTA CHART VIEW";
    PersonService service;
    RentaHistoryService rentaHistoryService;
    VehicleService vehicleService;


    public RentaPieChartView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        // The view root layout
        HorizontalLayout viewLayout = new HorizontalLayout();

        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();

        HighChart pieChart = initChart();

        if (pieChart != null) {
            verticalLayout.addComponent(pieChart);
            verticalLayout.setComponentAlignment(pieChart, Alignment.MIDDLE_CENTER);
        }

//        Button refresh = new Button("Refresh data");
//        refresh.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                verticalLayout.removeComponent(pieChart);
//                viewLayout.removeComponent(verticalLayout);
//                pieChart = initChart();
//
//                if (pieChart != null) {
//                    verticalLayout.addComponent(pieChart);
//                    verticalLayout.setComponentAlignment(pieChart, Alignment.MIDDLE_CENTER);
//                    viewLayout.addComponent(verticalLayout);
//                    viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
//                    viewLayout.setSizeFull();
//                    setCompositionRoot(viewLayout);
//
//                }
//
//            }
//        });
//
//        verticalLayout.addComponent(refresh);
//        verticalLayout.setComponentAlignment(refresh, Alignment.BOTTOM_CENTER);

        viewLayout.addComponent(verticalLayout);
        viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        viewLayout.setSizeFull();
        setCompositionRoot(viewLayout);

    }

    private HighChart initChart() {
        PieChartSeries pieVehicles = new PieChartSeries("Vehicles");
        pieVehicles.getData().clear();
        HighChart pieChart = new HighChart();
        Double working = Double.valueOf(rentaHistoryService.findAllWorking().size());
        Double active = Double.valueOf(vehicleService.findAllActive().size());
        Double all = Double.valueOf(vehicleService.findAll().size());
        Double notActive = all - active;
        if (!all.equals(0.0)) {

            PieChartData workingVehicles = new PieChartData("Working", Double.valueOf((working / all) * 100));
            PieChartData notWorkingVehicles = new PieChartData("Not working", Double.valueOf(((active - working) / all) * 100));
            PieChartData notActiveVehicles = new PieChartData("Not active", Double.valueOf((notActive / all) * 100));

            pieVehicles.getData().add(workingVehicles);
            pieVehicles.getData().add(notActiveVehicles);
            pieVehicles.getData().add(notWorkingVehicles);
            ChartConfiguration rentaConfiguration = new ChartConfiguration();


            rentaConfiguration.setTitle("TestRenta");
            rentaConfiguration.setChartType(ChartType.PIE);
            rentaConfiguration.setBackgroundColor(Colors.LIGHTCYAN);


            rentaConfiguration.getSeriesList().add(pieVehicles);

            try {
                pieChart = HighChartFactory.renderChart(rentaConfiguration);
                pieChart.setSizeFull();
                System.out.println("PieChart Script : " + rentaConfiguration.getHighChartValue());
                return pieChart;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
