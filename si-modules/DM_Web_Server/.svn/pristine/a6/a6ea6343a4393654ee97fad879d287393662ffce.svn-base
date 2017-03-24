
var errCodeMap = {};

// 상세정보 UI를 MO 프로파일을 이용해서 업데이트
// - 페이지 로딩시 1회 수행
function initUI() {
	console.log("initUI called");

	if (deviceInfo.bsStatus == 0) {
		alert("DM서버로 REGISTERATION이 정상적으로 수행되지 않았습니다.\r\n확인 후 다시 시도해주세요.");
		return;
	}
	if (deviceInfo.connStatus == 0) {
		alert("디바이스가 DM서버에 정상적으로 연결되어 있지 않습니다. \r\n디바이스가 최종적으로 서버에 접속되었을때의 정보를 화면에 표시합니다.");
	}
	
	// 오류코드 정보 초기화 - 오류코드 리소스별로 오류코드정보 리스트 셋팅
	for (var i=0; i<profileList.length; i++) {
		var profile = profileList[i];
		if (profile.isError == "Y") {
			errCodeMap[profile.resourceUri] = {};
			for (var j=0; j<profile.errorCodeList.length; j++) {
				errCodeMap[profile.resourceUri][profile.errorCodeList[j].errorCode] = profile.errorCodeList[j];
			}
		}
	}

    
	// 제어 데이터 input객체 변경시 제어 버튼 활성화를 위한 핸들러 추가
	$(".cmdProfile").change(function() {
		var btnId = "controlBtn_"+$( this ).attr("id");
		enableControlBtn(btnId, $(this).attr("id").replace(/_/g, "/"));	
	});
	$(".writeProfile").change(function() {
		var btnId = "controlBtn_"+$( this ).attr("id");
		enableWriteBtn(btnId, $(this).attr("id").replace(/_/g, "/"));	
	});
	$(".writeProfile").keydown(function() {
		var btnId = "controlBtn_"+$( this ).attr("id");
		enableWriteBtn(btnId, $(this).attr("id").replace(/_/g, "/"));	
	});
	
	// refresh
	$("#refleshBtn").click(function() {
		refleshResourceData();	
	});
	// refresh
	$(".view_toggle_btn").click(function(event) {
		toggleView(event);	
	});

	
	gmap_initializeMap(document.getElementById('map-canvas'));

	initTrafficGraph($("#traffic_chart"));
	if (deviceInfo.connStatus != 0) {

		var e = [];
		var r = [];
		if (getResourceProfile("/6/-/0") != null) { e[e.length] = {"n":"/6/-/0"}; r[r.length] = "/6/-/0"; }	// TX
		if (getResourceProfile("/6/-/1") != null) { e[e.length] = {"n":"/6/-/1"}; r[r.length] = "/6/-/1"; }	// RX
		if (getResourceProfile("/6/-/2") != null) { e[e.length] = {"n":"/6/-/2"}; r[r.length] = "/6/-/2"; } // MAX Message Size
		if (getResourceProfile("/6/-/3") != null) { e[e.length] = {"n":"/6/-/3"}; r[r.length] = "/6/-/3"; }	// Avg Message Size
		if (e.length > 0) {
			trafficE = e;
			trafficR = r;
			updateTrafficGraph();
		}
	}

	// 조회해야 하는 리소스 추출
	e = [];
	r = [];
	var filter = "/0/-/0,/0/-/1,/0/-/2,/0/-/3,/0/-/4,/0/-/5,/0/-/6,"+
				"/1/-/0,/1/-/1,/1/-/2,/1/-/3"+
				"/4/-/2,/4/-/3,/4/-/4,"+
				"/6/-/0,/6/-/1,"+
				"/7/-/2,/7/-/4,"+
				"/2/-/0,/2/-/1,/2/-/2,/2/-/4,/2/-/5,/2/-/13";	// /2/-/3,
	for (var i=0; i<profileList.length; i++) {
		var profile = profileList[i];
		if (profile.operation.indexOf("R") >= 0 && filter.indexOf(profile.resourceUri) < 0) {
			if (profile.isMultiple == "Y") {
				e[e.length] = {"n":profile.resourceUri+"//"}; r[r.length] = profile.resourceUri+"//";
				//e[e.length] = {"n":profile.resourceUri}; r[r.length] = profile.resourceUri;		
			} else {
				e[e.length] = {"n":profile.resourceUri}; r[r.length] = profile.resourceUri;		
			}
		}
	}
	totalReadE = e;
	totalReadR = r;
	
	//refleshResourceData();

	updateData(moMap);
	
	updateSoftwareData();
	
	initializeLogView();

	if (deviceInfo.connStatus != 0) {
		initializeDiagnosisView();
	}
	
}

