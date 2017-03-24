
// - 페이지 로딩시 1회 수행
function initUI() {
	console.log("initUI called");
	

	$("#search_deviceModelId").change(function() {
		var deviceModelId = $(this).attr("value");
		// 패키지 선택 옵션 업데이트
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/firmware/package/list.do?deviceModelId="+deviceModelId
		})
		.done(function(msg) { 
			handlerDeviceModelChange(msg);
			});
	});

	$("#search_package").change(function() {
		var firmwareId = $(this).attr("value");
		// 패키지 선택 옵션 업데이트
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/firmware/version/list.do?firmwareId="+firmwareId
		})
		.done(function(msg) { 
			handlerPackageChange(msg);
			});
	});
	

	$(".upgradeCheckbox").change(function() {
		updateUpgradeBtnStatus();
	});


	$("#search_firmwareVersion").change(function() {

		// 체크 박스 상태 업데이트
		updateCheckboxStatus();

		// 업그레이드 실행 버튼 상태 업데이트
		updateUpgradeBtnStatus();
		
	});
	
	updateUpgradeBtnStatus();
}

function updateCheckboxStatus() {
	
	console.log("updateCheckboxStatus called");
	var selectedVersion = $("#search_firmwareVersion").attr("value");
	console.log("selected firmwareVersion:" + selectedVersion);
	
	$(".upgradeCheckbox").each(function(index) {
		console.log(index+"- id:"+ $(this).attr("id") +", curVersion:" +$(this).attr("curVersion"));
		if (selectedVersion == "" || selectedVersion == $(this).attr("curVersion")) {
			$(this).attr("disabled", true);
		} else {
			$(this).removeAttr("disabled");			
		}
	});
}

function updateUpgradeBtnStatus() {
	// 펌웨어 버전 선택 상태 체크
	if ($("#search_firmwareVersion").attr("value") == "") {
		disableUpgradeBtn();
		return;
	}
	
	// checkbox 상태 체크
	for (var i=0; i<$(".upgradeCheckbox").length; i++) {
		var cb = $(".upgradeCheckbox")[i];
		if ($(cb).prop("disabled") == false && $(cb).prop("checked") == true) {
			enableUpgradeBtn();
			return;
		}		
	}
	/*for (var idx in $(".upgradeCheckbox")) {
		var cb = $(".upgradeCheckbox")[idx];
		if ($(cb).prop("disabled") == false && $(cb).prop("checked") == true) {
			enableUpgradeBtn();
			return;
		}
	}*/
	
	disableUpgradeBtn();	
}

function disableUpgradeBtn() {
	$("#btnUpgrade").addClass("search_bt_gray");
	$("#btnUpgrade").removeClass("search_bt_green");
	$("#btnUpgrade").unbind("click");
}

function enableUpgradeBtn() {
	$("#btnUpgrade").addClass("search_bt_green");
	$("#btnUpgrade").removeClass("search_bt_gray");
	$("#btnUpgrade").unbind("click");
	$("#btnUpgrade").click(executeUpgrade);
}

function getPackageNameWithFirmwareId(firmwareId) {
	for (var i=0; i<firmwareListJson.length; i++) {
		var fw = firmwareListJson[i];
		if (fw.id == firmwareId) {
			return fw['package'];
		}
	}
	
	return null;
}

var firmwareUpgradeStatus = [];
function executeUpgrade() {
	console.log("executeUpgrade called");
	var version = $("#search_firmwareVersion").attr("value");
	var firmwareId = $("#search_package").attr("value");
	var packageName = getPackageNameWithFirmwareId(firmwareId);

	// 진행중인 FW UP이 있는지 체크
	if (firmwareUpgradeStatus.length > 0) {
		alert("업그레이드 요청 처리중...");
		return false;
	}
	
	$(".upgradeCheckbox").each(function(idx) {
		if ($(this).prop("checked")) {
			var deviceId = $(this).attr("id").substring(3);	// cb_[deviceId]에서 "cb_" 삭제
			firmwareUpgradeStatus[firmwareUpgradeStatus.length] = {"deviceId": deviceId, "packageName": packageName, 
														"version": version, "result": null};	
		}
	});
	console.log("  firmwareUpgradeStatus:"+JSON.stringify(firmwareUpgradeStatus));
	
	$(".upgradeCheckbox").each(function(idx) {
		if ($(this).prop("checked")) {
			var deviceId = $(this).attr("id").substring(3);	// cb_[deviceId]에서 "cb_" 삭제
			executeUpgradeDevice(deviceId, packageName, version, firmwareUpgradeStatus);	
		}
	});
	return true;
}

