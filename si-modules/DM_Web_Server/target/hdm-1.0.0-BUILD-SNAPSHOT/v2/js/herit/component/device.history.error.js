// Custom scripts
$(document).ready(function () {
//	console.log("device.history.error js initialized");
	deviceErrorHistory.initialize();
});

var deviceErrorHistory = (function(){
	
	//리프레쉬 주기
	var refreshPeriodSecond = 30;

	var initialize = function(){

		initUIComponents();
		
		getdeviceErrorHistoryInfoList();

		//이하 호출은 setTimeout 으로 해결
		executeFunctionPerPeirod(getdeviceErrorHistoryInfoList, refreshPeriodSecond);
		
	};

	var executeFunctionPerPeirod = function(executeFunctionTarget, periodSecond){
		setInterval(executeFunctionTarget, (periodSecond * 1000));
	};


	var getdeviceErrorHistoryInfoList = function(){

		var context = { 
				"param" : {
					"deviceId":_ucc.getDeviceId()
				}, 
				"handler": successCallback 
			};
		
		hdm_get_device_error_history_list(context, false);
		
	};
	

	var successCallback = function(msg, context){
//		console.log("successCallback!!!");
//		console.log("msg : ", msg);
//		console.log("context : ", context);
		
		var content = msg.content;
		var dataList = content.list;

		var deviceErrorHistoryDataListHtml = "";
		if(dataList.length != 0){
			$.each(dataList, function(index, data){
				deviceErrorHistoryDataListHtml += "<tr>";
				deviceErrorHistoryDataListHtml += "	<td>" + data.ERROR_TIME + "</td>";
				deviceErrorHistoryDataListHtml += "	<td>" + data.ERROR_CODE + "</td>";
				deviceErrorHistoryDataListHtml += "	<td>" + getErrorGradeName(data.ERROR_GRADE) + "</td>";
				deviceErrorHistoryDataListHtml += "	<td>" + data.ERROR_DATA + "</td>";
				deviceErrorHistoryDataListHtml += "</tr>";
			});
		}else{
			deviceErrorHistoryDataListHtml += "<tr><td colspan='4'>데이터가 없습니다.</td></tr>";
		}
		
		$('#deviceErrorHistoryList').html(deviceErrorHistoryDataListHtml);
		
	};
		
	
	
	var initUIComponents = function(){
		addEventHandler();
	};

	var addEventHandler = function(){
		
	};

	var getErrorGradeName = function(errGrade){
		//장애시 등급 (0: NO ERROR, 1:MINOR, 2:MAJOR, 3:CRITICAL, 4:FATAL)
		var errorGradeName = "";
		switch (errGrade) {
		case 0:
			errorGradeName = "NORMAL"	
			break;
		case 1:
			errorGradeName = "MINOR"	
			break;
		case 2:
			errorGradeName = "MAJOR"	
			break;
		case 3:
			errorGradeName = "CRITICAL"
			break;
		case 4:
			errorGradeName = "FATAL"	
			break;
		}		
		return errorGradeName;
	};
	
	return {
		initialize: initialize
	};
})();