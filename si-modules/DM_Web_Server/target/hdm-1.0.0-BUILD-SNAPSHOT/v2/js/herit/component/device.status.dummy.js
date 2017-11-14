// Custom scripts
$(document).ready(function () {
	
	dsdmy.init();

	console.log("device.status.dummy js initialized");
		
});

var dsdmy = {
	init: function() {
		
		_ucc.setResourceHandler("dsdmy", [], dsdmy.refresh);
	}, 
	refresh: function(type) {
		console.log("dsdmy.refresh("+type+") called");
	}
}

