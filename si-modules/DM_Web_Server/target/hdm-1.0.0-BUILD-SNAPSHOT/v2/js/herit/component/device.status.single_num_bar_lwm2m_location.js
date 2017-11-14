// Custom scripts
$(document).ready(function () {
	
	dssnb_lwm2m_location.init();

	console.log("device.status.single_num_bar js initialized");
		
    $(".dssnb_lwm2m_location-bar-graph").peity("bar", {
        fill: ["#1ab394", "#d7d7d7"]
    })
});

var dssnb_lwm2m_location = {
	divs: null,
	resources: null,
	init: function() {
		dssnb_lwm2m_location.divs = $(".hit-component-dssnb_lwm2m_location");
		dssnb_lwm2m_location.resources = [];
		$.each(dssnb_lwm2m_location.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(idx, val) {
				dssnb_lwm2m_location.resources.push(val);
			});
			//dssnb_lwm2m_location.resources.push(_ucc.getResListWithComponentDiv(div));
			//dssnb_lwm2m_location.resources = _ucc.getResListWithComponentDiv(div);
		});
		
		_ucc.setResourceHandler("dssnb_lwm2m_location", dssnb_lwm2m_location.resources, dssnb_lwm2m_location.refresh);
		dssnb_lwm2m_location.refresh();
	}, 
	refresh: function(type) {
		console.log("dssnb_lwm2m_location.refresh("+type+") called");
		
		$.each(dssnb_lwm2m_location.divs, function(idx, div) {
			dssnb_lwm2m_location.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);
		

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dssnb_lwm2m_location:"+resources[0]+")");
			//alert("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dssnb_lwm2m_location:"+resources[0]+")");					
			$(div).find(".dssnb_lwm2m_location-value").text("-");
			$(div).find(".dssnb_lwm2m_location-unit").text("-");
			return;
		}			
		
		var value = parseFloat(moData.data);
		if(value != null && value != undefined){
			value = value.toFixed(4);
		}
		$(div).find(".dssnb_lwm2m_location-value").text(value);
		$(div).find(".dssnb_lwm2m_location-unit").text(unit);
		
	}
}

