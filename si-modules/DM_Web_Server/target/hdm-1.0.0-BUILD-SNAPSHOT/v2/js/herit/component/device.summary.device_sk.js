// Custom scripts
$(document).ready(function () {
	dsdsk.init();
	
	console.log("device.summary.device_sk js initialized");
});

var dsdsk = {
	deviceInfo: null,
	package: null,
	init: function() {
		console.log("dsdsk.refresh called ");
		
		dsdsk.deviceInfo = _ucc.getDeviceInfo();
		
		$("#dsd_regdate").text(dsdsk.deviceInfo.createTime);
		$("#dsd_sn").text(dsdsk.deviceInfo.sn);
		
	}
};
