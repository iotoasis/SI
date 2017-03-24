// Custom scripts
$(document).ready(function () {
	
	// 데이터 수집 시작하도록 execute
	$.ajax({
	  type: "POST",
	  dataType: "json",
	  url: "/hdm/api/gw/device/write.do",
	  contentType: "application/json",
	  data: JSON.stringify({"d":deviceInfo.deviceId ,"n":"/7/-/6", "sv":"1"})
	})
	.done(function(msg) { 
		 
	});
	
	// start
	dsdlg_lwm2m.init();
	console.log("device.status.double_line_graph js initialized");
});


var dsdlg_lwm2m = {
	divs: null,
	resources: null,
	init: function() {
		dsdlg_lwm2m.divs = $(".hit-component-dsdlg_lwm2m");
		dsdlg_lwm2m.resources = [];
		$.each(dsdlg_lwm2m.divs, function(idx, div) {

			var resources = _ucc.getResListWithComponentDiv(div);
			
			dsdlg_lwm2m.resources = resources;
			var graph = new trafficGraph();
			graph.initTrafficGraph($(div).find(".dsdlg_lwm2m-chart"));
			div.graph = graph;

			var val1 = _ucc.getMoData(resources[0]);
			if (val1 == null || typeof val1 == "undefined") {
				console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dsdlg_lwm2m:"+resources[0]+")");
				return;
			}		
			
			var unit1 = _ucc.getUnitString(resources[0]);

			$(div).find(".dsdlg_lwm2m-res-1-name").text(val1.resourceName);
			$(div).find(".dsdlg_lwm2m-res-1-unit").text(unit1);

			var val2 = _ucc.getMoData(resources[1]);
			if (val2 == null || typeof val2 == "undefined") {
				console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dsdlg_lwm2m:"+resources[1]+")");
				return;
			}
			
			var unit2 = _ucc.getUnitString(resources[1]);

			$(div).find(".dsdlg_lwm2m-res-2-name").text(val2.resourceName);
			$(div).find(".dsdlg_lwm2m-res-2-unit").text(unit2);
		});
		
		
		_ucc.setResourceHandler("dsdlg_lwm2m", dsdlg_lwm2m.resources, dsdlg_lwm2m.refresh);
		dsdlg_lwm2m.refresh();
	}, 
	refresh: function(type) {
		console.log("dsdlg_lwm2m.refresh("+type+") called");
		
		if (type == "periodic") {	// 그래프는 주기적으로 업데이트 해야 하므로 "called"처럼 사용자 액션에 의한 상태값 업데이트시 UI에 반영안함
			
			$.each(dsdlg_lwm2m.divs, function(idx, div) {
				dsdlg_lwm2m.refreshComponent(div);
			});		
			
		} 
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var val1 = _ucc.getMoData(resources[0]);
		var val2 = _ucc.getMoData(resources[1]);
		if (val1 == null || typeof val1 == "undefined" || val2 == null || typeof val2 == "undefined" ) {
			console.error("dsdlg_lwm2m.refreshComponent undefined resource"+resources[0]+","+resources[1]);
			return;
		}
		
		var graph = div.graph;
		graph.addTraffic(val1.data, val2.data);
		
		var vals = graph.getCurValues();
		$(div).find(".dsdlg_lwm2m-res-1-value").text(vals[0]=='-' ? '-' : parseInt(vals[0]).toFixed(1));
		$(div).find(".dsdlg_lwm2m-res-2-value").text(vals[1]=='-' ? '-' : parseInt(vals[1]).toFixed(1));
		
	}
}