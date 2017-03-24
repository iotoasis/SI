
function searchDevice() {
	//context: {"oui":"001122", 
	//			"modelName": "HGW_01",
	//			"sn": "HR_GV_0001%",
	//			"limit": 50, 
	//			"handler": searchDeviceHandler};
	var deviceModel = $("#search_deviceModelId").attr("value");
	var tokens = deviceModel.split("|");
	if (tokens.length < 2) {
		alert("디바이스 모델을 선택해주세요.");
		return;
	}
	
	var oui = tokens[0];
	var modelName = tokens[1];
	var sn = $("#search_sn").attr("value");
	var context = {"oui":oui, "modelName":modelName, "sn":sn, "limit":30, "handler":searchDeviceHandler};
	
	hdb_search_device_for_select(context, false);
}

function searchDeviceHandler(msg, context) {
	console.log("searchDeviceHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));
	
	var list = msg.content.list
	if (list.length == 0) {
		alert("검색된 디바이스가 없습니다. 검색조건을 확인 후 다시 시도해주세요.");
		return;
	}
	
	var selDeviceId = $("#search_deviceId");
	selDeviceId.empty();
	for (var i=0; i<msg.content.list.length; i++) {
		var device = msg.content.list[i];
		selDeviceId.append($('<option value="'+device.deviceId+'">'+device.deviceId+'</option>'));
	}

}

function updateSearchBtnStatus() {
	
	console.log("updateSearchBtnStatus called");
		
}

function refreshHistoryTable(type, deviceId, page) {
	console.log("refreshHistoryTable called - type:"+type+", deviceId:"+deviceId +", page:"+page);
	
	var context = { "param": { "deviceId":deviceId, "page":page }, "handler":getHistoryHandler}
	hdb_get_history(type, context, false);
}

function refreshHistoryTableContext(type, context) {
	console.log("refreshHistoryTable called - type:"+type+", context:"+context);
	
	if (typeof context.handler == "undefined" || context.handler == null) {
		context.handler = getHistoryHandler;
	}
	
	hdb_get_history(type, context, false);
}


function getHistoryHandler(msg, context) {
	console.log("getHistoryHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var ths = $("#history_table thead th");
	var histBody = $("#history_table tbody");
	histBody.empty();
	
	// 목록 표시
	for (var i=0; i<msg.content.list.length; i++) {
		var hist = msg.content.list[i];
		
		var tr = $("<tr></tr>");
		for (var j=0; j<ths.length; j++) {
			if (typeof $(ths[j]).attr("id") == "undefined") {
				continue;
			} 
			var fn = $(ths[j]).attr("id").substring(3); 	// hd_<fieldName>
			if ($(ths[j]).hasClass("history_manual_renderer")) {
				tr.append(context.manualRenderer(fn, hist, context));
			} else {
				if (hist[fn] == null) {
					tr.append($("<td>-</td>"));						
				} else {
					tr.append($("<td>"+hist[fn]+"</td>"));						
				}			
			}
		}
		histBody.append(tr);
	}
	if (msg.content.list.length == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>이력 없음</td>"));
		histBody.append(tr);
	}

	// 페이징 계산
	var curPage = parseInt(msg.parameter.page);
	var pageSize = parseInt(msg.parameter.pageSize);
	var listCount = parseInt(msg.content.count);
	var pageCount = parseInt((listCount + pageSize - 1) / pageSize);
	var startPage = (curPage - 5) < 1 ? 1 : (curPage - 5);
	var endPage = (startPage + 9) > pageCount ? pageCount : (startPage + 9);

	// 페이징 표시
	$(".paging_wrap").empty();
	// 이전/시작 페이지 링크 추가
	if (curPage > 1) {
		$(".paging_wrap").append($('<a href="#" onclick="movePage(1);"><img src="'+contextPath+'/images/hitdm/common/start_bt.gif"/></a>'));
		$(".paging_wrap").append($('<a href="#" onclick="movePage('+(curPage-1)+');"><img src="'+contextPath+'/images/hitdm/common/prev_bt.gif"/></a>'));
	} else {
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/start_bt_disabled.gif"/>'));
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/prev_bt_disabled.gif"/>'));		
	}
	// 페이지 링크 추가
	for (i=startPage; i<=endPage; i++ ) {
		if (i == curPage) {
			$(".paging_wrap").append($('<span class="active"><a href="#">'+i+'</a></span>'));
		} else {
			$(".paging_wrap").append($('<span><a href="#" onclick="movePage('+i+');">'+i+'</a></span>'));			
		}
	}
	// 다음/끝 페이지 링크 추가
	if (curPage < endPage) {
		$(".paging_wrap").append($('<a href="#" onclick="movePage('+(curPage+1)+');"><img src="'+contextPath+'/images/hitdm/common/next_bt.gif"/></a>'));
		$(".paging_wrap").append($('<a href="#" onclick="movePage('+pageCount+');"><img src="'+contextPath+'/images/hitdm/common/end_bt.gif"/></a>'));
	} else {
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/next_bt_disabled.gif"/>'));
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/end_bt_disabled.gif"/>'));
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
	var context = {"param": {"id":id, "oui":oui, "modelName":modelName}, "handler":getModelProfileHandler};
	
	console.log("akjgoijglajslkabnelnglsdgjsdl ja : " + context);
	hdb_get_device_profile(context, false);
}

