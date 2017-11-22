// Custom scripts
$(document).ready(function () {
	
	dssnb_lwm2m_ip.init();

	console.log("device.status.single_num_bar js initialized");
		
    $(".dssnb_lwm2m_ip-bar-graph").peity("bar", {
        fill: ["#1ab394", "#d7d7d7"]
    })
});

var dssnb_lwm2m_ip = {
	divs: null,
	resources: null,
	init: function() {
		dssnb_lwm2m_ip.divs = $(".hit-component-dssnb_lwm2m_ip");
		dssnb_lwm2m_ip.resources = [];
		$.each(dssnb_lwm2m_ip.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(idx, val) {
				dssnb_lwm2m_ip.resources.push(val);
			});
			//dssnb_lwm2m_ip.resources.push(_ucc.getResListWithComponentDiv(div));
			//dssnb_lwm2m_ip.resources = _ucc.getResListWithComponentDiv(div);
		});
		
		_ucc.setResourceHandler("dssnb_lwm2m_ip", dssnb_lwm2m_ip.resources, dssnb_lwm2m_ip.refresh);
		dssnb_lwm2m_ip.refresh();
	}, 
	refresh: function(type) {
		console.log("dssnb_lwm2m_ip.refresh("+type+") called");
		
		$.each(dssnb_lwm2m_ip.divs, function(idx, div) {
			dssnb_lwm2m_ip.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);
		

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dssnb_lwm2m_ip:"+resources[0]+")");
			//alert("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dssnb_lwm2m_ip:"+resources[0]+")");					
			$(div).find(".dssnb_lwm2m_ip-value").text("-");
			$(div).find(".dssnb_lwm2m_ip-unit").text("-");
			return;
		}			
		
		
		var value = moData.data;
		var realIp = value;
		/*
		if(value != null && value != undefined && value.indexOf("\n")){
			var valArray = value.split("\n");
			realIp = valArray[1];
		}
		*/
		$(div).find(".dssnb_lwm2m_ip-value").text(realIp);
		$(div).find(".dssnb_lwm2m_ip-unit").text(unit);
		
	}
}

