// Custom scripts
$(document).ready(function () {
	
	//alert("제어이력 컴포넌트 구현중!!!");

	console.log("device.history.control js initialized");
	deviceControlHistory.initialize();
});



var dhc = {
	refreshInterval: 30,
	refresh: function() {
		console.log("dhc.refresh called ");
		
		// 제어 이력 요청
		var today = now.getFullYear() +"-" + (now.getUTCMonth()+1) +"-"+ now.getUTCDate();
		var deviceInfo = _ucc.getDeviceInfo();
		
		var context = { "param":{"today":today, "deviceId":deviceInfo.deviceId}, "handler": dhc.refreshHandler };
		hdb_get_device_today(context, false);
		// hdb_get_history
	},
	
	refreshHandler: function(msg, context) {
		console.log("dhc.refreshHandler called ");
		var info = msg.content.info;

		
		$("#dst_alarm_change").text(dhc.getChangeRatio(info.alarmBefore2, info.alarmBefore1));
		$("#dst_alarm_change_dir").addClass(dhc.getChangeClass(info.alarmBefore2, info.alarmBefore1));
		$("#dst_alarm_yesterday").text(info.alarmBefore1);
		$("#dst_alarm_today").text(info.alarmToday);

		$("#dst_noti_change").text(dhc.getChangeRatio(info.notiBefore2, info.notiBefore1));
		$("#dst_noti_change_dir").addClass(dhc.getChangeClass(info.notiBefore2, info.notiBefore1));
		$("#dst_noti_yesterday").text(info.notiBefore1);
		$("#dst_noti_today").text(msg.content.info.notiToday);

		$("#dst_error_change").text(dhc.getChangeRatio(info.errorBefore2, info.errorBefore1));
		$("#dst_error_change_dir").addClass(dhc.getChangeClass(info.errorBefore2, info.errorBefore1));
		$("#dst_error_yesterday").text(info.errorBefore1);
		$("#dst_error_today").text(msg.content.info.errorToday);
		
		setTimeout(dhc.refresh, dhc.refreshInterval*1000);
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



var deviceControlHistory = (function(){
	
	//리프레쉬 주기
	var refreshPeriodSecond = 30;

	var initialize = function(){

		initUIComponents();
		
		getDeviceControlHistoryInfoList();

		//이하 호출은 setTimeout 으로 해결
		executeFunctionPerPeirod(getDeviceControlHistoryInfoList, refreshPeriodSecond);
		
	};

	var executeFunctionPerPeirod = function(executeFunctionTarget, periodSecond){
		setInterval(executeFunctionTarget, (periodSecond * 1000));
	};


	var getDeviceControlHistoryInfoList = function(){
		
		var context = { 
				"param" : {
					"deviceId":_ucc.getDeviceId()
				}, 
				"handler": successCallback 
			};

		hdm_get_device_controll_history_list(context, false);
		
	};

	

	var successCallback = function(msg, context){
//		console.log("successCallback!!!");
//		console.log("msg : ", msg);
//		console.log("context : ", context);
		
		var content = msg.content;
		var dataList = content.list;

		var deviceControlHistoryDataListHtml = "";

		if(dataList.length != 0){
			$.each(dataList, function(index, data){
				deviceControlHistoryDataListHtml += "<tr>";
				deviceControlHistoryDataListHtml += "	<td>" + data.CREATE_TIME + "</td>";
				deviceControlHistoryDataListHtml += "	<td>" + data.RESOURCE_NAME + "</td>";
				deviceControlHistoryDataListHtml += "	<td style='word-break:break-all'>" + data.CTL_DATA + "</td>";
				if(data.CTL_RESULT == null){
					deviceControlHistoryDataListHtml += "	<td> - </td>";
				}else{
					deviceControlHistoryDataListHtml += "	<td>" + data.CTL_RESULT + "</td>";
				}
				if(data.ERROR_CODE == 200){
					deviceControlHistoryDataListHtml += "	<td> - </td>";
				}else{
					deviceControlHistoryDataListHtml += "	<td>" + data.ERROR_CODE + "</td>";
				}
				deviceControlHistoryDataListHtml += "</tr>";
			});
		}else{
			deviceControlHistoryDataListHtml += "<tr><td colspan='5'>데이터가 없습니다.</td></tr>";
		}

		
		$('#deviceControlHistoryList').html(deviceControlHistoryDataListHtml);
		
	};
		
	
	
	var initUIComponents = function(){
		addEventHandler();
	};

	var addEventHandler = function(){
		
	};

	
	return {
		initialize: initialize
	};
})();