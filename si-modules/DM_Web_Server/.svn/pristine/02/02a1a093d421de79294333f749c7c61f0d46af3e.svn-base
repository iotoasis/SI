
// - 페이지 로딩시 1회 수행
function initUI() {
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
		deviceId = $("#search_deviceId").attr("value");
		resourceUri = $("#search_resourceUri").attr("value");
		
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
	for (var i=0; i<profileList.length; i++) {
		var profile = profileList[i];
		if (profile.operation.indexOf("W") >= 0 || profile.operation.indexOf("E") >= 0) {
			selResource.append($('<option value="'+profile.resourceUri+'">'+profile.displayName+'</option>'));
		}
	}	

	var selDeviceId = $("#search_deviceId");
	selDeviceId.empty();
}

function movePage(page){
	var context = {"param":{"deviceId":deviceId, "page":page}, "manualRenderer":controlManualRenderer};
	if (resourceUri != "") {
		context.param.resourceUri = resourceUri;
	}
	
	refreshHistoryTableContext("control", context);
}
function controlManualRenderer(id, profile, context) {
	console.log("controlManualRenderer called - id:"+id);
	var td = "<td></td>";
	switch (id) {
	case "ctlData":
		var data = profile["ctlData"];
		td = "<td alt='"+data+"'>"+(data.length>60?(data.substring(0,60)+"..."):data)+"</td>";
		break;
	}
	return td;
}
