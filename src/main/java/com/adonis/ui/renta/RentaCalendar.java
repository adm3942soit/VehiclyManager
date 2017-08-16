package com.adonis.ui.renta;

import com.adonis.data.service.PersonService;
import com.adonis.data.service.RentaHistoryService;
import com.adonis.data.service.VehicleService;
import com.adonis.utils.DateUtils;
import com.adonis.utils.PaymentsUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by oksdud on 12.04.2017.
 */

public class RentaCalendar extends CustomComponent implements View {
    public static final String NAME = "RENTA CALENDAR";
    private PersonService service;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;
    // The view root layout
    HorizontalLayout viewLayout = new HorizontalLayout();


    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RentaCalendar(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

        JsHighChartRenta chart = initChart();
        chart.setId("myJSComponent");

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

    private JsHighChartRenta initChart() {

        Double all = Double.valueOf(vehicleService.findAll().size());
        if (all.equals(0.0)) {
            return new JsHighChartRenta("");
        }
        StringBuffer data = new StringBuffer("");
        List<String> numbers = vehicleService.findAllActiveNumbers();
        data.append("Categories,From date,To date\n");
        Double hour = Double.valueOf((60 * 60 * 1000));
        Date nullDate = DateUtils.convertToDate("01/01/1970");
        int i = 1;
        for (String number : numbers) {
            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            Date toDate = rentaHistoryService.getHistory(number).getToDate();
            data.append(vehicleService.findByVehicleNumber(number).getModel() + " " + number + "(" + sdf.format(fromDate) + "-" + sdf.format(toDate) + ",");
            data.append(
                    String.valueOf(PaymentsUtils.round(
                            Double.valueOf((fromDate.getTime() - nullDate.getTime()) / hour))) + ", " +
                            String.valueOf(PaymentsUtils.round(
                                    Double.valueOf((toDate.getTime() - nullDate.getTime()) / hour))) +
                            ((i < numbers.size()) ? "\n" : "")
            );
            i++;
        }
        JsHighChartRenta chart = new JsHighChartRenta(data.toString());
        chart.setSizeFull();
        return chart;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
