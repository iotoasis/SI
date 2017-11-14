// Custom scripts
$(document).ready(function () {
	dstsk.refresh();

	console.log("device.summary.today js initialized");
});

var dstsk = {
	refreshInterval: 60,
	refresh: function() {
		console.log("dstsk.refresh called ");
		// 장애디바이스수 통계 요청
		var now = new Date(), endDate = new Date();
		var today = now.getFullYear() +"-" + (now.getUTCMonth()+1) +"-"+ now.getUTCDate();
		var deviceInfo = _ucc.getDeviceInfo();
		
		var context = { "param":{"today":today, "deviceId":deviceInfo.deviceId}, "handler": dstsk.refreshHandler };
		hdb_get_device_today(context, false);
	},
	
	refreshHandler: function(msg, context) {
		console.log("dstsk.refreshHandler called ");
		var info = msg.content.info;

		
		$("#dst_alarm_change").text(dstsk.getChangeRatio(info.alarmBefore2, info.alarmBefore1));
		$("#dst_alarm_change_dir").addClass(dstsk.getChangeClass(info.alarmBefore2, info.alarmBefore1));
		$("#dst_alarm_yesterday").text(info.alarmBefore1);
		$("#dst_alarm_today").text(info.alarmToday);

		/*$("#dst_noti_change").text(dstsk.getChangeRatio(info.notiBefore2, info.notiBefore1));
		$("#dst_noti_change_dir").addClass(dstsk.getChangeClass(info.notiBefore2, info.notiBefore1));
		$("#dst_noti_yesterday").text(info.notiBefore1);
		$("#dst_noti_today").text(msg.content.info.notiToday);

		$("#dst_error_change").text(dstsk.getChangeRatio(info.errorBefore2, info.errorBefore1));
		$("#dst_error_change_dir").addClass(dstsk.getChangeClass(info.errorBefore2, info.errorBefore1));
		$("#dst_error_yesterday").text(info.errorBefore1);
		$("#dst_error_today").text(msg.content.info.errorToday);*/
		
		setTimeout(dstsk.refresh, dstsk.refreshInterval*1000);
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
