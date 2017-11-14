// Custom scripts
$(document).ready(function () {
	
	dsdmysk.init();

	console.log("device.status.dsdmysk js initialized");
		
});

var dsdmysk = {
	init: function() {
		
		_ucc.setResourceHandler("dsdmysk", [], dsdmysk.refresh);
	}, 
	refresh: function(type) {
		console.log("dsdmysk.refresh("+type+") called");
	}
}