function initializeLogView() {
	var supportDebugStart = getResourceProfile("/7/-/0");
	var supportDebugStop = getResourceProfile("/7/-/1");
	var supportDebugStatus = getResourceProfile("/7/-/2");
	if (supportDebugStart == null || supportDebugStop == null || supportDebugStatus == null) {
		// Debug 지원안함
		enableExecuteBtn("btnDebugStop", false, null);
		enableExecuteBtn("btnDebugStart", false, null);	
	}
	
	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setDate(today.getDate()+1);

    $("#dpLogStart").datepicker({dateFormat: "yy-mm-dd"});
    $("#dpLogStart").datepicker("setDate", today);
    $("#dpLogEnd").datepicker({dateFormat: "yy-mm-dd"});
    $("#dpLogEnd").datepicker("setDate", tomorrow);
	$("#btnSearchLog").click(function() {	
		var sHour = $("#selLogStartHour").attr("value");
		var sMinute = $("#selLogStartMinute").attr("value");
		var eHour = $("#selLogEndHour").attr("value");
		var eMinute = $("#selLogEndMinute").attr("value");
		logSearchStartTime = $("#dpLogStart").attr("value") + " " + sHour +":"+ sMinute +":00";
		logSearchEndTime = $("#dpLogEnd").attr("value") + " " + eHour +":"+ eMinute +":00";
		logTableMovePage(1);
	});
	$("#cbDeviceLogAll").change(function() {
		$(".cb_device_log_file").each(function(index, value) {
			console.log(".cb_device_log_file each " + index +":"+ value);
			$(value).prop("checked", $("#cbDeviceLogAll").prop("checked"));
		});	
		setTimeout(updateLogfileDownloadBtnStatus, 500);
	});
	
}

function initializeDiagnosisView() {
	// 로그수집 지원 여부 체크
	var supportDebugStart = getResourceProfile("/7/-/0");
	var supportDebugStop = getResourceProfile("/7/-/1");
	var supportDebugStatus = getResourceProfile("/7/-/2");
	if (supportDebugStart != null && supportDebugStop != null && supportDebugStatus!= null) {
		// 로그 수집 상태 업데이트
		readDebugStatus();
	}
	
	
	// 리부팅 지원 여부 체크
	var supportReboot = getResourceProfile("/2/-/4");
	enableExecuteBtn("btnReboot", supportReboot != null, function() {
		//var context = {"deviceId":deviceInfo.deviceId, "resourceUris":["/2/-/4"], 
		//		"e":[ {"n":"/2/-/4"} ], "handler": executeResultHandler};		
		//dm_execute(context, false);
		var context = {"deviceId":deviceInfo.deviceId, "handler": executeResultHandler};		
		dm_reboot(context, false);
	});
	
	// 공장 초기화 지원 여부 체크
	var supportFReset = getResourceProfile("/2/-/5");
	enableExecuteBtn("btnFactoryReset", supportFReset != null, function() {
		//var context = {"deviceId":deviceInfo.deviceId, "resourceUris":["/2/-/5"], 
		//		"e":[ {"n":"/2/-/5"} ], "handler": executeResultHandler};		
		//dm_execute(context, false);
		var context = {"deviceId":deviceInfo.deviceId, "handler": executeResultHandler};		
		dm_factory_reset(context, false);
	});
}

function readDebugStatus() {
	context = {"deviceId":deviceInfo.deviceId, "resourceUris":"/7/-/2", 
			"e":[ {"n":"/7/-/2"} ], "handler": readDebugStatusHandler};
	dm_read(context, false);
	
}

