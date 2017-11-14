// Custom scripts
$(document).ready(function () {
	dss.init();
	
	console.log("device.summary.status js initialized");
});

var dss = {
	resourceUri: "/2/-/29",
	deviceInfo: null,
	gradeColor: ["#0066FF","#999999","#FAFA66","#FFCC00","#FF0000"],
	gradeName: ["NORMAL","MINOR","MAJOR","CRITICAL","FATAL"],
	init: function() {
		console.log("dss.init called ");
			
		dss.deviceInfo = _ucc.getDeviceInfo();
		var errInfo = _ucc.getMoData(dss.resourceUri+"/0");

		$("#dss_error_grade_color").css("color", dss.gradeColor[dss.deviceInfo.errGrade]);
		$("#dss_error_grade_name").text(dss.gradeName[dss.deviceInfo.errGrade]);
		if (dss.deviceInfo.errGrade == 0) {
			$("#dss_error_grade_desc").text("No error");
		} else {
			var errName = _ucc.getErrorCodeName(dss.resourceUri, errInfo.data);
			$("#dss_error_grade_desc").text(errName);	
		}
		
		if (errInfo != null && typeof errInfo != "undefined") {
			$("#dss_error_grade_time").text(errInfo.updateTime);
		}
	}
};
