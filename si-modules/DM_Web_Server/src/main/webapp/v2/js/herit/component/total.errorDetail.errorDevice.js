// Custom scripts
$(document).ready(function () {
	errorDevice.initialize();
});




var errorDevice = (function(){

	//리프레쉬 주기
	var refreshPeriodSecond = 30;
	//
	var deviceLocationUrl = "/hdm/v2/monitor/device.do";
	
	var initialize = function(){
//		console.log("total.errorDetail.errorMessage js initialized");		
		initUIComponents();
		
		getErrorDeviceInfoList();

		//이하 호출은 setTimeout 으로 해결
		executeFunctionPerPeirod(getErrorDeviceInfoList, refreshPeriodSecond);
	};

	var executeFunctionPerPeirod = function(executeFunctionTarget, periodSecond){
		setInterval(executeFunctionTarget, (periodSecond * 1000));
	};
	
	var getErrorDeviceInfoList = function(){
			

		var context = { 
				"handler": successCallback 
			};
		
		hdm_get_error_device_list(context, false);
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
				errorDeviceDataListHtml += "	<td>" + getErrorGradeName(data.ERR_GRADE) + "</td>";
				errorDeviceDataListHtml += "	<td>" + data.ERR_GRADE + "</td>";
				if(data.ERR_GRADE == 0){
					errorDeviceDataListHtml += "	<td><i id='"+data.DEVICE_ID+"' class='fa fa-cogs errorDeviceButton' style='cursor:pointer'></i></td>";
				}else{
					errorDeviceDataListHtml += "	<td><button type='button' class='btn btn-primary btn-xs' style='font-size: 10px;'><i id='"+data.DEVICE_ID+"' class='fa fa-cogs errorDeviceButton'></i></button></td>";				
				}
				errorDeviceDataListHtml += "</tr>";
			});
		}else{
			errorDeviceDataListHtml += "<tr><td colspan='6'>데이터가 없습니다.</td></tr>";
		}
		
		$('#errorDeviceList').html(errorDeviceDataListHtml);
		
	};
	

	
	
	var initUIComponents = function(){
		addEventHandler();
	};

	var addEventHandler = function(){
		
		$("#errorDeviceList").on("click", ".errorDeviceButton",  function() {
//			console.log("object : ", $(this));			
			var deviceId = $(this).context.id;
			window.location.href = deviceLocationUrl + "?deviceId="+deviceId;			
		});
		
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
