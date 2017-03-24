
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
		//refreshHistoryTable("error", deviceId, 1);
		movePage(1);
	});
	
	updateSearchBtnStatus();
}


function movePage(page){

	var context = {"param":{"deviceId":deviceId, "page":page}, manualRenderer:errorManualRenderer};
	
	refreshHistoryTableContext("error", context);
	//refreshHistoryTable("error", deviceId, page);
	//window.location.href = "${pageContext.request.contextPath}/history/error.do?deviceModel="+deviceModel+
	//																"&sn="+sn+
	//																"&deviceId="+deviceId+
	//																"&page="+cPage;
}

function errorManualRenderer(id, profile, context) {
	console.log("errorManualRenderer called - id:"+id +":"+ profile['errorGrade']);
	var td = "<td></td>";
	switch (id) {
	case "errorGrade":
		var grade = "NORMAL";
		var gradeArr = ["NORMAL", "MINOR", "MAJOR", "CRITICAL", "FATAL"];
		td = "<td>"+gradeArr[parseInt(profile["errorGrade"])]+"</td>";
		break;
	}
	return td;
}