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

public class RentaCalendarForLastMonth extends CustomComponent implements View {
    public static final String NAME = "RENTA CALENDAR FOR LAST MONTH";
    private PersonService personService;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;
    public static  JsHighChartRenta chart;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RentaCalendarForLastMonth(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        HorizontalLayout viewLayout = new HorizontalLayout();
        this.personService = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();
        chart = initChart();
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
            return new JsHighChartRenta("", "myJSComponent");
        }
        StringBuffer data = new StringBuffer("");
        Date now = new Date();
        Date monthAgo = DateUtils.anyDaysAgo(now, 30);

        List<String> numbers = vehicleService.findAllActiveNumbers();
        data.append("Categories,From date,To date\n");
        Double hour = Double.valueOf((60 * 60 * 1000));
        int i = 1;
        for (String number : numbers) {

            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            Date toDate = rentaHistoryService.getHistory(number).getToDate();
            if(fromDate.getTime()>= monthAgo.getTime()) {//for the last month
                data.append(vehicleService.findByVehicleNumber(number).getModel() + " " + number + "(" + sdf.format(fromDate) + "-" + sdf.format(toDate) + ",");
                data.append(
                        String.valueOf(PaymentsUtils.round(
                                Double.valueOf((fromDate.getTime() - monthAgo.getTime()) / hour))) + ", " +
                                String.valueOf(PaymentsUtils.round(
                                        Double.valueOf((toDate.getTime() - monthAgo.getTime()) / hour))) +
                                ((i < numbers.size()) ? "\n" : "")
                );
            }
            i++;
        }
        JsHighChartRenta chart = new JsHighChartRenta(data.toString(), "myJSComponent");
        chart.setSizeFull();
        chart.setId("myJSComponent");
        return chart;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
