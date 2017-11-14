// Custom scripts
$(document).ready(function () {
	
	dssnp.init();

	console.log("device.status.single_num_pie js initialized");

	$(".dssnp-pie-graph").peity("pie", {
        fill: ['#1ab394', '#d7d7d7', '#ffffff']
    })
});

var dssnp = {
	divs: null,
	resources: null,
	init: function() {
		dssnp.divs = $(".hit-component-dssnp");
		dssnp.resources = [];
		$.each(dssnp.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(idx, val) {
				dssnp.resources.push(val);
			});
			//dssnp.resources.push(_ucc.getResListWithComponentDiv(div));
			//dssnp.resources = _ucc.getResListWithComponentDiv(div);
		});
		
		_ucc.setResourceHandler("dssnp", dssnp.resources, dssnp.refresh);
		dssnp.refresh();
	}, 
	refresh: function(type) {
		console.log("dssnp.refresh("+type+") called");
		
		$.each(dssnp.divs, function(idx, div) {
			dssnp.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSNP:"+resources[0]+")");
			return;
		}		

		$(div).find(".dssnp-value").text(moData.data);		
		$(div).find(".dssnp-unit").text(unit);
		
	}
}

