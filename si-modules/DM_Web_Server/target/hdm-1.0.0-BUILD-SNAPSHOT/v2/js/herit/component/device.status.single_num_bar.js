// Custom scripts
$(document).ready(function () {
	
	dssnb.init();

	console.log("device.status.single_num_bar js initialized");
		
    $(".dssnb-bar-graph").peity("bar", {
        fill: ["#1ab394", "#d7d7d7"]
    })
});

var dssnb = {
	divs: null,
	resources: null,
	init: function() {
		dssnb.divs = $(".hit-component-dssnb");
		dssnb.resources = [];
		$.each(dssnb.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(idx, val) {
				dssnb.resources.push(val);
			});
			//dssnb.resources.push(_ucc.getResListWithComponentDiv(div));
			//dssnb.resources = _ucc.getResListWithComponentDiv(div);
		});
		
		_ucc.setResourceHandler("dssnb", dssnb.resources, dssnb.refresh);
		dssnb.refresh();
	}, 
	refresh: function(type) {
		console.log("dssnb.refresh("+type+") called");
		
		$.each(dssnb.divs, function(idx, div) {
			dssnb.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);
		

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSNB:"+resources[0]+")");
			//alert("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSNB:"+resources[0]+")");					
			$(div).find(".dssnb-value").text("-");
			$(div).find(".dssnb-unit").text("-");
			return;
		}			
		
		$(div).find(".dssnb-value").text(moData.data);
		$(div).find(".dssnb-unit").text(unit);
		
	}
}

