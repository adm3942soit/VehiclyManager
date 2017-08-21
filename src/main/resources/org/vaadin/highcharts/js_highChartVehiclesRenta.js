com_adonis_ui_renta_JsHighChartVehiclesRenta = function()
{
    var element = $(this.getElement());
    // getData
    var title = this.getState().titleChart;
    var data = this.getState().data;
    var labels = this.getState().labels;
    var categories = this.getState().categories;
    var units = this.getState().titleY;

    $(document).ready(readDataAndDraw())

    this.onStateChange = function()
    {
        $(document).ready(readDataAndDraw())
    }

    function readDataAndDraw()
    {
        var id = document.getElementById("myJSRentaComponent");
        // double check if we really found the right div
        if (id == null) return;
        if(id.id != "myJSRentaComponent") return;

        var options = {
            chart: {
                renderTo: 'myJSRentaComponent',
                defaultSeriesType: 'line',
                marginRight: 130,
                marginBottom: 25
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
                categories: []
            },
            yAxis: {
                title: {
                    text: units
                }
            },
            tooltip: {
                headerFormat: '<b>{point.x}</b><br/>',
                pointFormat: '{point.x}: {point.y}<br/>{series.name[0]+series.name[1]}'
            },
            series: []
        };
        var categoryArray = categories.split(',');

        $.each(categoryArray, function(itemNo, item) {
             if (itemNo > 0) options.xAxis.categories.push(item);
        });
        // Split the lines
        var lines = data.split('\n');
        var labelsLines = labels.split('\n');
        // Iterate over the lines and add categories or series
        var numRow = 0;
        $.each(lines, function(lineNo, line) {

            var items = line.split(',');

                var series = {
                    data: []
                };

                $.each(items, function(itemNo, item) {
                    if (itemNo == 0) {
                        // series.name[0] = label;
                        series.name[0] = "!!!";
                        series.name[1] = labelsLines[numRow];
                    } else {
                        series.data.push(parseFloat(item));
                    }
                });

                options.series.push(series);
            numRow = numRow + 1;

        });

        // Create the chart
        var chart = new Highcharts.Chart(options);
    }
};