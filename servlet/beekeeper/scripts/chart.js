$(document).ready(function() {
    var options = {
        chart: {
            renderTo: 'weight',
            type: 'line',
            marginRight: 130,
            marginBottom: 50
        },
        title: {
            text: 'Weight: Hive 1',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: ''
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+
                    this.x +': '+ this.y;
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100,
            borderWidth: 0
        },
        series: []
    }
    //var inp = FileReader.readAsText("file:///C:/Users/brian/Documents/year5/project/code/testServlet/out/artifacts/testServlet_war_exploded/scripts/JSONData.json");
    //var inp = readTextFile("/scripts/JSONData.json");
    $.getJSON("./scripts/JSONData.json", function(json) {
        options.xAxis.categories = json[1]['data'];
        //options.series[0] = json[2];
        options.series[0] = json[3];
        //options.series[2] = json[4];
        chart = new Highcharts.Chart(options);
        //alert(json);
    });
});

$(document).ready(function() {
    var options2 = {
        chart: {
            renderTo: 'temp',
            type: 'line',
            marginRight: 130,
            marginBottom: 50
        },
        title: {
            text: 'Environmental Factors Hive 1',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: ''
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+
                    this.x +': '+ this.y;
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100,
            borderWidth: 0
        },
        series: []
    }
    //var inp = FileReader.readAsText("file:///C:/Users/brian/Documents/year5/project/code/testServlet/out/artifacts/testServlet_war_exploded/scripts/JSONData.json");
    //var inp = readTextFile("./scripts/JSONData.json");
    $.getJSON("./scripts/JSONData.json", function(json) {
        options2.xAxis.categories = json[1]['data'];
        options2.series[0] = json[2];
        //options.series[1] = json[3];
        options2.series[1] = json[4];
        chart = new Highcharts.Chart(options2);
        //alert(json);
    });
});

function readTextFile(file)
{
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                //alert(allText);
            }
        }
    }
    rawFile.send(null);
}
