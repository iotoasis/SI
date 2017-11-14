/** 자율제어여부 (자율적 상황판단 제어) */
function onControlSwitchClick(deviceId, resourceUri, resourceName, controlData) {
	console.log("onControlSwitchClick called");
	
	$(".js-switch").unbind("change");
	
	if (controlData == "act") {
		switchery.setPosition(true);
		switchery.handleOnchange(true);
	} else {
		switchery.setPosition(false);
		switchery.handleOnchange(false);
	}
	
	//변경에 대한 스위치 상태
    $(".js-switch").change( function(evt) {
    	console.log(".js-switch Click");

    	var data = $(".js-switch").is(':checked') == true ? "on" : "off";
    	if (data == "on") {data = "act";} else {data = "act_off";}
    	console.log("Control Control Control Control Control Control " + deviceId + resourceUri + resourceName + data);
    	
    	executeUpdateControl(deviceId, resourceUri, resourceName, data);
    });
}

/** 자율제어여부 (이벤트 Push 알림) */
function onPushSwitchClick(deviceId, resourceUri, resourceName, controlData) {
	console.log("onPushSwitchClick called");
	
	$(".js-switch2").unbind("change");
	
	if (controlData == "alert") {
		switchery_2.setPosition(true);
		switchery_2.handleOnchange(true);
	} else {
		switchery_2.setPosition(false);
		switchery_2.handleOnchange(false);
	}
	
	//변경에 대한 스위치 상태
    $(".js-switch2").change( function(evt) {
    	console.log(".js-switch2 Click");

    	var data = $(".js-switch2").is(':checked') == true ? "on" : "off";
    	if (data == "on") {
    		data = "alert";
    	} else {
    		data = "alert_off";
    		alert("센서값이 기준을 초과하여 액추에이터 동작을 종료하였습니다.");
    	}
    	console.log("push push push push push push push push push push " + deviceId + resourceUri + resourceName + data);
    	
    	executeUpdateControl(deviceId, resourceUri, resourceName, data);
    });
}

/** 자율제어여부 (센서 데이터 업데이트) */
function onUpdateSwitchClick(deviceId, resourceUri, resourceName, controlData) {
	console.log("onUpdateSwitchClick called");
	
	$(".js-switch3").unbind("change");
	
	if (controlData == "update") {
		switchery_3.setPosition(true);
		switchery_3.handleOnchange(true);
	} else {
		switchery_3.setPosition(false);
		switchery_3.handleOnchange(false);
	}
	
	
	//변경에 대한 스위치 상태
    $(".js-switch3").change( function(evt) {
    	console.log(".js-switch3 Click");

    	var data = $(".js-switch3").is(':checked') == true ? "on" : "off";
    	if (data == "on") {data = "update";} else {data = "update_off";}
    	console.log("Update Update Update Update Update Update Update Update " + deviceId + resourceUri + resourceName + data);
    	
    	executeUpdateControl(deviceId, resourceUri, resourceName, data);
    });
}

/** 자율제어여부 (업데이트 주기 설정 on/off) */
function onPeriodSwitchClick(deviceId, resourceUri, resourceName, controlData) {
	console.log("onPeriodSwitchClick called");
	
	$(".js-switch4").unbind("change");
	
	if (controlData == "period") {
		switchery_4.setPosition(true);
		switchery_4.handleOnchange(true);
		drawSlider(periodData, perResourceUri, perResourceName, preDeviceId, periodData);
	 } else {
		 switchery_4.setPosition(false);
		switchery_4.handleOnchange(false);
	 }
	
	
	//변경에 대한 스위치 상태
    $(".js-switch4").change( function(evt) {
    	console.log(".js-switch4 Click");

    	var data = $(".js-switch4").is(':checked') == true ? "on" : "off";
    	if (data == "on") {data = "period";} else {data = "period_off";}
    	console.log("Period Period Period Period Period Period Period Period " + deviceId + resourceUri + resourceName + data);
    	
    	//스위치 변경에 의한 Slider 상태
    	if (data == "period") {
    		//Slider 생성
    		drawSlider(periodData, perResourceUri, perResourceName, preDeviceId);
    	} else if (data == "period_off") {
    		//Slider 제거
    		reSlider();
    		
    		//주기를 default값으로 update (1분)
    		var defaultData = "10sec";
    		executeDefaultSliderData(preDeviceId, perResourceUri, perResourceName, defaultData);
    	}
    	
    	executeUpdateControl(deviceId, resourceUri, resourceName, data);
    });
}



/** 변경에 대한 스위치 상태 업데이트 */
function executeUpdateControl(deviceId, resourceUri, resourceName, data) {
	console.log("executeUpdate called");
	console.log("executeUpdate DATA" + deviceId + resourceUri + resourceName + data);
	
	
	var context = {"deviceId":deviceId, "resourceUris":[resourceUri],
			"e":[{"n":resourceUri, "sv":data}], "handler":executeUpdateControlHandler};
	dm_write(context, false);
	//var context = {"param":{"deviceId":deviceId, "resourceUri":resourceUri, "resourceName":resourceName, "data":data}, "handler":executeUpdateControlHandler};
	//hdb_update_deviceMoData(context, false);
}

function executeUpdateControlHandler(msg, context) {
	console.log("executeUpdateHandler called");
	console.log("context :" + JSON.stringify(context));
	console.log("msg " + JSON.stringify(msg));
}