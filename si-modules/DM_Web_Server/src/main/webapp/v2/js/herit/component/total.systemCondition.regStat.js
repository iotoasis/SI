// Custom scripts
$(document).ready(function () {
	console.log("total.systemCondition.regStat js initialized");
	
	tscrs.refresh();
});

var tscrs = {
	refresh: function() {
		console.log("tscrs.refresh called ");
		
		// 모델별 디바이스 등록수 통계 요청
		var statCount = 100;
		var startDate = new Date(), endDate = new Date();
		startDate.setDate(startDate.getDate()-statCount);
		var start = startDate.getFullYear() +"-" + (startDate.getUTCMonth()+1) +"-"+ startDate.getUTCDate();
		var end = endDate.getFullYear() +"-" + (endDate.getUTCMonth()+1) +"-"+ endDate.getUTCDate();
		
		var context = { "param":{"start":start, "end":end}, "handler": tscrs.regStatHandler };
		hdb_get_stat_register_day_per_model(context, false);
	},
	
	getTimestamp: function(dateStr) {
		var date = new Date(dateStr);
		return date.getTime();
	},
	
	regStatHandler: function(msg, context) {
		console.log("tscrs.regStatHandler called ");
		//console.log("	msg: "+JSON.stringify(msg));

		var totalData = [];
		var modelData = {};
		for (var i=0; i<msg.content.list.length; i++) {
			var stat = msg.content.list[i];
			if (stat.oui == null) {	// 전체 등록 통계
				totalData[totalData.length] = [tscrs.getTimestamp(stat.statDate), stat.statCount];
			} else {	// 모델별 등록 통계
				if (modelData[stat.oui+"$$"+stat.modelName] == null) {
					modelData[stat.oui+"$$"+stat.modelName] = [];
				}
				var dataArr = modelData[stat.oui+"$$"+stat.modelName];
				dataArr[dataArr.length] = [tscrs.getTimestamp(stat.statDate), stat.statCount];
			}			
		}
		
		var dataArr = [{
			data : totalData,
			label : "전체"
		}];
		
		for (var name in modelData) {
			dataArr[dataArr.length] = {data: modelData[name], label: name.split("$$")[1]};
		}
		
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

		$.plot("#tscrs_reg_stat_line_graph", dataArr, {
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