function readDebugStatusHandler(msg, context) {
	console.log("readRDebugStatusHandler called ");
	console.log("msg: "+JSON.stringify(msg));
	console.log("context: "+JSON.stringify(context));
	
	// moMap 업데이트
	updateMoMapWithMsg(msg);
	
	// UI 업데이트
	if (typeof moMap["/7/-/2"] != "undefined" && moMap["/7/-/2"] != null) {
		var status = moMap["/7/-/2"].data;
		enableExecuteBtn("btnDebugStart", status != "1", function() {
			//var context = {"deviceId":deviceInfo.deviceId, "resourceUris":["/7/-/0"], 
			//				"e":[ {"n":"/7/-/0"} ], "handler": readDebugStatus};		
			//dm_execute(context, false);
			var context = {"deviceId":deviceInfo.deviceId, "handler": readDebugStatus};			
			dm_debug_start(context, false);		
		});
		enableExecuteBtn("btnDebugStop", status == "1", function() {
			//var context = {"deviceId":deviceInfo.deviceId, "resourceUris":["/7/-/0"], 
			//		"e":[ {"n":"/7/-/1"} ], "handler": readDebugStatus};			
			//dm_execute(context, false);		
			var context = {"deviceId":deviceInfo.deviceId, "handler": readDebugStatus};			
			dm_debug_stop(context, false);	
		});
		$("#logStatus").text(status == "1" ? "수집중" : "수집안함");
	}
}


function enableExecuteBtn(id, enable, handler) {
	console.log("enableExecuteBtn("+id+", "+enable+")");
	var btn = $("#"+id);
	if (enable) {
		btn.removeClass("search_bt_gray");
		btn.addClass("search_bt_green");
		btn.unbind("click");
		btn.bind("click", function() {
			console.log(id+" clicked");
			handler();
		});
		console.log(id+" enabled");		
	} else {
		btn.removeClass("search_bt_green");
		btn.addClass("search_bt_gray");
		btn.unbind("click");
		console.log(id+" disabled");		
	}
	
}

function movePage(page) {
	logTableMovePage(page);
}

function logTableMovePage(page){
	
	var context = {"param":{"deviceId":deviceInfo.deviceId, "page":page, "startTime":logSearchStartTime, "endTime":logSearchEndTime},
					"manualRenderer":logItemRenderer};
	
	refreshHistoryTableContext("deviceLog", context);
	
}

function logItemRenderer(id, hist, context) {
	console.log("logOperationRenderer called - id:"+id);
	switch (id) {
	case "select":
		var cb = $("<input type='checkbox' class='cb_device_log_file' id='cbLog_"+hist.id+"' />");
		cb.change(function() {
			updateLogfileDownloadBtnStatus();
			});
		var td = $("<td></td>");
		td.append(cb);
		return td;
		//return $("<td><input type='checkbox' class='cb_device_log_file' id='cbLog_"+hist.id+"' /></td>");
		break;
	case "operation":
		return $("<td><a href='javascript:showLog("+hist.path.substring(10)+")'>보기</a></td>");
		break;
	case "path":
		//return $("<td><a href='http://10.101.101.107:8000/hdmlog/download"+ hist.path.substring(10) +"'>"+hist.path.substring(10)+"</a></td>");
		return $("<td><a href='http://"+fileServerHost+":"+fileServerPort+"/hdmlog/download"+ hist.path.substring(10) +"'>"+hist.path.substring(10)+"</a></td>");
		break;
	case "filesize":
		return $("<td>"+getFilesizeString(parseInt(hist.filesize))+"</td>");
		break;
	}
}

function updateLogfileDownloadBtnStatus() {
	console.log("updateLogfileDownloadBtnStatus called");
	var chs = $(".cb_device_log_file");
	for (var i=0; i<chs.length; i++) {
		if ($(chs[i]).prop("checked") == true) {
			enableDownloadBtn(true);
			return;
		}
	}
	enableDownloadBtn(false);
}

function enableDownloadBtn(enable) {
	if (enable) {
		$("#btnDownloadLog").removeClass("search_bt_gray");
		$("#btnDownloadLog").addClass("search_bt_green");
		$("#btnDownloadLog").bind("click", function() {
			console.log("btnDonwloadLog clicked");
		});
		console.log("download btn enabled");		
	} else {
		$("#btnDownloadLog").removeClass("search_bt_green");
		$("#btnDownloadLog").addClass("search_bt_gray");
		$("#btnDownloadLog").unbind("click");
		console.log("download btn disabled");		
	}
	
}

