// Custom scripts
$(document).ready(function () {
	errorMessage.initialize();
});

var errorMessage = (function(){
	
	//리프레쉬 주기
	var refreshPeriodSecond = 30;

	var initialize = function(){
//		console.log("total.errorDetail.errorDevice js initialized");

		initUIComponents();
		
		getErrorMessageInfoList();

		//이하 호출은 setTimeout 으로 해결
		executeFunctionPerPeirod(getErrorMessageInfoList, refreshPeriodSecond);
		
	};

	var executeFunctionPerPeirod = function(executeFunctionTarget, periodSecond){
		setInterval(executeFunctionTarget, (periodSecond * 1000));
	};


	var getErrorMessageInfoList = function(){

		var context = { 
				"handler": successCallback 
			};
		
		hdm_get_error_message_list(context, false);
		
	};

	

	var successCallback = function(msg, context){
//		console.log("successCallback!!!");
//		console.log("msg : ", msg);
//		console.log("context : ", context);
		
		var content = msg.content;
		var dataList = content.list;

		var errorDeviceDataListHtml = "";
		if(dataList.length != 0){
			$.each(dataList, function(index, data){
				errorDeviceDataListHtml += "<tr>";
				errorDeviceDataListHtml += "	<td>" + data.MODEL_NAME + "</td>";
				errorDeviceDataListHtml += "	<td>" + data.MANUFACTURER + "</td>";
				errorDeviceDataListHtml += "	<td>" + data.SN + "</td>";
				errorDeviceDataListHtml += "	<td>" + getErrorGradeName(data.ERROR_GRADE) + "</td>";
				errorDeviceDataListHtml += "	<td>" + data.ERROR_NAME + "</td>";
				errorDeviceDataListHtml += "	<td>" + data.ERROR_TIME + "</td>";
				errorDeviceDataListHtml += "</tr>";
			});
		}else{
			errorDeviceDataListHtml += "<tr><td colspan='6'>데이터가 없습니다.</td></tr>";
		}
		
		$('#errorMessageList').html(errorDeviceDataListHtml);
		
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