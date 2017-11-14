// Custom scripts
$(document).ready(function () {
	
	teseg.refresh();

	console.log("total.errorStat.errorGraph js initialized");
	
});


var teseg = {
	refresh: function() {
		console.log("teseg.refresh called ");
		
		// 장애디바이스수 통계 요청
		var statCount = 300;
		var startDate = new Date(), endDate = new Date();
		startDate.setDate(startDate.getDate()-statCount);
		var start = startDate.getFullYear() +"-" + (startDate.getUTCMonth()+1) +"-"+ startDate.getUTCDate();
		var end = endDate.getFullYear() +"-" + (endDate.getUTCMonth()+1) +"-"+ endDate.getUTCDate();
		
		var context = { "param":{"start":start, "end":end}, "handler": teseg.errorStatHandler };
		hdb_get_stat_error_day(context, false);
	},
	
	getTimestamp: function(dateStr) {
		var date = new Date(dateStr);
		return date.getTime();
	},
	
	errorStatHandler: function(msg, context) {
		console.log("teseg.regStatHandler called ");
		//console.log("	msg: "+JSON.stringify(msg));

		var totalStat = [], minorStat = [], majorStat = [], criticalStat = [], fatalStat = [];
		var modelData = {};
		
		for (var i=0; i<msg.content.list.length; i++) {
			var stat = msg.content.list[i];
			totalStat[totalStat.length] = [tscrs.getTimestamp(stat.statDate), stat.totalCount];
			minorStat[minorStat.length] = [tscrs.getTimestamp(stat.statDate), stat.minorCount];
			majorStat[majorStat.length] = [tscrs.getTimestamp(stat.statDate), stat.majorCount];
			criticalStat[criticalStat.length] = [tscrs.getTimestamp(stat.statDate), stat.criticalCount];
			fatalStat[fatalStat.length] = [tscrs.getTimestamp(stat.statDate), stat.fatalCount];
		}
		
		var dataArr = [
		               { data : totalStat, label : "TOTAL" },
		               { data : minorStat, label : "MINOR" },
		               { data : majorStat, label : "MAJOR" },
		               { data : criticalStat, label : "CRITICAL" },
		               { data : fatalStat, label : "FATAL" }
		               ];
		
		var xaxes = [ {
			mode : "time"
			,timeformat : "%m/%d"
		} ];
		
		var yaxes = [ {
			min : 0
		}, {
            // align if we are to the right
            alignTicksWithAxis: 1,
            position: "right"
        }];
		
		var legend = {
				position : "sw"
		};

		$.plot("#tscrs_error_stat_line_graph", dataArr, {
			xaxes : xaxes,
			yaxes : yaxes,
			legend : legend, 
            grid: {
                color: "#999999",
                hoverable: true,
                clickable: true,
                tickColor: "#D4D4D4",
                borderWidth:0,
                hoverable: true //IMPORTANT! this is needed for tooltip to work,

            }
		});
	}
}