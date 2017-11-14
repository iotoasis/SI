
// - 페이지 로딩시 1회 수행
function initUI() {
	console.log("initUI called");
	

}


/*
function refreshStatus() {
	console.log("refreshHistoryTable called ");
	
	var context = { "handler": deviceCountHandler };
	hdb_get_device_count(context, false);
	
	context = { "handler": deviceModelListHandler };
	hdb_get_deviceModel_list(context, false);
	
	context = { "handler": packageListHandler };
	hdb_get_device_package(context, false);
}
function deviceCountHandler(msg, context) {
	console.log("deviceCountHandler called ");

	$("#totalRegCount").text(msg.content.info.totalRegCount);
	$("#todayRegCount").text(msg.content.info.todayRegCount);
	$("#weekRegCount").text(msg.content.info.weekRegCount);
	$("#monthRegCount").text(msg.content.info.monthRegCount);
	$("#normalCount").text(msg.content.info.normalCount);
	$("#criticalCount").text(msg.content.info.criticalCount);
	$("#majorCount").text(msg.content.info.majorCount);
	$("#minorCount").text(msg.content.info.minorCount);
}

var lastId = 0;
function refreshErrorList() {
	console.log("refreshError called ");
	
	var grade = 0;
			
	var context = { "grade":grade, "handler": errorDeviceListHandler };
	hdb_get_device_error_list(context, false);
	
	context = { "id":lastId, "limit":100, "handler": errorHistoryHandler };
	hdb_get_history_error_list(context, false)
}


function deviceModelListHandler(msg, context) {
	console.log("deviceModelListHandler called ");
	$("#deviceModelCount").text(msg.content.list.length);
	updateTable("deviceModel_table", msg, context);
}

function packageListHandler(msg, context) {
	console.log("packageListHandler called ");
	$("#firmwareCount").text(msg.content.list.length);
	updateTable("package_table", msg, context);
}

var errDeviceList = [];
var pinType = ["NORMAL", "MINOR", "MAJOR", "CRITICAL", "FATAL"];
function errorDeviceListHandler(msg, context) {
	console.log("errorListHandler called ");
	
	for (var i=0; i<msg.content.list.length; i++) {
		var device = msg.content.list[i];
		device.infoString = "<ul class='map_device_info'><li>모델명: "+device.modelName +"</li><li>SN: "+device.sn+"</li><li>상태: "+pinType[device.errGrade]+"</li><ul>";

		if (device.latitude != null && device.longitude != null) {
			console.log(device.deviceId+":"+device.latitude +","+device.longitude);
			msg.content.list[i].marker = gmap_setMarker(parseFloat(device.latitude), 
														parseFloat(device.longitude), 
														pinType[device.errGrade], 
														device, 
														function() {
					var device = this.data;					
					console.log("marker clicked - deviceId:"+ device.deviceId);
					moveToDeviceDetail(device.deviceId);
				});
		}
	}
	
	var oldErrDeviceList = errDeviceList;
	errDeviceList = msg.content.list;
	
	updateErrorDeviceMap();
	clearErrorDeviceMap(oldErrDeviceList);
}

function moveToDeviceDetail(deviceId) {
	window.location.href = contextPath+"/device/detail.do?deviceId="+deviceId;
}

function updateErrorDeviceMap() {
	console.log("updateErrorDeviceMap called");
	
	var show = [$("#cb_showNormal").prop("checked") == true,
	            $("#cb_showMinor").prop("checked") == true,
	            $("#cb_showMajor").prop("checked") == true,
	            $("#cb_showCritical").prop("checked") == true];
	
	for (var i=0; i<errDeviceList.length; i++) {
		var device = errDeviceList[i];
		if (device.marker != null && typeof device.marker != "undefined") {
			if (show[device.errGrade]) {
				gmap_showMarker(device.marker);
			} else {
				gmap_hideMarker(device.marker);
			}
		}
	}
}

function clearErrorDeviceMap(deviceList) {
	console.log("clearErrorDeviceMap called");
	
	for (var i=0; i<deviceList.length; i++) {
		var device = deviceList[i];
		if (device.marker != null && typeof device.marker != "undefined") {
			gmap_removeMarker(device.marker);
		}
	}
}

function errorHistoryHandler(msg, context) {
	console.log("errorHistoryHandler called ");
	
	context.manualRenderer = errorListRenderer;

	updateTable("error_table", msg, context);
}

function errorListRenderer(id, error, context) {
	console.log("errorListRenderer called - id:"+id);
	//console.log(JSON.stringify(error));
	switch (id) {
	case "errorGrade":
		var grade = "NORMAL";
		switch (error.errorGrade) {
		case 0:
			grade = "NORMAL";
			break;
		case 1:
			grade = "MINOR";
			break;
		case 2:
			grade = "MAJOR";
			break;
		case 3:
			grade = "CRITICAL";
			break;
		case 4:
			grade = "FATAL";
			break;			
		}
		return $("<td>"+grade+"</td>");
		break;
	case "sn":
		var sn = error.sn;
		if (sn.length > 13) {
			sn = sn.substring(0, 11) + "...";
		}
		var a = $("<a herf='#' device_id='"+error.deviceId+"' onclick='onClickDeviceId();'>"+sn+"</a>");
		var td = $("<td></td>");
		td.append(a);
		return td;
		break;
	case "deviceId":
		var deviceId = error.deviceId;
		if (deviceId.length > 30) {
			deviceId = deviceId.substring(0, 28) + "...";
		}
		var a = $("<a herf='#' device_id='"+error.deviceId+"' onclick='onClickDeviceId();'>"+deviceId+"</a>");
		//a.bind("clcik", onClickDeviceId);
		var td = $("<td></td>");
		td.append(a);
		return td;
		break;
	}
}

function onClickDeviceId() {
	var deviceId = $(event.srcElement).attr("device_id");
	console.log("onClickDeviceId - deviceId:"+deviceId);
	
	moveToDeviceDetail(deviceId);
}

function updateTable(tableId, msg, context) {
	console.log("updateTable called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var ths = $("#"+tableId+" thead th");
	var histBody = $("#"+tableId+" tbody");
	histBody.empty();
	
	// 목록 표시
	for (var i=0; i<msg.content.list.length; i++) {
		var hist = msg.content.list[i];
		
		var tr = $("<tr></tr>");
		for (var j=0; j<ths.length; j++) {
			if (typeof $(ths[j]).attr("id") == "undefined") {
				continue;
			} 
			var fn = $(ths[j]).attr("id").substring(6); 	// hd_dm_<fieldName>
			if ($(ths[j]).hasClass("manual_renderer")) {
				tr.append(context.manualRenderer(fn, hist, context));
			} else {
				tr.append($("<td>"+hist[fn]+"</td>"));				
			}
		}
		histBody.append(tr);
	}
	if (msg.content.list.length == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>이력 없음</td>"));
		histBody.append(tr);
	}
	
}


function getModelProfile(getModelProfileHandler) {
	//context: {"id":123,  
	//			"handler": getModelInfoHandler};
	var deviceModel = $("#search_deviceModelId").attr("value");
	var tokens = deviceModel.split("|");
	if (tokens.length < 2) {
		console.log("getModelInfo failed - selectedDeviceModel:"+deviceModel)
		return;
	}
	
	var oui = tokens[0];
	var modelName = tokens[1];
	var id = tokens[2];
	var context = {"id":id, "oui":oui, "modelName":modelName, "handler":getModelProfileHandler};
	
	hdb_get_device_profile(context, false);
}
*/
