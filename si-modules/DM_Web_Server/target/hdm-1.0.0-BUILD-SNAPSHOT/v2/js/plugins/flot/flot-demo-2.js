//Flot Pie Chart
$(function() {

    var data = [{
        label: "Sales 1",
        data: 21,
        color: "#d3d3d3",
    }, {
        label: "Sales 2",
        data: 3,
        color: "#bababa",
    }, {
        label: "Sales 3",
        data: 15,
        color: "#79d2c0",
    }, {
        label: "Sales 4",
        data: 52,
        color: "#1ab394",
    }];

    var plotObj = $.plot($("#flot-pie-chart"), data, {
        series: {
            pie: {
                show: true
            }
        },
        grid: {
            hoverable: true
        },
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false
        }
    });
    
    var plotObj = $.plot($("#flot-pie-chart2"), data, {
        series: {
            pie: {
                show: true
            }
        },
        grid: {
            hoverable: true
        },
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false
        }
    });

    var plotObj = $.plot($("#flot-pie-chart3"), data, {
        series: {
            pie: {
                show: true
            }
        },
        grid: {
            hoverable: true
        },
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false
        }
    }); 
});