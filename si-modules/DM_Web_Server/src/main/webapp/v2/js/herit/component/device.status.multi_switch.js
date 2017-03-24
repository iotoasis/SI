// Custom scripts
$(document).ready(function () {
	
	// 구현 안됨.
	dsms.init();

	console.log("device.status.multi_switch js initialized");
});


var dsms = {
	divs: null,
	resources: null,
	init: function() {
		alert("Who do you use this component?");
		dsms.divs = $(".hit-component-dsms");
		dsms.resources = [];
		$.each(dsms.divs, function(idx, div) {
			//dsms.resources.push(_ucc.getResListWithComponentDiv(div));
			dsms.resources = _ucc.getResListWithComponentDiv(div);
		});
		
		_ucc.setResourceHandler("dsms", dsms.resources, dsms.refresh);
		dsms.refresh();
	}, 
	refresh: function(type) {
		console.log("dsms.refresh("+type+") called");
		$.each(dsms.divs, function(idx, div) {
			//dsms.refreshComponent(div);
		});		
	}
}



