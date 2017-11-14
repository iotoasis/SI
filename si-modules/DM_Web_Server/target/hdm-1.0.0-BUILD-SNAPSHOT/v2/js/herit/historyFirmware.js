
// - 페이지 로딩시 1회 수행
function initUI2() {
	console.log("initUI called");
	
	// 목록 업데이트
	if (deviceId != "") {
		refreshHistoryTable(deviceId, page);
	}

	// refresh
	$("#btnSearchDevice").click(function() {
		searchDevice();	
	});
	$("#btnSearchHistory").click(function() {
		deviceId = $("#search_deviceId").val();
		packageName = $("#search_packageName").val();
		
		movePage(1);
	});

	$("#search_deviceModelId").change(function() {
		getFirmwarePackageList(getPackageListHandler);	
	});
	
	updateSearchBtnStatus();
}


function getFirmwarePackageList(getPackageListHandler) {
	//context: {"id":123,  
	//			"handler": getModelInfoHandler};
	var deviceModel = $("#search_deviceModelId").val();
	var tokens = deviceModel.split("|");
	if (tokens.length < 2) {
		console.log("getModelInfo failed - selectedDeviceModel:"+deviceModel)
		return;
	}
	
	var oui = tokens[0];
	var modelName = tokens[1];
	var id = tokens[2];
	var context = {"id":id, "oui":oui, "modelName":modelName, "handler":getPackageListHandler};
	
	hdb_get_device_package(context, false);
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

function firmwareManualRenderer(id, profile, context) {
	console.log("firmwareManualRenderer called - id:"+id +":"+ profile['errorGrade']);
	var td = "<td></td>";
	switch (id) {
	case "status":
		var strStatus = "";
		var status = profile["status"];
		console.log("status ---------------------" + status);
		var result = profile["result"];
		console.log("result ---------------------" + result);
		if (result == 0 || result == -1) {
			strStatus = UPGRADE_STATUS[status]+" <span style='color:white;'>"+status+","+result+"</span>";
		} else {
			strStatus = UPGRADE_RESULT[result]+" <span style='color:white;'>"+status+","+result+"</span>";
		}
		td = "<td style='text-align:left;'>"+strStatus+"</td>";
		break;
	}
	return td;
}

function getPackageListHandler(msg, context) {
	console.log("getPackageListHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var selPackage = $("#search_packageName");
	selPackage.empty();
	var packageList = msg.content.list;
	selPackage.append($('<option value="">전체</option>'));
	for (var i=0; i<packageList.length; i++) {
		var packageInfo = packageList[i];
		selPackage.append($('<option value="'+packageInfo.package+'">'+packageInfo.package+'</option>'));
	}	

	var selDeviceId = $("#search_deviceId");
	selDeviceId.empty();
}

function movePage(page){
	var context = {"param":{"deviceId":deviceId, "page":page}, "manualRenderer":firmwareManualRenderer};
	if (packageName != "") {
		context.param.packageName = packageName;
	}
	
	refreshHistoryTableContext("firmware", context);
}