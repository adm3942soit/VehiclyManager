package com.adonis.ui.renta;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.utils.CollectionUtils;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
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

/**
 * Created by oksdud on 12.04.2017.
 */
@Widgetset("org.dussan.vaadin.dcharts.DchartsWidgetset.gwtxml")
//@SpringView(name = LoginView.NAME)
public class RentaLineChartView extends CustomComponent implements View {

    public static final String NAME =  "RENTA LINE CHART VIEW";
    PersonService service;
    RentaHistoryService rentaHistoryService;
    VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();
//    Configuration rentaConfiguration = lineChart.getConfiguration();


    public RentaLineChartView(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService){
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

//        rentaConfiguration.setTitle("Calendar available vehicles");
//        rentaConfiguration.getChart().;
//        rentaConfiguration.getChart().setBackgroundColor();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);

        DCharts barChart = initChart();

        if(barChart!=null){
            verticalLayout.addComponent(barChart);
            verticalLayout.setComponentAlignment(barChart, Alignment.MIDDLE_CENTER);
        }

//        Button refresh = new Button("Refresh data");
//        refresh.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                if(barChart!=null) verticalLayout.removeComponent(barChart);
//                viewLayout.removeComponent(verticalLayout);
//                barChart = initChart();
//
//                if(barChart!=null){
//                    verticalLayout.addComponent(barChart);
//                    verticalLayout.setComponentAlignment(barChart, Alignment.MIDDLE_CENTER);
//                    viewLayout.addComponent(verticalLayout);
//                    viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
//                    viewLayout.setSizeFull();
//                    setCompositionRoot(viewLayout);
//
//                }
//
//            }
//        });

//        verticalLayout.addComponent(refresh);
//        verticalLayout.setComponentAlignment(refresh, Alignment.BOTTOM_CENTER);

        viewLayout.addComponent(verticalLayout);
        viewLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        viewLayout.setSizeFull();
        setCompositionRoot(viewLayout);

    }

    private DCharts initChart(){

//        Double working = Double.valueOf(rentaHistoryService.findAllWorking().size());
//        Double active = Double.valueOf(vehicleService.findAllActive().size());
        Double all = Double.valueOf(vehicleService.findAll().size());
//        Double notActive = all - active;
        if(all.equals(0.0))  return null;
            DataSeries dateSeries = new DataSeries();
            SeriesDefaults seriesDefaults = new SeriesDefaults()
                    .setRenderer(SeriesRenderers.BAR);
            Axes axes = new Axes()
                    .addAxis(
                            new XYaxis()
                                    .setRenderer(AxisRenderers.CATEGORY)
                                    .setTicks(
                                            new Ticks()
                                                    .add(CollectionUtils.convertIntoCommaSeparatedString(vehicleService.findAllActiveNumbers()))));

            Highlighter highlighter = new Highlighter()
                    .setShow(false);

            Options options = new Options()
                    .setSeriesDefaults(seriesDefaults)
                    .setAxes(axes)
                    .setHighlighter(highlighter);

            vehicleService.findAllActiveNumbers().forEach(
                    number->{dateSeries.add(
                            rentaHistoryService.getAvailableDate(number));
                    });

            DCharts chart = new DCharts()
                    .setDataSeries(dateSeries)
                    .setOptions(options)
                    .show();
       return chart;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
