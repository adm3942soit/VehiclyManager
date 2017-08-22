package com.adonis.ui.renta;

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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by oksdud on 12.04.2017.
 */

public class RentaCalendarForLastMonthRevert extends CustomComponent implements View {
    public static final String NAME = "CALENDAR FOR LAST MONTH";
    private PersonService service;
    private RentaHistoryService rentaHistoryService;
    private VehicleService vehicleService;
    public static JsHighChartVehiclesRenta chart;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RentaCalendarForLastMonthRevert(PersonService personService, RentaHistoryService rentaHistoryService, VehicleService vehicleService) {
        HorizontalLayout viewLayout = new HorizontalLayout();
        this.service = personService;
        this.rentaHistoryService = rentaHistoryService;
        this.vehicleService = vehicleService;

        setSizeFull();

        chart = initChart();
        chart.setId("myJSRentaComponent");

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

    private JsHighChartVehiclesRenta initChart() {

        Double all = Double.valueOf(vehicleService.findAll().size());
        if (all.equals(0.0)) return new JsHighChartVehiclesRenta("", "", "", "myJSRentaComponent");
        StringBuffer data = new StringBuffer("");
        Date now = new Date();
        Date monthAgo = DateUtils.anyDaysAgo(now, 30);

        List<String> numbers = vehicleService.findAllActiveNumbers();
        //x
        data.append("Categories");
        List<Date> dates = new ArrayList<>();
        List<String> datesString = new ArrayList<>();
        dates.add(monthAgo);
        numbers.forEach(number -> {
            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            if (fromDate.getTime() >= monthAgo.getTime()) {//for the last month
                dates.add(rentaHistoryService.getHistory(number).getFromDate());
                dates.add(rentaHistoryService.getHistory(number).getToDate());
            }
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

        datesString.forEach(dateString -> {
            data.append(", " + dateString);
        });
        data.append("\n");

        Double hour = Double.valueOf((60 * 60 * 1000));
        Date nullDate = DateUtils.convertToDate("01/01/1970");
        int j = 1;
        for (int i = 0; i < numbers.size(); i++) {
            String number = numbers.get(i);
            Date fromDate = rentaHistoryService.getHistory(number).getFromDate();
            Date toDate = rentaHistoryService.getHistory(number).getToDate();
            //y - name
            data.append(vehicleService.findByVehicleNumber(number).getModel() + " " + number
            );
            String counter = String.valueOf(j);
            //y - data
            dates.forEach(date -> {
                if (fromDate.getTime() >= monthAgo.getTime()) {//for the last month
                    if (fromDate.equals(date) || toDate.equals(date)) {
                        data.append(", " + counter);
                    } else {
                        data.append(", " + String.valueOf(0));
                    }
                }
            });
            if(i < numbers.size()){
                data.append("\n");
            }
            j++;
        }

        StringBuffer labels = new StringBuffer("");
        StringBuffer categories = new StringBuffer("");
        categories.append("Categories");//,From date,To date


        JsHighChartVehiclesRenta chart = new JsHighChartVehiclesRenta(data.toString(), labels.toString(), categories.toString(), "myJSRentaComponent");
        chart.setSizeFull();
        chart.setId("myJSRentaComponent");
        return chart;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
