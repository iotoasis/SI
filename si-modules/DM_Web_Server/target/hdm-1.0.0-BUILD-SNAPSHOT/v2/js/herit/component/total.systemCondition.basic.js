// Custom scripts
$(document).ready(function () {
	tscb.refresh();
	
	console.log("total.systemCondition.basic js initialized");
});

var tscb = {
	refresh: function() {
		console.log("tscb.refresh called ");
		// 디바이스 등록수 요청
		var context = { "handler": tscb.deviceCountHandler };
		hdb_get_devicereg_count(context, false);
		
		// 플랫폼 현황 요청: 모델수, 사용자수
		context = { "handler": tscb.platformSummaryHandler };
		hdb_get_platform_summary(context, false);
	},
	deviceCountHandler: function(msg, context) {
		console.log("tscb.deviceCountHandler called ");
		//console.log("	msg: "+JSON.stringify(msg));
		
		var today = parseInt(msg.content.info.today);
		var yesterday = parseInt(msg.content.info.yesterday);
		$("#tscb_device").text(msg.content.info.total);
		$("#tscb_today").text(today);
		$("#tscb_yesterday").text(yesterday);
		
		if (yesterday > today) {			
			$("#tscb_today_change_sign").addClass("fa-minus");
			$("#tscb_today_change_sign").removeClass("fa-plus");
			$("#tscb_today_change").text(yesterday > 0 ? (today-yesterday)/yesterday : "");
		} else if (yesterday < today ){
			$("#tscb_today_change_sign").addClass("fa-minus");
			$("#tscb_today_change_sign").removeClass("fa-plus");
			$("#tscb_today_change").text(yesterday > 0 ? (today-yesterday)/yesterday : "");
		} else {
			$("#tscb_today_change").text("0");
		}
		
	},
	platformSummaryHandler: function(msg, context) {
		console.log("tscb.deviceCountHandler called ");
		//console.log("	msg: "+JSON.stringify(msg));

		$("#tscb_model").text(msg.content.info.deviceModelCount);
		$("#tscb_user").text(msg.content.info.userCount);
		
		
	}	
};
