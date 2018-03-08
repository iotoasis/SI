
// - 페이지 로딩시 1회 수행
function initUI2() {
	console.log("initUI2 called");
	
	// 목록 업데이트
	if (deviceId != "") {
		refreshHistoryTable(deviceId, page);
	}

	// refresh
	$("#btnSearchDevice").click(function() {
		searchDevice();	
	});
	$("#btnSearchHistory").click(function() {
		//deviceId = $("#search_deviceId").attr("value");
		//resourceUri = $("#search_resourceUri").attr("value");
		deviceId = $("#search_deviceId").val();
		resourceUri = $("#search_resourceUri").val();
		movePage(1);
	});

	$("#search_deviceModelId").change(function() {
		getModelProfile(getModelProfileHandler);	
	});
	
	
	updateSearchBtnStatus();
}



function getModelProfileHandler(msg, context) {
	console.log("getModelInfoHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var selResource = $("#search_resourceUri");
	selResource.empty();
	var profileList = msg.content.profile;
	selResource.append($('<option value="">전체</option>'));
	var histFieldCount = 0;
	for (var i=0; i<profileList.length; i++) {
		var profile = profileList[i];
		if (profile.operation.indexOf("R") >= 0 && profile.isHistorical == 'Y') {
			selResource.append($('<option value="'+profile.resourceUri+'">'+profile.displayName+'</option>'));
			histFieldCount ++;
		}
	}
	if (histFieldCount == 0) {
		alert("선택한 디바이스 모델은 상태이력 필드가 없습니다.");
	}

	var selDeviceId = $("#search_deviceId");
	selDeviceId.empty();
}

function movePage(page){
	var context = {"param":{"deviceId":deviceId, "page":page}};
	if (resourceUri != "") {
		context.param.resourceUri = resourceUri;
	}
	
	refreshHistoryTableContext("status", context);
}