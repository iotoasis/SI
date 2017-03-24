// Custom scripts
$(document).ready(function () {

	console.log("total.errorStat.errorCount js initialized");
	
	tesec.refresh();
});


var tesec = {
	refresh: function() {
		console.log("tesec.refresh called ");

		// 장애 통계 요청
		var now = new Date();
		var today = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0,0,0);
		var thisMonth = new Date(now.getFullYear(), now.getMonth(), 1, 0,0,0);

		var today = now.getFullYear() +"-" + (now.getUTCMonth()+1) +"-"+ now.getUTCDate();
		var thisMonth = now.getFullYear() +"-" + (now.getUTCMonth()+1) +"-01";
		
		var context = { "param":{"today":today, "thisMonth":thisMonth}, "handler": tesec.errorCountHandler };
		hdb_get_stat_error_summary(context, false);
	},
	
	errorCountHandler: function(msg, context) {
		console.log("tesec.errorCountHandler called ");
		//console.log("	msg: "+JSON.stringify(msg));
		
		var info = msg.content.info;

		$("#tesec_day_before1").text(info.dayBefore1);
		$("#tesec_day_before1_change").text(tesec.getChangeRatio(info.dayBefore1, info.dayBefore2));
		if (info.dayBefore1 > info.dayBefore2) {
			$("#tesec_day_before1_arrow").addClass("fa-level-up");
			$("#tesec_day_before1_arrow").removeClass("fa-level-down");			
		} else if (info.dayBefore1 < info.dayBefore2) {
			$("#tesec_day_before1_arrow").addClass("fa-level-down");
			$("#tesec_day_before1_arrow").removeClass("fa-level-up");			
		}

		$("#tesec_day_before2").text(msg.content.info.dayBefore2);
		$("#tesec_day_before2_change").text(tesec.getChangeRatio(info.dayBefore2, info.dayBefore3));
		if (info.dayBefore2 > info.dayBefore3) {
			$("#tesec_day_before2_arrow").addClass("fa-level-up");
			$("#tesec_day_before2_arrow").removeClass("fa-level-down")		
		}  else if (info.dayBefore2 < info.dayBefore3) {
			$("#tesec_day_before2_arrow").addClass("fa-level-down");
			$("#tesec_day_before2_arrow").removeClass("fa-level-up")		
		}
		
		$("#tesec_day_average").text(msg.content.info.dayAverage);

		$("#tesec_month_before1").text(msg.content.info.monthBefore1);
		$("#tesec_month_before1_change").text((tesec.getChangeRatio(info.monthBefore1, info.monthBefore2)));
		if (info.monthBefore1 > info.monthBefore2) {
			$("#tesec_month_before1_arrow").addClass("fa-level-up");
			$("#tesec_month_before1_arrow").removeClass("fa-level-down");
		} else if (info.monthBefore1 < info.monthBefore2) {
			$("#tesec_month_before1_arrow").addClass("fa-level-down");
			$("#tesec_month_before1_arrow").removeClass("fa-level-up");
		}
		
		$("#tesec_month_before2").text(msg.content.info.monthBefore2);
		$("#tesec_month_before2_change").text((tesec.getChangeRatio(info.monthBefore2, info.monthBefore3)));
		if (info.monthBefore2 > info.monthBefore3) {
			$("#tesec_month_before2_arrow").addClass("fa-level-up");
			$("#tesec_month_before2_arrow").removeClass("fa-level-down");
		} else if (info.monthBefore2 < info.monthBefore3) {
			$("#tesec_month_before2_arrow").addClass("fa-level-down");
			$("#tesec_month_before2_arrow").removeClass("fa-level-up");
		}		
		$("#tesec_month_average").text(msg.content.info.monthAverage);
	},
	getChangeRatio : function(after, before) {
		if (before == null || before == 0) {
			return "-";
		} else {
			return Math.abs(Math.round((after - before)*100 / before));
		}
		
	}
}

