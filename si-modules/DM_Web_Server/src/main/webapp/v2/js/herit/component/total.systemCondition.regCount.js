// Custom scripts
$(document).ready(function () {
	console.log("total.systemCondition.regCount js initialized");
	
	tscr.refresh();
});


var tscr = {
	colorTable: ["#d3d3d3","#bababa","#79d2c0","#1ab394","#435394"],
	graphData: [],
	refresh: function() {
		console.log("tscr.refresh called ");
		// 모델별 디바이스 등록수 요청
		var context = { "handler": tscr.modelCountHandler };
		hdb_get_devicecount_per_model(context, false);
	},
	modelCountHandler: function(msg, context) {
		console.log("tscr.modelCountHandler called ");
		//console.log("	msg: "+JSON.stringify(msg));
		
		var maxModel = 4;
		var maxNameLength = 11;
		var etcCount = 0;
		
		var tbody = $("#tscr_model_list_body"); 
		tbody.empty();
		var i = 0;
		for ( ; i<msg.content.list.length; i++) {
			if (i<maxModel) {
				var model = msg.content.list[i];
				if (msg.content.list.length < i) {
					tbody.append($("<tr><td>-</td><td>-</td><td>-</td></tr>"));	
				} else {
					tbody.append($("<tr><td>"+(model.manufacturer.length > maxNameLength ? (model.manufacturer.substring(0,maxNameLength-3)+"...") : model.manufacturer) +
									"</td><td>"+(model.modelName.length > maxNameLength ? (model.modelName.substring(0,maxNameLength-3)+"...") : model.modelName)+
									"</td><td>"+model.deviceCount+"</td></tr>"));
					tscr.graphData[tscr.graphData.length] = {label: model.modelName, data: model.deviceCount,  color: tscr.colorTable[i]};
				}				
			} else {
				etcCount += model.deviceCount;
			}						
		}
		if (i>=maxModel) {
			tbody.append($("<tr><td>-</td><td>etc</td><td>"+etcCount+"</td></tr>"));
			tscr.graphData[tscr.graphData.length] = {label: "etc", data: etcCount,  color: tscr.colorTable[maxModel]};						
		}
		
		// 파이 그래프
		var plotObj = $.plot($("#tscr_model_pie_chart"), tscr.graphData, {
		    series: { pie: { show: true  } },
		    grid: { hoverable: true },
		    tooltip: true,
		    tooltipOpts: {
		        content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
		        shifts: { x: 20, y: 0 },
		        defaultTheme: false
		    }
		});
	}
}


