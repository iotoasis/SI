
// - 페이지 로딩시 1회 수행
function initUI2() {
	console.log("initUI called");
	
	$("#search_deviceModelId").change(function() {
		var deviceModel = $("#search_deviceModelId").val();
		if (deviceModel == 'choose') {
			//$("#search_package").empty();
			return;
		} else {
			firmwarePackage();
		}
	});

	$("#search_package").change(function() {
		var firmwareId = $(this).val();
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
	
	//SCHEDULE_TIME
	$("#dpStart").datepicker({dateFormat: "yy-mm-dd"});
		updateDatePicker();
	
	$("#btnUpgradeSchedule").click(function(){
		executeUpgrade();
	});
	
}

function firmwarePackage() {
	var deviceModelId = $("#search_deviceModelId").val();
	// 패키지 선택 옵션 업데이트
	$.ajax({
	  type: "GET",
	  dataType: "json",
	  url: "/hdm/api/hdp/firmware/package/list.do?deviceModelId="+deviceModelId
	})
	.done(function(msg) { 
		handlerDeviceModelChange(msg);
		});
}

function updateDatePicker() {
	var startMonth = new Date();

    $("#dpStart").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#dpStart").datepicker("setDate", startMonth);
}

function executeUpgrade(context, loading) {
	console.log("executeUpgrade called");
	//GROUP_ID
	var deviceModelId = $("#search_deviceModelId").val();
	//GROUP_TYPE
	var groupType = "M";
	//PACKAGE
	var tokens = $("#search_package").val().split("|");
	//FIRMWARE_ID
	var firmwareId = tokens[0];
	var packageName = tokens[1];
	//VERSION
	var version = $("#search_firmwareVersion").val();
	//SCHEDULE_TIME
	var scheduleTime = $("#dpStart").val() + " " + $("#dpTime").val();
	
	console.log("GROUP_ID : " + deviceModelId + " GROUP_TYPE : " + groupType + " FIRMWARE_ID : " + firmwareId + " PACKAGE : " + packageName + " VERSION : " + version + " SCHEDULE_TIME : " + scheduleTime);
	
	if (deviceModelId.length == 0 || packageName == null || version.length == 0) {
		alert("값을 선택해주세요");
		return;
	}
	
	var context = {"param":{"groupId":deviceModelId, "groupType":groupType, "firmwareId":firmwareId, "package":packageName, "version":version, "scheduleTime":scheduleTime}, "handler":regfirmwareUpdateHandler};
	hdb_firmware_schedule(context, false);
}

function regfirmwareUpdateHandler(msg, context) {
	console.log("regfirmwareUpdateHandler called ");
	
	if (msg.result == 0) {
		alert("완료되었습니다.");
		
		window.location.href = contextPath + "/firmware/device/schedule.do"
		return;
	}
}

function handlerPackageChange(msg) {
	console.log("handlerPackageChange called ");
	console.log(JSON.stringify(msg));
	
	try {				
		if (msg.result == 0) {	
			$("#search_firmwareVersion").empty();
			$("#search_firmwareVersion").append("<option value=''>선택</option>");
			for (var i=0; i<msg.content.list.length; i++) {
				var firmware = msg.content.list[i];
				$("#search_firmwareVersion").append("<option value='"+firmware.version+"'>"+firmware.version+"</option>");
			}
		}
	}
	catch (e) {
		console.log("exception in handlerPackageChange ");
		console.log(e);
	}
}

function handlerDeviceModelChange(msg) {
	console.log("handlerDeviceModelChange called ");
	console.log(JSON.stringify(msg));
	
	try {		
		if (msg.result == 0) {	
			$("#search_package").empty();
			$("#search_package").append("<option value=''>선택</option>");
			for (var i=0; i<msg.content.list.length; i++) {
				var package = msg.content.list[i];
				$("#search_package").append("<option value='"+package.id+"|"+package.package+"'>"+package.package+"</option>");
			}
		}
	}
	catch (e) {
		console.log("exception in handlerDeviceModelChange ");
		console.log(e);
	}
}