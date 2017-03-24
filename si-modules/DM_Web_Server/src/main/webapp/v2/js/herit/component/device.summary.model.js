// Custom scripts
$(document).ready(function () {
	dsm.refresh();
	
	console.log("device.summary.model js initialized");
});

var dsm = {
	refresh: function() {
		console.log("dsm.refresh called ");
		
		var info = _ucc.getDeviceModelInfo();
		
		$("#dsm_model_manufacturer").text(info.manufacturer);
		$("#dsm_model_oui").text(info.oui);
		$("#dsm_model_name").text(info.modelName);
		$("#dsm_model_image").attr("src", "/hdm"+info.iconUrl);		
	}
};
