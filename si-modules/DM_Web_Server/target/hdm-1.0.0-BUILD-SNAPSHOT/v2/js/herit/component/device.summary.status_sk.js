// Custom scripts
$(document).ready(function () {
	dsssk.init();
	
	console.log("device.summary.status_sk js initialized");
});

var dsssk = {
	resourceUri: "/2/-/29",
	deviceInfo: null,
	gradeColor: ["#0066FF","#999999","#FAFA66","#FFCC00","#FF0000"],
	gradeName: ["정상","장애(minor)","장애(major)","장애(critical)","연결안됨"],
	init: function() {
		console.log("dss.init called ");
			
		dsssk.deviceInfo = _ucc.getDeviceInfo();
		var errInfo = _ucc.getMoData(dsssk.resourceUri+"/0");

		$("#dss_error_grade_color").css("color", dsssk.gradeColor[dsssk.deviceInfo.errGrade]);
		$("#dss_error_grade_name").text(dsssk.gradeName[dsssk.deviceInfo.errGrade]);
		if (dsssk.deviceInfo.errGrade == 0) {
			$("#dss_error_grade_desc").text("-");
		} else {
			var errName = _ucc.getErrorCodeName(dsssk.resourceUri, errInfo.data);
			$("#dss_error_grade_desc").text(errName);
		}
		if (errInfo != null && typeof errInfo != "undefined") {
			$("#dss_error_grade_time").text(errInfo.updateTime);
		}
	},
	updateDeviceStatus: function(statusCode) {
		if (statusCode == "200") {
			$("#dss_error_grade_color").css("color", dsssk.gradeColor[0]);
			$("#dss_error_grade_name").text(dsssk.gradeName[0]);
		} else {
			$("#dss_error_grade_color").css("color", dsssk.gradeColor[4]);
			$("#dss_error_grade_name").text(dsssk.gradeName[4]);
		}
	}
};
		
		