function getFilesizeString(size) {
    var i = Math.floor( Math.log(size) / Math.log(1024) );
    return ( size / Math.pow(1024, i) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
};

function showLog(id) {
	console.log("showLog called - id:"+id);
	alert("showLog called - id:"+id);
}

function toggleView(event) {
	console.log("toggleView called");
	var btn = $(event.target);
	var id = btn.attr("id");
	var viewId = id.substring(0, id.length-9);
	var view = $("#"+viewId);
	if (view.css("display") != "none") {
		view.toggle("fast");
		//view.css("display", "none");
		btn.css('transform', 'rotate(180deg)');
	} else {
		view.toggle("fast");
		//view.css("display", "block");
		btn.css('transform', 'rotate(0deg)');
	}
}

var totalReadE = [];
var totalReadR = [];
function refleshResourceData() {
	console.log("refleshResourceData called");
	
	context = {"deviceId":deviceInfo.deviceId, "resourceUris":totalReadR, "e":totalReadE, "handler": refreshResultHandler};
	dm_read(context, false);
	
	// 소프트웨어 정보 업데이트
	updateSoftwareData();
}

function refreshResultHandler(msg, context) {
	updateMoMapWithMsg(msg);
	
	updateData(moMap);
}

var trafficE = [];
var trafficR = [];
function updateTrafficGraph() {
	
	context = {"deviceId":deviceInfo.deviceId, "resourceUris":trafficR, "e":trafficE, "handler": trafficHandler};
	
	dm_read(context, false);
	
}

function getResourceProfile(resource) {
	for (var i=0; i<profileList.length; i++) {
		var profile = profileList[i];
		if (profile.resourceUri == resource) {
			return profile;
		}
	}
	return null;
}

function trafficHandler(msg, context) {

	var tx = parseInt(dm_get_resource_value_from_msg(msg, "/6/-/0"));
	var rx = parseInt(dm_get_resource_value_from_msg(msg, "/6/-/1"));

	//$("#traffic_tx").text(tx/1024);
	//$("#traffic_rx").text(rx/1024);
	
	addTraffic(rx,tx);
	
	setTimeout(updateTrafficGraph, trafficInterval*1000);
	console.log("trafficHandler RX:"+rx +", TX:"+tx);
}

function disableControlBtn(btnId) {
	$("#"+btnId).addClass("search_bt_gray");
	$("#"+btnId).removeClass("search_bt_green");	
	$("#"+btnId).removeClass("controlEnabled");
	$("#"+btnId).unbind("click");	
}

function enableControlBtn(btnId, resUri) {
	$("#"+btnId).removeClass("search_bt_gray");	
	$("#"+btnId).addClass("search_bt_green");
	$("#"+btnId).addClass("controlEnabled");
	$("#"+btnId).unbind("click");
	$("#"+btnId).click(function() {
		console.log("controlBtn clicked resURI:"+resUri);
		executeControl(resUri);
	});
}

function enableWriteBtn(btnId, resUri) {
	$("#"+btnId).removeClass("search_bt_gray");	
	$("#"+btnId).addClass("search_bt_green");
	$("#"+btnId).addClass("controlEnabled");
	$("#"+btnId).unbind("click");
	$("#"+btnId).click(function() {
		console.log("controlBtn clicked resURI:"+resUri);
		writeControl(resUri);
	});
}

function executeResultHandler(msg, context) {
	console.log("executeResultHandler called ");
	console.log("msg: "+JSON.stringify(msg));
	console.log("context: "+JSON.stringify(context));

	context = {"deviceId":deviceInfo.deviceId, "resourceUris":context.resourceUris, 
				"e":[ {"n":context.resourceUris[0]} ], "handler": readResultHandler};
	
	dm_read(context, false);
}

function readResultHandler(msg, context) {
	console.log("readResultHandler called ");
	console.log("msg: "+JSON.stringify(msg));
	console.log("context: "+JSON.stringify(context));
	
	// moMap 업데이트
	updateMoMapWithMsg(msg);
	
	// UI 업데이트
	for (var i=0; i<context.resourceUris.length; i++) {
		var value = dm_get_resource_value_from_msg(msg, context.resourceUris[i]);
		updateResourceUI(context.resourceUris[i], value);		
	}	
}

function updateMoMapWithMsg(msg) {
	var uri;
	var timeStr = getTimeString(new Date());
	var errs = [];
	for (var i=0; i<msg.content.e.length; i++) {
		uri = msg.content.e[i].n;
		if (uri.indexOf('/2/-/29') >= 0) {
			errs[errs.length] = msg.content.e[i];
			continue;
		}
		if (moMap[uri] == null) {
			moMap[uri] = {"createTime": timeStr, 
					"resourceName": uri, 
					"resourceUri": uri}
		}
		moMap[uri].data = msg.content.e[i].sv;
		moMap[uri].updateTime = timeStr;
	}
	if (errs.length > 0) {
		// clear err
		for (var moName in moMap) {
			if (moName.indexOf("/2/-/29/") >= 0) {
				delete moMap[moName];
			}
		}
		// set err
		for (var i=0; i<errs.length; i++) {
			uri = errs[i].n;
			if (moMap[uri] == null) {
				moMap[uri] = {"createTime": timeStr, 
						"resourceName": uri, 
						"resourceUri": uri}
			}
			moMap[uri].data = errs[i].sv;
			moMap[uri].updateTime = timeStr;
		}
	}
}

function updateResourceUI(uri, value, uTime, alt) {
	console.log("updateResourceUI called uri:"+ uri +", value:"+value);
	if (typeof value == "undefined" || value == null) {
		console.warn("fail to updateResourceUI: value:"+value);		
	}
	var resId = uri.replace(/\//g, "_");
	
	var uiObj = $("#"+resId);
	
	switch (uiObj.prop("tagName")) {
	case "SPAN":
		uiObj.text(value);
		break;
	case "SELECT":
		uiObj.attr("value", value);
		// DISABLE BTN
		disableControlBtn("controlBtn_"+resId);
		break;
	case "INPUT":
		uiObj.attr("value", value);
		disableControlBtn("controlBtn_"+resId);
		// DISABLE BTN
		break;
	} 
	if (alt != null && typeof alt != "undefined") {
		uiObj.attr("alt", alt);
	}
	if (uTime != null && typeof uTime != "undefined") {
		var utimeUiObj = $("#"+resId+"_updateTime");
		if (utimeUiObj != null && typeof utimeUiObj != "undefined") {
			utimeUiObj.text(makeShortTime(uTime));
		}
	}
}

// YYYY-MM-DD HH:MM:SS => MM-DD (어제 이전), HH:MM:SS (오늘)
function makeShortTime(time) {
	var tokens =  time.split(/[ -]/);
	var y = tokens[0], m = tokens[1], d = tokens[2], t = tokens[3];
	
	var sTime = "";
	var now = new Date();
	if (now.getFullYear() == parseInt(y) &&
		now.getMonth()+1 == parseInt(m) &&
		now.getDate() == parseInt(d)) {
		sTime = t;
	} else {
		sTime = time.substring(5, 10);
	}		
	console.log("sTime:"+sTime);
	return sTime;
}


function executeControl(resUri) {
	var inputObjId = resUri.replace(/\//g, "_");
	var resVal = $("#"+inputObjId).val();
	console.log("executeControl called - resUri:"+resUri+", resVal:"+ resVal);
	
	var context = {"deviceId":deviceInfo.deviceId, "resourceUris":[resUri], 
					"e":[ {"n":resUri, "sv":resVal} ], "handler": executeResultHandler};
	
	dm_execute(context, false);
	
}


function writeControl(resUri) {
	var inputObjId = resUri.replace(/\//g, "_");
	var resVal = $("#"+inputObjId).val();
	console.log("writeControl called - resUri:"+resUri+", resVal:"+ resVal);
	
	var context = {"deviceId":deviceInfo.deviceId, "resourceUris":[resUri], 
					"e":[ {"n":resUri, "sv":resVal} ], "handler": executeResultHandler};
	
	dm_write(context, false);
	
}

// 상세정보 UI를 MO정보를 이용해서 업데이트
// - 정보가 업데이트 될 때마다 수행
function updateData(moMap) {
	console.log("updateData called");
	
	// 일반정보 셋팅
	/* $( ".deviceProfile" ).each(function() {
		var resUri = $(this).attr("id").replace(/_/g, "/");
		console.log(resUri+":"+moMap[resUri]+":"+$(this).prop("tagName"));
		if (typeof(moMap[resUri])  !== "undefined") {
			switch ($(this).prop("tagName")) {
			case "SPAN":
				$(this).text(moMap[resUri].data);
				break;
			case "SELECT":
				$(this).attr("value", (moMap[resUri].data));
				break;
			} 
		}
	}) */
	
	var multipleMap = {};
	for (var moName in moMap) {
		try {
			var moData = moMap[moName];
			
			var uri = getResourceUriFromInstanceUri(moName);
			var profile = getResourceProfile(uri);
			if (profile.isMultiple == "Y") {
				if (multipleMap[uri] == null || typeof multipleMap[uri] == "undefined") { 
					multipleMap[uri] = [];
				}
				var multiple = multipleMap[uri];
				multiple[multiple.length] = {"moName":uri, "moData":moData, "profile":profile};
				multipleMap[uri] = multiple;
				continue;
			}
			//data = getFormattedDataWithProfile(data, profile);

			var data = getFormattedDataWithProfile(moData.data, profile);
			updateResourceUI(uri, data, moData.updateTime);
			
		} catch(e) {
			console.log("exception in updateDate moName:"+moName +":"+ uri);
			console.error("message:"+e.message);
			console.error("stack:"+e.stack);
		}
	}
	for (var name in multipleMap) {
		var value = "";
		var multiple = multipleMap[name];
		for (var i=0; i<multiple.length; i++) {
			if (value != "") value = value+", ";
			value += getFormattedDataWithProfile(multiple[i]["moData"].data, multiple[i]["profile"]);
			
		}		
		updateResourceUI(name, value);
	}
	

	// 소프트웨어 정보 셋팅 - 별도의 DB쿼리로 변경
	//updateSoftwareData(moMap);
	
	// 오류 정보
	updateErrorData(moMap);
	
	// 위치 정보
	updateLocationData(moMap);
}

function formatBytes(bytes) {
    if(bytes < 1024) return bytes + " Bytes";
    else if(bytes < 1048576) return(bytes / 1024).toFixed(2) + " KB";
    else if(bytes < 1073741824) return(bytes / 1048576).toFixed(2) + " MB";
    else return(bytes / 1073741824).toFixed(2) + " GB";
}

function getFormattedDataWithProfile(data, profile) {
	if (profile.dataType == "T") {
		data = getTimeString(new Date(parseInt(data, 10)*1000)); 
	} else if (typeof profile.optionDataList != "undefined" && profile.optionDataList != null) {
		data = getDisplayDataFromOptions(data, profile.optionDataList);
	} else if (profile.unit != null && typeof profile.unit != "undefined") {
		switch (profile.unit) {
		case "KB":
			data = formatBytes(parseFloat(data)*1024);
			break;
		case "By":
			data = formatBytes(data);
			break;
		case "Pe":
			data = data +"%";
			break;
		case "Ce":	// 섭시
			data = data +"도";
			break;
		case "Vo":
			data = data +"V";
			break;
		case "mA":
			data = data +"mA";
			break;
		case "dBm":
			data = data +"dBm";
			break;
		case "la":
			break;
		case "lo":
			break;
		case "al":
			break;
		}
	}
	
	if (data == null)
		data = "-";
	
	return data;
}

function getDisplayDataFromOptions(data, options) {
	for (var i=0; i<options.length; i++) {
		if (options[i].data == data) {
			data = options[i].displayData;
		}
	}
	return data;
}

function getResourceUriFromInstanceUri(iUri) {
	// /1/-/1 		=> /1/-/1
	// /1/-/1/0		=> /1/-/1
	// /1/0/1/		=> /1/-/1
	var tokens = iUri.split(/\//g);
	return "/"+ tokens[1] +"/-/"+ tokens[3];
}

var marker = null;
var pinType = ["NORMAL", "MINOR", "MAJOR", "CRITICAL", "FATAL"];
function updateLocationData(moMap) {
	console.log("updateLocationData called");
	// /5/-/0 : Latitude
	// /5/-/1 : Longitude
	// /5/-/2 : Altitude
	// /5/-/3 : Timestamp
	if (typeof moMap["/5/-/0"] == "undefined" || typeof moMap["/5/-/1"] == "undefined") {
		updateResourceUI("/5/-/0", "위치정보 없음");
		updateResourceUI("/5/-/1", "");
		updateResourceUI("/5/-/2", "");
		updateResourceUI("/5/-/3", "");
		return;
	}
	var latitude = moMap["/5/-/0"].data;
	var longitude = moMap["/5/-/1"].data;
	var timestamp;
	var altitude;
	try {
		var timestamp = moMap["/5/-/3"].data;
		var altitude = moMap["/5/-/2"].data;
	} catch(e) {
		console.error("message:"+e.message);
		console.error("stack:"+e.stack);

	}
	
	if (latitude != null && longitude != null) {
		if (marker == null) {
			marker = gmap_setMarker(parseFloat(latitude), parseFloat(longitude), pinType[deviceInfo.errGrade]);	
			gmap_showMarker(marker);		
		} else {
			gmap_moveMarker(marker, parseFloat(latitude), parseFloat(longitude), pinType[deviceInfo.errGrade]);
		}
		//marker = gmap_codeLatLng(parseFloat(latitude), parseFloat(longitude), function(addr) {
		//	$("#device_ADDRESS").text(addr);
		//});
		gmap_codeLatLng(parseFloat(latitude), parseFloat(longitude), marker, function(addr) {
			$("#device_ADDRESS").text(addr);
		});
	}
	var date = new Date(parseInt(timestamp)*1000);
	updateResourceUI("/5/-/0", latitude);
	updateResourceUI("/5/-/1", longitude);
	updateResourceUI("/5/-/2", altitude);
	updateResourceUI("/5/-/3", getTimeString(date));
}


function getTimeString(date) {
	var timeStr = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	console.log("getTimeString return "+timeStr);
	return timeStr;
}
 
var ERROR_GRADE = ['NORMAL', 'MINOR', 'MAJOR', 'CRITICAL', 'FATAL'];
function updateErrorData(moMap) {
	console.log("updateErrorData called");
	$("#errorCodeTable").empty();
	var errCount = 0;
	for (var moName in moMap) {
		try {
			if (moName.indexOf("/2/-/29/") >= 0) {
				var mo = moMap[moName];
				var errCode = errCodeMap["/2/-/29"][mo.data];
				console.log("Error: grade-"+errCode.errorGrade+", name-"+errCode.errorName);
				
				var errName = errCode.errorName.length > 15 ? errCode.errorName.substring(0,12)+"..." : errCode.errorName;

				/*var errHtm ="<li><div class='col1'>"+getErrorGradeImage(errCode.errorGrade)+"</div><div class='col2'>"+mo.createTime+"</div>"+
				"<div class='col4'>"+ERROR_GRADE[errCode.errorGrade]+"</div><div class='col5'>"+errName+"</div></li>";
				$("#errorCodeTable").append(errHtm);*/
				var errHtm ="<tr><td class='col1'>"+getErrorGradeImage(errCode.errorGrade)+"</td><td class='col2'>"+mo.createTime+"</td>"+
					"<td class='col4'>"+ERROR_GRADE[errCode.errorGrade]+"</td><td class='col5'>"+errName+"</td></tr>";
					$("#errorCodeTable").append(errHtm);
				errCount ++;
			}
		} catch (e) {
			console.log("Exception in updateErrorData - "+ moName)			
			console.error("message:"+e.message);
			console.error("stack:"+e.stack);
		}
	}
	for (var i=errCount; i<=3; i++) {
		/*var errHtm ="<li><div class='col1'></div><div class='col2'>-</div>"+
		"<div class='col4'>-</div><div class='col5'>-</div></li>";*/
		var errHtm ="<tr><td class='col1'></td><td class='col2'>-</td>"+
			"<td class='col4'>-</td><td class='col5'>-</td></tr>";
		$("#errorCodeTable").append(errHtm);		
	}
}

function getErrorGradeImage(grade) {
	var img = "<img src='"+contextPath +"/images/hitdm/common/box_icon.gif'>";
	switch (grade) {
	case "0":
		img = "<img src='"+contextPath +"/images/hitdm/common/box_icon_green.gif'>";
		break;
	case "1":
		img = "<img src='"+contextPath +"/images/hitdm/common/box_icon_gray.gif'>";
		break;
	case "2":
		img = "<img src='"+contextPath +"/images/hitdm/common/box_icon_yellow.gif'>";
		break;
	case "3":
		img = "<img src='"+contextPath +"/images/hitdm/common/box_icon.gif'>";
		break;
	case "4":
		img = "<img src='"+contextPath +"/images/hitdm/common/box_icon_red.gif'>";
		break;
	}
	return img;
}

function updateSoftwareData() {
	console.log("updateSoftwareData()");

	var context = {"param": {"deviceId":deviceInfo.deviceId}, "handler": updateSoftwareDataHandler};
	hdb_get_device_firmware_list(context, false);
	
}

var UPGRADE_STATUS = [];
UPGRADE_STATUS[100] = "업그레이드 명령전송";
UPGRADE_STATUS[1] = "업그레이드 초기화";
UPGRADE_STATUS[2] = "다운로드중";
UPGRADE_STATUS[3] = "다운로드완료";
UPGRADE_STATUS[10] = "설치시작";
UPGRADE_STATUS[101] = "설치종료";
var UPGRADE_RESULT = [];
UPGRADE_RESULT[0] = "";
UPGRADE_RESULT[1] = "실패:Storage오류";
UPGRADE_RESULT[2] = "실패:Memory오류";
UPGRADE_RESULT[3] = "실패:Connection오류";
UPGRADE_RESULT[4] = "실패:CRC오류";
UPGRADE_RESULT[5] = "실패:PackageType오류";
UPGRADE_RESULT[6] = "실패:URI오류";
UPGRADE_RESULT[10] = "업그레이드성공";
UPGRADE_RESULT[11] = "실패:앱오류 ";
UPGRADE_RESULT[101] = "실패:명령연결실패";
UPGRADE_RESULT[102] = "실패:명령타임아웃";

function updateSoftwareDataHandler(msg, context) {
	console.log("updateSoftwareDataHandler()");
	
	var firmwares = msg.content.list;

	$("#firmwareTable").empty();
	var i, firmware, upgrade;
	for (i=0; i<firmwares.length; i++) {
		firmware = firmwares[i];
		upgrade = "-";
		upStatus = "";
		if (firmware['upVersion'] != null && firmware['upVersion'] != firmware['version']) {
			if (firmware['upResult'] == 0 || firmware['upResult'] == -1) {
				upStatus = UPGRADE_STATUS[firmware['upStatus']];
			} else {
				upStatus = UPGRADE_RESULT[firmware['upResult']];
			}
			upgrade = firmware['upVersion']+ ":" + upStatus;
		}

		/*var fwHtm ="<li><div class='col5'>"+firmware['package']+
					"</div><div class='col4'>"+firmware['version']+
					"</div><div class='col2'>"+upgrade+"</div></li>";*/
		var fwHtm ="<tr><td class='col5'>"+firmware['package']+
					"</td><td class='col4'>"+firmware['version']+
					"</td><td class='col2'>"+upgrade+"</td></tr>";
		
		$("#firmwareTable").append(fwHtm);
	}
	for (; i<4; i++) {
		//var fwHtm ="<li><div class='col5'></div><div class='col4'></div><div class='col2'></div></li>";
		var fwHtm ="<tr><td class='col5'></td><td class='col4'></td><td class='col2'></td></tr>";
		$("#firmwareTable").append(fwHtm);
	}
	
	for (var idx in msg.content.list) {
		var firmware = [idx];
		
	}
	
}

/*
 * 별도의 DB쿼리로 변경
 * function updateSoftwareData(moMap) {
	console.log("updateSoftwareData called");
	
	var firmwares = [];
	for (var moName in moMap) {
		// moNmae ex: /4/0/1
		if (moName.indexOf("/4/") >= 0) {
			var mo = moMap[moName];
			var tokens = moName.split("/");
			var idx = parseInt(tokens[2]);
			if (firmwares[idx] == null) {
				firmwares[idx] = {};
			}
			if (tokens[3] == "0")
				firmwares[idx]["version"] = mo.data;
			else if (tokens[3] == "1")
				firmwares[idx]["packageName"] = mo.data;
		}
	}
	$("#firmwareTable").empty();
	var i;
	for (i=0; i<firmwares.length; i++) {
		var firmware = firmwares[i];

		var fwHtm ="<li><div class='col5'></div><div class='col2'>"+firmware['packageName']+
					"</div><div class='col4'>"+firmware['version']+"</div></li>";
		$("#firmwareTable").append(fwHtm);
	}
	for (; i<4; i++) {
		var fwHtm ="<li><div class='col5'></div><div class='col2'></div><div class='col4'></div></li>";
		$("#firmwareTable").append(fwHtm);
	}
}*/


