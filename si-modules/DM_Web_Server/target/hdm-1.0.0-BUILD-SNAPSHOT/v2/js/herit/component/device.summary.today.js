// Custom scripts
$(document).ready(function () {
	dst.refresh();

	console.log("device.summary.today js initialized");
});

var dst = {
	refreshInterval: 60,
	refresh: function() {
		console.log("dst.refresh called ");
		// 장애디바이스수 통계 요청
		var now = new Date(), endDate = new Date();
		var today = now.getFullYear() +"-" + (now.getUTCMonth()+1) +"-"+ now.getUTCDate();
		var deviceInfo = _ucc.getDeviceInfo();
		
		var context = { "param":{"today":today, "deviceId":deviceInfo.deviceId}, "handler": dst.refreshHandler };
		hdb_get_device_today(context, false);
	},
	
	refreshHandler: function(msg, context) {
		console.log("dst.refreshHandler called ");
		var info = msg.content.info;

		
		$("#dst_alarm_change").text(dst.getChangeRatio(info.alarmBefore2, info.alarmBefore1));
		$("#dst_alarm_change_dir").addClass(dst.getChangeClass(info.alarmBefore2, info.alarmBefore1));
		$("#dst_alarm_yesterday").text(info.alarmBefore1);
		$("#dst_alarm_today").text(info.alarmToday);

		$("#dst_noti_change").text(dst.getChangeRatio(info.notiBefore2, info.notiBefore1));
		$("#dst_noti_change_dir").addClass(dst.getChangeClass(info.notiBefore2, info.notiBefore1));
		$("#dst_noti_yesterday").text(info.notiBefore1);
		$("#dst_noti_today").text(msg.content.info.notiToday);

		$("#dst_error_change").text(dst.getChangeRatio(info.errorBefore2, info.errorBefore1));
		$("#dst_error_change_dir").addClass(dst.getChangeClass(info.errorBefore2, info.errorBefore1));
		$("#dst_error_yesterday").text(info.errorBefore1);
		$("#dst_error_today").text(msg.content.info.errorToday);
		
		setTimeout(dst.refresh, dst.refreshInterval*1000);
	},
	
	getChangeRatio: function(before, after) {
		if (before == 0 || before == null || typeof before == "undefined" || after == null || typeof after == "undefined") {
			return "-";			
		} else {
			return Math.round((after - before)*100 / before);
		}
	},
	
	getChangeClass: function(before, after) {
		if (before == 0 || before == null || typeof before == "undefined" || after == null || typeof after == "undefined") {
			return "fa-minus";			
		} else if (before > after){
			return "fa-level-down";	
		} else if (before < after){
			return "fa-level-up";
		} else {
			return "fa-minus";						
		}			
	}
}