//context: {"deviceId":deviceInfo.deviceId, 
//			"packageName": "herit.gasvalve.svc1", 
//			"firmwareVersion": "1.1", 
//			"handler": upgradeResultHandler};
function executeUpgradeDevice(deviceId, packageName, ver, statusArr) {
	console.log("executeUpgradeDevice called - deviceId:"+deviceId +", packageName:"+packageName+", version:"+ ver);
	
	var context = {"deviceId":deviceId, "packageName":packageName, 
					"version": ver, "statusArray":statusArr, "handler": executeUpgradeHandler};
	
	dm_firmware_upgrade(context, false);
}


function executeUpgradeHandler(msg, context) {
	console.log("executeUpgradeHandler called ");
	console.log("msg: "+JSON.stringify(msg));
	console.log("context: "+JSON.stringify(context));
		
	updateFirmwareUpgradeTarget(context.deviceId, context.packageName, context.version, context.statusArray, msg);
	if (checktFirmwareUpgradeCompleted(context.statusArray)) {
		console.log("firmwareUpgrade completed!!!");
		
		// 업그레이드 결과 표시
		var resultStr = getFirmwareUpgradeResultString(context.statusArray);
		console.log(resultStr);
		alert("펌웨어 업그레이드 요청이 완료되었습니다.");
		
		// statusArray clear
		while (context.statusArray.length > 0) {
			context.statusArray.pop();
		}
		
		document.location.reload();
	}
}

function getFirmwareUpgradeResultString(statusArray) {
	var str = "";
	for (var index in statusArray) {
		var status = JSON.stringify(statusArray[index]);
		str += status +"\r\n";
	}
	return str;
}

function updateFirmwareUpgradeTarget(deviceId, packageName, version, statusArray, msg) {
	for (var index in statusArray) {
		var status = statusArray[index];
		if (status.deviceId == deviceId) {
			status.result = msg;
			return;
		}
	}	
}

function checktFirmwareUpgradeCompleted(statusArray) {
	var result = "";
	for (var index in statusArray) {
		var status = statusArray[index];
		if (status.result == null) {
			return false;
		}
	}
	return true;
}

function handlerPackageChange(msg) {
	console.log("handlerPackageChange called ");
	console.log(JSON.stringify(msg));
	
	try
	{				
		if (msg.result == 0)
		{	
			for (var i=0; i<msg.content.list.length; i++) {
				var firmware = msg.content.list[i];
				$("#search_firmwareVersion").append("<option value='"+firmware.version+"'>"+firmware.version+"</option>");
			}
		}
	}
	catch (e)
	{
		console.log("exception in handlerPackageChange ");
		console.error("message:"+e.message);
		console.error("stack:"+e.stack);

	}
}

function handlerDeviceModelChange(msg) {
	console.log("handlerDeviceModelChange called ");
	console.log(JSON.stringify(msg));
	
	try
	{				
		if (msg.result == 0)
		{	
			$("#search_package").empty();
			for (var i=0; i<msg.content.list.length; i++) {
				var package = msg.content.list[i];
				$("#search_package").append("<option value='"+package.id+"'>"+package.package+"</option>");
			}
		}
	}
	catch (e)
	{
		console.log("exception in handlerDeviceModelChange ");
		console.error("message:"+e.message);
		console.error("stack:"+e.stack);
	}
}


