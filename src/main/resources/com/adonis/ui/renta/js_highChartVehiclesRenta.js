com_adonis_ui_renta_JsHighChartVehiclesRenta = function () {
    var element = $(this.getElement());
    // getData
    var title = this.getState().titleChart;
    var data = this.getState().data;
    var labels = this.getState().labels;
    var categories = this.getState().categories;
    var units = this.getState().titleY;
    var idComponent = this.getState().idComponent;
    $(document).ready(readDataAndDraw())

    this.onStateChange = function () {
        $(document).ready(readDataAndDraw())
    }

    function readDataAndDraw() {
        var id = document.getElementById(idComponent);
        // double check if we really found the right div
        if (id == null) return;
        if (id.id != idComponent) return;

        var options = {
            chart: {
                // type: 'columnrange',
                // inverted: true,
                renderTo: idComponent,
                defaultSeriesType: 'line',
                marginRight: 130,
                marginBottom: 250
            },
            title: {
                text: title
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -10,
                y: 100,
                borderWidth: 0
            },
            xAxis: {
                type: 'category',
                labels: {
                    // rotation: -45,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    },
                    y: 10,
                    marginBottom: 250,
                    formatter: function () {
                        return Date.parse(this.value);//this.value / 1000;
                    }
                    // format: '{point.y:.0f}', // 0 decimal
                },
                categories: []
            },
            yAxis: {
                min: 0,
                title: {
                    text: units
                },
                dataLabels: {
                    enabled: true,
                    y: 10, // 10 pixels down from the top
                    format: '{point.y:.0f}', // 0 decimal
                }
            },
            tooltip:
                {
                    headerFormat: '<b>{point.x}</b><br/>',
                    pointFormat:
                        '{point.x}: {point.y}<br/>'
                }
            ,
            series: []
        };
        // var categoryArray = categories.split(',');
        //
        // $.each(categoryArray, function(itemNo, item) {
        //      if (itemNo > 0) options.xAxis.categories.push(item);
        // });
        // Split the lines
        var lines = data.split('\n');
        // Iterate over the lines and add categories or series
        $.each(lines, function (lineNo, line) {
            var items = line.split(',');

            // header line containes categories
            if (lineNo == 0) {
                $.each(items, function (itemNo, item) {
                    if (itemNo > 0) options.xAxis.categories.push(item);
                });
            }

            // the rest of the lines contain data with their name in the first position
            else {
                var series = {
                    data: []
                };
                $.each(items, function (itemNo, item) {
                    if (itemNo == 0) {
                        series.name = item;
                    } else {
                        series.data.push(parseFloat(item));
                    }
                });

                options.series.push(series);

            }

        });

        // Split the lines
        // var lines = data.split('\n');
        // var labelsLines = labels.split('\n');
        // // Iterate over the lines and add categories or series
        // // var numRow = 0;
        // $.each(lines, function(lineNo, line) {
        //
        //     var items = line.split(',');
        //
        //         var series = {
        //             data: []
        //         };
        //
        //         $.each(items, function(itemNo, item) {
        //             if (itemNo == 0) {
        //                 // series.name[0] = label;
        //                 series.name[0] = "!!!";
        //                 series.name[1] = labelsLines;//[numRow]
        //             } else {
        //                 series.data.push(parseFloat(item));
        //             }
        //         });
        //
        //         options.series.push(series);
        //     // numRow = numRow + 1;
        //
        // });

        // Create the chart
        var chart = new Highcharts.Chart(options);
    }
}
;